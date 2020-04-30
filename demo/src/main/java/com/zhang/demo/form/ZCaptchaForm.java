package com.zhang.demo.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ZCaptchaForm {

    @NotEmpty(message = "ip不能为空")
    private String ip;
    @NotEmpty(message = "验证码不能为空")
    private String captcha;

}
