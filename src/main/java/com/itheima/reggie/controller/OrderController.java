package com.itheima.reggie.controller;


import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Orders;
import com.itheima.reggie.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/order")
@Slf4j
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;


    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders order){

        orderService.submit(order);
        return R.success("下单成功");
    }

}
