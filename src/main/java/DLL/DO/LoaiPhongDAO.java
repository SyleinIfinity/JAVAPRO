package DLL.DO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

import DLL.DA.LoaiPhong;
import UTILS.CONNECTIONDATA.CONNECTIONSQLSERVER;

public class LoaiPhongDAO {
    HashMap <String, LoaiPhong> listLOAIPHONG;
    Connection conn = null;

    public LoaiPhongDAO(){
        listLOAIPHONG = new HashMap<>();


        try {
            conn = CONNECTIONSQLSERVER.getConnection();

            CallableStatement stmt = conn.prepareCall("{Call sp_LayDanhSachLoaiPhong}");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LoaiPhong lp = new LoaiPhong(
                    rs.getString("maLoaiPhong"),
                    rs.getString("tenLoaiPhong"),
                    rs.getInt   ("soLuongToiDa"),
                    rs.getString("moTa"),
                    rs.getDouble("giaPhong")
                );
                listLOAIPHONG.put(lp.getMaLoaiPhong(), lp);
            }
            System.out.println("Load thành công");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi");
        }
    }

    public HashMap<String, LoaiPhong> getListLOAIPHONG(){
        return listLOAIPHONG;
    }

    public LoaiPhong getLoaiPhong(String maLoaiPhong){
        return listLOAIPHONG.get(maLoaiPhong);
    }

    public int themLoaiPhong(LoaiPhong lp){
        try {
            CallableStatement stmt = conn.prepareCall("{Call sp_ThemLoaiPhong(?,?,?,?,?)}");
            stmt.setString(1, lp.getMaLoaiPhong());
            stmt.setString(2, lp.getTenLoaiPhong());
            stmt.setInt   (3, lp.getSoLuongToiDa());
            stmt.setString(4, lp.getMoTa());
            stmt.setDouble(5, lp.getGiaTien());

            int row = stmt.executeUpdate();
            return row;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int capNhatLoaiPhong(LoaiPhong lp){
        try {
            CallableStatement stmt = conn.prepareCall("{Call sp_CapNhatLoaiPhong(?,?,?,?,?)}");
            stmt.setString(1, lp.getMaLoaiPhong());
            stmt.setString(2, lp.getTenLoaiPhong());
            stmt.setInt   (3, lp.getSoLuongToiDa());
            stmt.setString(4, lp.getMoTa());
            stmt.setDouble(5, lp.getGiaTien());

            int row = stmt.executeUpdate();

            return row;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int xoaLoaiPhong(String maLoaiPhong){
        try {
            CallableStatement stmt = conn.prepareCall("{Call sp_XoaLoaiPhong(?)}");
            stmt.setString(1, maLoaiPhong);

            int row = stmt.executeUpdate();
            return row;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public LoaiPhong getLoaiPhongByTen(String tenLoaiPhong) {
        for (LoaiPhong lp : listLOAIPHONG.values()) {
            if (lp.getTenLoaiPhong().equals(tenLoaiPhong)) {
                return lp;
            }
        }
        return null; // Không tìm thấy loại phòng
    }

    public static void main(String[] args) {
        LoaiPhongDAO lpD = new LoaiPhongDAO();
    
    }

}