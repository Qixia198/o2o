package com.qixia.o2o.util;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 七夏
 * @create 2020-04-15
 */

public class HttpServletRequestUtil {

    public static int getInt(HttpServletRequest request,String key){
        try{
            return Integer.decode(request.getParameter(key));
        }catch (Exception e){
            return -1;
        }
    }

    public static Long getLong(HttpServletRequest request,String key){
        try {
            return Long.valueOf(request.getParameter(key));
        } catch (NumberFormatException e) {
            return -1L;
        }
    }

    public static Double getDouble(HttpServletRequest request,String key){
        try {
            return Double.valueOf(request.getParameter(key));
        } catch (NumberFormatException e) {
            return -1D;
        }
    }

    public static boolean getBoolean(HttpServletRequest request,String key){
        try {
            return Boolean.valueOf(request.getParameter(key));
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String getString(HttpServletRequest request,String key){
        try {
            String res = request.getParameter(key);
            if(res != null){
                res = res.trim();
            }
            if("".equals(res)){
                res = null;
            }
            return res;
        } catch (Exception e) {
            return null;
        }
    }
}
