package VIEW.STAFF;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import VIEW.view_main;

import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

public class view_DonDatPhong extends JPanel {
    private JTextField txtSoNguoi;
    private JComboBox<String> cbMaDatPhong;
    private JComboBox<Record> cbMaChiNhanh, cbMaPhong, cbDichVu;
    private JSpinner spinnerNgayThue, spinnerNgayTra;
    private JTable table;
    private DefaultTableModel tableModel;
    private Color mauChinh = new Color(52, 152, 219);
    private Color mauPhu = new Color(41, 128, 185);
    private Color mauNen = new Color(236, 240, 241);
    private Color mauNhan = new Color(230, 126, 34);
    private List<Object[]> originalData = new ArrayList<>();

    view_main vMain;

    public view_DonDatPhong(view_main vMain) {
        setLayout(new BorderLayout());
        setBackground(mauNen);
        this.vMain = vMain;

        // Header panel
        JPanel headerPanel = taoHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Main content panel
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBackground(mauNen);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Left panel (input form)
        JPanel leftPanel = taoInputPanel();
        contentPanel.add(leftPanel, BorderLayout.WEST);

        // Right panel (table)
        JPanel rightPanel = taoTablePanel();
        contentPanel.add(rightPanel, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel taoHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(mauPhu);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel lblTitle = new JLabel("QUẢN LÝ ĐẶT PHÒNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);

        headerPanel.add(lblTitle, BorderLayout.CENTER);
        return headerPanel;
    }

    private JPanel taoInputPanel() {
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Thông tin đặt phòng"),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        inputPanel.setPreferredSize(new Dimension(380, getHeight()));

        // Form panel với GridBagLayout
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Chi nhánh
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        JLabel lblMaChiNhanh = new JLabel("Chi nhánh:");
        lblMaChiNhanh.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblMaChiNhanh, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        cbMaChiNhanh = new JComboBox<>();
        cbMaChiNhanh.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbMaChiNhanh.setBackground(Color.WHITE);
        formPanel.add(cbMaChiNhanh, gbc);

        // Mã đặt phòng
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        JLabel lblMaDatPhong = new JLabel("Mã đặt phòng:");
        lblMaDatPhong.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblMaDatPhong, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        cbMaDatPhong = new JComboBox<>();
        cbMaDatPhong.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbMaDatPhong.setBackground(Color.WHITE);
        formPanel.add(cbMaDatPhong, gbc);

        // Mã phòng
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        JLabel lblMaPhong = new JLabel("Mã phòng:");
        lblMaPhong.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblMaPhong, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        cbMaPhong = new JComboBox<>();
        cbMaPhong.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbMaPhong.setBackground(Color.WHITE);
        formPanel.add(cbMaPhong, gbc);

        // Số người
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        JLabel lblSoNguoi = new JLabel("Số người:");
        lblSoNguoi.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblSoNguoi, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        txtSoNguoi = new JTextField();
        txtSoNguoi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSoNguoi.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        txtSoNguoi.addActionListener(e -> {
            try {
                Integer.parseInt(txtSoNguoi.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập số hợp lệ!");
                txtSoNguoi.setText("");
            }
        });
        formPanel.add(txtSoNguoi, gbc);

        // Dịch vụ sử dụng (Khách hàng)
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;
        JLabel lblDichVu = new JLabel("Khách hàng:");
        lblDichVu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblDichVu, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        cbDichVu = new JComboBox<>();
        cbDichVu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbDichVu.setBackground(Color.WHITE);
        formPanel.add(cbDichVu, gbc);

        // Ngày thuê phòng
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0;
        JLabel lblNgayThue = new JLabel("Ngày thuê phòng:");
        lblNgayThue.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblNgayThue, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        spinnerNgayThue = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditorNgayThue = new JSpinner.DateEditor(spinnerNgayThue, "dd/MM/yyyy");
        spinnerNgayThue.setEditor(dateEditorNgayThue);
        spinnerNgayThue.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(spinnerNgayThue, gbc);

        // Ngày trả phòng
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0;
        JLabel lblNgayTra = new JLabel("Ngày trả phòng:");
        lblNgayTra.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblNgayTra, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        spinnerNgayTra = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditorNgayTra = new JSpinner.DateEditor(spinnerNgayTra, "dd/MM/yyyy");
        spinnerNgayTra.setEditor(dateEditorNgayTra);
        spinnerNgayTra.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(spinnerNgayTra, gbc);
        
        inputPanel.add(formPanel, BorderLayout.CENTER);

        // Button panel với 4 button được thiết kế lại
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JButton btnDat = taoButtonDep("Đặt", mauChinh);
        JButton btnCapNhat = taoButtonDep("Cập nhật", mauPhu);
        JButton btnDuyet = taoButtonDep("Duyệt", mauNhan);
        JButton btnHuy = taoButtonDep("Hủy", new Color(231, 76, 60));
        
        // Thêm ActionListener mẫu cho các nút
        btnDat.addActionListener(e -> JOptionPane.showMessageDialog(this, "Chức năng Đặt đang được triển khai!"));
        btnCapNhat.addActionListener(e -> JOptionPane.showMessageDialog(this, "Chức năng Cập nhật đang được triển khai!"));
        btnDuyet.addActionListener(e -> JOptionPane.showMessageDialog(this, "Chức năng Duyệt đang được triển khai!"));
        btnHuy.addActionListener(e -> JOptionPane.showMessageDialog(this, "Chức năng Hủy đang được triển khai!"));
        
        buttonPanel.add(btnDat);
        buttonPanel.add(btnCapNhat);
        buttonPanel.add(btnDuyet);
        buttonPanel.add(btnHuy);
        
        inputPanel.add(buttonPanel, BorderLayout.SOUTH);

        return inputPanel;
    }

    private JButton taoButtonDep(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                g2.setColor(Color.BLACK);
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(getText(), x, y);
                
                g2.dispose();
            }
            
            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(toiMau(bgColor, 0.3f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
                g2.dispose();
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(140, 45));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }

    private JPanel taoTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(mauNen);

        // Panel cho bộ lọc
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(mauNen);
        JLabel lblFilter = new JLabel("Lọc theo trạng thái:");
        lblFilter.setFont(new Font("Segoe UI", Font.BOLD, 14));
        filterPanel.add(lblFilter);

        String[] trangThaiOptions = {"Tất cả", "Chờ duyệt", "Đã duyệt", "Đã hủy"};
        JComboBox<String> cbFilterTrangThai = new JComboBox<>(trangThaiOptions);
        cbFilterTrangThai.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbFilterTrangThai.setBackground(Color.WHITE);
        filterPanel.add(cbFilterTrangThai);

        String[] columnNames = {"Mã đặt phòng", "Mã khách hàng", "Mã chi nhánh", "Mã phòng", "Số người", "Dịch vụ", "Ngày thuê", "Ngày trả", "Trạng thái"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(52, 152, 219, 100));
        table.setSelectionForeground(Color.BLACK);
        table.setGridColor(new Color(189, 195, 199));
        table.setShowGrid(true);
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 45));
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel(value.toString());
                label.setFont(new Font("Segoe UI", Font.BOLD, 14));
                label.setOpaque(true);
                label.setBackground(mauChinh);
                label.setForeground(Color.WHITE);
                label.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, mauNhan));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                return label;
            }
        });

        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        caiDatTableColumns();

        cbFilterTrangThai.addActionListener(e -> {
            String selectedTrangThai = (String) cbFilterTrangThai.getSelectedItem();
            filterTableByTrangThai(selectedTrangThai);
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        tablePanel.add(filterPanel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private void caiDatTableColumns() {
        // Điều chỉnh độ rộng cột theo thứ tự mới
        int[] columnWidths = {120, 120, 100, 100, 80, 150, 120, 120, 100}; // Mã đặt phòng, Mã khách hàng, Mã chi nhánh, Mã phòng, Số người, Dịch vụ, Ngày thuê, Ngày trả, Trạng thái
        TableColumnModel columnModel = table.getColumnModel();
        int columnCount = Math.min(columnWidths.length, columnModel.getColumnCount());

        for (int i = 0; i < columnCount; i++) {
            TableColumn column = columnModel.getColumn(i);
            column.setPreferredWidth(columnWidths[i]);

            column.setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, 
                        boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    if (!isSelected) {
                        c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));
                    }
                    
                    if (column == 0) { 
                        c.setForeground(Color.GRAY);
                        c.setBackground(new Color(240, 240, 240));
                        setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                    } else {
                        c.setForeground(Color.BLACK);
                        setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                    }
                    
                    if (column == 0 || column == 1 || column == 2 || column == 3 || column == 4 || column == 8) {
                        setHorizontalAlignment(SwingConstants.CENTER);
                    } else {
                        setHorizontalAlignment(SwingConstants.LEFT);
                    }
                    
                    return c;
                }
            });
        }
    }

    private void filterTableByTrangThai(String trangThai) {
        if (originalData.isEmpty()) {
            return;
        }
        tableModel.setRowCount(0);
        for (Object[] row : originalData) {
            if (trangThai.equals("Tất cả") || (row[8] != null && row[8].equals(trangThai))) { // Cột "Trạng thái" giờ là cột 8
                tableModel.addRow(row);
            }
        }
    }

    private void hienThiThongTinDatPhong(int row) {
        // Chọn Mã chi nhánh (cột 2)
        String maChiNhanh = tableModel.getValueAt(row, 2) != null ? tableModel.getValueAt(row, 2).toString() : "";
        for (int i = 0; i < cbMaChiNhanh.getItemCount(); i++) {
            Record item = cbMaChiNhanh.getItemAt(i);
            if (item != null && item.getColumns() != null && item.getColumns().length > 0 && item.getColumns()[0].equals(maChiNhanh)) {
                cbMaChiNhanh.setSelectedIndex(i);
                break;
            }
        }

        cbMaDatPhong.setSelectedItem(tableModel.getValueAt(row, 0) != null ? tableModel.getValueAt(row, 0).toString() : "");

        String maPhong = tableModel.getValueAt(row, 3) != null ? tableModel.getValueAt(row, 3).toString() : "";
        for (int i = 0; i < cbMaPhong.getItemCount(); i++) {
            Record item = cbMaPhong.getItemAt(i);
            if (item != null && item.getColumns() != null && item.getColumns().length > 0 && item.getColumns()[0].equals(maPhong)) {
                cbMaPhong.setSelectedIndex(i);
                break;
            }
        }

        txtSoNguoi.setText(tableModel.getValueAt(row, 4) != null ? tableModel.getValueAt(row, 4).toString() : "");

        String maKhachHang = tableModel.getValueAt(row, 1) != null ? tableModel.getValueAt(row, 1).toString() : "";
        for (int i = 0; i < cbDichVu.getItemCount(); i++) {
            Record item = cbDichVu.getItemAt(i);
            if (item != null && item.getColumns() != null && item.getColumns().length > 0 && item.getColumns()[0].equals(maKhachHang)) {
                cbDichVu.setSelectedIndex(i);
                break;
            }
        }

        try {
            String ngayThueStr = tableModel.getValueAt(row, 6) != null ? tableModel.getValueAt(row, 6).toString() : "";
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            if (!ngayThueStr.isEmpty()) {
                Date ngayThue = dateFormat.parse(ngayThueStr);
                spinnerNgayThue.setValue(ngayThue);
            }
            
            String ngayTraStr = tableModel.getValueAt(row, 7) != null ? tableModel.getValueAt(row, 7).toString() : "";
            if (!ngayTraStr.isEmpty()) {
                Date ngayTra = dateFormat.parse(ngayTraStr);
                spinnerNgayTra.setValue(ngayTra);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi định dạng ngày tháng: " + e.getMessage());
        }
    }

    private void loadChiNhanhData(Connection conn) {
        try {
            String sql = "SELECT MaChiNhanh, TenChiNhanh FROM ChiNhanh";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                cbMaChiNhanh.removeAllItems();
                while (rs.next()) {
                    String[] columns = new String[] {
                        rs.getString("MaChiNhanh") != null ? rs.getString("MaChiNhanh") : "",
                        rs.getString("TenChiNhanh") != null ? rs.getString("TenChiNhanh") : ""
                    };
                    cbMaChiNhanh.addItem(new Record(columns));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu chi nhánh: " + e.getMessage());
        }
    }

    private void loadPhongData(Connection conn) {
        try {
            String sql = "SELECT MaPhong, SoPhong, Tang FROM Phong";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                cbMaPhong.removeAllItems();
                while (rs.next()) {
                    String[] columns = new String[] {
                        rs.getString("MaPhong") != null ? rs.getString("MaPhong") : "",
                        rs.getString("SoPhong") != null ? rs.getString("SoPhong") : "",
                        rs.getString("Tang") != null ? rs.getString("Tang") : ""
                    };
                    cbMaPhong.addItem(new Record(columns));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu phòng: " + e.getMessage());
        }
    }

    private void loadKhachHangData(Connection conn) {
        try {
            String sql = "SELECT MaKhachHang, TenKhachHang FROM KhachHang";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                cbDichVu.removeAllItems();
                while (rs.next()) {
                    String[] columns = new String[] {
                        rs.getString("MaKhachHang") != null ? rs.getString("MaKhachHang") : "",
                        rs.getString("TenKhachHang") != null ? rs.getString("TenKhachHang") : ""
                    };
                    cbDichVu.addItem(new Record(columns));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu khách hàng: " + e.getMessage());
        }
    }

    private void loadTableDataFromSQL(Connection conn) {
        try {
            String sql = "SELECT MaDatPhong, MaKhachHang, MaChiNhanh, MaPhong, SoLuongNguoi, DichVu, NgayThue, NgayTra, TrangThai FROM DonDatPhong";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                tableModel.setRowCount(0);
                originalData.clear();
                while (rs.next()) {
                    Object[] row = new Object[] {
                        rs.getString("MaDatPhong") != null ? rs.getString("MaDatPhong") : "",    
                        rs.getString("MaKhachHang") != null ? rs.getString("MaKhachHang") : "", 
                        rs.getString("MaChiNhanh") != null ? rs.getString("MaChiNhanh") : "",
                        rs.getString("MaPhong") != null ? rs.getString("MaPhong") : "",       
                        rs.getInt("SoLuongNguoi"),                                             
                        rs.getString("DichVu") != null ? rs.getString("DichVu") : "",          
                        rs.getString("NgayThue") != null ? rs.getString("NgayThue") : "",       
                        rs.getString("NgayTra") != null ? rs.getString("NgayTra") : "",         
                        rs.getString("TrangThai") != null ? rs.getString("TrangThai") : ""      
                    };
                    originalData.add(row);
                    tableModel.addRow(row);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + e.getMessage());
        }
    }

    private Color toiMau(Color color, float fraction) {
        int red = Math.max(0, (int) (color.getRed() * (1 - fraction)));
        int green = Math.max(0, (int) (color.getGreen() * (1 - fraction)));
        int blue = Math.max(0, (int) (color.getBlue() * (1 - fraction)));
        return new Color(red, green, blue);
    }


    class Record {
        private String[] columns;

        public Record(String[] columns) {
            this.columns = columns;
        }

        public String[] getColumns() {
            return columns;
        }

        @Override
        public String toString() {
            if (columns == null || columns.length == 0) {
                return "";
            }
            return String.join("|", columns);
        }
    }

}