package com.qixia.o2o.util;

import com.qixia.o2o.dto.ImageHolder;
import com.qixia.o2o.exceptions.ProductOperationException;
import com.qixia.o2o.exceptions.ShopOperationException;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * @author 七夏
 * @create 2020-04-15
 */
public class ImageUtil {
    //获取系统文件的分隔符属性
    private static String separator = System.getProperty("file.separator");
    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final Random r = new Random();
    private static String Path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
    private static String basePath = Path.substring(1,Path.length());
    private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);

    public static String generateThumbnail(ImageHolder thumbnail, String targetAddr) {
        String realFileName = getRandomFileName();
        String extension = getFileExtension(thumbnail.getImageName());
        makeDirPath(targetAddr);
        String relativeAddr = targetAddr + realFileName + extension;
        logger.debug("current relativeAddr is:" + relativeAddr);
        basePath = basePath.replace("/", separator);
        logger.debug("basePath is:" + basePath);
        File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
        try {
            Thumbnails.of(thumbnail.getImage()).size(200, 200)
                    .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "\\watermark.jpg")), 0.25f)
            .outputQuality(0.8f).toFile(dest);
        } catch (IOException e){
            logger.error(e.toString());
            e.printStackTrace();
            throw new ShopOperationException(e.getMessage());
        }
        return relativeAddr;
    }

    /**
     * 创建目标路径所涉及到的目录，即/projectdev/image/xxx.jpg
     * @param targetAddr
     */
    private static void makeDirPath(String targetAddr) {
        String realFileParentPath = PathUtil.getImgBasePath() + targetAddr;
        File dirPath = new File(realFileParentPath);
        if(!dirPath.exists()){
            dirPath.mkdirs();
        }
    }

    /**
     * 获取输入文件的扩展名
     */
    private static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 生成随机文件的名字，当前年月日小时分钟秒+五位随机数
     * @return
     */
    public static String getRandomFileName() {
        //获取随机的五位数
        int rannum = r.nextInt(89999) + 10000;
        String nowTimeStr = sDateFormat.format(new Date());
        return nowTimeStr + rannum;
    }

/*    public static void main(String[] args) throws IOException {
        basePath = basePath.substring(1,basePath.length());
        basePath = basePath.replace("/", separator);
        System.out.println(basePath + "watermark.jpg");
        Thumbnails.of("C:/Users/七夏/Desktop/微笑.jpg").size(200, 200)
                .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "watermark.jpg")), 0.25f)
                .outputQuality(0.8f).toFile("C:/Users/七夏/Desktop/微笑1.jpg");
    }*/

    /**
     * storePath是文件路径还是目录路径
     * 如果为文件路径则删除文件
     *如果为目录路径则删除该路径下的所有文件
     */
    public static void deleteFileOrPath(String storePath){
        File fileOrPath = new File(PathUtil.getImgBasePath() + storePath);
        if(fileOrPath.exists()){
            if(fileOrPath.isDirectory()){
                File[] files = fileOrPath.listFiles();
                for(int i = 0;i < files.length;i++){
                    files[i].delete();
                }
            }
            fileOrPath.delete();
        }
    }

    /**
     * 处理详情图
     */
    public static String generateNormalImg(ImageHolder thumbnail, String targetAddr) {
        String realFileName = getRandomFileName();
        String extension = getFileExtension(thumbnail.getImageName());
        makeDirPath(targetAddr);
        String relativeAddr = targetAddr + realFileName + extension;
        logger.debug("current relativeAddr is:" + relativeAddr);
        basePath = basePath.replace("/", separator);
        logger.debug("basePath is:" + basePath);
        File dest = new File(PathUtil.getImgBasePath() + relativeAddr);
        try {
            Thumbnails.of(thumbnail.getImage()).size(337, 640)
                    .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basePath + "\\watermark.jpg")), 0.25f)
                    .outputQuality(0.9f).toFile(dest);
        } catch (ProductOperationException | IOException e){
            logger.error(e.toString());
            e.printStackTrace();
            throw new ProductOperationException(e.getMessage());
        }
        return relativeAddr;
    }
}
