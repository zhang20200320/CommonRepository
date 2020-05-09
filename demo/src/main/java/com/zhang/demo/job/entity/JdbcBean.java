package com.zhang.demo.job.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 数据库信息
 * @author zhang
 * @date 2020-05-08 15:56:00
 */

@ApiModel(value="数据库信息")
public class JdbcBean {

    @ApiModelProperty(value = "ip")
    private String ip;
    @ApiModelProperty(value = "数据库名称")
    private String db;
    @ApiModelProperty(value = "端口号")
    private int port;
    @ApiModelProperty(value = "数据库用户名")
    private String username;
    @ApiModelProperty(value = "数据库密码")
    private String password;

    @ApiModelProperty(value = "sql文件路径")
    private String filePath;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
