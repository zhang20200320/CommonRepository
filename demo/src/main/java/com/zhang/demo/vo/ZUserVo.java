package com.zhang.demo.vo;

import com.zhang.demo.form.ZUserForm;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 响应信息
 */
@Data
public class ZUserVo {

    private String id;

    private String username;

    private String phoneNumber;

}
