package com.zhang.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 *
 */
@ApiModel(description = "用户登录记录")
@Data
@TableName("z_user_login_log")
public class ZUserLoginLogEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "记录id")
    @TableId
    private String id;

    @ApiModelProperty(value = "用户id")
    private String zUserId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "登录ip")
    private String ip;

    @ApiModelProperty(value = "登录地址")
    private String address;

    @ApiModelProperty(value = "浏览器登录类型")
    private String userAgent;

    public ZUserLoginLogEntity(ZUserEntity admin) {
        this.id = UUID.randomUUID().toString();
        this.zUserId = admin.getId();
        this.createTime = new Date();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        this.ip = request.getRemoteAddr();
    }
}
