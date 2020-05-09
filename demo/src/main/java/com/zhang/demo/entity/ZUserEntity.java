package com.zhang.demo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import com.zhang.demo.form.ZUserForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

/**
 *
 * 用户信息表
 * swagger应用
 * 		@ApiModel ———— 描述一个Model的信息
 * 		@ApiModelProperty ———— 描述一个model的属性
 * 			value ———— 描述值
 * 			position ———— 属性排序
 * position ———— 表示属性顺序
 * @author zhang
 * @date 2020-04-20 14:38:30
 */
@ApiModel(description = "用户实体")
@Data
@TableName("z_user")
public class ZUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键id", position = 1)
	@TableId
	private String id;

	@ApiModelProperty(value = "用户名")
	private String username;

	@ApiModelProperty(value = "密码")
	private String password;

	@ApiModelProperty(value = "手机号码")
	private String phoneNumber;

	@ApiModelProperty(value = "创建人")
	private String createBy;

	@ApiModelProperty(value = "创建时间")
	private Date createTime;

	@ApiModelProperty(value = "更新人")
	private String updateBy;

	@ApiModelProperty(value = "更新时间")
	private Date updateTime;

	@ApiModelProperty(value = "删除状态：0->未删除；1->已删除")
	private Integer isDelete;

	public ZUserEntity() {
	}

	/**
	 * 用户注册/更新
	 * @param zUserForm
	 */
	public ZUserEntity(ZUserForm zUserForm) {
		BeanUtils.copyProperties(zUserForm, this);
		if (StringUtils.isEmpty(zUserForm.getId())) {
			this.id = UUID.randomUUID().toString();
			this.createTime = new Date();
			this.isDelete = 0;
		} else {
			this.updateTime = new Date();
		}
	}


}
