package com.hellobike.riskControl.util;

import com.alibaba.druid.pool.DruidDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class MysqlUtil {
    private static Properties properties = null;
    public static DruidDataSource dataSource = null;


    static {
        try {
            properties = new Properties();
            // 1.加载properties文件
            InputStream is = MysqlUtil.class.getClassLoader().getResourceAsStream("druid.properties");
            // 2.加载输入流
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        dataSource = new DruidDataSource();

        dataSource.setDriverClassName(properties.getProperty("driverClassName"));
        dataSource.setUrl(properties.getProperty("url"));
        dataSource.setUsername(properties.getProperty("username"));
        dataSource.setPassword(properties.getProperty("password"));
        dataSource.setInitialSize(Integer.valueOf(properties.getProperty("initialSize")));
        dataSource.setMaxActive(Integer.valueOf(properties.getProperty("maxActive")));
        dataSource.setMaxWait(Integer.valueOf(properties.getProperty("maxWait")));
        dataSource.setTimeBetweenConnectErrorMillis(Integer.valueOf(properties.getProperty("timeBetweenEvictionRunsMillis")));
        dataSource.setMinEvictableIdleTimeMillis(Integer.valueOf(properties.getProperty("minEvictableIdleTimeMillis")));
        dataSource.setValidationQuery(properties.getProperty("validationQuery"));
        dataSource.setTestWhileIdle(Boolean.valueOf(properties.getProperty("testWhileIdle")));
        dataSource.setTestOnBorrow(Boolean.valueOf(properties.getProperty("testOnBorrow")));
        dataSource.setTestOnReturn(Boolean.valueOf(properties.getProperty("testOnReturn")));
    }


    public static synchronized void getConnection() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            // 获得连接:
            conn = dataSource.getConnection();
            // 编写SQL：
            String sql = "select * from user";
            pstmt = conn.prepareStatement(sql);
            // 执行sql:
            rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString("name") + "   " + rs.getString("age"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
//            Utils.releaseResouce(rs, pstmt, conn);
        }

    }


    public static  void executorSQL(String sql){
        Connection conn = MysqlUtil.getConn();

        Statement stmt = null;
        try{
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        }catch(SQLException se){
            se.printStackTrace();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(stmt!=null)
                    conn.close();
            }catch(SQLException se){
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
    }

    public static synchronized Connection getConn() {
        Connection conn = null;
        try {
            // 获得连接:
            conn = dataSource.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static synchronized void getConnection2() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            // 获得连接:
            conn = dataSource.getConnection();
            // 编写SQL：
            String sql = "select * from user";
            pstmt = conn.prepareStatement(sql);
            // 执行sql:
            rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString("name") + "   " + rs.getString("age"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                pstmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
//            Utils.releaseResouce(rs, pstmt, conn);
        }

    }


    private static Integer getAll() {
        Connection conn = MysqlUtil.getConn();;
        String sql = "select * from user";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement)conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            int col = rs.getMetaData().getColumnCount();
            System.out.println("============================");
            while (rs.next()) {
                for (int i = 1; i <= col; i++) {
                    System.out.print(rs.getString(i) + "\t");
                    if ((i == 2) && (rs.getString(i).length() < 8)) {
                        System.out.print("\t");
                    }
                }
                System.out.println("");
            }
            System.out.println("============================");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    public static void batchInsert() throws ClassNotFoundException, SQLException{
        long start = System.currentTimeMillis();
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/kxh?useServerPrepStmts=false&rewriteBatchedStatements=true",
                "root", "root");

        connection.setAutoCommit(false);
        PreparedStatement cmd = connection
                .prepareStatement("insert into test1 values(?,?)");

        for (int i = 0; i < 1000000; i++) {//100万条数据
            cmd.setInt(1, i);
            cmd.setString(2, "test");
            cmd.addBatch();
            if(i%1000==0){
                cmd.executeBatch();
            }
        }
        cmd.executeBatch();
        connection.commit();

        cmd.close();
        connection.close();

        long end = System.currentTimeMillis();
        System.out.println("批量插入需要时间:"+(end - start)); //批量插入需要时间:24675
    }


    public static void batchInsert_test() throws ClassNotFoundException, SQLException,Exception{
        long start = System.currentTimeMillis();
        Connection connection = MysqlUtil.getConn();

//        true：sql命令的提交（commit）由驱动程序负责
//        false：sql命令的提交由应用程序负责，程序必须调用commit或者rollback方法
        connection.setAutoCommit(false);
        PreparedStatement cmd = connection
                .prepareStatement("insert into user values(?,?)");

        for (int i = 0; i < 20; i++) {//100万条数据
            cmd.setString(1, "zhangsan"+i);
            cmd.setString(2, ""+i);
            cmd.addBatch();
            if(i%5==0){
                cmd.executeBatch();
            }
        }
        cmd.executeBatch();
        connection.commit();

        cmd.close();
        connection.close();

        long end = System.currentTimeMillis();
        System.out.println("批量插入需要时间:"+(end - start)); //批量插入需要时间:24675
    }



//    private static int update(Student student) {
//        Connection conn = MysqlUtil.getConn();;
//        int i = 0;
//        String sql = "update students set Age='" + student.getAge() + "' where Name='" + student.getName() + "'";
//        PreparedStatement pstmt;
//        try {
//            pstmt = (PreparedStatement) conn.prepareStatement(sql);
//            i = pstmt.executeUpdate();
//            System.out.println("resutl: " + i);
//            pstmt.close();
//            conn.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return i;
//    }

    private static int delete(String name) {
        Connection conn = MysqlUtil.getConn();;
        int i = 0;
        String sql = "delete from students where Name='" + name + "'";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            i = pstmt.executeUpdate();
            System.out.println("resutl: " + i);
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }
//    public static synchronized MysqlUtil getInstance() {
//        Connection conn = null;
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//        try {
//            // 获得连接:
//            conn = dataSource.getConnection();
//            // 编写SQL：
//            String sql = "select * from student";
//            pstmt = conn.prepareStatement(sql);
//            // 执行sql:
//            rs = pstmt.executeQuery();
//            while (rs.next()) {
//                System.out.println(rs.getString("name") + "   " + rs.getString("age"));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//
////            Utils.releaseResouce(rs, pstmt, conn);
//        }
//
//    }


    public static void main(String[] args) {
//        insert();
//        getConnection();
        try{
            batchInsert_test();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
