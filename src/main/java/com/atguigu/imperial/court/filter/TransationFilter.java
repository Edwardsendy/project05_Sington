package com.atguigu.imperial.court.filter;

import com.atguigu.imperial.court.util.JDBCUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class TransationFilter implements Filter {
    // 声明集合保存静态资源扩展名 //排除带这些后缀的文件，使他们被过滤器无视
   private  static  Set<String>  staticResourceExtNameSet;
    static {
        staticResourceExtNameSet =new HashSet<>();
        staticResourceExtNameSet.add(".png");
        staticResourceExtNameSet.add(".jpg");
        staticResourceExtNameSet.add(".css");
        staticResourceExtNameSet.add(".js");

    }


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}
    @Override
    public void destroy() {}
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        // 前置操作：排除静态资源
        //
     HttpServletRequest httpServletRequest= (HttpServletRequest) request;
     String servletPath= ((HttpServletRequest) request).getServletPath();





        Connection connection=null;
        try{
            connection= JDBCUtils.getConnection();
            filterChain.doFilter(request,response);

            connection.commit();
        }catch (Exception e){
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }finally {
            JDBCUtils.releaseConnection(connection);

        }
    }


}
