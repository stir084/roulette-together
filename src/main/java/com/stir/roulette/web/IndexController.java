package com.stir.roulette.web;

import com.stir.roulette.service.GameService;
import com.stir.roulette.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    @PostMapping("/ajax_canvasUpload_proc")
    @ResponseBody
    public String ajax_canvasUpload_proc(HttpServletRequest request, String strImg) throws Throwable{
        String uploadpath="uploadfile\\";
        String folder=request.getServletContext().getRealPath("/") +uploadpath;
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

}
