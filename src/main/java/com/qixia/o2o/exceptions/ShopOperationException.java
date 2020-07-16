package com.qixia.o2o.exceptions;

/**
 * @author 七夏
 * @create 2020-04-15
 */
public class ShopOperationException extends RuntimeException{

    private static final long serialVersionUID = 3693634936394105766L;

    public ShopOperationException(String msg){
        super(msg);
    }
}
