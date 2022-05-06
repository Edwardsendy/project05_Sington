package com.atguigu.maven;

import com.atguigu.imperial.court.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;

public class ImperialCourTest {
    @Test
    public void testGetConnection(){
        Connection connection= JDBCUtils.getConnection();
        System.out.println("connection="+connection);
        JDBCUtils.releaseConnection(connection);
    }
}
