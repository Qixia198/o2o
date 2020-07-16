package com.qixia.o2o.util;

/**
 * @author 七夏
 * @create 2020-04-15
 */
public class PathUtil {
    //获取系统文件的分隔符属性
    private static String separator = System.getProperty("file.separator");

    /**
     * 返回项目图片的根路径
     */
    public static String getImgBasePath() {
        String os = System.getProperty("os.name");
        String basePath = "";
        if (os.toLowerCase().startsWith("win")) {
            basePath = "E:/projectdev/image";
        } else {
            basePath = "/Users/baidu/work/image";
        }
        basePath = basePath.replace("/", separator);
        return basePath;
    }

    /**
     * 返回项目图片的子路径
     */
    public static String getShopImagePath(long shopId) {
        String imagePath = "/upload/item/shop/" + shopId + "/";
        return imagePath.replace("/", separator);
    }

}
