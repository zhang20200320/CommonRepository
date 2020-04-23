package com.zhang.demo.service.impl;

import com.zhang.demo.form.ZUserForm;
import com.zhang.demo.vo.ZUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhang.demo.dao.ZUserDao;
import com.zhang.demo.entity.ZUserEntity;
import com.zhang.demo.service.ZUserService;
//import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.List;


@Service("zUserService")
public class ZUserServiceImpl extends ServiceImpl<ZUserDao, ZUserEntity> implements ZUserService {

    @Autowired
    private ZUserService zUserService;

    @Override
    public ZUserForm register(ZUserForm zUserForm) {
        ZUserEntity zUserEntity = new ZUserEntity(zUserForm);
        //密码加密
//        String encodePassword = passwordEncoder.encode(zUserForm.getPassword());
//        zUserForm.setPassword(encodePassword);
        zUserEntity.setCreateBy("zhang");
        zUserEntity.setCreateTime(new Date());
        zUserService.save(zUserEntity);
        return zUserForm;
    }

    @Override
    public List<ZUserEntity> list(String keyword, Integer pageSize, Integer pageNum) {
        List<ZUserEntity> list = zUserService.list();
        return list;
    }
}
