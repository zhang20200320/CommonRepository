package com.zhang.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import com.zhang.demo.form.ZUserForm;
import lombok.Data;

/**
 *
 * 用户信息表
 * @author zhang
 * @date 2020-04-20 14:38:30
 */
@Data
@TableName("z_user")
public class ZUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId
	private String id;
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 手机号码
	 */
	private String phoneNumber;
	/**
	 * 创建人
	 */
	private String createBy;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新人
	 */
	private String updateBy;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	/**
	 * 是否删除（0：未删除 1：已删除）
	 */
	private Integer isDelete;

	public ZUserEntity() {
	}

	/**
	 * 用户注册
	 * @param zUserForm
	 */
	public ZUserEntity(ZUserForm zUserForm) {
//		BeanUtils.copyProperties(zUserForm, ZUserEntity.class);
		this.id = UUID.randomUUID().toString();
		this.username = zUserForm.getUsername();
		this.password = zUserForm.getPassword();
		this.phoneNumber = zUserForm.getPhoneNumber();
		this.isDelete = 0;
	}
}
