package com.examples.web.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author 
 * @date 2019/05/24 16:02:42
 * @description 管理员表
 */

@Data
@ApiModel("管理员表")
public class SysUserDTO {

	//主键id
	@ApiModelProperty("主键id")
	private Integer id;

	//头像
	@ApiModelProperty("头像")
	private String avatar;

	//账号
	@ApiModelProperty("账号")
	private String account;

	//密码
	@ApiModelProperty("密码")
	private String password;

	//md5密码盐
	@ApiModelProperty("md5密码盐")
	private String salt;

	//名字
	@ApiModelProperty("名字")
	private String name;

	//生日
	@ApiModelProperty("生日")
	private Date birthday;

	//性别（1：男 2：女）
	@ApiModelProperty("性别（1：男 2：女）")
	private Integer sex;

	//电子邮件
	@ApiModelProperty("电子邮件")
	private String email;

	//电话
	@ApiModelProperty("电话")
	private String phone;

	//角色id
	@ApiModelProperty("角色id")
	private String roleid;

	//部门id
	@ApiModelProperty("部门id")
	private Integer deptid;

	//状态(1：启用  2：冻结  3：删除）
	@ApiModelProperty("状态(1：启用  2：冻结  3：删除）")
	private Integer status;

	//创建时间
	@ApiModelProperty("创建时间")
	private Date createtime;

	//保留字段
	@ApiModelProperty("保留字段")
	private Integer version;


}
