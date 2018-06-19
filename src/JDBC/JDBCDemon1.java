package JDBC;

import java.sql.*;

public class JDBCDemon1 {
    public static void JDBCStep(){
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            //        DriverManger连接mysql时路径格式：jdbc:mysql://<host>:<port>/<dbname> msql端口号通常为3306
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/scott?user=root&password=100521");
             /*
            通过Connection创建Statement来执行SQL语句
             */
            Statement statement = connection.createStatement();

            /*
            execute用于执行DDL语句返回的结果为是否执行成功
            executeQuery用于执行DQL（select）语句，返回一个结果集resultSet
            executeUpdate用于执行DML（增删改操纵）返回int值为影响数据库多少条数据
             */

            /*
            通过Statment执行SQL语句，查询语句emp表中的信息：select empno,ename,sal,deptno from emp;
            并且输出sql，用于检查sql语句是否正确
             */
            String sql = "select empno,ename,sal,deptno from emp";
            System.out.println(sql);

            /*
            使用executeQuery执行DQL语句，且查询后会得到一个结果集
             */
            ResultSet resultSet =  statement.executeQuery(sql);

            /*
            不能在此时进行关闭，查询的结果集在服务器中，并不在客户端；
            resultSet是一个代理（是一个结果集，但不是全部都载到本地的）
            我们通过resultSet的next()方法获取下一条记录时，
            resultSet会发送请求到服务器端获取数据，若连接诶关闭，则会抛出异常
            connection.close();
             */
            while (resultSet.next()){
                int empno = resultSet.getInt("empno");
                String empname = resultSet.getString("ename");
                Double sal = resultSet.getDouble("sal");
                int deptno = resultSet.getInt("deptno");
                System.out.println("empno:"+empno+",ename:"+empname+",sal:"+sal+",deptno:"+deptno);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //关闭连接
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        JDBCStep();
    }
}
