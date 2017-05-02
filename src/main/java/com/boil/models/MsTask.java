package com.boil.models;

import javax.persistence.*;
import java.util.Date;

/**
 * 长时间任务类，比如扫描，复制，比较
 * Created by songyu on 2017/5/2.
 */
@Entity
@Table(name = "tb_ms_task")
public class MsTask {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private long project_id;

    private String task_type;

    private Date begin_time;

    private Date end_time;

    private int status = 0;

    public MsTask() {
    }

    public MsTask(long project_id, String task_type) {
        this.project_id = project_id;
        this.task_type = task_type;
        this.begin_time = new Date();
    }

    public void success() {
        this.end_time = new Date();
        this.status = 1;
    }

    public void fail() {
        this.end_time = new Date();
        this.status = 2;
    }

    public long getId() {

        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProject_id() {
        return project_id;
    }

    public void setProject_id(long project_id) {
        this.project_id = project_id;
    }

    public String getTask_type() {
        return task_type;
    }

    public void setTask_type(String task_type) {
        this.task_type = task_type;
    }

    public Date getBegin_time() {
        return begin_time;
    }

    public void setBegin_time(Date begin_time) {
        this.begin_time = begin_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
