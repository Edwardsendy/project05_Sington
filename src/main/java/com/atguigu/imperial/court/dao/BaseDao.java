package com.atguigu.imperial.court.dao;

import com.atguigu.imperial.court.util.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.Connection;
import java.sql.SQLException;

/***
 * BaseDao 类：所有Dao实现类的基类
 * @param<T>实体类的类型
 *
 * ***/
public class BaseDao<T> {

    //DBUtils 工具包提供的数据库操作对象
    private QueryRunner runner=new QueryRunner();

    /**
     * @param sql 执行查询的sql语句
     * @param entittyClass 实体类对应的Class 对象
     * @param parameters 传给sql语句的参数
     * @return
     */
    public T getSingleBean(String sql,Class<T> entittyClass,Object ...parameters){
        try { //获取数据库连接
            Connection connection= JDBCUtils.getConnection();


            return runner.query(connection, sql, new BeanHandler<>(entittyClass),parameters);
        } catch (SQLException e) {
            e.printStackTrace();

           new RuntimeException(e);
        }
        return null;
    }

}
