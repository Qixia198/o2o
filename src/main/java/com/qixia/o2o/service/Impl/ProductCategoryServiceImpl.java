package com.qixia.o2o.service.Impl;

import com.qixia.o2o.bean.ProductCategory;
import com.qixia.o2o.bean.Shop;
import com.qixia.o2o.dao.ProductCategoryDao;
import com.qixia.o2o.dao.ProductDao;
import com.qixia.o2o.dto.ProductCategoryExecution;
import com.qixia.o2o.enums.ProductCategoryStateEnum;
import com.qixia.o2o.exceptions.ProductCategotyOperationException;
import com.qixia.o2o.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 七夏
 * @create 2020-04-17
 */
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    @Autowired
    private ProductCategoryDao productCategoryDao;
    @Autowired
    private ProductDao productDao;

    @Override
    @Transactional
    public ProductCategoryExecution deleteProductCategory(long productCategoryId, long shopId) throws ProductCategotyOperationException {
        try {
            //删除商品类别之前将相关联的商品所属类别置为null
            int effectedNum = productDao.updateProductCategoryToNull(productCategoryId);
            if(effectedNum < 0){
                throw new ProductCategotyOperationException("商品类别更新失败");
            }
        } catch (Exception e) {
            throw new ProductCategotyOperationException("deleteProductCategory error:" + e.getMessage());
        }
        try{
            int effectedNum = productCategoryDao.deleteProductCategory(productCategoryId, shopId);
            if(effectedNum <= 0){
                throw new ProductCategotyOperationException("商品类别删除失败");
            }else{
                return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
            }
        }catch (RuntimeException e){
            throw new ProductCategotyOperationException("deleteProductCategory error:" + e.getMessage());
        }
    }

    @Override
    public List<ProductCategory> queryProductCategoryList(long shoId) {
        return productCategoryDao.queryProductCategoryList(shoId);
    }

    @Override
    @Transactional
    public ProductCategoryExecution batchAddProductCategory(List<ProductCategory> productCategoryList) throws ProductCategotyOperationException {
        if(productCategoryList != null && productCategoryList.size() > 0){
            try {
                int effectedNum = productCategoryDao.batchInsertProductCategory(productCategoryList);
                if(effectedNum <= 0){
                    throw new ProductCategotyOperationException("商品类别创建失败");
                }else{
                    return new ProductCategoryExecution(ProductCategoryStateEnum.SUCCESS);
                }
            } catch (Exception e) {
                throw new ProductCategotyOperationException("batchAddProductCategory error:" + e.getMessage());
            }
        }else{
            return new ProductCategoryExecution(ProductCategoryStateEnum.EMPTY_LIST);
        }
    }
}
