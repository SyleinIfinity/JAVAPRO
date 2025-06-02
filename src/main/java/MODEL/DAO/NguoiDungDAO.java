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
                    rs.getInt("trangThai")
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
        // Kiểm tra kết nối database
        if (conn == null || conn.isClosed()) {
            conn = CONNECTIONSQLSERVER.getConnection();
        }
        
        CallableStatement stmt = conn.prepareCall("{Call sp_ThemNguoiDung(?,?,?,?,?,?,?)}");
        
        // Debug: In ra thông tin để kiểm tra
        System.out.println("Đang thêm nhân viên:");
        System.out.println("Tên: " + nd.getTenNguoiDung());
        System.out.println("Ngày sinh: " + nd.getNgaySinh());
        System.out.println("SĐT: " + nd.getSDT());
        System.out.println("Email: " + nd.getEmail());
        System.out.println("Mật khẩu: " + nd.getMatKhau());
        System.out.println("Vai trò: " + nd.getMaVaiTro());
        System.out.println("Trạng thái: " + nd.isTrangThai());
        
        stmt.setString(1, nd.getTenNguoiDung());
        stmt.setString(2, nd.getNgaySinh());
        stmt.setString(3, nd.getSDT());
        stmt.setString(4, nd.getEmail());
        stmt.setString(5, nd.getMatKhau());
        stmt.setString(6, nd.getMaVaiTro());
        stmt.setInt(7, nd.isTrangThai());

        int row = stmt.executeUpdate();
        
        if (row > 0) {
            System.out.println("Thêm nhân viên thành công!");
        } else {
            System.out.println("Không thể thêm nhân viên!");
        }
        
        stmt.close();
        return row;
        
    } catch (Exception e) {
        System.out.println("Lỗi khi thêm nhân viên: " + e.getMessage());
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
            stmt.setInt(9, nd.isTrangThai());
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

    public String getPass(String email) {
        for (NguoiDung nd : listNGUOIDUNG.values()) {
            if (nd.getEmail().equalsIgnoreCase(email)) {
                return nd.getMatKhau(); // Đăng nhập thành công
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

    public void reloadData() {
    listNGUOIDUNG.clear();
    try {
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
                rs.getInt("trangThai")
            );
            listNGUOIDUNG.put(nd.getMaNguoiDung(), nd);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    public static void main(String[] args) {
        NguoiDungDAO nguoiDungDAO = new NguoiDungDAO();

        boolean a = nguoiDungDAO.checkGmail("khanhsky2k5nam@gmail.com");
    }

}
