package com.boil.service;

import com.boil.dao.DbObjectTmpDao;
import com.boil.dao.TabelColumnTmpDao;
import com.boil.models.*;
import com.dansun.server.core.sqltemplate.context.SqlTemplateContext;
import com.dansun.server.core.sqltemplate.service.SqlTemplateService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * 运维管理服务类
 * Created by songyu on 2017/4/27.
 */
@Service
public class MsService {

    private SqlTemplateService sqlTemplateService = null;

    @Autowired
    private DbObjectTmpDao dbObjectTmpDao;
    @Autowired
    private TabelColumnTmpDao tabelColumnTmpDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public MsService() {
        SqlTemplateContext context = new SqlTemplateContext();
        context.init();
        sqlTemplateService = new SqlTemplateService(context);
    }

    public static void main(String[] args) throws Exception {
//        Project project = new Project();
//        project.setName("测试");
//        project.setJdbcUrl("jdbc:oracle:thin:@168.168.168.200:1521:orcl");
//        project.setUsername("test1");
//        project.setPassword("dba");
//        MsService service = new MsService();
//        service.scanProject(project);
        String username_str = String.format("\"%s\".", "test1".toUpperCase());
        System.out.println(username_str);
    }

    /**
     * 扫描项目，比较是否有改变，并保存改变明细
     *
     * @param project
     * @param task
     * @return 扫描结果0代表无变化, 1代表第一个版本, 2代表与最新的版本有区别
     * @throws Exception
     */
    public int scanProject(Project project, MsTask task) throws Exception {
        //搜索项目数据库相关信息
        inspectProjectDb(project);
        //比较版本，是否有变动,如果有需要的话
        return findDiff(project,task);
    }

    private int findDiff(Project project, MsTask task) {
        if (StringUtils.isNotBlank(project.getCurrent_version())) {
            //查询临时表数据库对象，和最新版本数据库对象进行比较
            String sql_query_db_objects_by_project = sqlTemplateService.getSql("sql_query_db_objects_by_project");
            String sql_query_tmp_db_objects_by_project = sqlTemplateService.getSql("sql_query_tmp_db_objects_by_project");
            final List<DbObject> latest_dbobjects = new ArrayList<DbObject>();
            final List<DbObject> tmp_dbobjects = new ArrayList<DbObject>();

            jdbcTemplate.query(sql_query_db_objects_by_project, new Object[]{project.getId(), project.getCurrent_version_id()},
                    new RowMapper() {
                        @Override
                        public Object mapRow(ResultSet rs, int rowNum)
                                throws SQLException {
                            DbObject model = new DbObject();
                            model.setObject_type(rs.getString("object_type"));
                            model.setObject_name(rs.getString("object_name"));
                            model.setComments(rs.getString("comments"));
                            model.setDdl_text(rs.getString("ddl_text"));
                            latest_dbobjects.add(model);
                            return null;
                        }
                    });
            jdbcTemplate.query(sql_query_tmp_db_objects_by_project, new Object[]{project.getId()},
                    new RowMapper() {
                        @Override
                        public Object mapRow(ResultSet rs, int rowNum)
                                throws SQLException {
                            DbObject model = new DbObject();
                            model.setObject_type(rs.getString("object_type"));
                            model.setObject_name(rs.getString("object_name"));
                            model.setComments(rs.getString("comments"));
                            model.setDdl_text(rs.getString("ddl_text"));
                            tmp_dbobjects.add(model);
                            return null;
                        }
                    });


            //比较得到新增的,修改的,删除的集合
            List new_dbobjects = (List) CollectionUtils.subtract(tmp_dbobjects, latest_dbobjects);
            List delete_dbobjects = (List) CollectionUtils.subtract(latest_dbobjects,tmp_dbobjects);
            List intersection = (List) CollectionUtils.intersection(tmp_dbobjects,latest_dbobjects);
            List modified_dbobjects = new ArrayList(intersection );
            for (Iterator it = intersection.iterator(); it.hasNext();) {
                DbObject new_object = (DbObject) it.next();
                DbObject old_object = latest_dbobjects.get(latest_dbobjects.indexOf(new_object));
                if (new_object.getDdl_text().equals(old_object.getDdl_text())) {
                    modified_dbobjects.remove(new_object);
                }
            }

            //三个集合长度都为0,返回0，否则返回2
            if (new_dbobjects.size()==0 && delete_dbobjects.size()==0 && modified_dbobjects.size()==0) {
                return 0;
            } else {
                //有变动保存至结果表


                return 2;
            }
        } else {
            return 1;//第一个版本
        }


    }

    /**
     * inspect项目数据库所有的数据对象和tablecolumn的信息，保存至管理数据库临时表
     *
     * @param project
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private void inspectProjectDb(Project project) throws ClassNotFoundException, SQLException {
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        Statement statement = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        String dialect = "";//不同数据库类型
        String sql_scan_db_objects = sqlTemplateService.getSql("sql_scan_db_objects_oracle");//查询数据库对象sql
        String sql_scan_table_columns = sqlTemplateService.getSql("sql_scan_table_column_oracle");//查询table column的sql
        String username_str = String.format("\"%s\".", project.getUsername().toUpperCase());
        List<DbObjectTmp> dbObjectTmps = new ArrayList<>();
        try {
            //先清空数据库对象临时表
            dbObjectTmpDao.deleteByProjectId(project.getId());

            //查询项目的所有数据库对象
            Class.forName("oracle.jdbc.driver.OracleDriver");// 加载Oracle驱动程序
            System.out.println("开始尝试连接项目数据库！");
            con = DriverManager.getConnection(project.getJdbcUrl(), project.getUsername(), project.getPassword());// 获取连接
            System.out.println("连接成功！");

            //屏蔽metadata的一些参数
            statement = con.createStatement();
            String sql = sqlTemplateService.getSql("reset_ddl_sql1");
            statement.executeUpdate(sql);

            pre = con.prepareStatement(sql_scan_db_objects);// 实例化预编译语句
            result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
            List<DbObjectTmp> models = new ArrayList<DbObjectTmp>();
            while (result.next()) {
                DbObjectTmp model = new DbObjectTmp();
                model.setProject_id(project.getId());
                model.setObject_name(result.getString("object_name"));
                model.setObject_type(result.getString("object_type"));
                model.setDdl_text(truncateUserame(result.getString("ddl"), username_str));
                model.setComments(result.getString("comments"));
                model.setStatus(result.getString("status"));
                Timestamp last_ddl_time = result.getTimestamp("last_ddl_time");
                if (last_ddl_time != null) {
                    model.setLast_ddl_time(new Date(last_ddl_time.getTime()));
                }
                Timestamp created = result.getTimestamp("created");
                if (created != null) {
                    model.setCreated(new Date(created.getTime()));
                }
                models.add(model);
            }


            //保存至数据库对象临时表
            dbObjectTmpDao.save(models);

            //先清空tablecolumn对象临时表
            tabelColumnTmpDao.deleteByProjectId(project.getId());
            //查询项目的所有column
            pre = con.prepareStatement(sql_scan_table_columns);// 实例化预编译语句
            result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
            List<TableColumnTmp> column_models = new ArrayList<TableColumnTmp>();
            while (result.next()) {
                TableColumnTmp model = new TableColumnTmp();
                model.setProject_id(project.getId());
                model.setComments(result.getString("comments"));
                model.setData_type(result.getString("data_type"));
                model.setColumn_name(result.getString("column_name"));
                model.setTable_name(result.getString("table_name"));
                column_models.add(model);
            }

            //保存至column临时表
            tabelColumnTmpDao.save(column_models);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            pre.close();
            con.close();
        }
    }

    private String truncateUserame(String ddl, String username) {
        System.out.println(ddl);
        System.out.println(username);
        String replace = ddl.replace(username, "");
        System.out.println(replace);
        return replace;
    }
}
