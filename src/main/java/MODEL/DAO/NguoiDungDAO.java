package MODEL.DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

import MODEL.ENTITY.NguoiDung;
import MODEL.ENTITY.NguoiDung;
import UTILS.CONNECTIONDATA.CONNECTIONSQLSERVER;

public class NguoiDungDAO {
    HashMap <String, NguoiDung> listNGUOIDUNG;
    Connection conn = null;

    public NguoiDungDAO(){
        listNGUOIDUNG = new HashMap<>();


        try {
            conn = CONNECTIONSQLSERVER.getConnection();

            CallableStatement stmt = conn.prepareCall("{Call sp_LayDanhSachNguoiDung}");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                NguoiDung nd = new NguoiDung(
                    rs.getString("maNguoiDung"),
                    rs.getString("tenNguoiDung"),
                    rs.getString("ngaySinh"),
                    rs.getString("SDT"),
                    rs.getString("email"),
                    rs.getString("matKhau"),
                    rs.getDouble("soDuTaiKhoan"),
                    rs.getString("maVaiTro"),
                    rs.getBoolean("trangThai")
                );
                listNGUOIDUNG.put(nd.getMaNguoiDung(), nd);
            }
            System.out.println("Load thành công");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi");
        }
    }

    public HashMap<String, NguoiDung> getListNGUOIDUNG(){
        return listNGUOIDUNG;
    }

    public NguoiDung getNguoiDung(String maNguoiDung){
        return listNGUOIDUNG.get(maNguoiDung);
    }

    public int themNguoiDung(NguoiDung nd){
        try {
            CallableStatement stmt = conn.prepareCall("{Call sp_ThemNguoiDung(?,?,?,?,?,?,?,?,?)}");
            stmt.setString(1, nd.getMaNguoiDung());
            stmt.setString(2, nd.getTenNguoiDung());
            stmt.setString(3, nd.getNgaySinh());
            stmt.setString(4, nd.getSDT());
            stmt.setString(5, nd.getEmail());
            stmt.setString(6, nd.getMatKhau());
            stmt.setDouble(7, nd.getSoDuTaiKhoan());
            stmt.setString(8, nd.getMaVaiTro());
            stmt.setBoolean(9, nd.isTrangThai());

            int row = stmt.executeUpdate();
            return row;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int capNhatNguoiDung(NguoiDung nd){
        try {
            CallableStatement stmt = conn.prepareCall("{Call sp_CapNhatNguoiDung(?,?,?,?,?,?,?,?,?)}");
            stmt.setString(1, nd.getMaNguoiDung());
            stmt.setString(2, nd.getTenNguoiDung());
            stmt.setString(3, nd.getNgaySinh());
            stmt.setString(4, nd.getSDT());
            stmt.setString(5, nd.getEmail());
            stmt.setString(6, nd.getMatKhau());
            stmt.setDouble(7, nd.getSoDuTaiKhoan());
            stmt.setString(8, nd.getMaVaiTro());
            stmt.setBoolean(9, nd.isTrangThai());
            int row = stmt.executeUpdate();

            return row;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int xoaNguoiDung(String maNguoiDung){
        try {
            CallableStatement stmt = conn.prepareCall("{Call sp_XoaNguoiDung(?)}");
            stmt.setString(1, maNguoiDung);

            int row = stmt.executeUpdate();
            return row;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public NguoiDung checkLogin(String email, String password) {
        for (NguoiDung nd : listNGUOIDUNG.values()) {
            if (nd.getEmail().equalsIgnoreCase(email) && nd.getMatKhau().equals(password)) {
                return nd; // Đăng nhập thành công
            }
        }
        return null; // Sai tài khoản hoặc mật khẩu
    }

    public boolean checkGmail(String email){
        for(NguoiDung nd : listNGUOIDUNG.values()){
            if (nd.getEmail().equalsIgnoreCase(email)) {
                System.out.println(nd.getEmail());
                System.out.println(email);
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        NguoiDungDAO nguoiDungDAO = new NguoiDungDAO();

        boolean a = nguoiDungDAO.checkGmail("khanhsky2k5nam@gmail.com");
    }

}
