package JDBC;

import org.apache.commons.dbcp2.BasicDataSource;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Properties;

/**
 * 使用连接池技术管理数据库连接
 *
 * */
public class DBUtil2 {
    //数据库连接池
    private static BasicDataSource basicDataSource;
    //为不同线程管理连接
    private static ThreadLocal<java.sql.Connection> threadLocal;

    static {
        try {
            //加载配置文件
            Properties properties = new Properties();
            InputStream inputStream = DBUtil2.class.getClassLoader().getResourceAsStream("JDBC/config.properties");
            properties.load(inputStream);
            inputStream.close();

            //初始化连接池
            basicDataSource = new BasicDataSource();
            //设置驱动（Class.forName()）
            basicDataSource.setDriverClassName(properties.getProperty("driver"));
            //设置url
            basicDataSource.setUrl(properties.getProperty("url"));
            //设置数据库库用户名
            basicDataSource.setUsername(properties.getProperty("user"));
            //设置数据库密码
            basicDataSource.setPassword(properties.getProperty("pwd"));
            //初始连接数量
            basicDataSource.setInitialSize(Integer.parseInt(properties.getProperty("initsize")));
            //连接池允许的最大连接数
            basicDataSource.setMaxTotal(Integer.parseInt(properties.getProperty("maxactive")));
            //设置最大等待时间
            basicDataSource.setMaxWaitMillis(Integer.parseInt(properties.getProperty("maxwait")));
            //设置最小空闲线程数
            basicDataSource.setMinIdle(Integer.parseInt(properties.getProperty("minidle")));
            //设置最大空闲线程数
            basicDataSource.setMaxIdle(Integer.parseInt(properties.getProperty("maxidle")));
            //初始化本地线程
            threadLocal = new ThreadLocal<java.sql.Connection>();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取数据库连接
  public static java.sql.Connection getConnection()throws SQLException {
      //通过连接池获取一个空闲连接
      java.sql.Connection connection = basicDataSource.getConnection();
      threadLocal.set(connection);
      return connection;
  }
  public static void closeConnection() {
      try {
         Connection connection = threadLocal.get();
          if (connection != null) {
              //通过连接池获取的Connection的
              // close方法实际上并没有将连接池关闭，
              // 而是将该连接归还
              connection.close();
              threadLocal.remove();
          }
      } catch (Exception e) {
          e.printStackTrace();
      }
  }
      public static void main(String[] args) {
          try{
              Connection connection = DBUtil2.getConnection();
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
