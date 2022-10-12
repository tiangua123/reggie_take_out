package com.itheima.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.service.UserService;
import com.itheima.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;


    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession httpSession){
        String phone = user.getPhone();

        if(!StringUtils.isEmpty(phone)){
            String code = ValidateCodeUtils.generateValidateCode(4).toString();

            log.info("code为{}",code);
            redisTemplate.opsForValue().set(phone,code,15, TimeUnit.MINUTES);
            return R.success("手机验证码短信发送成功");
        }

        return R.error("验证码发送失败");
    }



    @PostMapping("/login")
    public R<User> login(@RequestBody Map map,HttpSession httpSession){


        String code = map.get("code").toString();
        String phone = map.get("phone").toString();
        Object attribute = redisTemplate.opsForValue().get(phone);
        if(attribute!=null&&code.equals(attribute)){

            LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User one = userService.getOne(queryWrapper);
            if(one==null){
                one = new User();
                one.setPhone(phone);
                userService.save(one);
            }
            httpSession.setAttribute("user",one.getId());
            redisTemplate.delete(phone);
            return R.success(one);
        }


        return R.error("登陆失败");
    }
}
