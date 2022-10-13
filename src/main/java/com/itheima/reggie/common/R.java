package com.itheima.reggie.common;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * 通用返回结果，服务端响应数据会封装成此对象
 * @param <T>
 */

@Data
@ApiModel("返回结果")
public class R<T> implements Serializable {

    @ApiModelProperty("编码")
    private Integer code;

    @ApiModelProperty("信息")
    private String msg;

    @ApiModelProperty("数据")
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
