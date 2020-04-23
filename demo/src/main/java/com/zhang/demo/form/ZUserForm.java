package com.zhang.demo.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ZUserForm {

    @NotEmpty(groups = {ZUserRegister.class,
                        ZUserLogin.class}, message = "用户名不能为空")
    private String username;
    @NotEmpty(groups = {ZUserRegister.class,
                        ZUserLogin.class}, message = "密码不能为空")
    private String password;
    @NotEmpty(groups = {ZUserRegister.class}, message = "手机号码不能为空")
    private String phoneNumber;

    public interface ZUserRegister{}
    public interface ZUserLogin{}

}
