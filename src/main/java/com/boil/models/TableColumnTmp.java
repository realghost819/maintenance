package com.boil.models;

import javax.persistence.*;

/**
 * Created by songyu on 2017/4/27.
 */
@Entity
@Table(name = "tb_ms_table_column_tmp")
public class TableColumnTmp {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private long version_id;

    private long project_id;

    private String table_name;
    private String column_name;
    private String data_type;
    private String comments;
    private boolean enable;

    public long getVersion_id() {
        return version_id;
    }

    public void setVersion_id(long version_id) {
        this.version_id = version_id;
    }

    public long getProject_id() {
        return project_id;
    }

    public void setProject_id(long project_id) {
        this.project_id = project_id;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getColumn_name() {
        return column_name;
    }

    public void setColumn_name(String column_name) {
        this.column_name = column_name;
    }

    public String getData_type() {
        return data_type;
    }

    public void setData_type(String data_type) {
        this.data_type = data_type;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }


}
