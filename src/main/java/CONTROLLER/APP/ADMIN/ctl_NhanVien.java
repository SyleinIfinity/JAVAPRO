package CONTROLLER.APP.ADMIN;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import VIEW.ADMIN.view_NhanVien;
import MODEL.DAO.NguoiDungDAO;
import MODEL.DAO.VaiTroDAO;
import MODEL.ENTITY.NguoiDung;
import MODEL.ENTITY.VaiTro;

public class ctl_NhanVien {
    private view_NhanVien viewNhanVien;
    private NguoiDungDAO nguoiDungDAO;
    private VaiTroDAO vaiTroDAO;
    
    // Form dialog để thêm/sửa nhân viên
    private JDialog formDialog;
    private JTextField txtMaNV, txtTenNV, txtNgaySinh, txtSDT, txtEmail, txtMatKhau;
    private JComboBox<String> cmbTrangThai, cmbVaiTro;
    private JButton btnLuu, btnHuy;
    private boolean isEditMode = false;
    private String currentMaNV = "";

    public ctl_NhanVien(view_NhanVien viewNhanVien) {
        this.viewNhanVien = viewNhanVien;
        this.nguoiDungDAO = viewNhanVien.getNguoiDungDAO();
        this.vaiTroDAO = viewNhanVien.getVaiTroDAO();
        
        initController();
    }

    private void initController() {
        // Gắn sự kiện cho các nút
        viewNhanVien.getBtnThem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFormDialog(false, null);
            }
        });

        viewNhanVien.getBtnSua().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                suaNhanVien();
            }
        });

        viewNhanVien.getBtnXoa().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                xoaNhanVien();
            }
        });

        viewNhanVien.getBtnTimKiem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showTimKiemDialog();
            }
        });

        // Gắn sự kiện double click cho table
        viewNhanVien.getTblNhanVien().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    NguoiDung selectedNV = viewNhanVien.getSelectedNguoiDung();
                    if (selectedNV != null) {
                        showFormDialog(true, selectedNV);
                    }
                }
            }
        });
    }

    // Hiển thị form thêm/sửa nhân viên
    private void showFormDialog(boolean editMode, NguoiDung nguoiDung) {
        isEditMode = editMode;
        
        formDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(viewNhanVien), 
                                editMode ? "Sửa nhân viên" : "Thêm nhân viên", true);
        formDialog.setSize(400, 500);
        formDialog.setLocationRelativeTo(viewNhanVien);
        formDialog.setLayout(null);

        // Tạo các components
        JLabel lblMaNV = new JLabel("Mã nhân viên:");
        lblMaNV.setBounds(20, 20, 120, 25);
        txtMaNV = new JTextField();
        txtMaNV.setBounds(150, 20, 200, 25);
        txtMaNV.setEnabled(false); // Mã NV tự động tạo

        JLabel lblTenNV = new JLabel("Tên nhân viên:");
        lblTenNV.setBounds(20, 60, 120, 25);
        txtTenNV = new JTextField();
        txtTenNV.setBounds(150, 60, 200, 25);

        JLabel lblNgaySinh = new JLabel("Ngày sinh:");
        lblNgaySinh.setBounds(20, 100, 120, 25);
        txtNgaySinh = new JTextField();
        txtNgaySinh.setBounds(150, 100, 200, 25);
        txtNgaySinh.setToolTipText("Định dạng: dd/MM/yyyy");

        JLabel lblSDT = new JLabel("Số điện thoại:");
        lblSDT.setBounds(20, 140, 120, 25);
        txtSDT = new JTextField();
        txtSDT.setBounds(150, 140, 200, 25);

        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setBounds(20, 180, 120, 25);
        txtEmail = new JTextField();
        txtEmail.setBounds(150, 180, 200, 25);

        JLabel lblMatKhau = new JLabel("Mật khẩu:");
        lblMatKhau.setBounds(20, 220, 120, 25);
        txtMatKhau = new JPasswordField();
        txtMatKhau.setBounds(150, 220, 200, 25);

        JLabel lblVaiTro = new JLabel("Vai trò:");
        lblVaiTro.setBounds(20, 260, 120, 25);
        cmbVaiTro = new JComboBox<>();
        cmbVaiTro.setBounds(150, 260, 200, 25);
        loadVaiTroComboBox();

        JLabel lblTrangThai = new JLabel("Trạng thái:");
        lblTrangThai.setBounds(20, 300, 120, 25);
        String[] trangThaiOptions = {"Đang hoạt động", "Không hoạt động", "Bị khóa"};
        cmbTrangThai = new JComboBox<>(trangThaiOptions);
        cmbTrangThai.setBounds(150, 300, 200, 25);

        btnLuu = new JButton("Lưu");
        btnLuu.setBounds(100, 380, 80, 30);
        btnHuy = new JButton("Hủy");
        btnHuy.setBounds(200, 380, 80, 30);

        // Thêm components vào dialog
        formDialog.add(lblMaNV);
        formDialog.add(txtMaNV);
        formDialog.add(lblTenNV);
        formDialog.add(txtTenNV);
        formDialog.add(lblNgaySinh);
        formDialog.add(txtNgaySinh);
        formDialog.add(lblSDT);
        formDialog.add(txtSDT);
        formDialog.add(lblEmail);
        formDialog.add(txtEmail);
        formDialog.add(lblMatKhau);
        formDialog.add(txtMatKhau);
        formDialog.add(lblVaiTro);
        formDialog.add(cmbVaiTro);
        formDialog.add(lblTrangThai);
        formDialog.add(cmbTrangThai);
        formDialog.add(btnLuu);
        formDialog.add(btnHuy);

        // Nếu là chế độ sửa, điền dữ liệu vào form
        if (editMode && nguoiDung != null) {
            fillFormData(nguoiDung);
        }

        // Gắn sự kiện cho các nút
        btnLuu.addActionListener(e -> luuNhanVien());
        btnHuy.addActionListener(e -> formDialog.dispose());

        formDialog.setVisible(true);
    }

    // Điền dữ liệu vào form khi sửa
    private void fillFormData(NguoiDung nguoiDung) {
        currentMaNV = nguoiDung.getMaNguoiDung();
        txtMaNV.setText(nguoiDung.getMaNguoiDung());
        txtTenNV.setText(nguoiDung.getTenNguoiDung());
        txtNgaySinh.setText(nguoiDung.getNgaySinh());
        txtSDT.setText(nguoiDung.getSDT());
        txtEmail.setText(nguoiDung.getEmail());
        txtMatKhau.setText(nguoiDung.getMatKhau());
        
        // Set vai trò
        for (int i = 0; i < cmbVaiTro.getItemCount(); i++) {
            String item = cmbVaiTro.getItemAt(i);
            if (item.startsWith(nguoiDung.getMaVaiTro())) {
                cmbVaiTro.setSelectedIndex(i);
                break;
            }
        }
        
        // Set trạng thái
        switch (nguoiDung.isTrangThai()) {
            case 1:
                cmbTrangThai.setSelectedIndex(0);
                break;
            case 0:
                cmbTrangThai.setSelectedIndex(1);
                break;
            case -1:
                cmbTrangThai.setSelectedIndex(2);
                break;
        }
    }

    // Load vai trò vào ComboBox
    private void loadVaiTroComboBox() {
        cmbVaiTro.removeAllItems();
        for (VaiTro vt : vaiTroDAO.getListVAITRO().values()) {
            cmbVaiTro.addItem(vt.getMaVaiTro() + " - " + vt.getTenVaiTro());
        }
    }

// Sửa lại phương thức luuNhanVien() trong ctl_NhanVien.java

private void luuNhanVien() {
    if (!validateForm()) {
        return;
    }

    try {
        // Chuyển đổi trạng thái
        int trangThai;
        switch (cmbTrangThai.getSelectedIndex()) {
            case 0: trangThai = 1; break;  // Đang hoạt động
            case 1: trangThai = 0; break;  // Không hoạt động
            case 2: trangThai = -1; break; // Bị khóa
            default: trangThai = 1; break;
        }

        // Lấy mã vai trò từ ComboBox
        String selectedVaiTro = (String) cmbVaiTro.getSelectedItem();
        String maVaiTro = selectedVaiTro.split(" - ")[0];

        if (isEditMode) {
            // Cập nhật nhân viên
            NguoiDung nguoiDung = new NguoiDung(
                currentMaNV,
                txtTenNV.getText().trim(),
                txtNgaySinh.getText().trim(),
                txtSDT.getText().trim(),
                txtEmail.getText().trim(),
                txtMatKhau.getText().trim(),
                0.0, // Số dư tài khoản mặc định
                maVaiTro,
                trangThai
            );

            int result = nguoiDungDAO.capNhatNguoiDung(nguoiDung);
            if (result > 0) {
                // Cập nhật lại HashMap sau khi update thành công
                nguoiDungDAO.reloadData();
                
                JOptionPane.showMessageDialog(formDialog, "Cập nhật nhân viên thành công!");
                formDialog.dispose();
                viewNhanVien.refreshData();
            } else {
                JOptionPane.showMessageDialog(formDialog, "Lỗi khi cập nhật nhân viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            // Thêm nhân viên mới
            
            // Kiểm tra email đã tồn tại chưa
            if (!nguoiDungDAO.checkGmail(txtEmail.getText().trim())) {
                JOptionPane.showMessageDialog(formDialog, "Email đã tồn tại trong hệ thống!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Tạo đối tượng NguoiDung mới - SỬA ĐỔI: Không truyền mã nhân viên vì sẽ tự động tạo
            NguoiDung nguoiDung = new NguoiDung(
                txtTenNV.getText().trim(),
                txtNgaySinh.getText().trim(),
                txtSDT.getText().trim(),
                txtEmail.getText().trim(),
                txtMatKhau.getText().trim(),
                0.0, // Số dư tài khoản mặc định
                maVaiTro,
                trangThai
            );

            // Thêm nhân viên vào database
            int result = nguoiDungDAO.themNguoiDung(nguoiDung);
            if (result > 0) {
                // Reload lại dữ liệu trong HashMap sau khi thêm thành công
                nguoiDungDAO.reloadData();
                
                JOptionPane.showMessageDialog(formDialog, "Thêm nhân viên thành công!");
                formDialog.dispose();
                viewNhanVien.refreshData(); // Refresh UI
            } else {
                JOptionPane.showMessageDialog(formDialog, "Lỗi khi thêm nhân viên! Vui lòng kiểm tra lại thông tin.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(formDialog, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
    }
}



// Thêm phương thức kiểm tra định dạng ngày
private boolean isValidDate(String dateStr) {
    try {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        sdf.parse(dateStr);
        return true;
    } catch (Exception e) {
        return false;
    }
}
    // Kiểm tra tính hợp lệ của form
    private boolean validateForm() {
        if (txtTenNV.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(formDialog, "Vui lòng nhập tên nhân viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtTenNV.requestFocus();
            return false;
        }

        if (txtNgaySinh.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(formDialog, "Vui lòng nhập ngày sinh!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtNgaySinh.requestFocus();
            return false;
        }

        if (txtSDT.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(formDialog, "Vui lòng nhập số điện thoại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtSDT.requestFocus();
            return false;
        }

        // Kiểm tra định dạng số điện thoại
        String sdt = txtSDT.getText().trim();
        if (!sdt.matches("\\d{10,11}")) {
            JOptionPane.showMessageDialog(formDialog, "Số điện thoại phải có 10-11 chữ số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtSDT.requestFocus();
            return false;
        }

        if (txtEmail.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(formDialog, "Vui lòng nhập email!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtEmail.requestFocus();
            return false;
        }

        // Kiểm tra định dạng email
        String email = txtEmail.getText().trim();
        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            JOptionPane.showMessageDialog(formDialog, "Email không đúng định dạng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtEmail.requestFocus();
            return false;
        }

        if (txtMatKhau.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(formDialog, "Vui lòng nhập mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtMatKhau.requestFocus();
            return false;
        }

        if (txtMatKhau.getText().trim().length() < 6) {
            JOptionPane.showMessageDialog(formDialog, "Mật khẩu phải có ít nhất 6 ký tự!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtMatKhau.requestFocus();
            return false;
        }

        return true;
    }

    // Sửa nhân viên
    private void suaNhanVien() {
        NguoiDung selectedNV = viewNhanVien.getSelectedNguoiDung();
        if (selectedNV == null) {
            JOptionPane.showMessageDialog(viewNhanVien, "Vui lòng chọn nhân viên cần sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        showFormDialog(true, selectedNV);
    }

    // Xóa nhân viên
    private void xoaNhanVien() {
        NguoiDung selectedNV = viewNhanVien.getSelectedNguoiDung();
        if (selectedNV == null) {
            JOptionPane.showMessageDialog(viewNhanVien, "Vui lòng chọn nhân viên cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(viewNhanVien, 
            "Bạn có chắc chắn muốn xóa nhân viên: " + selectedNV.getTenNguoiDung() + "?", 
            "Xác nhận xóa", 
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int result = nguoiDungDAO.xoaNguoiDung(selectedNV.getMaNguoiDung());
                if (result > 0) {
                    JOptionPane.showMessageDialog(viewNhanVien, "Xóa nhân viên thành công!");
                    viewNhanVien.refreshData();
                } else {
                    JOptionPane.showMessageDialog(viewNhanVien, "Lỗi khi xóa nhân viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(viewNhanVien, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Hiển thị dialog tìm kiếm
    private void showTimKiemDialog() {
        JDialog timKiemDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(viewNhanVien), "Tìm kiếm nhân viên", true);
        timKiemDialog.setSize(350, 200);
        timKiemDialog.setLocationRelativeTo(viewNhanVien);
        timKiemDialog.setLayout(null);

        JLabel lblTimKiem = new JLabel("Nhập thông tin tìm kiếm:");
        lblTimKiem.setBounds(20, 20, 200, 25);

        JTextField txtTimKiem = new JTextField();
        txtTimKiem.setBounds(20, 50, 250, 25);
        txtTimKiem.setToolTipText("Có thể tìm theo tên, email, số điện thoại");

        JButton btnTimKiem = new JButton("Tìm kiếm");
        btnTimKiem.setBounds(50, 100, 100, 30);

        JButton btnHienThiTatCa = new JButton("Hiển thị tất cả");
        btnHienThiTatCa.setBounds(170, 100, 120, 30);

        timKiemDialog.add(lblTimKiem);
        timKiemDialog.add(txtTimKiem);
        timKiemDialog.add(btnTimKiem);
        timKiemDialog.add(btnHienThiTatCa);

        btnTimKiem.addActionListener(e -> {
            String keyword = txtTimKiem.getText().trim();
            if (!keyword.isEmpty()) {
                timKiemNhanVien(keyword);
                timKiemDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(timKiemDialog, "Vui lòng nhập từ khóa tìm kiếm!");
            }
        });

        btnHienThiTatCa.addActionListener(e -> {
            viewNhanVien.loadDataFromDatabase();
            timKiemDialog.dispose();
        });

        timKiemDialog.setVisible(true);
    }

    // Tìm kiếm nhân viên
    private void timKiemNhanVien(String keyword) {
        DefaultTableModel model = viewNhanVien.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ

        keyword = keyword.toLowerCase();
        int count = 0;

        for (NguoiDung nd : nguoiDungDAO.getListNGUOIDUNG().values()) {
            boolean match = false;
            
            // Tìm kiếm theo các trường
            if (nd.getTenNguoiDung().toLowerCase().contains(keyword) ||
                nd.getEmail().toLowerCase().contains(keyword) ||
                nd.getSDT().contains(keyword) ||
                nd.getMaNguoiDung().toLowerCase().contains(keyword)) {
                match = true;
            }

            if (match) {
                // Chuyển đổi trạng thái
                String trangThai;
                switch (nd.isTrangThai()) {
                    case 1: trangThai = "Đang hoạt động"; break;
                    case 0: trangThai = "Không hoạt động"; break;
                    case -1: trangThai = "Bị khóa"; break;
                    default: trangThai = "Không xác định"; break;
                }

                Object[] row = {
                    nd.getMaNguoiDung(),
                    nd.getTenNguoiDung(),
                    nd.getNgaySinh(),
                    nd.getSDT(),
                    nd.getEmail(),
                    trangThai
                };
                model.addRow(row);
                count++;
            }
        }

        JOptionPane.showMessageDialog(viewNhanVien, 
            "Tìm thấy " + count + " nhân viên phù hợp với từ khóa: " + keyword, 
            "Kết quả tìm kiếm", 
            JOptionPane.INFORMATION_MESSAGE);
    }
}