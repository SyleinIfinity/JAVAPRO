package MODEL.DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import MODEL.ENTITY.HoaDon;
import UTILS.CONNECTIONDATA.CONNECTIONSQLSERVER;

public class HoaDonDAO {
    HashMap <String, HoaDon> listHOADON;
    Connection conn = null;

    public HoaDonDAO(){
        listHOADON = new HashMap<>();


        try {
            conn = CONNECTIONSQLSERVER.getConnection();

            CallableStatement stmt = conn.prepareCall("{Call sp_LayDanhSachHoaDon}");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                HoaDon hd = new HoaDon(
                    rs.getString("maHoaDon"),
                    rs.getString("maDatPhong"),
                    rs.getString("maDichVu"),
                    rs.getString("nhanVienPhuTrach"),
                    rs.getDouble("tongTien"),
                    rs.getString("ngayGiaoDich")
                );
                listHOADON.put(hd.getMaHoaDon(), hd);
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
            CallableStatement stmt = conn.prepareCall("{Call sp_ThemHoaDon(?,?,?,?,?)}");
            // stmt.setString(1, hd.getMaHoaDon());
            stmt.setString(1, hd.getMaDatPhong());
            stmt.setString(2, hd.getMaDichVu());
            stmt.setString(3, hd.getNhanVienPhuTrach());
            stmt.setDouble(4, hd.getTongTien());
            stmt.setString(5, hd.getNgayGiaoDich());

            int row = stmt.executeUpdate();
            return row;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int capNhatHoaDon(HoaDon hd){
        try {
            CallableStatement stmt = conn.prepareCall("{Call sp_CapNhatHoaDon(?,?,?,?,?,?)}");
            stmt.setString(1, hd.getMaHoaDon());
            stmt.setString(2, hd.getMaDatPhong());
            stmt.setString(3, hd.getMaDichVu());
            stmt.setString(4, hd.getNhanVienPhuTrach());
            stmt.setDouble(5, hd.getTongTien());
            stmt.setString(6, hd.getNgayGiaoDich());

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
    
    public List<HoaDon> getHoaDon(Date from, Date to, String tenChiNhanh) {
        List<HoaDon> result = new ArrayList<>();
        try {
            CallableStatement stmt = conn.prepareCall("{Call sp_LayHoaDonTheoNgayVaChiNhanh(?,?,?)}");
            stmt.setDate(1, from);
            stmt.setDate(2, to);
            stmt.setString(3, tenChiNhanh);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                HoaDon hd = new HoaDon(
                    rs.getString("maHoaDon"),
                    rs.getString("maDatPhong"),
                    rs.getString("maDichVu"),
                    rs.getString("nhanVienPhuTrach"),
                    rs.getDouble("tongTien"),
                    rs.getString("ngayGiaoDich")
                );
                result.add(hd);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }



}