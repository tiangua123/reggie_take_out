package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.mapper.DishFlavorMapper;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {


    @Autowired
    private DishFlavorService dishFlavorService;

    @Override
    public void saveWithFlavor(DishDto dishDto) {
        this.save(dishDto);

        Long dishDtoId = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDtoId);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public DishDto getByIdWithFlavor(Long id) {
        Dish dish = this.getById(id);
        DishDto dishDto=new DishDto();

        BeanUtils.copyProperties(dish,dishDto);
        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> list = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(list);

        return dishDto;
    }

    @Override
    public void updateWithFlavor(DishDto dishDto) {
        this.updateById(dishDto);

        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);

        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public void deleteWithFlavor(Long id) {
        this.removeById(id);
        LambdaQueryWrapper<DishFlavor> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,id);
        dishFlavorService.remove(queryWrapper);

    }
}
