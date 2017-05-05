package com.boil.models;

import javax.persistence.*;
import java.util.List;

/**
 * Created by songyu on 2017/4/21.
 */
@Entity
@Table(name = "tb_ms_projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    private String comment;

    //数据库连接属性
    private String jdbcUrl;
    private String username;
    private String password;

    //当前版本名称
    private String current_version;

    public long getCurrent_version_id() {
        return current_version_id;
    }

    public void setCurrent_version_id(long current_version_id) {
        this.current_version_id = current_version_id;
    }

    //当前版本id
    private long current_version_id;


//    private List<ProjectVersion> versions;
//
    public String getCurrent_version() {
        return current_version;
    }

    public void setCurrent_version(String current_version) {
        this.current_version = current_version;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
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

//    public List<ProjectVersion> getVersions() {
//        return versions;
//    }
//
//    public void setVersions(List<ProjectVersion> versions) {
//        this.versions = versions;
//    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
