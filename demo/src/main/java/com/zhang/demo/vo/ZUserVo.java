package com.zhang.demo.vo;

import com.zhang.demo.form.ZUserForm;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 响应信息
 */
@Data
public class ZUserVo {

    @NotEmpty(message = "用户名不能为空")
    private String username;
    @NotEmpty(message = "密码不能为空")
    private String password;

    private String phoneNumber;


    public ZUserVo(ZUserForm zUserForm) {
        this.username = zUserForm.getUsername();
        this.password = zUserForm.getPassword();
        this.phoneNumber = zUserForm.getPhoneNumber();
    }
}
