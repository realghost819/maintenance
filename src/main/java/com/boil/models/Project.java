package com.boil.models;

import java.util.List;

/**
 * Created by songyu on 2017/4/21.
 */
public class Project {

    private long id;

    private String name;

    private String comment;

    private String configfile;

    private List<ProjectVersion> versions;

    public List<ProjectVersion> getVersions() {
        return versions;
    }

    public void setVersions(List<ProjectVersion> versions) {
        this.versions = versions;
    }

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

    public String getConfigfile() {
        return configfile;
    }

    public void setConfigfile(String configfile) {
        this.configfile = configfile;
    }
}
