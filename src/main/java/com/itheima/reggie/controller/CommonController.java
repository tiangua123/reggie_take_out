package com.itheima.reggie.controller;


import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String basepath;


    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){

        String originalFilename = file.getOriginalFilename();

        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
        String s = UUID.randomUUID().toString()+substring;

        File file1 = new File(basepath);
        if(!file1.exists()){
            file1.mkdirs();
        }
        try {
            file.transferTo(new File(basepath,s));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(s);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try {
            FileInputStream fileInputStream=new FileInputStream(new File(basepath+name));

            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");
            int len=0;
            byte[] bytes=new byte[1024];

            while((len=fileInputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
