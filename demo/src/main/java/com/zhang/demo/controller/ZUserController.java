package com.zhang.demo.controller;

import com.zhang.demo.common.Annotation.NoRepeatSubmit;
import com.zhang.demo.common.CommonResult;
import com.zhang.demo.entity.ZUserEntity;
import com.zhang.demo.form.ZUserForm;
import com.zhang.demo.service.ZUserService;
import com.zhang.demo.vo.ZUserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * 用户
 * @author zhang
 * @date 2020-04-20 14:38:30
 */
@RestController
@RequestMapping("/user")
public class ZUserController {

    private static final Logger logger = LoggerFactory.getLogger(ZUserController.class);

    @Autowired
    private ZUserService zUserService;

    /**
     * 注册
     * @param zUserForm
     * @return
     */
    @NoRepeatSubmit(lockTime = 30)
    @PostMapping(value = "/register")
    public CommonResult<ZUserForm> register(@Validated(value = {ZUserForm.ZUserRegister.class}) @RequestBody ZUserForm zUserForm) {
        return CommonResult.success(zUserService.register(zUserForm));
    }

    /**
     * 登录
     * @param zUserForm
     * @return
     */
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
    @PostMapping(value = "/logout")
    public CommonResult logout() {
        return CommonResult.success(null);
    }

    /**
     * 用户信息
     * @param userId
     * @return
     */
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
    @GetMapping(value = "/zUserList")
    public CommonResult<List<ZUserEntity>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<ZUserEntity> zUserList = zUserService.list(keyword, pageSize, pageNum);
        return CommonResult.success(zUserList);
    }

    /**
     * 上传文件
     * @param file
     * @param req
     * @return
     * @throws IOException
     */
    @PostMapping(value = "/uploadFile")
    public CommonResult<ZUserForm> uploadFile(MultipartFile file, HttpServletRequest req) throws IOException {
        return CommonResult.success("上传成功", zUserService.uploadFile(file, req));
    }


}
