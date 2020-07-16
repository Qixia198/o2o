package com.qixia.o2o.web.shopadmin;

import com.qixia.o2o.bean.ProductCategory;
import com.qixia.o2o.bean.Shop;
import com.qixia.o2o.dto.ProductCategoryExecution;
import com.qixia.o2o.dto.Result;
import com.qixia.o2o.enums.ProductCategoryStateEnum;
import com.qixia.o2o.exceptions.ProductCategotyOperationException;
import com.qixia.o2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 七夏
 * @create 2020-04-17
 */
@Controller
@RequestMapping("/shop")
public class ProductCategoryManagementController {
    @Autowired
    private ProductCategoryService productCategoryService;

    @ResponseBody
    @RequestMapping(value = "/removeproductcategory",method = RequestMethod.POST)
    private Map<String,Object> removeProductCategory(Long productCategoryId,HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        if(productCategoryId != null && productCategoryId > 0){
            Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
            long shopId = currentShop.getShopId();
            try {
                ProductCategoryExecution pe = productCategoryService.deleteProductCategory(productCategoryId, shopId);
                if(pe.getState() == ProductCategoryStateEnum.SUCCESS.getState()){
                    modelMap.put("success", true);
                }else{
                    modelMap.put("success", false);
                    modelMap.put("errMsg", pe.getStateInfo());
                }
            } catch (ProductCategotyOperationException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
                return modelMap;
            }
        }else{
            modelMap.put("success", false);
            modelMap.put("errMsg","请至少选择一个类别");
        }
        return modelMap;
    }


    @RequestMapping(value = "/addproductcategories",method = RequestMethod.POST)
    @ResponseBody
    private Map<String,Object> addProductCategories(@RequestBody List<ProductCategory> productCategoryList,
                                                    HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        for (ProductCategory pc : productCategoryList) {
            pc.setShopId(currentShop.getShopId());
            pc.setCreateTime(new Date());
        }
        if(productCategoryList != null && productCategoryList.size() > 0){
            try {
                ProductCategoryExecution pe = productCategoryService.batchAddProductCategory(productCategoryList);
                if(pe.getState() == ProductCategoryStateEnum.SUCCESS.getState()){
                    modelMap.put("success", true);
                    return modelMap;
                }else{
                    modelMap.put("success", false);
                    modelMap.put("errMsg", pe.getStateInfo());
                    return modelMap;
                }
            } catch (RuntimeException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
                return modelMap;
            }
        }else{
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入至少一个商品类别");
            return modelMap;
        }
    }


    @ResponseBody
    @RequestMapping(value = "/getproductcategory",method = RequestMethod.GET)
    private Result<List<ProductCategory>> getProductCategory( HttpServletRequest request){
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        List<ProductCategory> list = null;
        if(currentShop != null && currentShop.getShopId() > 0){
            list = productCategoryService.queryProductCategoryList(currentShop.getShopId());
            return new Result<List<ProductCategory>>(true,list);
        }else{
            ProductCategoryStateEnum ps = ProductCategoryStateEnum.INNER_ERROR;
            return new Result<List<ProductCategory>>(false,ps.getStateInfo(),ps.getState());
        }
    }
}
