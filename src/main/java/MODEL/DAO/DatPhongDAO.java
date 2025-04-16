package MODEL.DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

import MODEL.ENTITY.DatPhong;
import UTILS.CONNECTIONDATA.CONNECTIONSQLSERVER;

public class DatPhongDAO {
    HashMap <String, DatPhong> listDATPHONG;
    Connection conn = null;

    public DatPhongDAO(){
        listDATPHONG = new HashMap<>();


        try {
            conn = CONNECTIONSQLSERVER.GetConnection();

            CallableStatement stmt = conn.prepareCall("{Call sp_LayDanhSachDatPhong}");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                DatPhong dp = new DatPhong(
                    rs.getString("maDatPhong"),
                    rs.getString("maNguoiDung"),
                    rs.getString("maPhong"),
                    rs.getString("soNguoi"),
                    rs.getString("dichVuSuDung"),
                    rs.getString("ngayThuePhong"),
                    rs.getString("ngayTraPhong"),
                    rs.getString("trangThai")
                );
                listDATPHONG.put(dp.getMaDatPhong(), dp);
            }
            System.out.println("Load thành công");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi");
        }
    }

    public HashMap<String, DatPhong> getListDATPHONG(){
        return listDATPHONG;
    }

    public DatPhong getDatPhong(String maDatPhong){
        return listDATPHONG.get(maDatPhong);
    }

    public int themDatPhong(DatPhong dp){
        try {
            CallableStatement stmt = conn.prepareCall("{Call sp_ThemDatPhong(?,?,?,?,?,?,?,?)}");
            stmt.setString(1, dp.getMaDatPhong());
            stmt.setString(2, dp.getMaNguoiDung());
            stmt.setString(3, dp.getMaPhong());
            stmt.setString(4, dp.getSoNguoi());
            stmt.setString(5, dp.getDichVuSuDung());
            stmt.setString(6, dp.getNgayThuePhong());
            stmt.setString( 7, dp.getNgayTraPhong());
            stmt.setString(8, dp.getTrangThai());

            int row = stmt.executeUpdate();
            return row;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int capNhatDatPhong(DatPhong dp){
        try {
            CallableStatement stmt = conn.prepareCall("{Call sp_CapNhatDatPhong(?,?,?,?,?,?,?,?)}");
            stmt.setString(1, dp.getMaDatPhong());
            stmt.setString(2, dp.getMaNguoiDung());
            stmt.setString(3, dp.getMaPhong());
            stmt.setString(4, dp.getSoNguoi());
            stmt.setString(5, dp.getDichVuSuDung());
            stmt.setString(6, dp.getNgayThuePhong());
            stmt.setString( 7, dp.getNgayTraPhong());
            stmt.setString(8, dp.getTrangThai());

            int row = stmt.executeUpdate();

            return row;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int xoaDatPhong(String maDatPhong){
        try {
            CallableStatement stmt = conn.prepareCall("{Call sp_XoaDatPhong(?)}");
            stmt.setString(1, maDatPhong);

            int row = stmt.executeUpdate();
            return row;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static void main(String[] args) {
        DatPhongDAO dpD = new DatPhongDAO();
    
    }

}