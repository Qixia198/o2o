package com.qixia.o2o.web.frontend;

import com.qixia.o2o.bean.HeadLine;
import com.qixia.o2o.bean.ShopCategory;
import com.qixia.o2o.service.HeadLineService;
import com.qixia.o2o.service.ShopCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 七夏
 * @create 2020-04-20
 */
@Controller
@RequestMapping("/frontend")
public class MainPageController {
    @Autowired
    private HeadLineService headLineService;
    @Autowired
    private ShopCategoryService shopCategoryService;

    /**
     * 初始化前端展示系统的主页信息，包括获取一级店铺类别列表以及头条列表
     */
    @ResponseBody
    @RequestMapping(value = "/listmainpageinfo",method = RequestMethod.GET)
    private Map<String,Object> listMainPageInfo(){
        Map<String,Object> modelMap = new HashMap<>();
        List<ShopCategory> shopCategoryList = new ArrayList<>();
        try {
            //获取一级店铺类别
            shopCategoryList = shopCategoryService.getShopCategoryList(null);
            modelMap.put("shopCategoryList",shopCategoryList);
        } catch (Exception e) {
            modelMap.put("successs", false);
            modelMap.put("errMsg", e.getMessage());
        }
        List<HeadLine> headLineList = new ArrayList<>();
        try {
            //获取状态为1可用的头条
            HeadLine headLine = new HeadLine();
            headLine.setEnableStatus(1);
            headLineList = headLineService.getHeadLineList(headLine);
            modelMap.put("headLineList", headLineList);
        } catch (IOException e) {
            modelMap.put("successs", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        modelMap.put("success", true);
        return modelMap;
    }

}
