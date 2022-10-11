package com.itheima.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.SetmealService;
import com.itheima.reggie.service.SetmeanlDishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmeanlDishService setmeanlDishService;

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功");
    }


    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        Page<Setmeal> page1=new Page<>(page,pageSize);
        Page<SetmealDto> page2=new Page<>();
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null,Setmeal::getName,name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(page1,queryWrapper);

        BeanUtils.copyProperties(page1,page2,"records");
        List<Setmeal> records = page1.getRecords();
        List<SetmealDto> list = records.stream().map((item) -> {
            Long categoryId = item.getCategoryId();
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            setmealDto.setCategoryName(categoryName);
            return setmealDto;
        }).collect(Collectors.toList());

        page2.setRecords(list);
        return R.success(page2);
    }

    /**
     * 删除套餐分类
     * @param ids
     * @return
     */

    @DeleteMapping
    public R<String> delete( List<Long> ids){
        setmealService.deleteWithDish(ids);
        return R.success("删除成功");
    }


    /**
     * 停售套餐
     * @param ids
     * @return
     */
    @PostMapping("status/0")
    public R<String> stop( @RequestParam List<Long> ids){
        for (Long id :ids) {
            Setmeal setmealServiceById = setmealService.getById(id);
            setmealServiceById.setStatus(0);
            setmealService.updateById(setmealServiceById);
        }

        return R.success("停售成功");
    }

    /**
     * 启售套餐
     * @param ids
     * @return
     */
    @PostMapping("status/1")
    public R<String> start(@RequestParam List<Long> ids){
        for (Long id :ids) {
            Setmeal setmealServiceById = setmealService.getById(id);
            setmealServiceById.setStatus(1);
            setmealService.updateById(setmealServiceById);
        }
        return R.success("启售成功");
    }


    /**
     * 根据条件查询套餐数据
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null,Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(queryWrapper);

        return R.success(list);
    }
}
