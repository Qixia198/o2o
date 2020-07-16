package com.qixia.o2o.dao;

import com.qixia.o2o.BaseTest;
import com.qixia.o2o.bean.ProductImg;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 七夏
 * @create 2020-04-18
 */
public class ProductImgDaoTest extends BaseTest{
    @Autowired
    private ProductImgDao productImgDao;

    @Test
    public void testQueryProductImgList(){
        List<ProductImg> productImgList = productImgDao.queryProductImgList(2L);
        System.out.println("===============" + productImgList.get(0).getImgDesc());
    }

    @Test
    public void testABatchInsertProductImg(){
        ProductImg productImg = new ProductImg();
        productImg.setImgAddr("测试图片1");
        productImg.setImgDesc("测试1");
        productImg.setPriority(1);
        productImg.setCreateTime(new Date());
        productImg.setProductId(2L);

        ProductImg productImg1 = new ProductImg();
        productImg1.setImgAddr("测试图片2");
        productImg1.setImgDesc("测试2");
        productImg1.setPriority(1);
        productImg1.setCreateTime(new Date());
        productImg1.setProductId(2L);

        ArrayList<ProductImg> list = new ArrayList<>();
        list.add(productImg);
        list.add(productImg1);
        int effectedNum = productImgDao.batchInsertProductImg(list);
        System.out.println("================" + effectedNum);
    }

    @Test
    public void testDeleteProductImgByProductId(){
        int count = productImgDao.deleteProductImgByProductId(9L);
        System.out.println("===============" + count);
    }

}
