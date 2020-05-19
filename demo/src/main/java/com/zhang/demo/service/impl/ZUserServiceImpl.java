package com.zhang.demo.service.impl;

import com.github.pagehelper.PageHelper;
import com.google.common.base.Stopwatch;
import com.zhang.demo.common.CommonResult;
import com.zhang.demo.common.utils.Constant;
import com.zhang.demo.common.utils.PageUtils;
import com.zhang.demo.common.utils.RandomValidateCodeUtil;
import com.zhang.demo.common.utils.VerifyUtils;
import com.zhang.demo.common.utils.security.JwtTokenUtil;
import com.zhang.demo.entity.ZUserLoginLogEntity;
import com.zhang.demo.form.ZUserForm;
import com.zhang.demo.service.ZUserLoginLogService;
import com.zhang.demo.vo.ZUserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhang.demo.dao.ZUserDao;
import com.zhang.demo.entity.ZUserEntity;
import com.zhang.demo.service.ZUserService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Service("zUserService")
public class ZUserServiceImpl extends ServiceImpl<ZUserDao, ZUserEntity> implements ZUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZUserServiceImpl.class);

    @Autowired
    private ZUserService zUserService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
//    @Autowired
    @Resource
    private ZUserDao zUserDao;
    @Autowired
    private ZUserLoginLogService zUserLoginLogService;
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;



    @Override
    public ZUserVo register(ZUserForm zUserForm) {
        LOGGER.info("开始注册");
        try {
            // 模拟业务场景
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            LOGGER.info("错误");
        }
        // 复制属性值
        ZUserEntity zUserEntity = new ZUserEntity(zUserForm);
        //密码加密 // 此处用于security加密密码
        String encodePassword = passwordEncoder.encode(zUserForm.getPassword());
        zUserEntity.setPassword(encodePassword);
        zUserEntity.setCreateBy("zhang");
        boolean flog = zUserService.save(zUserEntity);
        if (!flog) {
            LOGGER.info("注册失败");
            CommonResult.failed();
        }
        LOGGER.info("注册成功");
        ZUserVo zUserVo = new ZUserVo();
        BeanUtils.copyProperties(zUserForm, zUserVo);
        return zUserVo;
    }

    @Override
    public CommonResult<Map<String, String>> login(ZUserForm zUserForm) {
        LOGGER.info("登陆中...");
        String token = "";
        Map<String, String> resultMap = new HashMap<String, String>();
        resultMap.put("tokenHead", tokenHead);
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(zUserForm.getUsername());
            if (!passwordEncoder.matches(zUserForm.getPassword(), userDetails.getPassword())) {
                throw new BadCredentialsException("密码不正确");
            }
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtTokenUtil.generateToken(userDetails);
//            updateLoginTimeByUsername(username);
            // 添加登录记录
            insertLoginLog(zUserForm.getUsername());
            LOGGER.info("登录成功");
        } catch (AuthenticationException e) {
            LOGGER.error("登录异常, error-message = [{}]", e.getMessage());
            return CommonResult.failed(e.getMessage());
        }
        resultMap.put("token",token);
        return CommonResult.success(resultMap);
    }

    @Override
    public ZUserEntity getZUserByUsername(String username) {
        return zUserDao.getZUserByUsername(username);
    }

    /**
     * 添加登录记录 ZUserLoginLog
     *
     * @param username 用户名
     */
    private void insertLoginLog(String username) {
        ZUserEntity admin = zUserService.getZUserByUsername(username);
        if (admin == null) return;
        ZUserLoginLogEntity loginLog = new ZUserLoginLogEntity(admin);
        zUserLoginLogService.save(loginLog);
    }
//
//    @Override
//    public UserDetails loadUserByUsername(String username) {
//        //获取用户信息
//        ZUserEntity admin = this.getZUserByUsername(username);
//        if (admin != null) {
////            List<UmsResource> resourceList = getResourceList(admin.getId()); // 角色
////            return new AdminUserDetails(admin, resourceList);
//            return new AdminUserDetails(admin);
//        }
//        throw new UsernameNotFoundException("用户名或密码错误");
//    }

    @Override
    public boolean updateById(ZUserForm zUserForm) {
        ZUserEntity zUserEntity = new ZUserEntity(zUserForm);
        BeanUtils.copyProperties(zUserForm, zUserEntity);
        zUserEntity.setUpdateBy("zhang");
        return zUserService.updateById(zUserEntity);
    }

    @Override
    public PageUtils list(String keyword, Integer pageSize, Integer pageNum) {
        // 列表分页
        PageHelper.startPage(pageNum, pageSize);
        List<ZUserEntity> list = zUserService.list();
        // 使用PageUtils结果进行包装
        PageUtils page = new PageUtils(list);
        return page;
    }

    @Override
    public ZUserVo queryZUserInfo(String userId) {
        ZUserVo zUserVo = new ZUserVo();
        ZUserEntity zUserInfo = zUserService.getById(userId);
        BeanUtils.copyProperties(zUserInfo, zUserVo);
        return zUserVo;
    }

    @Override
    public String uploadFile(MultipartFile file, HttpServletRequest req) throws IOException {
        // 创建之后立刻计时，耗时检查
        Stopwatch stopwatch = Stopwatch.createStarted();
        LOGGER.info("开始上传...");

        // 存储路径(resources-static-upload)
//        String fileDirPath = new String(Constant.ROOT_DIRECTORY + Constant.IMAGES_URL); // 项目中存储文件
        // 存储路径(F-uploadFileDirs-images)
//        String fileDirPath2 = new String(Constant.LOCAL_FILE_URL + File.separator + "images"); // 本地存储文件
        // 存储路径(C-user-01)
//        String realPath = req.getServletContext().getRealPath("/upload");
        // 存储路径(target-classes-static-upload)
        String fileDirPath = ResourceUtils.getURL("classpath:").getPath().concat(Constant.IMAGES_URL);

        System.out.println("path: " + fileDirPath);
        File fileDir = VerifyUtils.isExistsDirectory(fileDirPath);

        String fileName = file.getOriginalFilename(); // 文件名称（xxx.jpg）

        // 截取字符串
//        String suffix = UUID.randomUUID().toString() + fileName.substring(oldName.lastIndexOf("."));
//        String fileName = fileName.substring(0, fileName.indexOf("."));;// 截取指定字符之前的字符串

        String absolutePath = fileDir.getAbsolutePath(); // 绝对路径
        System.out.println("fileDir.getAbsolutePath(): " + absolutePath);
        String newFileName = UUID.randomUUID().toString() + fileName.substring(fileName.lastIndexOf("."));
        File newFile = new File(absolutePath + File.separator + newFileName);
        file.transferTo(newFile); // 转存文件到指定路径

//        String url = fileDirPath2 + File.separator + newFileName; // 本地存储路径
        String url = req.getScheme().concat("://") + req.getServerName().concat(":") + req.getServerPort()
                + "/upload/".concat(newFileName);
        System.out.println("url: " + url);

//        newFile.delete(); // 删除临时创建的文件

        LOGGER.info("上传成功");
        // 当前已经消耗的时间
        System.out.println("上传文件耗时 : " + stopwatch.elapsed(TimeUnit.SECONDS));
        return url;
    }

    @Override
    public void getVerify(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("image/jpeg");//设置相应类型,告诉浏览器输出的内容为图片
            response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);
            RandomValidateCodeUtil randomValidateCode = new RandomValidateCodeUtil();
            randomValidateCode.getRandcode(request, response);//输出验证码图片方法
        } catch (Exception e) {
            LOGGER.error("获取验证码失败");
            e.printStackTrace();
        }
    }

    @Override
    public boolean checkVerify(String verifyInput, HttpSession session) {
        try {
            //从session中获取随机数
            String random = (String) session.getAttribute("RANDOMVALIDATECODEKEY");
            if (random == null) {
                return false;
            }
            if (random.equals(verifyInput)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            LOGGER.error("验证码校验失败");
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 测试方法间互相调用，事务的传播特性以及回滚
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void testUser1(){

        // 未使用代理
//        this.testUser2();

        // 使用代理
        ZUserServiceImpl  zUserServiceImpl = (ZUserServiceImpl) AopContext.currentProxy();
        zUserServiceImpl.testUser2();
        //异常
        System.out.println("testUser1()");
        this.test();
        int a = 10/0;

    }

    @Transactional
    public void testUser2(){
        this.test();
        System.out.println("testUser2()");
//        int a = 10/0;
    }

    public void test(){
        ZUserEntity zUserEntity = new ZUserEntity();
        zUserEntity.setId("b24e3af2-9627-4752-b19a-e55c2a7cc37c");
        zUserEntity.setUsername("zhang01");
        zUserEntity.setPhoneNumber("18292000007");
        zUserEntity.setUpdateBy("zhang");
        zUserEntity.setUpdateTime(new Date());
        zUserService.updateById(zUserEntity);
        list(null, 5,1);
    }


}
