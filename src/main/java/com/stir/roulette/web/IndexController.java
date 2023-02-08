package com.stir.roulette.web;

import com.stir.roulette.config.ConfigBean;
import com.stir.roulette.service.RouletteService;
import com.stir.roulette.service.UserService;
import com.stir.roulette.web.dto.RouletteFavoriteResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;


import java.net.UnknownHostException;
import java.util.Base64;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
@Slf4j
public class IndexController {

    private final ConfigBean configBean;
    private final RouletteService rouletteService;

    @GetMapping("/")
    public String index() {
        return "redirect:roulette";
    }

    @GetMapping("/roulette")
    public String roulette() throws UnknownHostException {
        return "roulette";
    }

    @GetMapping("/roulette/{rouletteUID}") //특정 룰렛 선택(ex. Favorite 에서 넘어온 룰렛)
    public String specificRoulette(@PathVariable UUID rouletteUID, Model model) {
        model.addAttribute("rouletteUID", rouletteUID);
        return "roulette";
    }

    @GetMapping("/roulette/share/{rouletteUID}") //특정 룰렛 선택(ex. Favorite 에서 넘어온 룰렛)
    public String shareRoulette(@PathVariable UUID rouletteUID, Model model) {
        model.addAttribute("rouletteUID", rouletteUID);
        return "roulette-share";
    }

    @GetMapping("/setting")
    public String setting(@CookieValue String userUUID, ModelMap model, HttpServletRequest request) {
        //String userIp = configBean.getUserIp(request);
        model.addAttribute("roulette", rouletteService.findLastGame(userUUID));
        return "roulette-setting";
    }

    @GetMapping("/setting/{rouletteUID}")
    public String settingFavoriteGame(@PathVariable UUID rouletteUID, ModelMap model, HttpServletRequest request) {
        model.addAttribute("roulette", rouletteService.getSharedRoulette(rouletteUID));
        return "roulette-setting";
    }

    @GetMapping("/history")
    public String history(ModelMap model, HttpServletRequest request) {
        return "roulette-history";
    }

    @GetMapping("/favorite")
    public String favorite(@CookieValue String userUUID, ModelMap model, HttpServletRequest request) {
        List<RouletteFavoriteResponseDto> rouletteResponseDtoList = rouletteService.getRouletteFavorite(userUUID);
        if(rouletteResponseDtoList.size() == 0){
            model.addAttribute("isEmpty", "true");
        }
        model.addAttribute("rouletteList", rouletteResponseDtoList);
        return "roulette-favorite";
    }


    @GetMapping("/FncUserData")
    public String FncUserData(ModelMap model, @RequestParam(value="key") String key) {
        return "roulette";
    }

    @ResponseBody
    @PostMapping("/saveRouletteImg")
    public String saveRouletteImg(String strImg, String gameCode) throws Throwable{
        String uploadpath="uploadImage" + File.separator;
        String folder = File.separator + uploadpath;
        String fullpath="";
        String[] strParts=strImg.split(",");
        String rstStrImg=strParts[1];  //,로 구분하여 뒷 부분 이미지 데이터를 임시저장
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd_hhmmss");
        String filenm = gameCode + ".png";
        BufferedImage image=null;
        byte[] byteImg;

        byteImg = Base64.getDecoder().decode(rstStrImg);//decoder.decode(rstStrImg);  //base64 디코더를 이용하여 byte 코드로 변환
        ByteArrayInputStream bis= new ByteArrayInputStream(byteImg);
        image= ImageIO.read(bis);   //BufferedImage형식으로 변환후 저장
        bis.close();

        fullpath=folder+filenm;
        File folderObj= new File(folder);
        if( !folderObj.isDirectory() )
            folderObj.mkdir();
        File outputFile= new File(fullpath);  //파일객체 생성
        if( outputFile.exists() )
            outputFile.delete();
        ImageIO.write(image, "png", outputFile); //서버에 파일로 저장
        return uploadpath+filenm;
    }

    @RequestMapping(value="/loadImage.do")
    public String displayPhoto(HttpServletRequest request, @RequestParam(value="fileId") String fileId, @RequestParam(value="gameCode") String gameCode, HttpServletResponse response)throws Exception{


        response.setContentType("image/jpg");
        ServletOutputStream bout = response.getOutputStream();
        //파일의 경로
        //String imgpath = "C:\\uploadImage"+File.separator+gameCode+".png";
        //System.getProperty("user.home"); //Root Directory가 아닌 Home Directory에 하고 싶을 경우 이 메소드를 경로 맨 앞에 설정
        String imgpath = File.separator + "uploadImage" + File.separator + gameCode+".png";

        FileInputStream f = new FileInputStream(imgpath);
        int length;
        byte[] buffer = new byte[10];
        while((length=f.read(buffer)) != -1){
            bout.write(buffer,0,length);
        }
        f.close();


        File file = new File( File.separator + "uploadImage" + File.separator + gameCode +".png");
        if( file.exists() ){
            if(file.delete()){
                log.debug("파일 삭제 성공");
            } else {
                log.error("파일 삭제 실패 {}", configBean.getUserIp(request));
            }
        }else{
            log.error("파일이 존재하지 않습니다. {}", configBean.getUserIp(request));
        }
        return null;
    }
}
