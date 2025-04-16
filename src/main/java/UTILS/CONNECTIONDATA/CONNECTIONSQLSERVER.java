package UTILS.CONNECTIONDATA;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CONNECTIONSQLSERVER {
    private static final String UserName = "sa";
    private static final String PassWord = "12345";
    // private static final String  DriverURL;
    private static final String url = "jdbc:sqlserver://localhost:1433;databaseName=quanlykhachsan;encrypt=true;trustServerCertificate=true";
    private static Connection cnn=null;
    private static Statement stm=null;

    public CONNECTIONSQLSERVER()
    {
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = "jdbc:sqlserver://localhost:1433;databaseName=quanlykhachsan;encrypt=true;trustServerCertificate=true";
            // this.UserName = "sa";
            // this.PassWord = "12345";
            this.cnn = DriverManager.getConnection(url, this.UserName, this.PassWord);
            System.out.println("Connect Success!");
        } catch(Exception ex)
        {
            System.out.println("Error" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            if (cnn == null || cnn.isClosed()) {
                cnn = DriverManager.getConnection(url, UserName, PassWord);
            }
            return cnn;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    

    public static void main(String[] args) {
        Connection conn = getConnection();
        if (conn != null) {
            System.out.println("Thành công");
        }
    }

}
