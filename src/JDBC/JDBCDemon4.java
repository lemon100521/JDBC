package JDBC;
/**
 * 使用配置文件来配置JDBC连接数据库
 * 该类用来管理数据库的连接
 */

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class JDBCDemon4 {
    //数据库驱动
    private static String driver;
    //连接数据库的路径
    private static String url;
    //连接数据库的用户名
    private static String user;
    //连接数据库的密码
    private static String pwd;
    //用于管理不同线程所获取的连接
    private static ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();

    //静态块：类被虚拟机加载时执行一次
    static {
        try {
            //读取配置文件
            Properties properties = new Properties();

            //更加推荐的相对路径写法
            InputStream inputStream = JDBCDemon4.class.getClassLoader()
                    .getResourceAsStream("JDBC/config.properties");
            properties.load(inputStream);
            driver = properties.getProperty("driver").trim();
            url = properties.getProperty("url").trim();
            pwd = properties.getProperty("pwd").trim();
            user = properties.getProperty("user").trim();
            System.out.println("Driver:" + driver);
            System.out.println("user:" + user);
            System.out.println("url:" + url);
            System.out.println("pwd:" + pwd);
            inputStream.close();
            //获取驱动
            Class.forName(driver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
    * 获取一个连接
    */
    public static Connection getConnection() throws Exception{
        try {
            /*
             * 通过DriverManager创建一个数据库连接并返回
             * */
            Connection connection =  DriverManager.getConnection(url,user,pwd);
            /*
            * ThreadLocal的set方法会将当前线程作为key
            * 并将给定的值作为value存入内部的map中保存
            * */
            threadLocal.set(connection);
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
            //通知调用者，创建连接出错
            throw e;
        }
    }

    /**
     *  关闭给定的连接
     */
    public static void closeConnection(){
        try{
            //在ThreadLocal中获取connection即（value值）
            Connection connection = threadLocal.get();
            if(connection != null){
                connection.close();
                //删除value值，防止污染
                threadLocal.remove();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取连接并执行sql
     */
    public static void main(String[] args) {
        try{
            Connection connection = JDBCDemon4.getConnection();
            System.out.println("数据库已经连接");
            Statement statement = connection.createStatement();
            String sql = "select * from emp";
            System.out.println(sql);
            //执行sql，得到结果集
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                int empno = resultSet.getInt("empno");
                String ename = resultSet.getString("ename");
                int deptno = resultSet.getInt("deptno");
                double sal = resultSet.getDouble("sal");
                System.out.println("empno:"+empno+" ename:"+ename+" deptno:"+deptno+" sal:"+sal);
            }
            //当结果集使用完毕后就应该关闭，释放资源
            //但是若Statment关闭了，那么resultSet也会自动关闭
            resultSet.close();
            //当不再使用Statment执行其他sql时，应该及时关闭Statment以释放JDBC与数据库的资源连接
            statement.close();
            //使用后关闭连接
            closeConnection();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
