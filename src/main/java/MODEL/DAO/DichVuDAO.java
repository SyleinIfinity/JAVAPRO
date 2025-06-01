package MODEL.DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

import MODEL.ENTITY.DichVu;
import UTILS.CONNECTIONDATA.CONNECTIONSQLSERVER;

public class DichVuDAO {
    HashMap<String, DichVu> listDICHVU;
    Connection conn = null;

    public DichVuDAO(){
        listDICHVU = new HashMap<>();
        try {
            conn = CONNECTIONSQLSERVER.getConnection();

            CallableStatement stmt = conn.prepareCall("{Call sp_LayDanhSachDichVu}");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                DichVu dv = new DichVu(
                    rs.getString("maDichVu"),
                    rs.getString("tenDichVu"),
                    rs.getString("giaDichVu"),
                    rs.getString("moTa")
                );
                listDICHVU.put(dv.getMaDichVu(), dv);
            }
            System.out.println("Load thanh cong");
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi");
        }
    }

    public HashMap<String, DichVu> getListDICHVU(){
        return listDICHVU;
    }

    public DichVu getDichVu(String maDichvu){
        return listDICHVU.get(maDichvu);
    }

// Sửa phương thức themDichVu
public int themDichVu(DichVu dv){
    try {
        // Kiểm tra kết nối
        if (conn == null || conn.isClosed()) {
            conn = CONNECTIONSQLSERVER.getConnection();
        }
        
        // CHỈ SỬ DỤNG 3 THAM SỐ - không truyền maDichVu vì stored procedure tự tạo
        CallableStatement stmt = conn.prepareCall("{Call sp_ThemDichVu(?,?,?)}");
        stmt.setString(1, dv.getTenDichVu());    // tenDichVu
        stmt.setDouble(2, Double.parseDouble(dv.getGiaDichVu())); // giaDichVu - chuyển sang DECIMAL
        stmt.setString(3, dv.getMoTa());         // moTa

        System.out.println("Đang thêm dịch vụ:");
        System.out.println("- Tên: " + dv.getTenDichVu());
        System.out.println("- Giá: " + dv.getGiaDichVu());
        System.out.println("- Mô tả: " + dv.getMoTa());
        System.out.println("- Mã sẽ được tự động tạo bởi stored procedure");

        int row = stmt.executeUpdate();
        
        // Nếu thành công, cần refresh lại để lấy mã mới được tạo
        if (row > 0) {
            refreshFromDatabase(); // Làm mới dữ liệu để có mã mới
            System.out.println("Thêm thành công - dữ liệu đã được làm mới");
        }
        
        stmt.close();
        return row;
    } catch (Exception e) {
        System.out.println("Lỗi khi thêm dịch vụ:");
        e.printStackTrace();
        return -1;
    }
}

// Constructor để tạo dịch vụ mới (chỉ cần tên, giá, mô tả)
public DichVu createNewDichVu(String tenDichVu, String giaDichVu, String moTa) {
    return new DichVu("", tenDichVu, giaDichVu, moTa);
}

    public int capNhatDichVu(DichVu dv){
        try {
            // Kiểm tra kết nối
            if (conn == null || conn.isClosed()) {
                conn = CONNECTIONSQLSERVER.getConnection();
            }
            
            // Sử dụng 4 tham số cho stored procedure
            CallableStatement stmt = conn.prepareCall("{Call sp_CapNhatDichVu(?,?,?,?)}");
            stmt.setString(1, dv.getMaDichVu());
            stmt.setString(2, dv.getTenDichVu());
            stmt.setString(3, dv.getGiaDichVu());
            stmt.setString(4, dv.getMoTa());

            int row = stmt.executeUpdate();

            // Cập nhật HashMap nếu thành công
            if (row > 0) {
                listDICHVU.put(dv.getMaDichVu(), dv);
            }

            stmt.close();
            return row;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int xoaDichVu(String maDichVu){
        try {
            // Kiểm tra kết nối
            if (conn == null || conn.isClosed()) {
                conn = CONNECTIONSQLSERVER.getConnection();
            }
            
            CallableStatement stmt = conn.prepareCall("{Call sp_XoaDichVu(?)}");
            stmt.setString(1, maDichVu);

            int row = stmt.executeUpdate();
            
            // Xóa khỏi HashMap nếu thành công
            if (row > 0) {
                listDICHVU.remove(maDichVu);
            }
            
            stmt.close();
            return row;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    // Thêm phương thức để làm mới dữ liệu từ database
    public void refreshFromDatabase() {
        try {
            // Kiểm tra kết nối
            if (conn == null || conn.isClosed()) {
                conn = CONNECTIONSQLSERVER.getConnection();
            }
            
            listDICHVU.clear();
            
            CallableStatement stmt = conn.prepareCall("{Call sp_LayDanhSachDichVu}");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                DichVu dv = new DichVu(
                    rs.getString("maDichVu"),
                    rs.getString("tenDichVu"),
                    rs.getString("giaDichVu"),
                    rs.getString("moTa")
                );
                listDICHVU.put(dv.getMaDichVu(), dv);
            }
            
            rs.close();
            stmt.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Phương thức đóng kết nối
    public void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
    DichVuDAO dvD = new DichVuDAO();

    try {
        // Test thêm dịch vụ mới - CHỈ CẦN NHẬP: tên, giá, mô tả
        System.out.println("=== TEST THÊM DỊCH VỤ ===");
        DichVu dv = dvD.createNewDichVu("Dịch vụ massage", "120000", "Massage toàn thân");
        
        int row = dvD.themDichVu(dv);
        if (row > 0) {
            System.out.println("✓ Thêm thành công");
            
            // In ra danh sách để xem mã mới được tạo
            System.out.println("\nDanh sách dịch vụ sau khi thêm:");
            for (DichVu dichvu : dvD.getListDICHVU().values()) {
                System.out.println("- " + dichvu.getMaDichVu() + ": " + dichvu.getTenDichVu() 
                                 + " | Giá: " + dichvu.getGiaDichVu());
            }
        } else {
            System.out.println("✗ Thêm thất bại");
        }

        // Test với cập nhật và xóa vẫn giữ nguyên cách cũ
        // (Uncomment để test nếu cần)
        /*
        // Cập nhật dịch vụ - VẪN CẦN NHẬP MÃ
        DichVu dvUpdate = new DichVu("DV001", "Tên mới", "150000", "Mô tả mới");
        int rowUpdate = dvD.capNhatDichVu(dvUpdate);
        
        // Xóa dịch vụ - VẪN CẦN NHẬP MÃ
        int rowDelete = dvD.xoaDichVu("DV001");
        */

    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        dvD.closeConnection();
    }
}
}