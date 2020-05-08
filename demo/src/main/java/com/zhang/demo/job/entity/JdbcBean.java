package com.zhang.demo.job.entity;

/**
 * 数据库信息
 * @author zhang
 * @date 2020-05-08 15:56:00
 */
public class JdbcBean {

    private String ip;

    private String db = "design_center_system";

    private int port = 0;

    private String username;

    private String password;


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
