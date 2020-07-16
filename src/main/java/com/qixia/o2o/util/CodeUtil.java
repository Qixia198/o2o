package com.qixia.o2o.util;

import com.google.code.kaptcha.Constants;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 七夏
 * @create 2020-04-16
 */
public class CodeUtil {

    public static boolean checkVerifyCode(HttpServletRequest request){
        //原本的验证码
        String verifyCodeExpected = (String) request.getSession().getAttribute(
                com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        //获取到输入进来的验证码
        String verifyCodeActual = HttpServletRequestUtil.getString(request,
                "verifyCodeActual");
        if(verifyCodeActual == null || !verifyCodeActual.equals(verifyCodeExpected)){
            return false;
        }
        return true;
    }

}
