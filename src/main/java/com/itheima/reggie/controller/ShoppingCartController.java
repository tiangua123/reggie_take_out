package com.itheima.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.ShoppingCart;
import com.itheima.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;


    /**
     * 购物车新增
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){

        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);

        Long dishId = shoppingCart.getDishId();
        if(dishId!=null){
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
        }else{
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }

        ShoppingCart shoppingCartServiceOne = shoppingCartService.getOne(queryWrapper);
        if(shoppingCartServiceOne!=null){
            Integer number = shoppingCartServiceOne.getNumber();
            shoppingCartServiceOne.setNumber(number+1);
            shoppingCartService.updateById(shoppingCartServiceOne);
        }else {
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
            shoppingCartServiceOne=shoppingCart;
        }

        return R.success(shoppingCartServiceOne);

    }

    /**
     * 查看购物车
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);

        return R.success(list);
    }

    @DeleteMapping("/clean")
    public R<String> clean(){

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());

       shoppingCartService.remove(queryWrapper);

        return R.success("删除成功");
    }


}
