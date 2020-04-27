package com.zhang.demo.service.impl;

import com.zhang.demo.common.CommonResult;
import com.zhang.demo.common.utils.DateUtils;
import com.zhang.demo.form.ZUserForm;
import com.zhang.demo.vo.ZUserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhang.demo.dao.ZUserDao;
import com.zhang.demo.entity.ZUserEntity;
import com.zhang.demo.service.ZUserService;
import org.springframework.web.multipart.MultipartFile;
//import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;


@Service("zUserService")
public class ZUserServiceImpl extends ServiceImpl<ZUserDao, ZUserEntity> implements ZUserService {

    private static final Logger logger = LoggerFactory.getLogger(ZUserServiceImpl.class);

    @Autowired
    private ZUserService zUserService;

    @Override
    public ZUserForm register(ZUserForm zUserForm) {
        logger.info("开始注册");
        try {
            // 模拟业务场景
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.info("错误");
        }
        // 复制属性值
        ZUserEntity zUserEntity = new ZUserEntity(zUserForm);
        //密码加密
//        String encodePassword = passwordEncoder.encode(zUserForm.getPassword());
//        zUserForm.setPassword(encodePassword);
        zUserEntity.setCreateBy("zhang");
        boolean flog = zUserService.save(zUserEntity);
        if (!flog) {
            logger.info("注册失败");
            CommonResult.failed();
        }
        logger.info("注册成功");
        return zUserForm;
    }

    @Override
    public List<ZUserEntity> list(String keyword, Integer pageSize, Integer pageNum) {
        List<ZUserEntity> list = zUserService.list();
        return list;
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
        return url;
    }
}
