package com.qixia.o2o.web.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author 七夏
 * @create 2020-04-20
 */
@Controller
@RequestMapping("/frontend")
public class FrontendController{
    /**
     * 店铺详情页面
     */
    @RequestMapping(value = "/shopdetail",method = RequestMethod.GET)
    public String showShopDetail(){
        return "frontend/shopdetail";
    }

    /**
     * 商铺列表页面
     */
    @RequestMapping(value = "/shoplist",method = RequestMethod.GET)
    public String showShopList(){
        return "frontend/shoplist";
    }

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public String index(){
        return "frontend/index";
    }

}
