package com.zhang.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhang.demo.entity.ZUserEntity;
import com.zhang.demo.form.ZUserForm;
import com.zhang.demo.vo.ZUserVo;

import java.util.List;


/**
 *
 *
 * @author zhang
 * @date 2020-04-20 14:38:30
 */
public interface ZUserService extends IService<ZUserEntity> {

    /**
     * 用户注册
     * @param zUserForm
     * @return
     */
    ZUserForm register(ZUserForm zUserForm);
    /**
     * 根据用户名或昵称分页查询用户
     */
    List<ZUserEntity> list(String keyword, Integer pageSize, Integer pageNum);

}

