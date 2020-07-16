package com.qixia.o2o.exceptions;

/**
 * @author 七夏
 * @create 2020-04-18
 */
public class ProductOperationException extends RuntimeException{

    private static final long serialVersionUID = 1820945475683731551L;

    public ProductOperationException(String msg){
        super(msg);
    }
}
