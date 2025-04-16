package MODEL.DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

import MODEL.ENTITY.Phong;
import UTILS.CONNECTIONDATA.CONNECTIONSQLSERVER;

public class PhongDAO {
    HashMap <String, Phong> listPHONG;
    Connection conn = null;

    public PhongDAO(){
        listPHONG = new HashMap<>();
        try {
            conn = CONNECTIONSQLSERVER.GetConnection();

            CallableStatement stmt = conn.prepareCall("{Call sp_LayDanhSachPhong}");

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Phong p = new Phong(
                    rs.getString("maPhong"),
                    rs.getString("soPhong"),
                    rs.getString("maLoaiPhong"),
                    rs.getInt("soTang"),
                    rs.getString("maChiNhanh"),
                    rs.getString("trangThai")
                );
                listPHONG.put(p.getMaPhong(), p);
            }
            System.out.println("Load thành công");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi");
        }
    }

    public HashMap<String, Phong> listPHONG(){
        return listPHONG;
    }

    public Phong getPhong(String maPhong){
        return listPHONG.get(maPhong);
    }
    
    public int themPhong(Phong p){
        try {
            CallableStatement stmt = conn.prepareCall("{Call sp_ThemPhong(?,?,?)}");
            stmt.setString(1, p.getMaPhong());
            stmt.setString(2, p.getSoPhong());
            stmt.setString(3, p.getMaLoaiPhong());
            stmt.setInt(4, p.getSoTang());
            stmt.setString(5, p.getMaChiNhanh());
            stmt.setString(6, p.getTrangThai());

            int row = stmt.executeUpdate();
            return row;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int capNhatPhong(Phong p){
        try {
            CallableStatement stmt = conn.prepareCall("{Call sp_CapNhatPhong(?,?,?)}");
            stmt.setString(1, p.getMaPhong());
            stmt.setString(2, p.getSoPhong());
            stmt.setString(3, p.getMaLoaiPhong());
            stmt.setInt(4, p.getSoTang());
            stmt.setString(5, p.getMaChiNhanh());
            stmt.setString(6, p.getTrangThai());

            int row = stmt.executeUpdate();
            return row;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int xoaPhong(String maPhong){
        try {
            CallableStatement stmt = conn.prepareCall("{Call sp_XoaPhong(?)}");
            stmt.setString(1, maPhong);

            int row = stmt.executeUpdate();
            return row;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    
    public static void main(String[] args) {
        PhongDAO pD = new PhongDAO();
    }
}
