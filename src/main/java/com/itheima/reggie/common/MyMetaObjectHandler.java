package com.itheima.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {     //原数据处理器
    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        Long currentId = BaseContext.getCurrentId();
        metaObject.setValue("createUser", currentId);
        metaObject.setValue("updateUser", currentId);

    }

    /**
     * 修改操作自动填充
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        Long currentId = BaseContext.getCurrentId();
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", currentId);
    }
}
