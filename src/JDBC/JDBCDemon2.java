package JDBC;
/**
 * 测试配置文件的读取
 * config.properties
 */
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class JDBCDemon2 {
    public static void main(String[] args) {
        Connection connection = null;
        try {
            //java.util.Properties
            /*
            *  Properties用于读取properties文件
            *  使用该类可以以类似Map的形式读取配置文件中的内容
            *  properties文件总的内容格式类似：
            *  user=root
            *  那么等号左边就是key，等号右边就是value
            */
            Properties properties = new Properties();

            /*
            * 使用Properties读取配置文件
            * */
            FileInputStream fileInputStream = new FileInputStream("./src/JDBC/config.properties");

            /*
            * 当通过Properties读取文件后，
            * 那么这个流依然保持打开状态，
            * 我们应该自行对其进行关闭
            * */
            properties.load(fileInputStream);
            System.out.println("成功加载完配置文件");

            /*
            * 当加载完毕后，就可以根据文本文件中
            * 等号左边的内容（key）来获取
            * 等号右边的值(value)
            * 可以变相的吧Properties看做一个Map
            * */
            String driver = properties.getProperty("driver").trim();
            String url = properties.getProperty("url").trim();
            String pwd = properties.getProperty("pwd").trim();
            String user = properties.getProperty("user").trim();
            System.out.println("Driver:"+driver);
            System.out.println("user:"+user);
            System.out.println("url:"+url);
            System.out.println("pwd:"+pwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
