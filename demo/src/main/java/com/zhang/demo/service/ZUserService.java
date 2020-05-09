package com.zhang.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.zhang.demo.common.utils.PageUtils;
import com.zhang.demo.entity.ZUserEntity;
import com.zhang.demo.form.ZUserForm;
import com.zhang.demo.vo.ZUserVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
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
    ZUserVo register(ZUserForm zUserForm);

    /**
     * 根据用户名或昵称分页查询用户
     * @param keyword
     * @param pageSize
     * @param pageNum
     * @return
     */
//    List<ZUserEntity> list(String keyword, Integer pageSize, Integer pageNum);
    PageUtils list(String keyword, Integer pageSize, Integer pageNum);

    /**
     * 查询用户详细信息
     * @param userId
     * @return zUserVo
     */
    ZUserVo queryZUserInfo(String userId);

    String uploadFile(MultipartFile file, HttpServletRequest req) throws IOException;

    /**
     * 生成验证码
     * @param request
     * @param response
     */
    void getVerify(HttpServletRequest request, HttpServletResponse response);

    /**
     * 忘记密码页面校验验证码
     * @param verifyInput
     * @param session
     * @return
     */
    boolean checkVerify(String verifyInput, HttpSession session);

    /**
     * 测试事务的传播特性以及回滚
     * 方法间调用事务的传播
     */
    void testUser1();

    /**
     * 更新用户信息
     * @param zUserForm
     * @return
     */
    boolean updateById(ZUserForm zUserForm);
}

