package com.itheima.reggie.common;


import lombok.Data;

import java.util.HashMap;
import java.util.Map;


/**
 * 通用返回结果，服务端响应数据会封装成此对象
 * @param <T>
 */

@Data
public class R<T> {

    private Integer code;

    private String msg;

    private T data;

    private Map map=new HashMap();

    public static <T> R<T> success(T object){
        R<T> r = new R<T>();
        r.code=1;
        r.data=object;
        return r;
    }

    public static <T> R<T> error(String msg){
        R r = new R();
        r.code=0;
        r.msg=msg;
        return r;
    }

    public R<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }
}
