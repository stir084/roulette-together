package com.stir.roulette.web;

import com.stir.roulette.config.ConfigBean;
import com.stir.roulette.service.RouletteService;
import com.stir.roulette.service.UserService;
import com.stir.roulette.web.dto.RouletteFavoriteResponseDto;
import lombok.RequiredArgsConstructor;
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
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
public class IndexController {


    private final UserService userService;
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
        //String userIp = configBean.getUserIp(request);
        //RouletteResponseDto rouletteResponseDto = rouletteService.findLastGame(userIp);
        List<RouletteFavoriteResponseDto> rouletteResponseDtoList = rouletteService.getRouletteFavorite(userUUID);
        if(rouletteResponseDtoList.size() == 0){
            model.addAttribute("isEmpty", "true");
        }
        model.addAttribute("rouletteList", rouletteResponseDtoList);
        return "roulette-favorite";
    }

    /*@GetMapping("/setting-ajax")
    public String searchMembers(Model model, HttpServletRequest request) {
        String userIp = configBean.getUserIp(request);
        //RouletteResponseDto rouletteResponseDto = rouletteService.findLastGame(userIp);
        //List<Member> findMembers = memberUseCase.findAll();
        model.addAttribute("roulette", rouletteService.findLastGame(userIp));

        return "/roulette-setting :: #commentTable";
    }
*/

   /* @PostMapping("/roulette/segment")
    public String createNewSegment(@Valid RouletteForm form, BindingResult result) {

        if (result.hasErrors()) {
            return "roulette";
            //같은 링크 같은 페이지로 쏴줘야함.. 제약 조건 1 - 매핑 메소드가 같은 성질(member/new)일 때 가능 가능
            //심지어 같은 Object(Roulette)도 담아야함.. 2 - 페이지에 있는 input 데이터가 Form 객체 모두 담겨져 넘어와야 제대로 된 리턴 가능
        }

        *//*Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);*//*
        return "redirect:/roulette";
    }
*/
    @GetMapping("/FncUserData")
    public String FncUserData(ModelMap model, @RequestParam(value="key") String key) {
        return "roulette";
    }

    @ResponseBody
    @PostMapping("/saveRouletteImg")
    public String saveRouletteImg(HttpServletRequest request, String strImg, String gameCode) throws Throwable{
        String uploadpath="uploadImage" + File.separator;
        //String folder=request.getServletContext().getRealPath("/") +uploadpath;
        String folder = File.separator + uploadpath;
                //"C:\\" + uploadpath;
        String fullpath="";
        String[] strParts=strImg.split(",");
        String rstStrImg=strParts[1];  //,로 구분하여 뒷 부분 이미지 데이터를 임시저장
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd_hhmmss");
        // String filenm=sdf.format(new Date()).toString()+"_testimg2.png";
        String filenm = gameCode + ".png";
        BufferedImage image=null;
        byte[] byteImg;
        //BASE64Decoder decoder = new BASE64Decoder();
       // Base64.Encoder decoder = Base64.getDecoder();

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
    public String displayPhoto(@RequestParam(value="fileId") String fileId, @RequestParam(value="gameCode") String gameCode, HttpServletResponse response)throws Exception{


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


        File file = new File(System.getProperty("user.home")+"uploadImage" + File.separator+gameCode + gameCode +".png");
        if( file.exists() ){
            if(file.delete()){
                System.out.println("파일삭제 성공");
            } else {
                System.out.println("파일삭제 실패");
            }
        }else{
            System.out.println("파일이 존재하지 않습니다.");
        }
        return null;
    }

    @RequestMapping(value="/deleteImg.do")
    @ResponseBody
    public String deleteImg(@RequestParam(value="gameCode") String gameCode) throws Exception{

     /*   File file = new File("C:\\uploadImage/" + gameCode +".png");
        if( file.exists() ){
            if(file.delete()){
                System.out.println("파일삭제 성공");
            } else {
                System.out.println("파일삭제 실패");
            }
        }else{
            System.out.println("파일이 존재하지 않습니다.");
        }*/
        return null;
    }

}
