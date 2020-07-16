package com.qixia.o2o;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author 七夏
 * @create 2020-04-15
 *
 * 用来配置spring和junit整合，junit启动时加载springioc容器
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit，spring的配置文件位置
@ContextConfiguration(locations = {"classpath:spring/spring-dao.xml","classpath:spring/spring-service.xml"})
public class BaseTest {
}
