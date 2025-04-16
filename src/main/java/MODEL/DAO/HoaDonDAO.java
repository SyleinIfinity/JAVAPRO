package MODEL.DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

import MODEL.ENTITY.HoaDon;
import UTILS.CONNECTIONDATA.CONNECTIONSQLSERVER;

public class HoaDonDAO {
    HashMap <String, HoaDon> listHOADON;
    Connection conn = null;

    public HoaDonDAO(){
        listHOADON = new HashMap<>();


        try {
            conn = CONNECTIONSQLSERVER.GetConnection();

            CallableStatement stmt = conn.prepareCall("{Call sp_LayDanhSachHoaDon}");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                NguoiDung nd = new HoaDon(
                    rs.getString("maHoaDon"),
                    rs.getString("maDatPhong"),
                    rs.getString("maDichVu"),
                    rs.getString("nhanVienPhuTrach"),
                    rs.getDouble("tongTien"),
                    rs.getString("ngayGiaoDich"),
                    rs.getString("phuongThucThanhToan")
                );
                listHOADON.put(nd.getMaHoaDon(), nd);
            }
            System.out.println("Load thành công");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi");
        }
    }

    public HashMap<String, HoaDon> getListHOADON(){
        return listHOADON;
    }

    public HoaDon getHoaDon(String maHoaDon){
        return listHOADON.get(maHoaDon);
    }

    public int themHoaDon(HoaDon hd){
        try {
            CallableStatement stmt = conn.prepareCall("{Call sp_ThemHoaDon(?,?,?,?,?,?,?,?)}");
            stmt.setString(1, hd.getMaHoaDon());
            stmt.setString(2, hd.getMaDatPhong());
            stmt.setString(3, hd.getMaDichVu());
            stmt.setString(4, hd.getNhanVienPhuTrach());
            stmt.setDouble(5, hd.getTongTien());
            stmt.setString(6, hd.getNgayGiaoDich());
            stmt.setString(7, hd.getPhuongThucThanhToan());

            int row = stmt.executeUpdate();
            return row;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int capNhatHoaDon(HoaDon hd){
        try {
            CallableStatement stmt = conn.prepareCall("{Call sp_CapNhatHoaDon(?,?,?,?,?,?,?,?)}");
            stmt.setString(1, hd.getMaHoaDon());
            stmt.setString(2, hd.getMaDatPhong());
            stmt.setString(3, hd.getMaDichVu());
            stmt.setString(4, hd.getNhanVienPhuTrach());
            stmt.setDouble(5, hd.getTongTien());
            stmt.setString(6, hd.getNgayGiaoDich());
            stmt.setString(7, hd.getPhuongThucThanhToan());

            int row = stmt.executeUpdate();

            return row;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int xoaHoaDon(String maHoaDon){
        try {
            CallableStatement stmt = conn.prepareCall("{Call sp_XoaHoaDon(?)}");
            stmt.setString(1, maHoaDon);

            int row = stmt.executeUpdate();
            return row;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static void main(String[] args) {
        HoaDonDAO hdD = new HoaDonDAO();
    
    }

}