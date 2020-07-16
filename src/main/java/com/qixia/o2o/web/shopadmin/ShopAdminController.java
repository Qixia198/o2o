package com.qixia.o2o.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author 七夏
 * @create 2020-04-15
 */
@Controller
@RequestMapping(value = "shopadmin",method = {RequestMethod.GET})
public class ShopAdminController {

    @RequestMapping("/productmanage")
    public String productManagement(){
        return "shop/productmanage";
    }

    @RequestMapping("/productoperation")
    public String productoperation(){
        //转发到商品添加/编辑界面
        return "shop/productoperation";
    }

    @RequestMapping("/productcategorymanage")
    public String productCategoryManage(){
        //转发到商品类别管理界面
        return "shop/productcategorymanage";
}

    @RequestMapping(value = "/shopmanagement",method = RequestMethod.GET)
    public String shopManagement(){
        //转发到店铺管理界面
        return "shop/shopmanagement";
    }

    @RequestMapping(value = "/shoplist")
    public String shopList(){
        //转发到店铺列表界面
        return "shop/shoplist";
    }


    @RequestMapping(value = "/shopoperation")
    public String shopOperation(){
        //转发到店铺注册/编辑界面
        return "shop/shopoperation";
    }
}
