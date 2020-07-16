package com.qixia.o2o.web.frontend;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qixia.o2o.bean.*;
import com.qixia.o2o.dto.ProductExecution;
import com.qixia.o2o.dto.ShopExecution;
import com.qixia.o2o.service.ProductCategoryService;
import com.qixia.o2o.service.ProductService;
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
 * @create 2020-04-21
 */
@Controller
@RequestMapping("/frontend")
public class ShopDetailController {
    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ShopService shopService;

    @ResponseBody
    @RequestMapping(value = "/listshopdetailpageinfo",method = RequestMethod.GET)
    private Map<String,Object> listShopDetailPageInfo(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        Shop shop = null;
        List<ProductCategory> productCategoryList = null;
        if(shopId != -1){
            //获取对应id的店铺信息
            shop = shopService.getShopById(shopId);
            //获取店铺下面的商铺列表
            productCategoryList = productCategoryService.queryProductCategoryList(shopId);
            modelMap.put("success", true);
            modelMap.put("shop", shop);
            modelMap.put("productCategoryList",productCategoryList);
        }else{
            modelMap.put("success", false);
            modelMap.put("errMsg", "店铺ID不能为空");
        }
        return modelMap;
    }

    /**
     * 根据查询条件获取分页列处该商铺下面的所有商品
     */
    @RequestMapping(value = "/listproductsbyshop",method = RequestMethod.GET)
    @ResponseBody
    private Map<String,Object> listProductByShop(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        //获取页码
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        //获取店铺id
        Long shopId = HttpServletRequestUtil.getLong(request, "shopId");
        if(pageIndex > -1 && pageSize > -1){
            //尝试获取商品类别
            Long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
            //尝试获取商品名称进行模糊查询
            String productName = HttpServletRequestUtil.getString(request, "productName");
            //组合条件
            Product product = compactShopCondition4Search(shopId, productCategoryId, productName);
            PageHelper.startPage(pageIndex,pageSize);
            ProductExecution pe = productService.getProductList(product);
            PageInfo page = new PageInfo(pe.getProductList(),100);
            modelMap.put("success", true);
            modelMap.put("count", pe.getCount());
            modelMap.put("productList", page.getList());
        }else{
            modelMap.put("success", false);
            modelMap.put("errMsg", "pageIndex或pageSize不能为空");
        }
        return modelMap;
    }

    /**
     * 将查询条件组装进一个product对象中
     */
    private Product compactShopCondition4Search(Long shopId, Long productCategoryId, String productName) {
        Product product = new Product();
        Shop shop = new Shop();
        shop.setShopId(shopId);
        product.setShop(shop);
        if(productCategoryId != -1L){
            ProductCategory productCategory = new ProductCategory();
            productCategory.setProductCategoryId(productCategoryId);
            product.setProductCategory(productCategory);
        }
        if(productName != null && !productName.equals("")){
            product.setProductName(productName);
        }
        //前端展示的店铺都是审核通过的店铺
        product.setEnableStatus(1);
        return product;
    }


}
