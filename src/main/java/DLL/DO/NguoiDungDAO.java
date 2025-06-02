package DLL.DO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

import DLL.DA.NguoiDung;
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
            CallableStatement stmt = conn.prepareCall("{Call sp_ThemNguoiDung(?,?,?,?,?,?,?)}");
            // stmt.setString(1, nd.getMaNguoiDung());
            stmt.setString(1, nd.getTenNguoiDung());
            stmt.setString(2, nd.getNgaySinh());
            stmt.setString(3, nd.getSDT());
            stmt.setString(4, nd.getEmail());
            stmt.setString(5, nd.getMatKhau());
            stmt.setString(6, nd.getMaVaiTro());
            stmt.setInt(7, nd.isTrangThai());

            int row = stmt.executeUpdate();
            return row;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int capNhatNguoiDung(NguoiDung nd) {
        try {
            System.out.println("Chuẩn bị cập nhật người dùng với thông tin: " + nd);
            
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
            
            System.out.println("Các tham số cho stored procedure:");
            System.out.println("1. Mã người dùng: " + nd.getMaNguoiDung());
            System.out.println("2. Tên người dùng: " + nd.getTenNguoiDung());
            System.out.println("3. Ngày sinh: " + nd.getNgaySinh());
            System.out.println("4. SDT: " + nd.getSDT());
            System.out.println("5. Email: " + nd.getEmail());
            System.out.println("6. Mật khẩu: " + nd.getMatKhau());
            System.out.println("7. Số dư tài khoản: " + nd.getSoDuTaiKhoan());
            System.out.println("8. Mã vai trò: " + nd.getMaVaiTro());
            System.out.println("9. Trạng thái: " + nd.isTrangThai());
            
            int row = stmt.executeUpdate();
            System.out.println("Kết quả cập nhật từ stored procedure: " + row);
            
            // Cập nhật hashmap nếu thành công
            if (row > 0) {
                // Cập nhật vào hashmap
                listNGUOIDUNG.put(nd.getMaNguoiDung(), nd);
                System.out.println("Đã cập nhật hash map với thông tin mới");
            }

            return row;
        } catch (Exception e) {
            System.err.println("Lỗi trong capNhatNguoiDung: " + e.getMessage());
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
        return null;
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
