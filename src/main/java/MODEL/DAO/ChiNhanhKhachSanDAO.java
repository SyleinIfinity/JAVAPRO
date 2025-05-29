package MODEL.DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

import MODEL.ENTITY.ChiNhanhKhachSan;
import UTILS.CONNECTIONDATA.CONNECTIONSQLSERVER;

public class ChiNhanhKhachSanDAO {
    HashMap <String, ChiNhanhKhachSan> listCHINHANHKHACHSAN;
    Connection conn = null;

    public ChiNhanhKhachSanDAO(){
        listCHINHANHKHACHSAN = new HashMap<>();
        try {
            conn = CONNECTIONSQLSERVER.getConnection();

            CallableStatement stmt = conn.prepareCall("{Call sp_LayDanhSachChiNhanh}");

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ChiNhanhKhachSan cnks = new ChiNhanhKhachSan(
                    rs.getString("maChiNhanh"),
                    rs.getString("tenChiNhanh"),
                    rs.getString("diaChi"),
                    rs.getString("SDT")
                );
                listCHINHANHKHACHSAN.put(cnks.getMaChiNhanh(), cnks);
            }
            System.out.println("Load thành công");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi");
        }
    }

    public HashMap<String, ChiNhanhKhachSan> listCHINHANHKHACHSAN(){
        return listCHINHANHKHACHSAN;
    }
    
    public ChiNhanhKhachSan getChiNhanhKhachSan(String maChiNhanh){
        return listCHINHANHKHACHSAN.get(maChiNhanh);
    }
    
    public int themChiNhanhKhachSan(ChiNhanhKhachSan cnks){
        try {
            CallableStatement stmt = conn.prepareCall("{Call sp_ThemChiNhanhKhachSan(?,?,?)}");
            stmt.setString(1, cnks.getTenChiNhanh());
            stmt.setString(2, cnks.getDiaChi());
            stmt.setString(3, cnks.getSDT());

            int row = stmt.executeUpdate();
            return row;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int capNhatChiNhanhKhachSan(ChiNhanhKhachSan cnks){
        try {
            CallableStatement stmt = conn.prepareCall("{Call sp_CapNhatChiNhanhKhachSan(?,?,?,?)}");
            stmt.setString(1, cnks.getMaChiNhanh());
            stmt.setString(2, cnks.getTenChiNhanh());
            stmt.setString(3, cnks.getDiaChi());
            stmt.setString(4, cnks.getSDT());

            int row = stmt.executeUpdate();
            return row;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int xoaChiNhanhKhachSan(String maChiNhanh){
        try {
            CallableStatement stmt = conn.prepareCall("{Call sp_XoaChiNhanhKhachSan(?)}");
            stmt.setString(1, maChiNhanh);

            int row = stmt.executeUpdate();
            return row;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static void main(String[] args) {
        ChiNhanhKhachSanDAO cnksD = new ChiNhanhKhachSanDAO();
    }
}
