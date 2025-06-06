package GUI.STAFF;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import GUI.view_main;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.MouseAdapter;
import javax.swing.plaf.basic.BasicButtonUI;

public class view_DonDatPhong extends JPanel {
    private JTextField txtSoNguoi;
    private JTextField txtMaDatPhong;
    public JComboBox<Record> cbMaChiNhanh, cbMaPhong, cbDichVu;
    private JSpinner spinnerNgayThue, spinnerNgayTra;
    public JTable table;
    private DefaultTableModel tableModel;
    private Color mauChinh = new Color(52, 152, 219);
    private Color mauPhu = new Color(41, 128, 185);
    private Color mauNen = new Color(236, 240, 241);
    private Color mauNhan = new Color(230, 126, 34);
    private List<Object[]> originalData = new ArrayList<>();
    private JButton btnDat, btnHuy;

    view_main vMain;
    private BLL.STAFF.ctl_DonDatPhong controller;

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
        
        // Initialize controller
        controller = new BLL.STAFF.ctl_DonDatPhong(this, vMain);
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
        txtMaDatPhong = new JTextField();
        txtMaDatPhong.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtMaDatPhong.setEnabled(false);
        txtMaDatPhong.setBackground(new Color(240, 240, 240));
        txtMaDatPhong.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(txtMaDatPhong, gbc);

        // Mã phòng
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        JLabel lblMaPhong = new JLabel("Phòng:");
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

        // Button panel với 2 button (removed Duyệt và Cập nhật)
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 15, 15));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        btnDat = taoButtonDep("Đặt", mauChinh);
        btnHuy = taoButtonDep("Hủy", new Color(231, 76, 60));
        
        buttonPanel.add(btnDat);
        buttonPanel.add(btnHuy);
        
        inputPanel.add(buttonPanel, BorderLayout.SOUTH);

        return inputPanel;
    }

    private JButton taoButtonDep(String text, Color bgColor) {
        final Color originalColor = bgColor;
        final Color hoverColor = new Color(
            Math.min(255, (int)(bgColor.getRed() * 1.2)),
            Math.min(255, (int)(bgColor.getGreen() * 1.2)),
            Math.min(255, (int)(bgColor.getBlue() * 1.2))
        );
        final Color pressedColor = toiMau(bgColor, 0.2f);

        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(140, 45));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setOpaque(false);
        
        button.setUI(new BasicButtonUI() {
            private boolean hover = false;
            private boolean pressed = false;
            
            @Override
            public void installUI(JComponent c) {
                super.installUI(c);
                c.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        hover = true;
                        c.repaint();
                    }
                    
                    @Override
                    public void mouseExited(java.awt.event.MouseEvent e) {
                        hover = false;
                        c.repaint();
                    }
                    
                    @Override
                    public void mousePressed(java.awt.event.MouseEvent e) {
                        pressed = true;
                        c.repaint();
                    }
                    
                    @Override
                    public void mouseReleased(java.awt.event.MouseEvent e) {
                        pressed = false;
                        c.repaint();
                    }
                });
            }
            
            @Override
            public void paint(Graphics g, JComponent c) {
                AbstractButton b = (AbstractButton) c;
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Determine current color based on state
                Color currentColor = originalColor;
                if (pressed) {
                    currentColor = pressedColor;
                } else if (hover) {
                    currentColor = hoverColor;
                }
                
                // Draw shadow effect when hovered
                if (hover && !pressed) {
                    g2.setColor(new Color(0, 0, 0, 30));
                    g2.fillRoundRect(3, 3, c.getWidth() - 6, c.getHeight() - 6, 20, 20);
                }
                
                // Draw button background
                g2.setColor(currentColor);
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 20, 20);
                
                // Draw border
                if (hover) {
                    g2.setColor(Color.WHITE);
                    g2.setStroke(new BasicStroke(1.5f));
                } else {
                    g2.setColor(toiMau(originalColor, 0.3f));
                    g2.setStroke(new BasicStroke(1.0f));
                }
                g2.drawRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, 20, 20);
                
                // Draw text with shadow if hovered
                FontMetrics fm = g2.getFontMetrics();
                int x = (c.getWidth() - fm.stringWidth(b.getText())) / 2;
                int y = ((c.getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                
                if (hover && !pressed) {
                    g2.setColor(new Color(0, 0, 0, 50));
                    g2.drawString(b.getText(), x + 1, y + 1);
                }
                
                g2.setColor(Color.WHITE);
                g2.drawString(b.getText(), x, y);
                
                g2.dispose();
            }
        });
        
        return button;
    }

    private JPanel taoTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(mauNen);

        String[] columnNames = {"Mã đặt phòng", "Mã khách hàng", "Mã chi nhánh", "Mã phòng", "Số người", "Dịch vụ", "Ngày thuê", "Ngày trả"};
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

        // Add row selection listener
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow >= 0) {
                        loadSelectedRowData(selectedRow);
                    }
                }
            }
        });

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

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private void caiDatTableColumns() {
        int[] columnWidths = {120, 120, 100, 100, 80, 150, 120, 120};
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
                    
                    if (column == 0 || column == 1 || column == 2 || column == 3 || column == 4) {
                        setHorizontalAlignment(SwingConstants.CENTER);
                    } else {
                        setHorizontalAlignment(SwingConstants.LEFT);
                    }
                    
                    return c;
                }
            });
        }
    }

    private void loadSelectedRowData(int row) {
        if (row < 0 || row >= tableModel.getRowCount()) return;

        String maDatPhong = tableModel.getValueAt(row, 0) != null ? tableModel.getValueAt(row, 0).toString() : "";
        String maKhachHang = tableModel.getValueAt(row, 1) != null ? tableModel.getValueAt(row, 1).toString() : "";
        String maChiNhanh = tableModel.getValueAt(row, 2) != null ? tableModel.getValueAt(row, 2).toString() : "";
        String maPhong = tableModel.getValueAt(row, 3) != null ? tableModel.getValueAt(row, 3).toString() : "";
        String soNguoi = tableModel.getValueAt(row, 4) != null ? tableModel.getValueAt(row, 4).toString() : "";
        String ngayThueStr = tableModel.getValueAt(row, 6) != null ? tableModel.getValueAt(row, 6).toString() : "";
        String ngayTraStr = tableModel.getValueAt(row, 7) != null ? tableModel.getValueAt(row, 7).toString() : "";
        
        // Update form fields
        txtMaDatPhong.setText(maDatPhong);
        txtSoNguoi.setText(soNguoi);
        controller.loadDetailedDataForRow(maDatPhong, maKhachHang, maChiNhanh, maPhong, ngayThueStr, ngayTraStr);
    }
    
    public void clearMaDatPhongComboBox() {
        // No longer needed since we changed to textfield
    }
    
    public void addMaDatPhongItem(String maDatPhong) {
        // No longer needed since we changed to textfield
    }
    
    public void clearChiNhanhComboBox() {
        cbMaChiNhanh.removeAllItems();
    }
    
    public void addChiNhanhItem(String[] data) {
        cbMaChiNhanh.addItem(new Record(data));
    }
    
    public void clearKhachHangComboBox() {
        cbDichVu.removeAllItems();
    }
    
    public void addKhachHangItem(String[] data) {
        cbDichVu.addItem(new Record(data));
    }
    
    public void clearPhongComboBox() {
        cbMaPhong.removeAllItems();
    }
    
    public void addPhongItem(String[] data) {
        cbMaPhong.addItem(new Record(data));
    }
    
    public void setChiNhanhWithDetail(String maChiNhanh, String tenChiNhanh) {
        for (int i = 0; i < cbMaChiNhanh.getItemCount(); i++) {
            Record item = cbMaChiNhanh.getItemAt(i);
            if (item.getColumns()[0].equals(maChiNhanh)) {
                cbMaChiNhanh.setSelectedIndex(i);
                return;
            }
        }
        
        cbMaChiNhanh.addItem(new Record(new String[]{maChiNhanh, tenChiNhanh}));
        cbMaChiNhanh.setSelectedIndex(cbMaChiNhanh.getItemCount() - 1);
    }
    
    public void setPhongWithDetail(String maPhong, String tenPhong, String tang) {
        for (int i = 0; i < cbMaPhong.getItemCount(); i++) {
            Record item = cbMaPhong.getItemAt(i);
            if (item.getColumns()[0].equals(maPhong)) {
                cbMaPhong.setSelectedIndex(i);
                return;
            }
        }
        
        cbMaPhong.addItem(new Record(new String[]{maPhong, tenPhong, tang}));
        cbMaPhong.setSelectedIndex(cbMaPhong.getItemCount() - 1);
    }
    
    public void setKhachHangWithDetail(String maKhachHang, String tenKhachHang) {
        for (int i = 0; i < cbDichVu.getItemCount(); i++) {
            Record item = cbDichVu.getItemAt(i);
            if (item.getColumns()[0].equals(maKhachHang)) {
                cbDichVu.setSelectedIndex(i);
                return;
            }
        }
        
        cbDichVu.addItem(new Record(new String[]{maKhachHang, tenKhachHang}));
        cbDichVu.setSelectedIndex(cbDichVu.getItemCount() - 1);
    }
    
    public void setNgayThue(Date date) {
        spinnerNgayThue.setValue(date);
    }
    
    public void setNgayTra(Date date) {
        spinnerNgayTra.setValue(date);
    }

    public void clearFormFields() {
        txtMaDatPhong.setText("");
        txtSoNguoi.setText("");
        cbMaChiNhanh.setSelectedIndex(cbMaChiNhanh.getItemCount() > 0 ? -1 : 0);
        cbMaPhong.setSelectedIndex(cbMaPhong.getItemCount() > 0 ? -1 : 0);
        cbDichVu.setSelectedIndex(cbDichVu.getItemCount() > 0 ? -1 : 0);
        
        spinnerNgayThue.setValue(new Date());
        spinnerNgayTra.setValue(new Date());
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
            String sql = "SELECT MaDatPhong, MaKhachHang, MaChiNhanh, MaPhong, SoLuongNguoi, DichVu, NgayThue, NgayTra FROM DonDatPhong";
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
                        rs.getString("NgayTra") != null ? rs.getString("NgayTra") : ""         
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

    public void updateTableData(Object[][] data, String[] columnNames) {
        tableModel.setRowCount(0);
        for (Object[] row : data) {
            tableModel.addRow(row);
        }
        originalData.clear();
        for (Object[] row : data) {
            originalData.add(row.clone());
        }
        tableModel.fireTableDataChanged();
    }

    public JButton getBtnDat() {
        return btnDat;
    }
    
    public JButton getBtnHuy() {
        return btnHuy;
    }
    
    public JTextField getTxtSoNguoi() {
        return txtSoNguoi;
    }
    
    public JTextField getTxtMaDatPhong() {
        return txtMaDatPhong;
    }
    
    public JSpinner getSpinnerNgayThue() {
        return spinnerNgayThue;
    }
    
    public JSpinner getSpinnerNgayTra() {
        return spinnerNgayTra;
    }

    public static class Record {
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