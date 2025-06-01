package CONTROLLER.APP.ADMIN;

import MODEL.DAO.DichVuDAO;
import MODEL.DAO.LoaiDichVuDAO;
import MODEL.ENTITY.DichVu;
import MODEL.ENTITY.LoaiDichVu;
import VIEW.ADMIN.view_DichVu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;

public class ctl_DichVu {
    private view_DichVu view;
    private DichVuDAO dichVuDAO;
    private LoaiDichVuDAO loaiDichVuDAO;
    private DefaultTableModel tableModel;
    private DecimalFormat currencyFormat;

    public ctl_DichVu(view_DichVu view) {
        this.view = view;
        this.dichVuDAO = new DichVuDAO();
        this.loaiDichVuDAO = new LoaiDichVuDAO();
        this.currencyFormat = new DecimalFormat("#,###,### VNĐ");
        
        khoiTaoController();
        taiDuLieuLenBang();
    }

    private void khoiTaoController() {
        // Lấy table model từ view
        this.tableModel = view.getTableModel();

        // Thêm event listeners cho các buttons
        view.btnThem.addActionListener(new ThemDichVuListener());
        view.btnSua.addActionListener(new SuaDichVuListener());
        view.btnXoa.addActionListener(new XoaDichVuListener());
        view.btnTimKiem.addActionListener(new TimKiemDichVuListener());

        // Thêm mouse listener cho table
        view.tblDichVu.addMouseListener(new TableMouseListener());
        
        // Thêm selection listener để cập nhật trạng thái buttons
        view.tblDichVu.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                capNhatTrangThaiUI();
            }
        });
    }

    private void taiDuLieuLenBang() {
        // Xóa dữ liệu cũ
        tableModel.setRowCount(0);

        try {
            // Lấy dữ liệu từ DAO
            HashMap<String, DichVu> listDichVu = dichVuDAO.getListDICHVU();

            // Thêm dữ liệu vào bảng
            for (DichVu dv : listDichVu.values()) {
                Object[] row = {
                    dv.getMaDichVu(),
                    dv.getTenDichVu(),
                    formatCurrency(dv.getGiaDichVu()),
                    dv.getMoTa()
                };
                tableModel.addRow(row);
            }
            
            // Cập nhật trạng thái UI
            capNhatTrangThaiUI();
            
            System.out.println("Đã tải " + listDichVu.size() + " dịch vụ lên bảng");
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, 
                "Lỗi khi tải dữ liệu: " + e.getMessage(), 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refreshData() {
        try {
            // Tạo lại DAO để lấy dữ liệu mới từ database
            dichVuDAO = new DichVuDAO();
            loaiDichVuDAO = new LoaiDichVuDAO();
            taiDuLieuLenBang();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, 
                "Lỗi khi làm mới dữ liệu: " + e.getMessage(), 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void capNhatTrangThaiUI() {
        // Enable/disable buttons dựa trên selection
        boolean hasSelection = view.tblDichVu.getSelectedRow() != -1;
        view.btnSua.setEnabled(hasSelection);
        view.btnXoa.setEnabled(hasSelection);
        
        // Cập nhật số lượng record nếu có label tương ứng
        int totalRecords = tableModel.getRowCount();
        System.out.println("Hiện có " + totalRecords + " dịch vụ trong bảng");
    }

    private String formatCurrency(String price) {
        try {
            if (price == null || price.trim().isEmpty()) {
                return "0 VNĐ";
            }
            double amount = Double.parseDouble(price.trim());
            return currencyFormat.format(amount);
        } catch (NumberFormatException e) {
            return price + " VNĐ"; // Trả về giá trị gốc nếu không parse được
        }
    }

    // Listener cho nút Thêm
    private class ThemDichVuListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JDialog dialog = taoDialogNhapLieu("Thêm Dịch Vụ Mới", null);
            dialog.setVisible(true);
        }
    }

    // Listener cho nút Sửa
    private class SuaDichVuListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = view.tblDichVu.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(view, 
                    "Vui lòng chọn dịch vụ cần sửa!", 
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String maDichVu = (String) tableModel.getValueAt(selectedRow, 0);
            DichVu dichVu = dichVuDAO.getDichVu(maDichVu);
            
            if (dichVu != null) {
                JDialog dialog = taoDialogNhapLieu("Sửa Thông Tin Dịch Vụ", dichVu);
                dialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(view, 
                    "Không tìm thấy thông tin dịch vụ!", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Listener cho nút Xóa
    private class XoaDichVuListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = view.tblDichVu.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(view, 
                    "Vui lòng chọn dịch vụ cần xóa!", 
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String maDichVu = (String) tableModel.getValueAt(selectedRow, 0);
            String tenDichVu = (String) tableModel.getValueAt(selectedRow, 1);

            int confirm = JOptionPane.showConfirmDialog(view, 
                "Bạn có chắc chắn muốn xóa dịch vụ?\n\n" +
                "Mã: " + maDichVu + "\n" +
                "Tên: " + tenDichVu + "\n\n" +
                "Hành động này không thể hoàn tác!", 
                "Xác nhận xóa", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    int result = dichVuDAO.xoaDichVu(maDichVu);
                    if (result > 0) {
                        JOptionPane.showMessageDialog(view, 
                            "Xóa dịch vụ thành công!", 
                            "Thành công", JOptionPane.INFORMATION_MESSAGE);
                        refreshData();
                    } else {
                        JOptionPane.showMessageDialog(view, 
                            "Xóa dịch vụ thất bại!\nCó thể dịch vụ đang được sử dụng.", 
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(view, 
                        "Lỗi khi xóa dịch vụ: " + ex.getMessage(), 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    // Listener cho nút Tìm kiếm
    private class TimKiemDichVuListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String keyword = JOptionPane.showInputDialog(view, 
                "Nhập từ khóa tìm kiếm:\n(Có thể tìm theo mã, tên dịch vụ hoặc mô tả)", 
                "Tìm kiếm dịch vụ", JOptionPane.QUESTION_MESSAGE);
            
            if (keyword != null) {
                if (!keyword.trim().isEmpty()) {
                    timKiemDichVu(keyword.trim());
                } else {
                    // Nếu nhập rỗng, hiển thị lại toàn bộ dữ liệu
                    refreshData();
                }
            }
        }
    }

    // Listener cho mouse click trên table
    private class TableMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) { // Double click
                int selectedRow = view.tblDichVu.getSelectedRow();
                if (selectedRow != -1) {
                    String maDichVu = (String) tableModel.getValueAt(selectedRow, 0);
                    DichVu dichVu = dichVuDAO.getDichVu(maDichVu);
                    if (dichVu != null) {
                        hienThiThongTinChiTiet(dichVu);
                    }
                }
            }
        }
    }

    private JDialog taoDialogNhapLieu(String title, DichVu dichVu) {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(view), title, true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(view);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        // Main panel với padding
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Tạo các trường nhập liệu
        JLabel lblMaDV = new JLabel("Mã dịch vụ:");
        lblMaDV.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JTextField txtMaDichVu = new JTextField(20);
        txtMaDichVu.setEnabled(dichVu == null); // Chỉ cho phép nhập khi thêm mới
        txtMaDichVu.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel lblTenDV = new JLabel("Tên dịch vụ:");
        lblTenDV.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JTextField txtTenDichVu = new JTextField(20);
        txtTenDichVu.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel lblGiaDV = new JLabel("Giá (VNĐ):");
        lblGiaDV.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JTextField txtGiaDichVu = new JTextField(20);
        txtGiaDichVu.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel lblMoTa = new JLabel("Mô tả:");
        lblMoTa.setFont(new Font("Segoe UI", Font.BOLD, 14));
        JTextArea txtMoTa = new JTextArea(4, 20);
        txtMoTa.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtMoTa.setLineWrap(true);
        txtMoTa.setWrapStyleWord(true);
        txtMoTa.setBorder(BorderFactory.createLoweredBevelBorder());
        JScrollPane scrollMoTa = new JScrollPane(txtMoTa);
        scrollMoTa.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
       
        // Nếu là sửa, điền thông tin hiện tại
        if (dichVu != null) {
            txtMaDichVu.setText(dichVu.getMaDichVu());
            txtTenDichVu.setText(dichVu.getTenDichVu());
            txtGiaDichVu.setText(dichVu.getGiaDichVu());
            txtMoTa.setText(dichVu.getMoTa() != null ? dichVu.getMoTa() : "");
        }

        // Validation cho số
        txtGiaDichVu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isDigit(c) && c != '.' && c != java.awt.event.KeyEvent.VK_BACK_SPACE) {
                    evt.consume();
                }
            }
        });

        // Layout components
        // gbc.gridx = 0; gbc.gridy = 0;
        // gbc.fill = GridBagConstraints.NONE;
        // mainPanel.add(lblMaDV, gbc);
        // gbc.gridx = 1;
        // gbc.fill = GridBagConstraints.HORIZONTAL;
        // gbc.weightx = 1.0;
        // mainPanel.add(txtMaDichVu, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        mainPanel.add(lblTenDV, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        mainPanel.add(txtTenDichVu, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        mainPanel.add(lblGiaDV, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        mainPanel.add(txtGiaDichVu, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        mainPanel.add(lblMoTa, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        mainPanel.add(scrollMoTa, gbc);

        // Panel buttons
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        JButton btnLuu = new JButton("Lưu");
        JButton btnHuy = new JButton("Hủy");
        
        btnLuu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnHuy.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        btnLuu.setPreferredSize(new Dimension(100, 35));
        btnHuy.setPreferredSize(new Dimension(100, 35));
        
        btnLuu.setBackground(new Color(46, 125, 50));
        btnLuu.setForeground(Color.WHITE);
        btnHuy.setBackground(new Color(211, 47, 47));
        btnHuy.setForeground(Color.WHITE);

        btnLuu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (xuLyLuuDichVu(dialog, txtMaDichVu, txtTenDichVu, txtGiaDichVu, txtMoTa, dichVu)) {
                    dialog.dispose();
                }
            }
        });

        btnHuy.addActionListener(e -> dialog.dispose());

        panelButtons.add(btnLuu);
        panelButtons.add(btnHuy);

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 0;
        gbc.weighty = 0;
        mainPanel.add(panelButtons, gbc);

        dialog.add(mainPanel);
        return dialog;
    }

    private boolean xuLyLuuDichVu(JDialog dialog, JTextField txtMaDichVu, JTextField txtTenDichVu, 
                                  JTextField txtGiaDichVu, JTextArea txtMoTa, DichVu dichVu) {
        String maDV = txtMaDichVu.getText().trim().toUpperCase();
        String tenDV = txtTenDichVu.getText().trim();
        String gia = txtGiaDichVu.getText().trim();
        String moTa = txtMoTa.getText().trim();

        // Validation
        if ( tenDV.isEmpty() || gia.isEmpty()) {
            JOptionPane.showMessageDialog(dialog, 
                "Vui lòng nhập đầy đủ thông tin bắt buộc!\n\n" +
                "• Tên dịch vụ\n• Giá dịch vụ", 
                "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Validate giá
        try {
            double giaNum = Double.parseDouble(gia);
            if (giaNum <= 0) {
                JOptionPane.showMessageDialog(dialog, 
                    "Giá dịch vụ phải lớn hơn 0!", 
                    "Giá không hợp lệ", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(dialog, 
                "Giá dịch vụ phải là số hợp lệ!", 
                "Giá không hợp lệ", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Validate mã dịch vụ khi thêm mới
        if (dichVu == null && dichVuDAO.getDichVu(maDV) != null) {
            JOptionPane.showMessageDialog(dialog, 
                "Tên dịch vụ \"" + tenDV + "\" đã tồn tại!\nVui lòng chọn tên khác.", 
                "Tên trùng lặp", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        DichVu dvMoi = new DichVu(maDV, tenDV, gia, moTa);
        int result;

        try {
            if (dichVu == null) { // Thêm mới
                result = dichVuDAO.themDichVu(dvMoi);
            } else { // Cập nhật
                result = dichVuDAO.capNhatDichVu(dvMoi);
            }

            if (result > 0) {
                JOptionPane.showMessageDialog(dialog, 
                    (dichVu == null ? "Thêm" : "Cập nhật") + " dịch vụ thành công!\n\n" +
                    "Tên: " + tenDV + "\nGiá: " + gia +"\nMô tả:" +moTa, 
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
                refreshData();
                return true;
            } else {
                JOptionPane.showMessageDialog(dialog, 
                    (dichVu == null ? "Thêm" : "Cập nhật") + " dịch vụ thất bại!", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(dialog, 
                "Lỗi: " + ex.getMessage(), 
                "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private void timKiemDichVu(String keyword) {
        tableModel.setRowCount(0);
        int count = 0;
        
        try {
            HashMap<String, DichVu> listDichVu = dichVuDAO.getListDICHVU();
            String keywordLower = keyword.toLowerCase();

            for (DichVu dv : listDichVu.values()) {
                // Tìm kiếm theo mã, tên dịch vụ hoặc mô tả
                if (dv.getMaDichVu().toLowerCase().contains(keywordLower) ||
                    dv.getTenDichVu().toLowerCase().contains(keywordLower) ||
                    (dv.getMoTa() != null && dv.getMoTa().toLowerCase().contains(keywordLower))) {
                    
                    Object[] row = {
                        dv.getMaDichVu(),
                        dv.getTenDichVu(),
                        formatCurrency(dv.getGiaDichVu()),
                        dv.getMoTa()
                    };
                    tableModel.addRow(row);
                    count++;
                }
            }

            if (count == 0) {
                JOptionPane.showMessageDialog(view, 
                    "Không tìm thấy dịch vụ nào phù hợp với từ khóa:\n\"" + keyword + "\"", 
                    "Không có kết quả", JOptionPane.INFORMATION_MESSAGE);
                refreshData(); // Hiển thị lại toàn bộ dữ liệu
            } else {
                JOptionPane.showMessageDialog(view, 
                    "Tìm thấy " + count + " dịch vụ phù hợp với từ khóa:\n\"" + keyword + "\"", 
                    "Kết quả tìm kiếm", JOptionPane.INFORMATION_MESSAGE);
            }
            
            capNhatTrangThaiUI();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, 
                "Lỗi khi tìm kiếm: " + e.getMessage(), 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hienThiThongTinChiTiet(DichVu dichVu) {
        StringBuilder info = new StringBuilder();
        info.append("╔══════════════════════════════════════════╗\n");
        info.append("║         THÔNG TIN CHI TIẾT DỊCH VỤ         ║\n");
        info.append("╠══════════════════════════════════════════╣\n");
        info.append("║ Mã dịch vụ: ").append(String.format("%-26s", dichVu.getMaDichVu())).append("║\n");
        info.append("║ Tên dịch vụ: ").append(String.format("%-25s", dichVu.getTenDichVu())).append("║\n");
        info.append("║ Giá dịch vụ: ").append(String.format("%-25s", formatCurrency(dichVu.getGiaDichVu()))).append("║\n");
        info.append("╠══════════════════════════════════════════╣\n");
        info.append("║ Mô tả:                                   ║\n");
        
        String moTa = dichVu.getMoTa() != null ? dichVu.getMoTa() : "Không có mô tả";
        String[] lines = moTa.split("\\n");
        for (String line : lines) {
            if (line.length() > 38) {
                // Chia dòng dài thành nhiều dòng ngắn
                while (line.length() > 38) {
                    info.append("║ ").append(String.format("%-38s", line.substring(0, 38))).append("║\n");
                    line = line.substring(38);
                }
                if (line.length() > 0) {
                    info.append("║ ").append(String.format("%-38s", line)).append("║\n");
                }
            } else {
                info.append("║ ").append(String.format("%-38s", line)).append("║\n");
            }
        }
        
        info.append("╚══════════════════════════════════════════╝");

        JTextArea textArea = new JTextArea(info.toString());
        textArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        textArea.setEditable(false);
        textArea.setBackground(new Color(248, 249, 250));
        textArea.setMargin(new Insets(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        
        JOptionPane.showMessageDialog(view, scrollPane, 
            "Chi tiết: " + dichVu.getTenDichVu(), JOptionPane.INFORMATION_MESSAGE);
    }

    // Các methods public để truy cập từ bên ngoài
    public void timKiem(String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            timKiemDichVu(keyword.trim());
        } else {
            refreshData();
        }
    }
    
    public DichVu getDichVuDangChon() {
        int selectedRow = view.tblDichVu.getSelectedRow();
        if (selectedRow != -1) {
            String maDichVu = (String) tableModel.getValueAt(selectedRow, 0);
            return dichVuDAO.getDichVu(maDichVu);
        }
        return null;
    }
    
    public int getTongSoDichVu() {
        return tableModel.getRowCount();
    }
    
    public HashMap<String, DichVu> getDanhSachDichVu() {
        return dichVuDAO.getListDICHVU();
    }
}