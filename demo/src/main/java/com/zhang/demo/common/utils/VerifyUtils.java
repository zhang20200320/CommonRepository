package com.zhang.demo.common.utils;

import org.springframework.util.ResourceUtils;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

/**
 * 该工具类来自网络(摘录记录便于学习) 网络链接：https://blog.csdn.net/lileLife/article/details/80874707?utm_source=app
 * java 生成随机验证码及图片
 * byte[]数组转图片文件
 * 图片文件转byte[]
 */
public class VerifyUtils {
    // 验证码字符集
    private static final char[] chars = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    // 字符数量
    private static final int SIZE = 4;
    // 干扰线数量
    private static final int LINES = 5;
    // 宽度
    private static final int WIDTH = 80;
    // 高度
    private static final int HEIGHT = 40;
    // 字体大小
    private static final int FONT_SIZE = 30;

    public static final String RANDOMCODEKEY = "RANDOMVALIDATECODEKEY";//放到session中的key

    /**
     * （一）
     * 生成随机验证码及图片
     * Object[0]：验证码字符串；
     * Object[1]：验证码图片。
     */
    public static Object[] createImage() {

        StringBuffer sb = new StringBuffer();
        // 1.创建空白图片
        BufferedImage image = new BufferedImage(
                WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        // 2.获取图片画笔
        Graphics graphic = image.getGraphics();
        // 3.设置画笔颜色
        graphic.setColor(Color.LIGHT_GRAY);
        // 4.绘制矩形背景
        graphic.fillRect(0, 0, WIDTH, HEIGHT);
        // 5.画随机字符
        Random ran = new Random();
        for (int i = 0; i < SIZE; i++) {
            // 取随机字符索引
            int n = ran.nextInt(chars.length);
            // 设置随机颜色
            graphic.setColor(getRandomColor());
            // 设置字体大小
            graphic.setFont(new Font(
                    null, Font.BOLD + Font.ITALIC, FONT_SIZE));
            // 画字符
            graphic.drawString(
                    chars[n] + "", i * WIDTH / SIZE, HEIGHT * 2 / 3);
            // 记录字符
            sb.append(chars[n]);
        }
        // 6.画干扰线
        for (int i = 0; i < LINES; i++) {
            // 设置随机颜色
            graphic.setColor(getRandomColor());
            // 随机画线
            graphic.drawLine(ran.nextInt(WIDTH), ran.nextInt(HEIGHT),
                    ran.nextInt(WIDTH), ran.nextInt(HEIGHT));
        }
        // 7.返回验证码和图片
//        return new Object[]{sb.toString(), image};

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "JPEG", baos);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Object[]{sb.toString(), baos.toByteArray()};

    }

    /**
     * （二）
     * 生成随机验证码及图片
     * Object[0]：验证码字符串；
     * Object[1]：验证码图片。
     */
    public static void createImage1(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();

        StringBuffer sb = new StringBuffer();
        // 1.创建空白图片
        BufferedImage image = new BufferedImage(
                WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        // 2.获取图片画笔
        Graphics graphic = image.getGraphics();
        // 3.设置画笔颜色
        graphic.setColor(Color.LIGHT_GRAY);
        // 4.绘制矩形背景
        graphic.fillRect(0, 0, WIDTH, HEIGHT);
        // 5.画随机字符
        Random ran = new Random();
        for (int i = 0; i < SIZE; i++) {
            // 取随机字符索引
            int n = ran.nextInt(chars.length);
            // 设置随机颜色
            graphic.setColor(getRandomColor());
            // 设置字体大小
            graphic.setFont(new Font(
                    null, Font.BOLD + Font.ITALIC, FONT_SIZE));
            // 画字符
            graphic.drawString(
                    chars[n] + "", i * WIDTH / SIZE, HEIGHT * 2 / 3);
            // 记录字符
            sb.append(chars[n]);
        }
        // 6.画干扰线
        for (int i = 0; i < LINES; i++) {
            // 设置随机颜色
            graphic.setColor(getRandomColor());
            // 随机画线
            graphic.drawLine(ran.nextInt(WIDTH), ran.nextInt(HEIGHT),
                    ran.nextInt(WIDTH), ran.nextInt(HEIGHT));
        }
        // 7.返回验证码和图片
//        return new Object[]{sb.toString(), image};

        //将生成的随机字符串保存到session中
        session.removeAttribute(RANDOMCODEKEY);
        session.setAttribute(RANDOMCODEKEY, sb.toString());
        try {
            ImageIO.write(image, "JPEG", response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 随机取色
     */
    public static Color getRandomColor() {
        Random ran = new Random();
        Color color = new Color(ran.nextInt(256),
                ran.nextInt(256), ran.nextInt(256));
        return color;
    }

    public static void main(String[] args) {
        Object[] objs = VerifyUtils.createImage();
        String randomStr = (String) objs[0];


    }

    /**
     * byte数组转图片
     * @return
     */
    public static String byte2image(byte[] data, HttpServletRequest req) {
//        if(data.length<3||path.equals("")) return;
        FileImageOutputStream fout = null;
        String fileDirPath = "";
        String file = "captcha.jpg";
        try{
            // target目录下，classes-static-images-imageCaptcha
            fileDirPath = ResourceUtils.getURL("classpath:").getPath().concat("static/images/imageCaptcha/");
//            fileDirPath = new String(Constant.LOCAL_FILE_URL + File.separator + "images"); // 本地存储文件
//            fileDirPath = new String(Constant.ROOT_DIRECTORY + Constant.IMAGES_URL);
            File newFile = isExistsDirectory(fileDirPath, true, "captcha.jpg");
            fout = new FileImageOutputStream(newFile);
            //将字节写入文件
            fout.write(data, 0, data.length);
        } catch(Exception ex) {
            System.out.println("Exception: " + ex);
            ex.printStackTrace();
        } finally {
            try{
                fout.close();
                System.out.println("Make Picture success,Please find image in " + fileDirPath);
            }catch (IOException e) {
                System.out.println("IOException: " + e);
                e.printStackTrace();
            }
        }
        String url = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/images/imageCaptcha/" + file;
        System.out.println(fileDirPath.concat("/").concat(file));
//        return fileDirPath.concat("/").concat(file);
        return url;
    }

    /**
     * 图片转byte数组
     * @param path
     * @return
     */
    public static byte[] image2byte(String path) {

        byte[] data = null;
        FileImageInputStream input = null;
        try {
            input = new FileImageInputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
            output.close();
            input.close();
        } catch (FileNotFoundException ex1) {
            ex1.printStackTrace();
        } catch (IOException ex1) {
            ex1.printStackTrace();
        }
        return data;
    }

    /**
     * 1. 判断路径目录是否存在，不存在则创建
     * 2. 判断文件是否存在，不存在则创建
     * @param fileDirPath
     * @param flag 是否创建文件 true or false
     * @param specifyFile 指定文件名称（带后缀如：captcha.jpg）
     * @return
     */
    public static File isExistsDirectory(String fileDirPath, boolean flag, String specifyFile) {
        File fileDirs = new File(fileDirPath);
        if(!fileDirs.exists()){
            fileDirs.mkdirs();
        }
        if (flag){
            File newFile = new File(fileDirPath.concat(specifyFile));
            try{
                if (!newFile.exists()) {
                    newFile.createNewFile();
                }
            } catch (IOException e) {
                System.out.println("IOException: " + e.getStackTrace());
            }
            return newFile;
        }

        return fileDirs;
    }

    /**
     * 判断路径是否存在，不存在则创建
     * @param fileDirPath 目录
     * @return
     */
    public static File isExistsDirectory(String fileDirPath) {
        return isExistsDirectory(fileDirPath, false, null);
    }



}
