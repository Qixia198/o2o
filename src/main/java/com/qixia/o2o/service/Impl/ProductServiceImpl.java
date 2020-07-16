package com.qixia.o2o.service.Impl;

import com.qixia.o2o.bean.Product;
import com.qixia.o2o.bean.ProductImg;
import com.qixia.o2o.dao.ProductDao;
import com.qixia.o2o.dao.ProductImgDao;
import com.qixia.o2o.dto.ImageHolder;
import com.qixia.o2o.dto.ProductExecution;
import com.qixia.o2o.enums.ProductStateEnum;
import com.qixia.o2o.exceptions.ProductOperationException;
import com.qixia.o2o.service.ProductService;
import com.qixia.o2o.util.ImageUtil;
import com.qixia.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author 七夏
 * @create 2020-04-18
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductImgDao productImgDao;

    /**
     * 查询某店铺下的所有商品，可以输入的条件：商品名（模糊），商品状态，商铺id，商品类别
     * @param product
     */
    @Override
    public ProductExecution getProductList(Product product){
        List<Product> products = productDao.queryProductList(product, 0, 0);
        int count = productDao.queryProductCount(product);
        ProductExecution pe = new ProductExecution();
        pe.setProductList(products);
        pe.setCount(count);
        return pe;
    }

    @Override
    @Transactional
    public ProductExecution modifyProduct(Product product,ImageHolder thumbnail,List<ImageHolder> productImgList) throws ProductOperationException {
        //判空
        if(product != null && product.getProductId() != null && product.getShop() != null && product.getShop().getShopId() != null){
            //赋默认值
            product.setLastEditTime(new Date());
            if(thumbnail != null){
                Product tempProduct = productDao.queryProductById(product.getProductId());
                if(tempProduct.getImgAddr() != null){
                    ImageUtil.deleteFileOrPath(tempProduct.getImgAddr());
                }
                addThumbnail(product, thumbnail);
            }
            if(productImgList != null && productImgList.size() > 0){
                deleteProductImgList(product.getProductId());
                addProductImgList(product, productImgList);
            }
            try {
                int effectedNum = productDao.updateProduct(product);
                if(effectedNum <= 0){
                   throw new ProductOperationException("更新商品信息失败");
                }
                return new ProductExecution(product,ProductStateEnum.SUCCESS);
            } catch (Exception e) {
                throw new ProductOperationException("更新商品信息失败：" + e.toString());
            }
        }else{
            return new ProductExecution(ProductStateEnum.EMPTY);
        }
    }

    /**
     * 根据productId查询商品
     * @param productId
     */
    @Override
    public Product getProductById(long productId) {
        return productDao.queryProductById(productId);
    }

    /*
        1.处理缩略图，获取缩略图的相对路径并赋值给product
        2.往tb_product写入商品信息，获取productId
        3.结合productId批量处理商品详情图
        4.将商品详情图列表批量插入tb_product_img中
         */
    @Override
    @Transactional
    public ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgList) throws ProductOperationException {
        //判空
        if(product != null && product.getShop() != null && product.getShop().getShopId() != null){
            //给商品设置默认属性
            product.setCreateTime(new Date());
            product.setLastEditTime(new Date());
            //商品默认为上架状态
            product.setEnableStatus(1);
            //若商品缩略图不为空就添加
            if(thumbnail != null){
                addThumbnail(product,thumbnail);
            }
            //创建商品信息
            try {
                int effectedNum = productDao.insertProduct(product);
                if(effectedNum <= 0){
                    throw new ProductOperationException("创建商品失败");
                }
            } catch (Exception e) {
                throw new ProductOperationException("创建商品失败：" + e.toString());
            }
            //如果商品详情图不为空时
            if(productImgList != null && productImgList.size() >= 0){
                addProductImgList(product,productImgList);
            }
            return new ProductExecution(product, ProductStateEnum.SUCCESS);
        }else{
            return new ProductExecution(ProductStateEnum.EMPTY);
        }
    }

    /**
     * 添加详情图
     * @param product
     * @param productImgHolderList
     */
    private void addProductImgList(Product product, List<ImageHolder> productImgHolderList){
        //获取图片的存储路径，直接添加到相应的店铺文件夹下
        String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
        List<ProductImg> productImgList = new LinkedList<>();
        //遍历图片一次去处理，并添加进productImg实体类中
        for (ImageHolder imageHolder : productImgHolderList) {
            String imgAddr = ImageUtil.generateNormalImg(imageHolder, dest);
            ProductImg productImg = new ProductImg();
            productImg.setImgAddr(imgAddr);
            productImg.setProductId(product.getProductId());
            productImg.setCreateTime(new Date());
            productImgList.add(productImg);
        }
        //如果确实是有图片需要添加，就执行批量添加操作
        if(productImgList.size() > 0 ){
            try {
                int effectedNum = productImgDao.batchInsertProductImg(productImgList);
                if(effectedNum <= 0){
                    throw new ProductOperationException("创建商品详情图失败");
                }
            } catch (Exception e) {
                throw new ProductOperationException("创建商品详情图失败：" + e.toString());
            }
        }
    }

    /**
     * 删除详情图
     */
    private void deleteProductImgList(long productId){
        List<ProductImg> productImgList = productImgDao.queryProductImgList(productId);
        if(productImgList != null && productImgList.size() > 0){
            for (ProductImg productImg : productImgList) {
                ImageUtil.deleteFileOrPath(productImg.getImgAddr());
            }
            productImgDao.deleteProductImgByProductId(productId);
        }
    }

    /**
     * 添加缩略图
     * @param product
     * @param thumbnail
     */
    private void addThumbnail(Product product, ImageHolder thumbnail) {
        String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
        String thumbnailAddr = ImageUtil.generateThumbnail(thumbnail, dest);
        product.setImgAddr(thumbnailAddr);
    }
}
