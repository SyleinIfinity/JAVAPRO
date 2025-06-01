package CONTROLLER.APP.ADMIN;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import MODEL.DAO.NguoiDungDAO;
import MODEL.DAO.VaiTroDAO;
import MODEL.ENTITY.NguoiDung;
import MODEL.ENTITY.VaiTro;
import VIEW.ADMIN.view_NhanVien;

public class ctl_NhanVien implements ActionListener, KeyListener {
    private view_NhanVien viewNhanVien;
    private NguoiDungDAO nguoiDungDAO;
    private VaiTroDAO vaiTroDAO;
    private DefaultTableModel tableModel;
    private JTextField txtTimKiem;

    public ctl_NhanVien(view_NhanVien viewNhanVien) {
        this.viewNhanVien = viewNhanVien;
        
        // Khởi tạo DAO
        try {
            this.nguoiDungDAO = new NguoiDungDAO();
            this.vaiTroDAO = new VaiTroDAO();
            System.out.println("Khởi tạo DAO thành công");
        } catch (Exception e) {
            System.out.println("Lỗi khởi tạo DAO: " + e.getMessage());
            e.printStackTrace();
        }
        
        this.tableModel = viewNhanVien.getModel();
        
        // Thêm action listeners cho các button
        addActionListeners();
        
        // Load dữ liệu ban đầu
        loadDataToTable();
    }

    private void addActionListeners() {
        viewNhanVien.getBtnThem().addActionListener(this);
        viewNhanVien.getBtnSua().addActionListener(this);
        viewNhanVien.getBtnXoa().addActionListener(this);
        viewNhanVien.getBtnTimKiem().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        
        if (source == viewNhanVien.getBtnThem()) {
            themNhanVien();
        } else if (source == viewNhanVien.getBtnSua()) {
            suaNhanVien();
        } else if (source == viewNhanVien.getBtnXoa()) {
            xoaNhanVien();
        } else if (source == viewNhanVien.getBtnTimKiem()) {
            timKiemNhanVien();
        }
    }

    private void loadDataToTable() {
        try {
            System.out.println("Bắt đầu load dữ liệu vào bảng...");
            
            // Xóa dữ liệu cũ
            tableModel.setRowCount(0);
            
            // Kiểm tra DAO có null không
            if (nguoiDungDAO == null) {
                System.out.println("nguoiDungDAO là null, khởi tạo lại...");
                nguoiDungDAO = new NguoiDungDAO();
            }
            
            if (vaiTroDAO == null) {
                System.out.println("vaiTroDAO là null, khởi tạo lại...");
                vaiTroDAO = new VaiTroDAO();
            }
            
            HashMap<String, NguoiDung> listNguoiDung = nguoiDungDAO.getListNGUOIDUNG();
            HashMap<String, VaiTro> listVaiTro = vaiTroDAO.getListVAITRO();
            
            System.out.println("Số lượng người dùng: " + (listNguoiDung != null ? listNguoiDung.size() : 0));
            System.out.println("Số lượng vai trò: " + (listVaiTro != null ? listVaiTro.size() : 0));
            
            if (listNguoiDung == null || listNguoiDung.isEmpty()) {
                System.out.println("Danh sách người dùng trống!");
                JOptionPane.showMessageDialog(viewNhanVien, 
                    "Không có dữ liệu người dùng!", 
                    "Thông báo", 
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            int countNhanVien = 0;
            for (NguoiDung nd : listNguoiDung.values()) {
                System.out.println("Xử lý người dùng: " + nd.getMaNguoiDung() + " - Vai trò: " + nd.getMaVaiTro());
                
                // Lấy thông tin vai trò
                VaiTro vaiTro = null;
                if (listVaiTro != null) {
                    vaiTro = listVaiTro.get(nd.getMaVaiTro());
                }
                
                // Kiểm tra vai trò - hiển thị tất cả nhân viên và quản lý
                boolean isNhanVien = false;
                if (vaiTro != null) {
                    String tenVaiTro = vaiTro.getTenVaiTro();
                    System.out.println("Tên vai trò: " + tenVaiTro);
                    isNhanVien = tenVaiTro.equals("Nhân viên") || 
                                 tenVaiTro.equals("Quản lý") ||
                                 nd.getMaVaiTro().equals("NV") ||
                                 nd.getMaVaiTro().equals("QL");
                } else {
                    // Nếu không tìm thấy vai trò, kiểm tra theo mã vai trò
                    isNhanVien = nd.getMaVaiTro().equals("NV") || nd.getMaVaiTro().equals("QL");
                    System.out.println("Không tìm thấy vai trò, kiểm tra theo mã: " + nd.getMaVaiTro());
                }
                
                if (isNhanVien) {
                    String trangThai = chuyenDoiTrangThai(nd.isTrangThai());
                    
                    Object[] row = {
                        nd.getMaNguoiDung(),
                        nd.getTenNguoiDung(),
                        nd.getNgaySinh(),
                        nd.getSDT(),
                        nd.getEmail(),
                        trangThai
                    };
                    tableModel.addRow(row);
                    countNhanVien++;
                    System.out.println("Đã thêm nhân viên: " + nd.getTenNguoiDung());
                }
            }
            
            System.out.println("Tổng số nhân viên được thêm vào bảng: " + countNhanVien);
            
            if (countNhanVien == 0) {
                // Thêm một số dữ liệu mẫu để test
                System.out.println("Không có nhân viên nào. Thêm dữ liệu mẫu để test...");
                tableModel.addRow(new Object[]{"NV001", "Nguyễn Văn A", "1990-01-01", "0123456789", "a.nguyen@email.com", "Đang hoạt động"});
                tableModel.addRow(new Object[]{"NV002", "Trần Thị B", "1992-05-15", "0987654321", "b.tran@email.com", "Đang hoạt động"});
                
                JOptionPane.showMessageDialog(viewNhanVien, 
                    "Không tìm thấy nhân viên nào trong hệ thống!\nĐang hiển thị dữ liệu mẫu.", 
                    "Thông báo", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
            // Refresh bảng
            tableModel.fireTableDataChanged();
            viewNhanVien.getTblNhanVien().repaint();
            
        } catch (Exception e) {
            System.out.println("Lỗi khi load dữ liệu: " + e.getMessage());
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(viewNhanVien, 
                "Lỗi khi tải dữ liệu: " + e.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
                
            // Thêm dữ liệu mẫu khi có lỗi
            tableModel.addRow(new Object[]{"NV001", "Nguyễn Văn A", "1990-01-01", "0123456789", "a.nguyen@email.com", "Đang hoạt động"});
            tableModel.addRow(new Object[]{"NV002", "Trần Thị B", "1992-05-15", "0987654321", "b.tran@email.com", "Đang hoạt động"});
        }
    }

    private void themNhanVien() {
        try {
            // Hiển thị form dialog để nhập thông tin nhân viên
            String tenNhanVien = JOptionPane.showInputDialog(viewNhanVien, "Nhập tên nhân viên:");
            if (tenNhanVien == null || tenNhanVien.trim().isEmpty()) return;
            
            String ngaySinh = JOptionPane.showInputDialog(viewNhanVien, "Nhập ngày sinh (yyyy-MM-dd):");
            if (ngaySinh == null || ngaySinh.trim().isEmpty()) return;
            
            String sdt = JOptionPane.showInputDialog(viewNhanVien, "Nhập số điện thoại:");
            if (sdt == null || sdt.trim().isEmpty()) return;
            
            String email = JOptionPane.showInputDialog(viewNhanVien, "Nhập email:");
            if (email == null || email.trim().isEmpty()) return;
            
            String matKhau = JOptionPane.showInputDialog(viewNhanVien, "Nhập mật khẩu:");
            if (matKhau == null || matKhau.trim().isEmpty()) return;
            
            // Chọn vai trò
            String[] vaiTroOptions = {"NV", "QL"}; // Nhân viên, Quản lý
            String maVaiTro = (String) JOptionPane.showInputDialog(viewNhanVien, 
                "Chọn vai trò:", "Vai trò", JOptionPane.QUESTION_MESSAGE, 
                null, vaiTroOptions, vaiTroOptions[0]);
            if (maVaiTro == null) return;
            
            // Kiểm tra email đã tồn tại chưa
            if (!nguoiDungDAO.checkGmail(email)) {
                JOptionPane.showMessageDialog(viewNhanVien, 
                    "Email đã tồn tại trong hệ thống!", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Tạo đối tượng NguoiDung mới
            NguoiDung newNhanVien = new NguoiDung(
                tenNhanVien, ngaySinh, sdt, email, matKhau, 0.0, maVaiTro, 1
            );
            
            int result = nguoiDungDAO.themNguoiDung(newNhanVien);
            
            if (result > 0) {
                JOptionPane.showMessageDialog(viewNhanVien, 
                    "Thêm nhân viên thành công!", 
                    "Thành công", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Reload lại DAO để cập nhật dữ liệu mới
                nguoiDungDAO = new NguoiDungDAO();
                loadDataToTable();
            } else {
                JOptionPane.showMessageDialog(viewNhanVien, 
                    "Thêm nhân viên thất bại!", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(viewNhanVien, 
                "Có lỗi xảy ra: " + ex.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void suaNhanVien() {
        int selectedRow = viewNhanVien.getTblNhanVien().getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(viewNhanVien, 
                "Vui lòng chọn nhân viên cần sửa!", 
                "Thông báo", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String maNhanVien = (String) tableModel.getValueAt(selectedRow, 0);
            NguoiDung nhanVien = nguoiDungDAO.getNguoiDung(maNhanVien);
            
            if (nhanVien != null) {
                // Hiển thị form để sửa thông tin
                String tenMoi = (String) JOptionPane.showInputDialog(viewNhanVien, 
                    "Tên nhân viên:", "Sửa thông tin", JOptionPane.QUESTION_MESSAGE, 
                    null, null, nhanVien.getTenNguoiDung());
                if (tenMoi == null) return;
                
                String ngaySinhMoi = (String) JOptionPane.showInputDialog(viewNhanVien, 
                    "Ngày sinh (yyyy-MM-dd):", "Sửa thông tin", JOptionPane.QUESTION_MESSAGE, 
                    null, null, nhanVien.getNgaySinh());
                if (ngaySinhMoi == null) return;
                
                String sdtMoi = (String) JOptionPane.showInputDialog(viewNhanVien, 
                    "Số điện thoại:", "Sửa thông tin", JOptionPane.QUESTION_MESSAGE, 
                    null, null, nhanVien.getSDT());
                if (sdtMoi == null) return;
                
                String emailMoi = (String) JOptionPane.showInputDialog(viewNhanVien, 
                    "Email:", "Sửa thông tin", JOptionPane.QUESTION_MESSAGE, 
                    null, null, nhanVien.getEmail());
                if (emailMoi == null) return;
                
                String[] trangThaiOptions = {"Đang hoạt động", "Không hoạt động", "Bị khóa"};
                String trangThaiHienTai = chuyenDoiTrangThai(nhanVien.isTrangThai());
                String trangThaiMoi = (String) JOptionPane.showInputDialog(viewNhanVien, 
                    "Trạng thái:", "Sửa thông tin", JOptionPane.QUESTION_MESSAGE, 
                    null, trangThaiOptions, trangThaiHienTai);
                if (trangThaiMoi == null) return;
                
                // Cập nhật thông tin
                nhanVien.setTenNguoiDung(tenMoi);
                nhanVien.setNgaySinh(ngaySinhMoi);
                nhanVien.setSDT(sdtMoi);
                nhanVien.setEmail(emailMoi);
                nhanVien.setTrangThai(chuyenDoiTrangThaiNguocLai(trangThaiMoi));
                
                int result = nguoiDungDAO.capNhatNguoiDung(nhanVien);
                
                if (result > 0) {
                    JOptionPane.showMessageDialog(viewNhanVien, 
                        "Cập nhật nhân viên thành công!", 
                        "Thành công", 
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    // Reload lại DAO để cập nhật dữ liệu
                    nguoiDungDAO = new NguoiDungDAO();
                    loadDataToTable();
                } else {
                    JOptionPane.showMessageDialog(viewNhanVien, 
                        "Cập nhật nhân viên thất bại!", 
                        "Lỗi", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(viewNhanVien, 
                "Có lỗi xảy ra: " + ex.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xoaNhanVien() {
        int selectedRow = viewNhanVien.getTblNhanVien().getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(viewNhanVien, 
                "Vui lòng chọn nhân viên cần xóa!", 
                "Thông báo", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(viewNhanVien,
            "Bạn có chắc chắn muốn xóa nhân viên này?",
            "Xác nhận xóa",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String maNhanVien = (String) tableModel.getValueAt(selectedRow, 0);
                
                int result = nguoiDungDAO.xoaNguoiDung(maNhanVien);
                
                if (result > 0) {
                    JOptionPane.showMessageDialog(viewNhanVien, 
                        "Xóa nhân viên thành công!", 
                        "Thành công", 
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    // Reload lại DAO để cập nhật dữ liệu
                    nguoiDungDAO = new NguoiDungDAO();
                    loadDataToTable();
                } else {
                    JOptionPane.showMessageDialog(viewNhanVien, 
                        "Xóa nhân viên thất bại!", 
                        "Lỗi", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(viewNhanVien, 
                    "Có lỗi xảy ra: " + ex.getMessage(), 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void timKiemNhanVien() {
        String keyword = JOptionPane.showInputDialog(viewNhanVien, 
            "Nhập từ khóa tìm kiếm (tên, email, SDT):", 
            "Tìm kiếm nhân viên", 
            JOptionPane.QUESTION_MESSAGE);
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            timKiemTheoTuKhoa(keyword.trim());
        }
    }
    
    private void timKiemTheoTuKhoa(String keyword) {
        try {
            // Xóa dữ liệu cũ
            tableModel.setRowCount(0);
            
            HashMap<String, NguoiDung> listNguoiDung = nguoiDungDAO.getListNGUOIDUNG();
            HashMap<String, VaiTro> listVaiTro = vaiTroDAO.getListVAITRO();
            
            keyword = keyword.toLowerCase();
            boolean found = false;
            
            for (NguoiDung nd : listNguoiDung.values()) {
                // Chỉ tìm trong nhân viên
                VaiTro vaiTro = listVaiTro.get(nd.getMaVaiTro());
                boolean isNhanVien = false;
                
                if (vaiTro != null) {
                    String tenVaiTro = vaiTro.getTenVaiTro();
                    isNhanVien = tenVaiTro.equals("Nhân viên") || 
                                 tenVaiTro.equals("Quản lý") ||
                                 nd.getMaVaiTro().equals("NV") ||
                                 nd.getMaVaiTro().equals("QL");
                } else {
                    isNhanVien = nd.getMaVaiTro().equals("NV") || nd.getMaVaiTro().equals("QL");
                }
                
                if (isNhanVien) {
                    // Kiểm tra từ khóa có khớp với tên, email hoặc SDT không
                    if (nd.getTenNguoiDung().toLowerCase().contains(keyword) ||
                        nd.getEmail().toLowerCase().contains(keyword) ||
                        nd.getSDT().contains(keyword) ||
                        nd.getMaNguoiDung().toLowerCase().contains(keyword)) {
                        
                        String trangThai = chuyenDoiTrangThai(nd.isTrangThai());
                        
                        Object[] row = {
                            nd.getMaNguoiDung(),
                            nd.getTenNguoiDung(),
                            nd.getNgaySinh(),
                            nd.getSDT(),
                            nd.getEmail(),
                            trangThai
                        };
                        tableModel.addRow(row);
                        found = true;
                    }
                }
            }
            
            if (!found) {
                JOptionPane.showMessageDialog(viewNhanVien, 
                    "Không tìm thấy nhân viên nào khớp với từ khóa!", 
                    "Kết quả tìm kiếm", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadDataToTable(); // Load lại toàn bộ dữ liệu
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(viewNhanVien, 
                "Lỗi khi tìm kiếm: " + e.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private String chuyenDoiTrangThai(int trangThai) {
        switch (trangThai) {
            case 1:
                return "Đang hoạt động";
            case 0:
                return "Không hoạt động";
            case -1:
                return "Bị khóa";
            default:
                return "Không xác định";
        }
    }

    private int chuyenDoiTrangThaiNguocLai(String trangThai) {
        switch (trangThai) {
            case "Đang hoạt động":
                return 1;
            case "Không hoạt động":
                return 0;
            case "Bị khóa":
                return -1;
            default:
                return 0;
        }
    }

    public void refreshData() {
        try {
            nguoiDungDAO = new NguoiDungDAO();
            vaiTroDAO = new VaiTroDAO();
            loadDataToTable();
        } catch (Exception e) {
            System.out.println("Lỗi refresh data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Implement KeyListener methods
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER && e.getSource() == txtTimKiem) {
            timKiemNhanVien();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}