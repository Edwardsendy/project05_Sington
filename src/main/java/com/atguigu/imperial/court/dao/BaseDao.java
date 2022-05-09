package com.atguigu.imperial.court.dao;

import com.atguigu.imperial.court.util.JDBCUtils;
import com.mysql.cj.jdbc.JdbcConnection;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/***
 * BaseDao 类：所有Dao实现类的基类
 * @param<T>实体类的类型
 *
 * ***/
public class BaseDao<T> {

    //DBUtils 工具包提供的数据库操作对象
    private QueryRunner runner=new QueryRunner();


    /***
     * 通用的增删改方法，insert、delete、update 操作都可以用这个方法
     * @param sql 执行操作的 SQL 语句
     * @param parameters SQL 语句的参数
     * @return 返回受影响的行数数字
     */
    public int update (String sql,Object ...parameters){
        try{
            Connection connection=JDBCUtils.getConnection();

            int affectedRowNumbers = runner.update(connection, sql, parameters);

            return affectedRowNumbers;
        }
        catch (Exception e){
            e.printStackTrace();
            // 如果真的抛出异常，则将编译时异常封装为运行时异常抛出
            new RuntimeException();
        }
        return 0;
    }




    /**
     * @param sql 执行查询的sql语句
     * @param entityClass 实体类对应的Class 对象
     * @param parameters 传给sql语句的参数
     * @return
     */
    public T getSingleBean(String sql,Class<T> entityClass,Object ...parameters){
        try { //获取数据库连接
            Connection connection= JDBCUtils.getConnection();


            return runner.query(connection, sql, new BeanHandler<>(entityClass),parameters);
        } catch (SQLException e) {
            e.printStackTrace();

           new RuntimeException(e);
        }
        return null;
    }



    public List<T> getBeanList(String sql, Class<T> entityClass, Object ...parameters){
        try{
                Connection connection = JDBCUtils.getConnection();

                return runner.query(connection,sql,new BeanListHandler<>(entityClass),parameters);
                }
        catch (Exception e){
            e.printStackTrace();
            new RuntimeException();

        }
        return null;
    }

}
