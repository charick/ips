package TOI.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: W.k
 * Date: 13-4-30
 * Time: 下午10:13
 * To change this template use File | Settings | File Templates.
 */
public class SQLUtils {
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        String url = "jdbc:mysql://localhost:3306/ikea?characterEncoding=UTF-8";
        Class.forName("com.mysql.jdbc.Driver");
        String userName = "root";
        String password = "491272";
        Connection con = DriverManager.getConnection(url, userName, password);
        return con;
    }




    /**
     * 获取系统时间
     *
     * @return
     */
    public static Timestamp getCurrentDateStr() {
        Date date = new Date();

        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        Timestamp str = new Timestamp(date.getTime());
        return str;
    }

}
