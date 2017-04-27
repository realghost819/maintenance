package com.boil.service;

import com.boil.models.Project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * 运维管理服务类
 * Created by songyu on 2017/4/27.
 */
public class MsService {

    static String sql_scan_db_objects_oracle = "select OBJECT_TYPE,OBJECT_NAME,created,last_ddl_time,status from user_objects where " +
            "object_type in ('TABLE''PROCEDURE','DATABASE LINK','PACKAGE','PACKAGE BODY','TRIGGER','VIEW','INDEX','FUNCTION','JOB')";

    public static void main(String[] args) {
        Project project = new Project();
        project.setName("测试");
        project.setJdbcUrl("jdbc:oracle:thin:@168.168.168.200:1521:orcl");
        project.setUsername("test1");
        project.setPassword("dba");
        MsService service  = new MsService();
        service.scanProject(project);
    }

    public  void scanProject(Project project) {
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");// 加载Oracle驱动程序
            System.out.println("开始尝试连接数据库！");
            con = DriverManager.getConnection(project.getJdbcUrl(), project.getUsername(), project.getPassword());// 获取连接
            System.out.println("连接成功！");
            pre = con.prepareStatement(sql_scan_db_objects_oracle);// 实例化预编译语句
            result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
            while (result.next())
                // 当结果集不为空时
                System.out.println("对象类型:" + result.getString("object_type") +
                        ",对象名称:" + result.getString("object_name"));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
