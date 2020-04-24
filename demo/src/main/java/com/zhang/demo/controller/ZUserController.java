package com.zhang.demo.controller;

import com.zhang.demo.common.Annotation.NoRepeatSubmit;
import com.zhang.demo.common.CommonResult;
import com.zhang.demo.common.utils.DateUtils;
import com.zhang.demo.dto.ZUserDto;
import com.zhang.demo.entity.ZUserEntity;
import com.zhang.demo.form.ZUserForm;
import com.zhang.demo.service.ZUserService;
import com.zhang.demo.vo.ZUserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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

    @NoRepeatSubmit(lockTime = 30)
    @PostMapping(value = "/register")
    public CommonResult<ZUserForm> register(@Validated(value = {ZUserForm.ZUserRegister.class}) @RequestBody ZUserForm zUserForm) {
        logger.info("开始注册");
        try {
            // 模拟业务场景
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.info("错误");
        }

        zUserForm = zUserService.register(zUserForm);
        if (zUserForm == null) {
            logger.info("注册失败");
            CommonResult.failed();
        }
        logger.info("注册成功");
        return CommonResult.success(zUserForm);
    }

    @PostMapping(value = "/login")
    public CommonResult<ZUserForm> login(@Validated(value = {ZUserForm.ZUserLogin .class}) @RequestBody ZUserForm zUserForm) {
        logger.info("登陆中...");

        logger.info("登录成功");
        return CommonResult.success(zUserForm);
    }

    @GetMapping(value = "/zUserList")
    public CommonResult<List<ZUserEntity>> list(@RequestParam(value = "keyword", required = false) String keyword,
                                              @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                              @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        List<ZUserEntity> zUserList = zUserService.list(keyword, pageSize, pageNum);
        return CommonResult.success(zUserList);
    }

    @PostMapping(value = "/uploadFile")
    public CommonResult<ZUserForm> uploadFile(MultipartFile file, HttpServletRequest req) throws IOException {
        logger.info("开始上传...");
        String format = DateUtils.format(new Date(), "/yyyy/MM/dd/");
        String realPath = req.getServletContext().getRealPath("/upload") + format;
        File folder = new File(realPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        String oldName = file.getOriginalFilename();
//        String newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."));
//        String newName = oldName.substring(0, oldName.indexOf("."));;// 截取指定字符之前的字符串
        String newName = oldName;
        file.transferTo(new File(folder,newName));
        String url1 = realPath + newName;
        String url = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/upload" + format + newName;
        System.out.println(url1);
        System.out.println(url);
        logger.info("上传成功");

        return CommonResult.success("上传成功", url);
    }


}
