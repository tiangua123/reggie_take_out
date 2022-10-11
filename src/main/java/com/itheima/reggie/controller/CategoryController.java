package com.itheima.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){

        categoryService.save(category);
        return R.success("新增分类成功");
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize){

        Page page1 = new Page(page, pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.orderByAsc(Category::getSort);
        categoryService.page(page1,queryWrapper);
        return R.success(page1);
    }

    @DeleteMapping
    public R<String> delete(Long ids){
        log.info("id是{}",ids);
        categoryService.remove(ids);
        return R.success("删除成功ll");
    }

    @PutMapping
    public R<String> update(@RequestBody Category category){
        categoryService.updateById(category);
        return R.success("修改信息成功");
    }

//    /**
//     * 根据条件查询分类数据
//     * @param category
//     * @return
//     */
//    @GetMapping("/list")
//    public R<List<Category>> list(Category category){
//        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
//        queryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
//        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
//        List<Category> list = categoryService.list(queryWrapper);
//        return R.success(list);
//    }

    @GetMapping("/list")
    public R<List<Category>> list(String type){
        LambdaQueryWrapper<Category> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(type!=null,Category::getType,type);
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }


}
