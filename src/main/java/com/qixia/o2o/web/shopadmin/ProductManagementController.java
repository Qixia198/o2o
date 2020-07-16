package com.qixia.o2o.web.shopadmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qixia.o2o.bean.Product;
import com.qixia.o2o.bean.ProductCategory;
import com.qixia.o2o.bean.Shop;
import com.qixia.o2o.dto.ImageHolder;
import com.qixia.o2o.dto.ProductExecution;
import com.qixia.o2o.enums.ProductStateEnum;
import com.qixia.o2o.exceptions.ProductOperationException;
import com.qixia.o2o.service.ProductCategoryService;
import com.qixia.o2o.service.ProductService;
import com.qixia.o2o.util.CodeUtil;
import com.qixia.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * @author 七夏
 * @create 2020-04-18
 */
@Controller
@RequestMapping("/shop")
public class ProductManagementController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductCategoryService productCategoryService;
    //支持上传商品详情图的最大数量
    private static final int IMAGEMAXCOUNT = 6;

    @ResponseBody
    @RequestMapping(value = "/getpoductlistbyshop",method = RequestMethod.GET)
    private Map<String,Object> getPoductListByShop(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        //前端传入的页码数
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        //前端出入每页显示多少条记录数
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        //当前的店铺对象
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        if(pageIndex > -1 && pageSize > -1 && currentShop != null && currentShop.getShopId() != null){
            Long productCategoryId = HttpServletRequestUtil.getLong(request, "productCategoryId");
            String productName = HttpServletRequestUtil.getString(request, "productName");
            Product productCondition = compactProductCondition(currentShop.getShopId(),productCategoryId,productName);
            PageHelper.startPage(pageIndex, pageSize);
            ProductExecution pe = productService.getProductList(productCondition);
            PageInfo page = new PageInfo(pe.getProductList(),5);

            modelMap.put("success", true);
            modelMap.put("count", pe.getCount());
            modelMap.put("page",page);
        }else{
            modelMap.put("success", false);
            modelMap.put("errMsg", "传入信息有误");
        }
        return modelMap;
    }


    @ResponseBody
    @RequestMapping(value = "/getproductbyid",method = RequestMethod.GET)
    private Map<String,Object> getProductById(@RequestParam("productId") Long productId){
        Map<String,Object> modelMap = new HashMap<>();
        if(productId > 0){
            Product product = productService.getProductById(productId);
            List<ProductCategory> productCategories = productCategoryService.queryProductCategoryList(product.getShop().getShopId());
            modelMap.put("success", true);
            modelMap.put("product", product);
            modelMap.put("productCategories",productCategories);
        }else{
            modelMap.put("success", false);
            modelMap.put("errMsg", "商品ID为空");
        }
        return modelMap;
    }

    @ResponseBody
    @RequestMapping(value = "/modifyproduct",method = RequestMethod.POST)
    private Map<String,Object> modifyProduct(HttpServletRequest request) throws IOException {
        Map<String,Object> modelMap = new HashMap<>();
        boolean statusChange = HttpServletRequestUtil.getBoolean(request, "statusChange");
        if(!statusChange && !CodeUtil.checkVerifyCode(request)){
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码错误或不能为空");
            return modelMap;
        }
        Product product = null;
        ObjectMapper mapper = new ObjectMapper();
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        MultipartHttpServletRequest multipartRequest = null;
        ImageHolder thumbnailImg = null;
        LinkedList<ImageHolder> list = new LinkedList<>();
        try {
            if(multipartResolver.isMultipart(request)){
                thumbnailImg = handleImage(request, list);
            }
        } catch (Exception e) {
           modelMap.put("success", false);
           modelMap.put("errMsg", e.getMessage());
           return modelMap;
        }
        try {
            String productStr = HttpServletRequestUtil.getString(request, "productStr");
            product = mapper.readValue(productStr, Product.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        //更新
        if(product != null){
            try {
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                product.setShop(currentShop);
                ProductExecution pe = productService.modifyProduct(product, thumbnailImg, list);
                if(pe.getState() == ProductStateEnum.SUCCESS.getState()){
                    modelMap.put("success", true);
                }else{
                    modelMap.put("success", false);
                    modelMap.put("errMsg", pe.getStateInfo());
                }
            } catch (ProductOperationException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
                return modelMap;
            }
        }else{
            modelMap.put("success", false);
            modelMap.put("errMsg", "商品信息不能为空");
        }
        return modelMap;
    }

    @ResponseBody
    @RequestMapping(value = "/addproduct",method = RequestMethod.POST)
    private Map<String,Object> addProduct(HttpServletRequest request){
        Map<String,Object> modelMap = new HashMap<>();
        //验证码校验
        if(!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码输入错误或为空");
            return modelMap;
        }
        //接收前端传入的商品（并初始化），缩略图，详情图列表实体类
        ObjectMapper mapper = new ObjectMapper();
        Product product = null;
        String productStr = HttpServletRequestUtil.getString(request, "productStr");
        MultipartHttpServletRequest multipartRequest = null;
        ImageHolder thumbnail = null;
        List<ImageHolder> productImgList = new LinkedList<>();
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());

        try {
            //若请求中存在文件流，则取出相关的文件（包括缩略图和详情图）
            if(multipartResolver.isMultipart(request)){
                thumbnail = handleImage(request, productImgList);
            }else{
                modelMap.put("success", false);
                modelMap.put("errMsg", "上传的图片不能为空");
                return modelMap;
            }
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }
        try {
            //尝试获取前端传入的表单中String流并将其准换成Product实体类
            product = mapper.readValue(productStr, Product.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }
        //经过上述的流程，如果商品信息，缩略图和详情图都不为空，则就将执行增加方法
        if(product != null && productImgList != null && thumbnail != null){
            try {
                //从Session域中取出当前登录的店铺的id值
                Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
                Shop shop = new Shop();
                shop.setShopId(currentShop.getShopId());
                product.setShop(shop);
                //执行增加操作
                ProductExecution pe = productService.addProduct(product, thumbnail, productImgList);
                if(pe.getState() == ProductStateEnum.SUCCESS.getState()){
                    modelMap.put("success", true);
                }else{
                    modelMap.put("success", false);
                    modelMap.put("errMsg", pe.getStateInfo());
                }
            } catch (ProductOperationException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
        }else{
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入商品信息");
        }
        return modelMap;
    }

    /*
    组合查询，将前端输入进来的查询条件拼装一起
     */
    private Product compactProductCondition(Long shopId, Long productCategoryId, String productName) {
        Product productCondition = new Product();
        Shop shop = new Shop();
        shop.setShopId(shopId);
        productCondition.setShop(shop);
        //如果有指定类别的要求则添加进去
        if(productCategoryId != -1L){
            ProductCategory productCategory = new ProductCategory();
            productCategory.setProductCategoryId(productCategoryId);
            productCondition.setProductCategory(productCategory);
        }
        //若有商品名模糊查询的要求则添加进去
        if(productName != null){
            productCondition.setProductName(productName);
        }
        return productCondition;
    }

    private ImageHolder handleImage(HttpServletRequest request, List<ImageHolder> productImgList) throws IOException {
        MultipartHttpServletRequest multipartRequest;
        ImageHolder thumbnail;
        multipartRequest = (MultipartHttpServletRequest)request;
        //取出缩略图并构建ImageHodler
        CommonsMultipartFile thumbnailFile = (CommonsMultipartFile) multipartRequest.getFile("thumbnail");
        thumbnail = new ImageHolder(thumbnailFile.getOriginalFilename(),thumbnailFile.getInputStream());

        //取出详情图列表并且构建List<ImageHolder>
        for (int i = 0; i < IMAGEMAXCOUNT; i++) {
            CommonsMultipartFile productImgFile = (CommonsMultipartFile) multipartRequest.getFile("productImg" + i);
            if(productImgFile != null){
                ImageHolder productImg = new ImageHolder(productImgFile.getOriginalFilename(),productImgFile.getInputStream());
                productImgList.add(productImg);
            }else{
                break;
            }
        }
        return thumbnail;
    }


}
