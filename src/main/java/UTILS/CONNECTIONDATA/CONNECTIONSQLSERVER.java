package UTILS.CONNECTIONDATA;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CONNECTIONSQLSERVER implements IConnector {
    private  String UserName;
    private  String PassWord ;
    // private  String DataBaseName;
    // private  String ServerName;
    // private  String  DriverClass;
    private  String  DriverURL;
    private  Connection cnn=null;
    private   Statement stm=null;

    public CONNECTIONSQLSERVER()
    {
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = "jdbc:sqlserver://localhost:1433;databaseName=quanlykhachsan;encrypt=true;trustServerCertificate=true";
            this.UserName = "sa";
            this.PassWord = "12345";
            this.cnn = DriverManager.getConnection(url, this.UserName, this.PassWord);
            System.out.println("Connect Success!");
        } catch(Exception ex)
        {
            System.out.println("Error" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void Open() {
            try{
            cnn = DriverManager.getConnection(this.DriverURL, this.UserName, this.PassWord);
            System.out.println("Connect Success!");
            stm = cnn.createStatement();  
            }catch(Exception ex){}
    }

    public Connection GetConnection() {
        return this.cnn;
    }

    @Override
    public void Close() {
        try{
        this.stm.close();
        this.cnn.close();
        }catch(SQLException ex){}
    }

    @Override
    public ResultSet GetResultSetSQL(String SQL) {
        try{
        this.Open();
        this.stm = cnn.createStatement();
        ResultSet rs=stm.executeQuery(SQL);        
        return  rs;
        }catch(SQLException ex){}
    
        return null;
    }

    @Override
    public ResultSet GetResultSetStoredProcedurce(String procedureName, Object[] param) {
        ResultSet rs = null;
    

        try {
            this.Open();
            CallableStatement  ps = this.cnn.prepareCall("{call "+procedureName+"}");
            if(param!=null)
            {
            int i = 1;
            for (Object value : param) {
                ps.setObject(i, value);
                i++;
            }
            }
            rs = ps.executeQuery();
            ps.close();
            return rs;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public int ExecuteUpdateSQL(String SQL) {
        try {
            this.Open(); // Mở kết nối
            if (this.cnn == null) {
                System.out.println("Connection is null!");
                return 0;
            }
            PreparedStatement pstmt = this.cnn.prepareStatement(SQL);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int ExecuteUpdateStoredProcedurce(String procedureName, Object[] param) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public static void main(String[] args) {
        CONNECTIONSQLSERVER conn = new CONNECTIONSQLSERVER();
    }

}
