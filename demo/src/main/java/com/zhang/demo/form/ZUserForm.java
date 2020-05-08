package com.zhang.demo.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class ZUserForm {

    @ApiModelProperty(value = "用户名称")
    @NotEmpty(groups = {ZUserRegister.class,
                        ZUserLogin.class}, message = "用户名不能为空")
    private String username;

    @ApiModelProperty(value = "密码")
    @NotEmpty(groups = {ZUserRegister.class,
                        ZUserLogin.class}, message = "密码不能为空")
    @Length(groups = {ZUserRegister.class},max = 16, min = 8, message = "密码长度限制8~16字符")
    private String password;

    @ApiModelProperty(value = "手机号码")
    @NotEmpty(groups = {ZUserRegister.class}, message = "手机号码不能为空")
    @Pattern(regexp = "1[3|4|5|7|8][0-9]\\d{8}", message = "手机号码格式错误")
    private String phoneNumber;

    public interface ZUserRegister{}
    public interface ZUserLogin{}

}
