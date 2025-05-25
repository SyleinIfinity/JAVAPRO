package VIEW.STAFF;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class view_DonDatPhong extends JFrame {
    private JTextField txtSoNguoi;
    private JComboBox<String> cbMaDatPhong, cbMaPhong, cbDichVu;
    private JSpinner spinnerNgayThue, spinnerNgayTra;
    private JTable table;
    private DefaultTableModel tableModel;
    private Color mauChinh = new Color(52, 152, 219);
    private Color mauPhu = new Color(41, 128, 185);
    private Color mauNen = new Color(236, 240, 241);
    private Color mauNhan = new Color(230, 126, 34);

    public view_DonDatPhong() {
        setTitle("Đặt phòng - Nhân viên");
        setSize(1080, 880);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(mauNen);
        setLayout(new BorderLayout());

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

        // Mã đặt phòng
        gbc.gridx = 0;
        gbc.gridy = 0;
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
        gbc.gridy = 1;
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
        gbc.gridy = 2;
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

        // Dịch vụ sử dụng
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        JLabel lblDichVu = new JLabel("Dịch vụ sử dụng:");
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
        gbc.gridy = 4;
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
        gbc.gridy = 5;
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
                
                // Vẽ nền button với góc bo tròn
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                // Vẽ chữ màu đen
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

        // Table model - Removed TrangThai column
        String[] columnNames = {"Mã đặt phòng", "Mã người dùng", "Mã phòng", "Số người", "Dịch vụ", "Ngày thuê", "Ngày trả"};
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

        // Custom header
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

        // Column settings
        caiDatTableColumns();

        // Row click event


        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private void caiDatTableColumns() {
        int[] columnWidths = {120, 120, 100, 80, 150, 120, 120};
        TableColumnModel columnModel = table.getColumnModel();

        for (int i = 0; i < columnWidths.length; i++) {
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
                    
                    if (column == 0 || column == 2 || column == 3) {
                        setHorizontalAlignment(SwingConstants.CENTER);
                    } else {
                        setHorizontalAlignment(SwingConstants.LEFT);
                    }
                    
                    return c;
                }
            });
        }
    }

    private void hienThiThongTinDatPhong(int row) {
        cbMaDatPhong.setSelectedItem(tableModel.getValueAt(row, 0) != null ? tableModel.getValueAt(row, 0).toString() : "");
        cbMaPhong.setSelectedItem(tableModel.getValueAt(row, 2) != null ? tableModel.getValueAt(row, 2).toString() : "");
        txtSoNguoi.setText(tableModel.getValueAt(row, 3) != null ? tableModel.getValueAt(row, 3).toString() : "");
        cbDichVu.setSelectedItem(tableModel.getValueAt(row, 4) != null ? tableModel.getValueAt(row, 4).toString() : "");
        
        try {
            String ngayThueStr = tableModel.getValueAt(row, 5) != null ? tableModel.getValueAt(row, 5).toString() : "";
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            if (!ngayThueStr.isEmpty()) {
                Date ngayThue = dateFormat.parse(ngayThueStr);
                spinnerNgayThue.setValue(ngayThue);
            }
            
            String ngayTraStr = tableModel.getValueAt(row, 6) != null ? tableModel.getValueAt(row, 6).toString() : "";
            if (!ngayTraStr.isEmpty()) {
                Date ngayTra = dateFormat.parse(ngayTraStr);
                spinnerNgayTra.setValue(ngayTra);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Color toiMau(Color color, float fraction) {
        int red = Math.max(0, Math.round(color.getRed() * (1 - fraction)));
        int green = Math.max(0, Math.round(color.getGreen() * (1 - fraction)));
        int blue = Math.max(0, Math.round(color.getBlue() * (1 - fraction)));
        return new Color(red, green, blue);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new view_DonDatPhong().setVisible(true));
    }
}