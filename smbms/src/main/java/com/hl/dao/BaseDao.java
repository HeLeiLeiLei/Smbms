package com.hl.dao;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

//操作数据库的公共类
public class BaseDao {
    private static String driver;
    private static String url;
    private static String username;
    private static String password;

    //静态代码块,类加载的时候就初始化了

    static {
        Properties properties = new Properties();
        //通过类加载器读取对应的资源
        InputStream resourceAsStream = BaseDao.class.getClassLoader().getResourceAsStream("db.properties");
        try {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        driver=properties.getProperty("driver");
        url=properties.getProperty("url");
        username=properties.getProperty("username");
        password=properties.getProperty("password");
    }

    //获取数据库的连接
    public static Connection getConnection() throws Exception {
        Connection connection=null;
        Class.forName(driver);
        connection = DriverManager.getConnection(url, username, password);
        return connection;
    }

    //编写查询共公类
    public static ResultSet executQ(Connection connection,
                                    PreparedStatement preparedStatement,
                                    ResultSet resultSet,
                                    Object[] params,
                                    String sql) throws Exception {
        preparedStatement= connection.prepareStatement(sql);
        for (int i = 0; i <params.length ; i++) {
            //setObject,占位符从1开始，但是我们的数组是从0开始!
            preparedStatement.setObject(i+1,params[i]);
        }
        resultSet= preparedStatement.executeQuery();
        return resultSet;
    }

    //编写增删改公共类
    public static int executU(Connection connection,
                              PreparedStatement preparedStatement,
                              Object[] params,
                              String sql
            ) throws Exception {
        preparedStatement= connection.prepareStatement(sql);
        for (int i = 0; i <params.length ; i++) {
            preparedStatement.setObject(i+1,params[i]);
        }
        int updateRows= preparedStatement.executeUpdate();
        return updateRows;
    }

    //关闭连接 释放资源
    public static boolean closeResource(Connection connection
            ,PreparedStatement preparedStatement,ResultSet resultSet){
        boolean flag=true;
        if(resultSet != null) {
            try {
                resultSet.close();
                //GC回收
                resultSet=null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag=false;
            }
        }
        if(preparedStatement != null) {
            try {
                preparedStatement.close();
                //GC回收
                preparedStatement=null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag=false;
            }
        }
        if(connection != null) {
            try {
                connection.close();
                //GC回收
                connection=null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag=false;
            }
        }
        return flag ;
    }
}
