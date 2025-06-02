package CONTROLLER.APP.ADMIN;

import MODEL.DAO.NguoiDungDAO;
import MODEL.ENTITY.NguoiDung;
import VIEW.ADMIN.view_KhachHang;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.awt.Rectangle;

public class ctl_KhachHang {
    private view_KhachHang view;
    private NguoiDungDAO nguoiDungDAO;
    private HashMap<String, NguoiDung> dsNguoiDung;
    private DefaultTableModel tableModel;
    private JTable tblKhachHang;
    private JTextField txtMaKhachHang, txtTenKhachHang, txtEmail, txtSoDienThoai, txtDiaChi;
    private JSpinner spnNgaySinh;
    private JComboBox<String> cmbTrangThai;
    private JButton btnSua;
    private JComboBox<String> cmbLoc;
    
    public ctl_KhachHang(view_KhachHang view) {
        this.view = view;
        this.nguoiDungDAO = new NguoiDungDAO();
        this.dsNguoiDung = nguoiDungDAO.getListNGUOIDUNG();
        
        // Lấy các components từ view
        layUIComponents();
        
        // Đăng ký sự kiện
        dangKySuKien();
        
        // Đổ dữ liệu vào bảng
        loadDataToTable("Tất cả");
    }
    
    private void layUIComponents() {
        // Lấy tham chiếu đến các components trong view
        try {
            // Bảng và model
            tblKhachHang = (JTable) getFieldValue(view, "tblKhachHang");
            tableModel = (DefaultTableModel) getFieldValue(view, "model");
            
            // Các textfield
            txtMaKhachHang = (JTextField) getFieldValue(view, "txtMaKhachHang");
            txtTenKhachHang = (JTextField) getFieldValue(view, "txtTenKhachHang");
            txtEmail = (JTextField) getFieldValue(view, "txtEmail");
            txtSoDienThoai = (JTextField) getFieldValue(view, "txtSoDienThoai");
            txtDiaChi = (JTextField) getFieldValue(view, "txtDiaChi");
            
            // Spinner và combobox
            spnNgaySinh = (JSpinner) getFieldValue(view, "spnNgaySinh");
            cmbTrangThai = (JComboBox<String>) getFieldValue(view, "cmbTrangThai");
            
            // Button
            btnSua = (JButton) getFieldValue(view, "btnSua");
            
            // Combobox lọc - tìm trong phương thức taoFilterPanel
            for (java.lang.reflect.Field field : view.getClass().getDeclaredFields()) {
                if (field.getType() == JComboBox.class) {
                    field.setAccessible(true);
                    Object value = field.get(view);
                    if (value != null && value instanceof JComboBox) {
                        // Tìm combobox chứa các item "Tất cả", "Khóa", "Đang hoạt động"
                        JComboBox<?> cmb = (JComboBox<?>) value;
                        if (cmb.getItemCount() >= 3) {
                            if ("Tất cả".equals(cmb.getItemAt(0)) && 
                                "Khóa".equals(cmb.getItemAt(1)) && 
                                "Đang hoạt động".equals(cmb.getItemAt(2))) {
                                cmbLoc = (JComboBox<String>) cmb;
                                break;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi khi khởi tạo controller: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private Object getFieldValue(Object obj, String fieldName) {
        try {
            java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private void dangKySuKien() {
        // Sự kiện click vào bảng để hiển thị thông tin khách hàng
        tblKhachHang.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tblKhachHang.getSelectedRow();
                if (selectedRow >= 0) {
                    hienThiThongTinKhachHang(selectedRow);
                }
            }
        });
        
        // Sự kiện nút Sửa
        btnSua.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                capNhatThongTinKhachHang();
            }
        });
        
        // Sự kiện lọc theo trạng thái
        if (cmbLoc != null) {
            cmbLoc.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String trangThai = (String) cmbLoc.getSelectedItem();
                    loadDataToTable(trangThai);
                }
            });
        }
    }
    
    private void loadDataToTable(String trangThaiLoc) {
        // Lưu lại mã người dùng đang được chọn (nếu có)
        String selectedUserID = null;
        int selectedRow = tblKhachHang.getSelectedRow();
        if (selectedRow >= 0) {
            selectedUserID = tableModel.getValueAt(selectedRow, 0).toString();
            System.out.println("Đang chọn người dùng: " + selectedUserID);
        }
        
        // Xóa dữ liệu cũ
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
        
        // Sắp xếp danh sách người dùng theo mã người dùng (tăng dần)
        java.util.List<NguoiDung> danhSachNguoiDung = new java.util.ArrayList<>(dsNguoiDung.values());
        java.util.Collections.sort(danhSachNguoiDung, new java.util.Comparator<NguoiDung>() {
            @Override
            public int compare(NguoiDung nd1, NguoiDung nd2) {
                return nd1.getMaNguoiDung().compareTo(nd2.getMaNguoiDung());
            }
        });
        
        // Đổ dữ liệu mới vào bảng
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        int rowToSelect = -1; // Hàng cần chọn sau khi cập nhật
        int rowCounter = 0;   // Biến đếm hàng
        
        for (NguoiDung nd : danhSachNguoiDung) {
            // Chỉ hiển thị người dùng có mã vai trò là VT003 (khách hàng)
            if (!"VT003".equals(nd.getMaVaiTro())) {
                continue;
            }
            
            String trangThaiND = nd.isTrangThai() == 1 ? "Đang hoạt động" : "Khóa";
            
            // Lọc theo trạng thái nếu không chọn "Tất cả"
            if (!trangThaiLoc.equals("Tất cả") && !trangThaiLoc.equals(trangThaiND)) {
                continue; // Bỏ qua nếu không khớp với trạng thái lọc
            }
            
            // Thêm dữ liệu vào bảng (đã loại bỏ cột địa chỉ)
            Object[] row = {
                nd.getMaNguoiDung(),
                nd.getTenNguoiDung(),
                nd.getEmail(),
                nd.getSDT(),
                nd.getNgaySinh(),
                trangThaiND
            };
            tableModel.addRow(row);
            
            // Nếu đây là người dùng đã được chọn trước đó, lưu lại vị trí
            if (selectedUserID != null && selectedUserID.equals(nd.getMaNguoiDung())) {
                rowToSelect = rowCounter;
            }
            
            rowCounter++;
        }
        
        // Chọn lại hàng đã chọn trước đó (nếu có)
        if (rowToSelect >= 0) {
            tblKhachHang.setRowSelectionInterval(rowToSelect, rowToSelect);
            // Cuộn đến hàng được chọn
            tblKhachHang.scrollRectToVisible(new Rectangle(tblKhachHang.getCellRect(rowToSelect, 0, true)));
        }
    }
    
    private void hienThiThongTinKhachHang(int selectedRow) {
        try {
            // Lấy giá trị từ bảng
            String maND = tableModel.getValueAt(selectedRow, 0).toString();
            String tenND = tableModel.getValueAt(selectedRow, 1).toString();
            String email = tableModel.getValueAt(selectedRow, 2).toString();
            String sdt = tableModel.getValueAt(selectedRow, 3).toString();
            String ngaySinh = tableModel.getValueAt(selectedRow, 4).toString();
            String trangThai = tableModel.getValueAt(selectedRow, 5).toString();
            
            // Hiển thị thông tin lên form
            txtMaKhachHang.setText(maND);
            txtTenKhachHang.setText(tenND);
            txtEmail.setText(email);
            txtSoDienThoai.setText(sdt);
            cmbTrangThai.setSelectedItem(trangThai);
            
            // Xử lý ngày sinh
            try {
                // Xử lý nhiều định dạng ngày có thể có
                Date date = null;
                
                // Thử với định dạng "yyyy-MM-dd HH:mm:ss.S"
                try {
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                    date = sdf1.parse(ngaySinh);
                } catch (Exception e1) {
                    // Thử với định dạng "yyyy-MM-dd"
                    try {
                        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                        date = sdf2.parse(ngaySinh);
                    } catch (Exception e2) {
                        // Thử với định dạng "dd/MM/yyyy"
                        try {
                            SimpleDateFormat sdf3 = new SimpleDateFormat("dd/MM/yyyy");
                            date = sdf3.parse(ngaySinh);
                        } catch (Exception e3) {
                            e3.printStackTrace();
                        }
                    }
                }
                
                if (date != null) {
                    spnNgaySinh.setValue(date);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi khi hiển thị thông tin: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void capNhatThongTinKhachHang() {
        int selectedRow = tblKhachHang.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn khách hàng cần cập nhật!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Lấy thông tin từ form
            String maND = txtMaKhachHang.getText();
            String tenND = txtTenKhachHang.getText();
            String email = txtEmail.getText();
            String sdt = txtSoDienThoai.getText();
            
            // Lấy ngày sinh từ spinner
            Date ngaySinhDate = (Date) spnNgaySinh.getValue();
            
            // Kiểm tra dữ liệu nhập vào
            if (tenND.isEmpty() || email.isEmpty() || sdt.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Vui lòng nhập đầy đủ thông tin khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            System.out.println("====== THÔNG TIN CẬP NHẬT ======");
            System.out.println("Mã khách hàng: " + maND);
            System.out.println("Tên khách hàng: " + tenND);
            System.out.println("Email: " + email);
            System.out.println("Số điện thoại: " + sdt);
            System.out.println("Ngày sinh (Date): " + ngaySinhDate);
            
            // Lấy thông tin người dùng hiện tại và cập nhật
            NguoiDung nguoiDung = dsNguoiDung.get(maND);
            if (nguoiDung != null) {
                System.out.println("Thông tin khách hàng trước khi cập nhật: " + nguoiDung);
                
                // Định dạng ngày theo định dạng của database
                // Thử các định dạng khác nhau cho ngày sinh
                String ngaySinh = "";
                try {
                    // Sử dụng định dạng yyyy-MM-dd cho database
                    SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");
                    ngaySinh = dbFormat.format(ngaySinhDate);
                    System.out.println("Ngày sinh (định dạng DB): " + ngaySinh);
                } catch (Exception e) {
                    System.err.println("Lỗi định dạng ngày sinh: " + e.getMessage());
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(view, "Lỗi định dạng ngày sinh!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Lấy trạng thái từ combobox
                String trangThaiStr = (String) cmbTrangThai.getSelectedItem();
                int trangThai = trangThaiStr.equals("Đang hoạt động") ? 1 : 0;
                System.out.println("Trạng thái: " + trangThaiStr + " (" + trangThai + ")");
                
                // Cập nhật thông tin người dùng
                nguoiDung.setTenNguoiDung(tenND);
                nguoiDung.setEmail(email);
                nguoiDung.setSDT(sdt);
                nguoiDung.setNgaySinh(ngaySinh);
                nguoiDung.setTrangThai(trangThai);
                
                System.out.println("Thông tin khách hàng sau khi cập nhật: " + nguoiDung);
                
                try {
                    // Cập nhật vào cơ sở dữ liệu
                    int result = nguoiDungDAO.capNhatNguoiDung(nguoiDung);
                    System.out.println("Kết quả cập nhật (từ DAO): " + result);
                    
                    if (result > 0) {
                        JOptionPane.showMessageDialog(view, "Cập nhật thông tin khách hàng thành công!");
                        
                        // Cập nhật lại bảng
                        String trangThaiLoc = (String) cmbLoc.getSelectedItem();
                        loadDataToTable(trangThaiLoc);
                    } else {
                        System.err.println("Cập nhật thất bại, database trả về: " + result);
                        JOptionPane.showMessageDialog(view, "Cập nhật thông tin khách hàng thất bại! (Lỗi database)", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    System.err.println("Lỗi khi gọi DAO.capNhatNguoiDung: " + e.getMessage());
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(view, "Lỗi khi cập nhật: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                System.err.println("Không tìm thấy người dùng với mã: " + maND);
                JOptionPane.showMessageDialog(view, "Không tìm thấy thông tin khách hàng với mã: " + maND, "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            System.err.println("Lỗi trong quá trình cập nhật: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}