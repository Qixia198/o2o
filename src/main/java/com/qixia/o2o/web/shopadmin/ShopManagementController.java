package com.qixia.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qixia.o2o.bean.Area;
import com.qixia.o2o.bean.PersonInfo;
import com.qixia.o2o.bean.Shop;
import com.qixia.o2o.bean.ShopCategory;
import com.qixia.o2o.dto.ImageHolder;
import com.qixia.o2o.dto.ShopExecution;
import com.qixia.o2o.enums.ShopStateEnum;
import com.qixia.o2o.exceptions.ShopOperationException;
import com.qixia.o2o.service.AreaService;
import com.qixia.o2o.service.ShopCategoryService;
import com.qixia.o2o.service.ShopService;
import com.qixia.o2o.util.CodeUtil;
import com.qixia.o2o.util.HttpServletRequestUtil;
import com.qixia.o2o.util.ImageUtil;
import com.qixia.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 七夏
 * @create 2020-04-15
 * 店铺管理
 */
@Controller
@RequestMapping("/shop")
public class ShopManagementController {
    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private AreaService areaService;

    @ResponseBody
    @RequestMapping(value = "/getshopmanagementinfo",method = RequestMethod.GET)
    private Map<String,Object> getShopManagementInfo(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        if(shopId <= 0){
            Object currentShopObj = request.getSession().getAttribute("currentShopObj");
            if(currentShopObj == null){
                modelMap.put("redirect", true);
                modelMap.put("url", "/o2o/shopadmin/shoplist");
            }else{
                Shop currentShop = (Shop)currentShopObj;
                modelMap.put("redirect", false);
                modelMap.put("shopId", currentShop.getShopId());
            }
        }else{
            Shop currentShop = new Shop();
            currentShop.setShopId(shopId);
            request.getSession().setAttribute("currentShop",currentShop);
            modelMap.put("redirect", false);
        }
        return modelMap;
    }

    @ResponseBody
    @RequestMapping(value = "/getshoplist",method = RequestMethod.GET)
    private Map<String,Object> getShopList(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        PersonInfo user = new PersonInfo();
        user.setUserId(1L);
        user.setName("七夏");
        request.getSession().setAttribute("user", user);
        user = (PersonInfo)request.getSession().getAttribute("user");
        try {
            Shop shopCondition = new Shop();
            shopCondition.setOwner(user);
            ShopExecution se = shopService.getShopList(shopCondition, 1, 100);
            modelMap.put("shopList", se.getShopList());
            modelMap.put("user", user);
            modelMap.put("success", true);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg",e.getMessage());
        }
        return modelMap;
    }


    @ResponseBody
    @RequestMapping(value = "/getshopbyid",method = RequestMethod.GET)
    private Map<String,Object> getShopById(HttpServletRequest request){
        HashMap<String, Object> modelMap = new HashMap<>();
        Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        if(shopId > -1){
            try {
                Shop shop = shopService.getShopById(shopId);
                List<Area> areaList = areaService.getAreaList();
                modelMap.put("shop", shop);
                modelMap.put("areaList", areaList);
                modelMap.put("success", true);
                return modelMap;
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg",e.getMessage());
                return modelMap;
            }
        }else{
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty shopId");
            return modelMap;
        }
    }

    /**
     * 获取区域和店铺类别的查询
     */
    @ResponseBody
    @RequestMapping(value = "/getshopinitinfo",method = RequestMethod.GET)
    private Map<String,Object> getShopInitInfo(){
        Map<String,Object> modelMap = new HashMap<>();
        List<ShopCategory> shopCategoryList = new ArrayList<>();
        List<Area> areaList = new ArrayList<>();
        try {
            shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
            areaList = areaService.getAreaList();
            modelMap.put("shopCategoryList",shopCategoryList);
            modelMap.put("areaList",areaList);
            modelMap.put("success", true);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg",e.getMessage());
        }
        return modelMap;
    }


    /**
     * 店铺注册
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/registershop", method = RequestMethod.POST)
    private Map<String, Object> registerShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        System.out.println(request.getParameter("shopStr"));
        System.out.println(request.getParameter("verifyCodeActual"));
        if(!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入的验证码有误");
            return modelMap;
        }
        //1.接收并转换相应的参数，包括店铺信息以及图片信息
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        //使用jackson来将String类型转换为shop对象
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try {
            shop = mapper.readValue(shopStr, Shop.class);
        } catch (IOException e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        CommonsMultipartFile shopImg = null;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (commonsMultipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "上传图片不能为空");
            return modelMap;
        }

        //2.注册店铺
        if (shop != null && shopImg != null) {
            PersonInfo owner = (PersonInfo) request.getSession().getAttribute("user");
            shop.setOwner(owner);

            ShopExecution shopExecution = null;
            try {
                ImageHolder imageHolder = new ImageHolder(shopImg.getOriginalFilename(),shopImg.getInputStream());
                shopExecution = shopService.addShop(shop,imageHolder);
                if(shopExecution.getState() == ShopStateEnum.CHECK.getState()){
                    modelMap.put("success", true);
                    //该用户可以操作的店铺列表
                    List<Shop> shopList = (List<Shop>) request.getSession().getAttribute("shopList");
                    if(shopList == null || shopList.size() == 0){
                        shopList = new ArrayList<Shop>();
                    }
                    shopList.add(shopExecution.getShop());
                    request.getSession().setAttribute("shopList", shopList);
                }else{
                    modelMap.put("success", false);
                    modelMap.put("errMsg", shopExecution.getStateInfo());
                }
            } catch (IOException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", "上传图片不能为空");
                return modelMap;
            }
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入店铺信息");
            return modelMap;
        }
    }


    /**
     * 店铺的修改
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/modifyshop", method = RequestMethod.POST)
    private Map<String, Object> modifyShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        System.out.println(request.getParameter("shopStr"));
        System.out.println(request.getParameter("verifyCodeActual"));
        if(!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入的验证码有误");
            return modelMap;
        }
        //1.接收并转换相应的参数，包括店铺信息以及图片信息
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        //使用jackson来将String类型转换为shop对象
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try {
            shop = mapper.readValue(shopStr, Shop.class);
        } catch (IOException e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        CommonsMultipartFile shopImg = null;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        if (commonsMultipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        }

        //2.修改店铺
        if (shop != null && shop.getShopId() != null) {
            ShopExecution shopExecution = null;
            try {
                ImageHolder imageHolder = new ImageHolder(shopImg.getOriginalFilename(),shopImg.getInputStream());
                if(shopImg == null){
                    shopExecution = shopService.modifyShop(shop, null);
                }else{
                    shopExecution = shopService.modifyShop(shop,imageHolder);
                }
                if(shopExecution.getState() == ShopStateEnum.SUCCESS.getState()){
                    modelMap.put("success", true);
                }else{
                    modelMap.put("success", false);
                    modelMap.put("errMsg", shopExecution.getStateInfo());
                }
            } catch (ShopOperationException | IOException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入店铺ID");
            return modelMap;
        }
    }

    /*private static void inputStreamToFile(InputStream ins, File file) {
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            int bytesRed = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRed = ins.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRed);
            }
        } catch (Exception e) {
            throw new RuntimeException("调用inputStreamToFile产生异常：" + e.getMessage());
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (ins != null) {
                    ins.close();
                }
            } catch (IOException e) {
                throw new RuntimeException("inputStreamToFile关闭IO产生异常：" + e.getMessage());
            }
        }
    }*/
}
