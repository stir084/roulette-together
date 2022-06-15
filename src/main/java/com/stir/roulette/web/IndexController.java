package com.stir.roulette.web;

import com.stir.roulette.service.GameService;
import com.stir.roulette.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final GameService gameService;

    @GetMapping("/")
    public String index(ModelMap model) {

        model.addAttribute("posts", gameService.findMyGame());
        model.addAttribute("data", "Hello Spring!");
        model.addAttribute("msg", 11);
        return "index";
    }

    @GetMapping("/gameSetting")
    public String gameSetting(ModelMap model) {
        model.addAttribute("data", "Hello Spring!");
        model.addAttribute("msg", 11);
        return "gameSetting";
    }

    @GetMapping("/rt/game/v1/games/{id}")
    public Long findByGameCode(@PathVariable Long gameCode) {
        return gameService.findByGameCode(gameCode);
    }

    @PostMapping("/ajax_canvasUpload_proc")
    @ResponseBody
    public String ajax_canvasUpload_proc(HttpServletRequest request, String strImg) throws Throwable{
        String uploadpath="uploadImage\\";
        //String folder=request.getServletContext().getRealPath("/") +uploadpath;
        String folder = "C:\\" + uploadpath;
        String fullpath="";
        String[] strParts=strImg.split(",");
        String rstStrImg=strParts[1];  //,로 구분하여 뒷 부분 이미지 데이터를 임시저장
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd_hhmmss");
        String filenm=sdf.format(new Date()).toString()+"_testimg2.png";
        BufferedImage image=null;
        byte[] byteImg;
        BASE64Decoder decoder = new BASE64Decoder();
        byteImg = decoder.decodeBuffer(rstStrImg);  //base64 디코더를 이용하여 byte 코드로 변환
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
    public String displayPhoto(@RequestParam(value="fileId") String fileId, HttpServletResponse response)throws Exception{

        //DB에 저장된 파일 정보를 불러오기
        //Map<String, String> map = new Map<String, String>();
        //map.put("fileId", fileId);
        //Map<String, String> result = 첨부파일검색서비스.첨부파일검색(map);

        response.setContentType("image/jpg");
        ServletOutputStream bout = response.getOutputStream();
        //파일의 경로
        String imgpath = "C:\\uploadImage"+File.separator+"test.png";
        FileInputStream f = new FileInputStream(imgpath);
        int length;
        byte[] buffer = new byte[10];
        while((length=f.read(buffer)) != -1){
            bout.write(buffer,0,length);
        }
        return null;
    }

}
