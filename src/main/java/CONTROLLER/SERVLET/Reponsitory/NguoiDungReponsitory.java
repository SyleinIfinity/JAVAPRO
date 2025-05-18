package CONTROLLER.SERVLET.Reponsitory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import MODEL.DAO.NguoiDungDAO;
import MODEL.ENTITY.NguoiDung;
import UTILS.CONNECTIONDATA.CONNECTIONSQLSERVER;

public class NguoiDungReponsitory {
  Connection conn = null;
  List<NguoiDung> users;

  public NguoiDungReponsitory() {
    conn = CONNECTIONSQLSERVER.getConnection();
    if (conn != null) {
        System.out.println("Thành công");
    }
  }

   public List<NguoiDung> getUserByEmailAndPassword(String email, String pass) {
        users = new ArrayList<>();
        String query = "SELECT maNguoiDung, tenNguoiDung, ngaySinh, SDT, email, soDuTaiKhoan, maVaiTro " +
                       "FROM NguoiDung " +
                       "WHERE email=? AND matKhau=? AND (maVaiTro = 'R002' OR maVaiTro = 'R003')";
        try{
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, email);
            pstmt.setString(2, pass);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    NguoiDung user = new NguoiDung();
                    user.setMaNguoiDung(rs.getString("maNguoiDung"));
                    user.setTenNguoiDung(rs.getString("tenNguoiDung"));
                    user.setNgaySinh(rs.getString("ngaySinh"));
                    user.setSDT(rs.getString("SDT"));
                    user.setEmail(rs.getString("email"));
                    user.setSoDuTaiKhoan(rs.getDouble("soDuTaiKhoan"));
                    user.setMaVaiTro(rs.getString("maVaiTro"));
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static void main(String[] args) {
        NguoiDungReponsitory ndgh = new NguoiDungReponsitory();
    
        List<NguoiDung> list = ndgh.getUserByEmailAndPassword("a@gmail.com", "khanh123");
    
        if (list.isEmpty()) {
            System.out.println("Không tìm thấy người dùng.");
        } else {
            for (NguoiDung user : list) {
                System.out.println("Mã người dùng: " + user.getMaNguoiDung());
                System.out.println("Tên: " + user.getTenNguoiDung());
                System.out.println("Ngày sinh: " + user.getNgaySinh());
                System.out.println("SĐT: " + user.getSDT());
                System.out.println("Email: " + user.getEmail());
                System.out.println("Số dư: " + user.getSoDuTaiKhoan());
                System.out.println("Vai trò: " + user.getMaVaiTro());
                System.out.println("-----------");
            }
        }
    }
    

}
