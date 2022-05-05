package com.atguigu.imperial.court.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/***
 * (1) 从数据源获取数据库连接
 * （2）将数据源绑定到本地线程（借助ThreadLocal）
 * （3）释放线程时何本地线程解除绑定
 *
 *
        **/
public class JDBCUtils {

    /****
     *数据源成员变量设置为静态资源，保证大对象的单例性（同一个）；同时保证静态方法中可以访问
     */
    private static DataSource dataSource;

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
      //(4)根据Properties 对象（已封装了数据库连接信息）来创建数据源
            dataSource= DruidDataSourceFactory.createDataSource(properties);
            properties.load(stream);
        }  catch (Exception e) {
            e.printStackTrace();
            //为了避免在真正抛出异常后，catch 块捕获异常从而掩盖问题
            //这里将所捕获到的异常封装为运行时异常继续抛出
            throw new RuntimeException(e);
        }
    }

}
