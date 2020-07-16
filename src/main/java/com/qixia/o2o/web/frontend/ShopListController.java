package com.qixia.o2o.web.frontend;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qixia.o2o.bean.Area;
import com.qixia.o2o.bean.Shop;
import com.qixia.o2o.bean.ShopCategory;
import com.qixia.o2o.dto.ShopExecution;
import com.qixia.o2o.service.AreaService;
import com.qixia.o2o.service.ShopCategoryService;
import com.qixia.o2o.service.ShopService;
import com.qixia.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 七夏
 * @create 2020-04-20
 */
@Controller
@RequestMapping("/frontend")
public class ShopListController {
    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private AreaService areaService;

    /**
     * 获取指定查询条件下的商铺列表
     */
    @ResponseBody
    @RequestMapping(value = "/listshops",method = RequestMethod.GET)
    private Map<String,Object> listShops(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        if(pageIndex > -1 && pageSize > -1){
            //尝试获取搜索的区域名
            int areaId = HttpServletRequestUtil.getInt(request, "areaId");
            //尝试获取以及类别ID
            Long parentId = HttpServletRequestUtil.getLong(request, "parentId");
            //尝试获取特定的二级类别ID
            Long shopCategoryId = HttpServletRequestUtil.getLong(request, "shopCategoryId");
            //尝试获取搜索的店铺名（用于模糊查询）
            String shopName = HttpServletRequestUtil.getString(request, "shopName");
            //获取之后进行组合
            Shop shopCondition = compactShopCondition4Search(parentId,shopCategoryId,areaId,shopName);
            ShopExecution se = shopService.getShopList(shopCondition, pageIndex, pageSize);
            PageHelper.startPage(pageIndex, pageSize);
            PageInfo page = new PageInfo(se.getShopList(),100);
            modelMap.put("success", true);
            modelMap.put("shopList",page.getList());
            modelMap.put("count", se.getCount());
        }else{
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty pageSize or pageIndex");
        }
        return modelMap;
    }

    /**
     * 将查询条件组装进一个shop对象中
     */
    private Shop compactShopCondition4Search(Long parentId, Long shopCategoryId, int areaId, String shopName) {
        Shop shopCondition = new Shop();
        if(parentId != -1L){
            //查询某个一级shopCategory下面的所有二级ShopCategory里面的店铺列表
            ShopCategory childCategory = new ShopCategory();
            ShopCategory parentCategory = new ShopCategory();
            parentCategory.setShopCategoryId(parentId);
            childCategory.setParent(parentCategory);
            shopCondition.setShopCategory(childCategory);
        }
        if(shopCategoryId != -1L){
            //查询某个二级ShopCategory下面的店铺列表
            ShopCategory shopCategory = new ShopCategory();
            shopCategory.setShopCategoryId(shopCategoryId);
            shopCondition.setShopCategory(shopCategory);
        }
        if(areaId != -1){
            //查询某个区域下的所有店铺列表
            Area area = new Area();
            area.setAreaId(areaId);
            shopCondition.setArea(area);
        }
        if(shopName != null && !shopName.equals("")){
            //查询指定的店铺名称
            shopCondition.setShopName(shopName);
        }
        //前端展示的店铺都是审核通过的店铺
        shopCondition.setEnableStatus(1);
        return shopCondition;
    }


    /**
     * 查询商品列表页面的ShopCategory列表（二级或者一级），以及区域信息
     */
    @ResponseBody
    @RequestMapping(value = "/listshopspageinfo",method = RequestMethod.GET)
    private Map<String,Object> listShopsPageInfo(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        //尝试从前端获取parentId的值
        Long parentId = HttpServletRequestUtil.getLong(request, "parentId");
        List<ShopCategory> shopCategoryList = null;
        if(parentId != -1){
            try{
                //如果parentId存在，则取出该一级ShopCategory下的二级ShopCategory列表
                ShopCategory shopCategoryCondition = new ShopCategory();
                ShopCategory parentCategory = new ShopCategory();
                parentCategory.setShopCategoryId(parentId);
                shopCategoryCondition.setParent(parentCategory);
                shopCategoryList = shopCategoryService.getShopCategoryList(shopCategoryCondition);
            }catch (Exception e){
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
        }else{
            try{
                //如果parentId不存在，就查询所有一级类别信息
                shopCategoryList = shopCategoryService.getShopCategoryList(null);
            }catch (Exception e){
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
        }
        modelMap.put("shopCategoryList",shopCategoryList);
        List<Area> areaList = null;
        try{
            //获取区域信息
            areaList = areaService.getAreaList();
            modelMap.put("areaList",areaList);
            modelMap.put("success", true);
        }catch (Exception e){
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }

}
