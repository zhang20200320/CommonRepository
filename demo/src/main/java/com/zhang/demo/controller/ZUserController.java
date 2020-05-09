package com.zhang.demo.controller;

import com.github.pagehelper.PageInfo;
import com.zhang.demo.common.Annotation.NoRepeatSubmit;
import com.zhang.demo.common.CommonResult;
import com.zhang.demo.common.utils.Constant;
import com.zhang.demo.common.utils.PageUtils;
import com.zhang.demo.common.utils.QRCode.QRCodeUtil;
import com.zhang.demo.common.utils.RedisUtils;
import com.zhang.demo.common.utils.VerifyUtils;
import com.zhang.demo.entity.ZUserEntity;
import com.zhang.demo.form.ZCaptchaForm;
import com.zhang.demo.form.ZUserForm;
import com.zhang.demo.job.entity.JdbcBean;
import com.zhang.demo.job.task.DbTask;
import com.zhang.demo.service.ZUserService;
import com.zhang.demo.vo.ZUserVo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 用户
 * @author zhang
 * @date 2020-04-20 14:38:30
 */
@Api(tags = "用户管理", value= "用户管理", description = "用户接口") // 表示类的作用
//@ApiSort(value = 1)
@RestController
@RequestMapping("/user")
public class ZUserController {

    private static final Logger logger = LoggerFactory.getLogger(ZUserController.class);

    @Autowired
    private ZUserService zUserService;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 登录
     * @param zUserForm
     * @return
     */
    @ApiOperationSort(1) // 用于接口方法排序
    @ApiOperation(httpMethod = "POST", value = "用户登录" ,  notes="登录用户")
    @PostMapping(value = "/login")
    public CommonResult<ZUserForm> login(@Validated(value = {ZUserForm.ZUserLogin .class}) @RequestBody ZUserForm zUserForm) {
        logger.info("登陆中...");

        logger.info("登录成功");
        return CommonResult.success(zUserForm);
    }

    /**
     * 登出
     * @return
     */
    @ApiOperationSort(2) // 用于接口方法排序
    @ApiOperation(httpMethod = "POST", value = "用户登出" ,  notes="用户退出登录")
    @PostMapping(value = "/logout")
    public CommonResult logout() {
        return CommonResult.success(null);
    }

    /**
     * 注册
     * 应用swagger
     *      @ApiOperationSort ———— 用于接口方法排序，使用该注解需要在swagger文档页面中文档管理中个性化设置中
     *                             勾选启动SwaggerBootstrapUi提供的增强功能，并保存。
     *      @ApiOperation ———— 表示方法说明
     * @param zUserForm
     * @return
     */
    @ApiOperationSort(3)
    @ApiOperation(httpMethod = "POST", value = "新增用户" ,  notes="新增注册") // 表示方法说明
    @NoRepeatSubmit(lockTime = 30)
    @PostMapping(value = "/register")
    public CommonResult<ZUserVo> register(@Validated(value = {ZUserForm.ZUserRegister.class}) @RequestBody ZUserForm zUserForm) {
        return CommonResult.success(zUserService.register(zUserForm));
    }

    @ApiOperation("修改指定用户信息")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public CommonResult update(@Validated(value = {ZUserForm.ZUserUpdate.class}) @RequestBody ZUserForm zUserForm) {
        boolean count = zUserService.updateById(zUserForm);
        if (count) {
            return CommonResult.success("更新用户成功");
        }
        return CommonResult.failed("更新用户失败");
    }

    /**
     * 用户信息
     * swagger2应用
     * @ApiImplicitParams : 用在方法上包含一组参数说明。
     * @ApiImplicitParam : 用来注解来给方法入参增加说明。
     *  参数：
     *    ·paramType：指定参数放在哪个地方
     *       ··header：请求参数放置于Request Header，使用@RequestHeader获取
     *       ··query：请求参数放置于请求地址，使用@RequestParam获取
     *       ··path：（用于restful接口）-->请求参数的获取：@PathVariable
     *       ··body：（不常用）
     *       ··form（不常用）
     *    ·name：参数名
     *    ·dataType：参数类型
     *    ·required：参数是否必须传(true | false)
     *    ·value：说明参数的意思
     *    ·defaultValue：参数的默认值
     *
     * @param userId 用户标识id
     * @return
     */
    @ApiOperationSort(4) // 用于接口方法排序
    @ApiOperation(httpMethod = "GET", value = "用户信息" ,  notes="获取指定用户详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "userId", value = "用户标识id", required = true, dataType = "String")
    })
    @GetMapping(value = "/zUserInfo")
    public CommonResult<ZUserVo> zUserInfo(@RequestParam(value = "userId", required = true) String userId) {
        return CommonResult.success(zUserService.queryZUserInfo(userId));
    }

    /**
     * 用户列表
     * @param keyword
     * @param pageSize
     * @param pageNum
     * @return
     */
    @ApiOperationSort(5) // 用于接口方法排序
    @ApiOperation(httpMethod = "GET", value = "用户列表" ,  notes="获取分页后的用户列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "关键字", required = false, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "pageSize", value = "条数", paramType = "query", dataType = "Integer", defaultValue = "5"),
            @ApiImplicitParam(name = "pageNum", value = "页数", paramType = "query", dataType = "Integer", defaultValue = "1")
    })
    @GetMapping(value = "/zUserList")
    public CommonResult<PageUtils> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
//        List<ZUserEntity> zUserList = zUserService.list(keyword, pageSize, pageNum);
        PageUtils zUserPageList = zUserService.list(keyword, pageSize, pageNum);
        return CommonResult.success(zUserPageList);
    }

    /**
     * 上传文件
     * @param file
     * @param req
     * @return
     * @throws IOException
     */
    @ApiOperationSort(6) // 用于接口方法排序
    @ApiOperation(httpMethod = "POST", value = "上传文件" ,  notes="上传文件并存储在项目target-static-upload目录下")
    @PostMapping(value = "/uploadFile")
    public CommonResult<ZUserForm> uploadFile(MultipartFile file, HttpServletRequest req) throws IOException {
        return CommonResult.success("上传成功", zUserService.uploadFile(file, req));
    }

    /**
     * 获取图片验证码
     * 后台创建图片验证码，并redis缓存验证码字符
     * 返回图片验证码byte[]
     * @param ip 调用接口客户端本地ip地址
     */
    @ApiOperationSort(7) // 用于接口方法排序
    @ApiOperation(httpMethod = "GET", value = "获取图片验证码" ,  notes="获取图片验证码，后台创建图片验证码，并redis缓存验证码字符，返回图片验证码byte[]")
    @GetMapping(value = "/getCaptcha/{ip}")
    public CommonResult<byte[]> getCaptcha(@PathVariable String ip, HttpServletRequest req) {
        logger.info("获取中...");

        Object[] imageCaptchaInfo = VerifyUtils.createImage();

        System.out.println("imageCaptcha result ： " + imageCaptchaInfo);
        if (imageCaptchaInfo == null || imageCaptchaInfo.length != 2) {
            return CommonResult.failed("Failed to get picture verification code");
        }
        String randmStr = (String) imageCaptchaInfo[0];
        redisUtils.set("captcha_" + ip, randmStr);
        Object[] b = (Object[])imageCaptchaInfo;
        System.out.println("imageCaptchaInfo[] result: " + Arrays.deepToString(imageCaptchaInfo));

        /**图片byte[]数组转图片保存指定目录，并返回访问地址*/
        String url = VerifyUtils.byte2image((byte[]) imageCaptchaInfo[1], req);
        System.out.println("url: " + url);

        /**指定地址图片文件转byte[]数组*/
        String url1 = "E:\\projects\\CommonProjects\\demo\\target\\classes\\static\\images\\imageCaptcha\\captcha.jpg";
        byte[] imageByte = VerifyUtils.image2byte(url1);
        System.out.println("imageByte[]: " + Arrays.toString(imageByte));

        logger.info("获取成功");
        return CommonResult.success("获取图片验证码地址：" + url);
    }

    /**
     * 生成验证码并返回客户端
     * 验证码字符缓存在session中
     * @param request
     * @param response
     */
    @ApiOperationSort(8) // 用于接口方法排序
    @ApiOperation(httpMethod = "GET", value = "生成图片验证码" ,  notes="生成验证码并返回客户端")
    @GetMapping(value = "/getCaptcha111")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) {
        logger.info("生成中...");

        try {
            response.setContentType("image/jpeg");//设置相应类型,告诉浏览器输出的内容为图片
            response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);
            VerifyUtils.createImage1(request, response);//输出验证码图片方法
        } catch (Exception e) {
            logger.error("获取验证码失败");
            e.printStackTrace();
        }
        logger.info("生成成功");
    }

    /**
     * 校验验证码
     *
     * @param zCaptchaForm
     * @return
     */
    @ApiOperationSort(9) // 用于接口方法排序
    @ApiOperation(httpMethod = "POST", value = "校验图片验证码" ,  notes="检验图片验证码是否正确")
    @PostMapping("/verify")
    public CommonResult<String> sendCaptcha(@Validated @RequestBody ZCaptchaForm zCaptchaForm) {
        logger.info("开始校验");
        // 验证
        String ip = zCaptchaForm.getIp();
        String captcha = zCaptchaForm.getCaptcha();
        String  redisCaptcha = redisUtils.get("captcha_" + ip);
        if (StringUtils.isEmpty(redisCaptcha)) {
            logger.info("校验失败");
            return CommonResult.failed("验证码获取失败");
        }
        if (StringUtils.isEmpty(redisCaptcha) || !captcha.equalsIgnoreCase(redisCaptcha)) {
            logger.info("校验失败");
            return CommonResult.failed("验证失败");
        } else {
            logger.info("校验成功");
            return CommonResult.success("验证成功");
        }

    }

    /**
     * 生成二维码
     * @return
     */
    @ApiOperationSort(10) // 用于接口方法排序
    @ApiOperation(httpMethod = "GET", value = "生成二维码" ,  notes="生成二维码并存储指定地址")
    @GetMapping("/createQRCode")
    public String createQRCode(){
        logger.info("生成中...");
        // 内容
        String text = Constant.MY_BLOG_ADDRESS;
        // 嵌入二维码的图片路径E:\photo
        String imgPath = Constant.QRCODE_STORAGE_PATH.concat("zhang.jpg");
        // 生成的二维码的路径及名称
        String destPath = Constant.QRCODE_STORAGE_PATH.concat("QRCodeImages.jpg");
        String str = "";

        try {
            //生成二维码
            QRCodeUtil.encode(text, imgPath, destPath, true);
            // 解析二维码
            str = QRCodeUtil.decode(destPath);
        }catch (Exception e) {
            System.out.println("生成/解析二维码异常：" + e);
        }
        logger.info("生成成功");

        // 打印出解析出的内容
        System.out.println(str);
        return str;
    }


    /**
     * 生成二维码并返回客户端
     * @param response
     */
    @ApiOperationSort(11) // 用于接口方法排序
    @ApiOperation(httpMethod = "GET", value = "创建二维码图片" ,  notes="生成二维码并返回客户端")
    @GetMapping(value = "/getQRCode")
    public void getQRCode(HttpServletResponse response) {
        logger.info("获取中...");

        try {
            response.setContentType("image/jpg");//设置相应类型,告诉浏览器输出的内容为图片
            response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);
            // 生成二维码并写入到OutputStream中输出
            QRCodeUtil.encode(Constant.MY_BLOG_ADDRESS, Constant.QRCODE_STORAGE_PATH.concat("zhang.jpg")
                    , response.getOutputStream(), true);
        } catch (Exception e) {
            logger.error("获取二维码失败");
            e.printStackTrace();
        }
        logger.info("获取成功");
    }

    /**
     * 测试方法间互相调用，事务的传播特性以及回滚
     */
    @ApiOperationSort(12) // 用于接口方法排序
    @ApiOperation(httpMethod = "GET", value = "测试spring事务应用", notes="测试方法间互相调用，事务的传播特性以及回滚")
    @GetMapping("/test")
    public void test(){
        zUserService.testUser1();
    }

    /**
     * 导入sql文件数据到指定数据库中
     * @return
     */
    @ApiOperationSort(13) // 用于接口方法排序
    @ApiOperation(value = "导入数据", notes="导入sql文件数据到指定数据库中")
    @PostMapping("/executeDataImport")
    public CommonResult executeImport(@RequestBody JdbcBean jdbcBean){
        boolean flag = false;

//        String filePath = DbTask.WINDOWS_UPLOAD_PATH.concat("demo_db1.sql");
        logger.info("sql文件路径filePath: " + jdbcBean.getFilePath());

        try{
            flag = DbTask.executeImportCommond(jdbcBean);
        }catch (Exception e){
            logger.error("数据导入失败");
            System.out.println(e);
        }
        if (flag) {
            return CommonResult.success("数据导入成功");
        }else{
            return CommonResult.failed("数据导入失败");
        }
    }




}
