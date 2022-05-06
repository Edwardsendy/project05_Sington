package com.atguigu.imperial.court.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/***
 * (1) 从数据源获取数据库连接
 * （2）从数据库获取到数据库连接后，绑定到本地线程（借助ThreadLocal）
 * （3）释放线程时何本地线程解除绑定
 *
 *
        **/
public class JDBCUtils {

    /****
     *数据源成员变量设置为静态资源，保证大对象的单例性（同一个）；同时保证静态方法中可以访问
     */

    private static DataSource dataSource;

    // 由于 ThreadLocal 对象需要作为绑定数据时 k-v 对中的 key，所以要保证唯一性
// 加 static 声明为静态资源即可保证唯一性
    private static ThreadLocal<Connection> threadLocal = new ThreadLocal<>();
    //静态代码块中初始化
    static{
        try {
    /***从觉得不错。properties获取配置信息
     *为了代码的可移植性，基于一个确定的基准来定位到这个文件
     * 确定的基准：类路径的根目录；resource 目录下的内容经过构建操作中的打包操作后会确定放在WEB-INF/classes/目录下。
     * WEB-INF/classes/目录 下 存放编译好的*。classes 字节码文件，称这个路径为类路径。
     * 类路径无论在本地运行还是服务器运行都是一个确定的基准。
    ***/

    /***
     * 具体代码操作
     * （1）获取当前类的加载器
     *
     * ***/
      //（1）获取当前类的加载器
    ClassLoader classLoader =JDBCUtils.class.getClassLoader();

    //（2）通过类加载器对象从类根路径目录下读取文件
      InputStream stream= classLoader.getResourceAsStream("jdbc.properties");
      //(3)使用Properties 类封装属性文件中的数据
        Properties properties=new Properties();
        properties.load(stream);
      //(4)根据Properties 对象（已封装了数据库连接信息）来创建数据源


            dataSource= DruidDataSourceFactory.createDataSource(properties);

        }  catch (Exception e) {
            e.printStackTrace();
            //为了避免在真正抛出异常后，catch 块捕获异常从而掩盖问题
            //这里将所捕获到的异常封装为运行时异常继续抛出
            throw new RuntimeException(e);
        }
    }



    /***
     *
     * 工具方法：获取数据库连接并返回
     * @return
     *
     * ***/


    public static Connection getConnection(){
        Connection connection=null;
        try {//1.尝试从当前线程检查是否存在已绑定的Connection 对象呢
        connection= threadLocal.get();
        //2.检查Connection对象是否为null
        if (connection == null) {
           //3.如果为null则从数据源获取数据库连接
            connection=dataSource.getConnection();
            //获取到数据库连接后绑定到当前线程
            threadLocal.set(connection);
            }
        }catch (SQLException e) {
            e.printStackTrace();
            //为了调用工具方法方便，编译时异常不往外抛
            //为了不掩盖问题，捕获的编译时问题封装成运行时异常抛出
            throw new RuntimeException(e);
        }

        return connection;
    }

    public static void  releaseConnection(Connection connection){
        /**
         * 释放数据库连接
         * ***/
        if (connection != null) {
            try {
                //将数据库连接池中当前连接对象标记为空闲
                connection.close();
                //将当前数据库连接从当前线程中移除
            } catch (SQLException e) {
                e.printStackTrace();

                throw new RuntimeException(e);
            }

        }
    }
}
