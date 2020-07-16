package com.qixia.o2o.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qixia.o2o.BaseTest;
import com.qixia.o2o.bean.Area;
import com.qixia.o2o.bean.PersonInfo;
import com.qixia.o2o.bean.Shop;
import com.qixia.o2o.bean.ShopCategory;
import com.qixia.o2o.dto.ImageHolder;
import com.qixia.o2o.dto.ShopExecution;
import com.qixia.o2o.enums.ShopStateEnum;
import com.qixia.o2o.exceptions.ShopOperationException;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * @author 七夏
 * @create 2020-04-15
 */
public class ShopServiceTest extends BaseTest{
    @Autowired
    private ShopService shopService;

    @Test
    public void testQueryShopListAndQueryShopCount(){
        Shop shopCondition = new Shop();
        ShopCategory sc = new ShopCategory();
        sc.setShopCategoryId(2L);
        shopCondition.setShopCategory(sc);
        ShopExecution se = shopService.getShopList(shopCondition, 2, 1);
        System.out.println("店铺列表数为：" + se.getShopList());
        System.out.println(se.getCount());
    }

    @Test
    @Ignore
    public void testModifyShop() throws ShopOperationException,FileNotFoundException {
        Shop shop = shopService.getShopById(1L);
        shop.setShopId(1L);
        shop.setShopName("修改后的店铺");
        File shopImg = new File("C:\\Users\\七夏\\Desktop\\微笑.jpg");
        InputStream is = new FileInputStream(shopImg);
        ImageHolder imageHolder = new ImageHolder("微笑.jpg\"",is);
        ShopExecution shopExecution = shopService.modifyShop(shop,imageHolder);
        System.out.println("新地址为：" + shopExecution.getShop());
    }

    @Test
    @Ignore
    public void testAddShop() throws FileNotFoundException {
        Shop shop = new Shop();
        PersonInfo owner = new PersonInfo();
        Area area = new Area();
        ShopCategory shopCategory = new ShopCategory();
        owner.setUserId(1L);
        area.setAreaId(2);
        shopCategory.setShopCategoryId(1L);
        shop.setOwner(owner);
        shop.setArea(area);
        shop.setShopCategory(shopCategory);
        shop.setShopName("测试的店铺3");
        shop.setShopDesc("test3");
        shop.setShopAddr("test3");
        shop.setPhone("test3");
        shop.setCreateTime(new Date());
        shop.setEnableStatus(ShopStateEnum.CHECK.getState());
        shop.setAdvice("审核中");
        File shopImg = new File("C:\\Users\\七夏\\Desktop\\微笑.jpg");
        InputStream is = new FileInputStream(shopImg);
        ImageHolder imageHolder = new ImageHolder("微笑.jpg\"",is);
        ShopExecution shopExecution = shopService.addShop(shop,imageHolder);
        System.out.println(shopExecution.getStateInfo());
    }
}
