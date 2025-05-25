package VIEW.ADMIN;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;

import java.util.Date;

public class view_KhachHang extends JFrame {
    private JTable tblKhachHang;
    private JTextField txtMaKhachHang, txtTenKhachHang, txtEmail, txtSoDienThoai, txtDiaChi;
    private JSpinner spnNgaySinh;
    private JComboBox<String> cmbTrangThai;
    private JButton btnSua;
    private DefaultTableModel model;
    private Color mauChinh = new Color(41, 128, 185);
    private Color mauPhu = new Color(52, 152, 219);
    private Color mauNhan = new Color(230, 126, 34);
    private Color mauNen = new Color(236, 240, 241);
    public String maNguoiDung;
    public String maVaiTro;

    public view_KhachHang(String maNguoiDung, String maVaiTro) {
        setTitle("Quản lý Khách Hàng");
        setSize(1080, 880);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(mauNen);

        this.maNguoiDung = maNguoiDung;
        this.maVaiTro = maVaiTro;

        JPanel headerPanel = taoHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBackground(mauNen);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        JPanel searchPanel = taoFilterPanel();
        contentPanel.add(searchPanel, BorderLayout.NORTH);

        JPanel tablePanel = taoTablePanel();
        contentPanel.add(tablePanel, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);

        JPanel buttonPanel = taoButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
    
    private JPanel taoHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(mauChinh);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel lblTitle = new JLabel("QUẢN LÝ KHÁCH HÀNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);

        headerPanel.add(lblTitle, BorderLayout.CENTER);
        return headerPanel;
    }

    private JPanel taoFilterPanel() {
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.setBackground(mauNen);
        filterPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel lblFilter = new JLabel("Lọc theo trạng thái:");
        lblFilter.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JComboBox<String> cmbLoc = new JComboBox<>();
        cmbLoc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbLoc.setBackground(Color.WHITE);
        cmbLoc.setPreferredSize(new Dimension(150, 30));
        
        // Thêm sự kiện lọc ngay khi thay đổi giá trị
        

        filterPanel.add(lblFilter);
        filterPanel.add(cmbLoc);

        return filterPanel;
    }

    private void locTheoTrangThai(String trangThai) {
        // Clear current table data
        while (model.getRowCount() > 0) {
            model.removeRow(0);
        }
        // In a real application, you would fetch data from a database based on the selected status
        // For now, the table remains empty as no sample data is added
    }

    private JPanel taoTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(mauNen);

        String[] columnNames = {"Mã khách hàng", "Tên khách hàng", "Email", "Số điện thoại", "Ngày sinh", "Địa chỉ", "Trạng thái"};
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép chỉnh sửa trực tiếp trên bảng
            }
        };

        tblKhachHang = new JTable(model);
        tblKhachHang.setRowHeight(40);
        tblKhachHang.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblKhachHang.setSelectionBackground(new Color(241, 196, 15, 100));
        tblKhachHang.setSelectionForeground(Color.BLACK);
        tblKhachHang.setShowGrid(true);
        tblKhachHang.setGridColor(new Color(189, 195, 199));
        tblKhachHang.setRowSelectionAllowed(true);
        tblKhachHang.setColumnSelectionAllowed(false);
        tblKhachHang.setFillsViewportHeight(true);

        // Thêm sự kiện khi click vào một hàng trong bảng
        

        JTableHeader header = tblKhachHang.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 45));
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel(value.toString());
                label.setFont(new Font("Segoe UI", Font.BOLD, 14));
                label.setOpaque(true);
                label.setBackground(mauChinh);
                label.setForeground(Color.BLACK);
                label.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, mauNhan));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                return label;
            }
        });

        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        // Thiết lập độ rộng cho các cột
        caiDatTableColumns();

        JScrollPane scrollPane = new JScrollPane(tblKhachHang);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Thêm panel nhập liệu phía dưới bảng
        JPanel formPanel = taoFormPanel();
        tablePanel.add(formPanel, BorderLayout.SOUTH);
        
        return tablePanel;
    }
    
    private JPanel taoFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(mauNen);
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Mã khách hàng (không được chỉnh sửa)
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblMaKhachHang = new JLabel("Mã khách hàng:");
        lblMaKhachHang.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblMaKhachHang, gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtMaKhachHang = new JTextField(15);
        txtMaKhachHang.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtMaKhachHang.setEnabled(false); // Không cho phép chỉnh sửa
        txtMaKhachHang.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        formPanel.add(txtMaKhachHang, gbc);
        
        // Tên khách hàng
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lblTenKhachHang = new JLabel("Tên khách hàng:");
        lblTenKhachHang.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblTenKhachHang, gbc);
        
        gbc.gridx = 1;
        txtTenKhachHang = new JTextField(15);
        txtTenKhachHang.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtTenKhachHang.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        formPanel.add(txtTenKhachHang, gbc);
        
        // Email
        gbc.gridx = 2;
        gbc.gridy = 0;
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblEmail, gbc);
        
        gbc.gridx = 3;
        txtEmail = new JTextField(15);
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtEmail.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        formPanel.add(txtEmail, gbc);
        
        // Số điện thoại
        gbc.gridx = 2;
        gbc.gridy = 1;
        JLabel lblSoDienThoai = new JLabel("Số điện thoại:");
        lblSoDienThoai.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblSoDienThoai, gbc);
        
        gbc.gridx = 3;
        txtSoDienThoai = new JTextField(15);
        txtSoDienThoai.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSoDienThoai.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        formPanel.add(txtSoDienThoai, gbc);
        
        // Ngày sinh (JSpinner)
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblNgaySinh = new JLabel("Ngày sinh:");
        lblNgaySinh.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblNgaySinh, gbc);
        
        gbc.gridx = 1;
        spnNgaySinh = new JSpinner(new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH));
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spnNgaySinh, "dd/MM/yyyy");
        spnNgaySinh.setEditor(dateEditor);
        spnNgaySinh.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        formPanel.add(spnNgaySinh, gbc);
        
        // Địa chỉ
        gbc.gridx = 2;
        gbc.gridy = 2;
        JLabel lblDiaChi = new JLabel("Địa chỉ:");
        lblDiaChi.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblDiaChi, gbc);
        
        gbc.gridx = 3;
        txtDiaChi = new JTextField(15);
        txtDiaChi.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtDiaChi.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        formPanel.add(txtDiaChi, gbc);
        
        // Trạng thái (ComboBox)
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel lblTrangThai = new JLabel("Trạng thái:");
        lblTrangThai.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblTrangThai, gbc);
        
        gbc.gridx = 1;
        cmbTrangThai = new JComboBox<>();
        cmbTrangThai.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbTrangThai.setBackground(Color.WHITE);
        formPanel.add(cmbTrangThai, gbc);
        
        return formPanel;
    }

    private void caiDatTableColumns() {
        int[] columnWidths = {100, 180, 150, 120, 100, 180, 120};
        TableColumnModel columnModel = tblKhachHang.getColumnModel();

        for (int i = 0; i < columnWidths.length; i++) {
            TableColumn column = columnModel.getColumn(i);
            column.setPreferredWidth(columnWidths[i]);

            column.setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    if (!isSelected) {
                        c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));
                    }
                    
                    // Làm cho cột mã khách hàng (cột đầu tiên) trông như bị disable
                    if (column == 0) {
                        c.setForeground(Color.GRAY);
                        c.setBackground(new Color(240, 240, 240));
                        setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                    } else {
                        c.setForeground(Color.BLACK);
                        setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                    }
                    
                    setHorizontalAlignment(column == 0 ? JLabel.CENTER : JLabel.LEFT);
                    return c;
                }
            });
        }
    }

    private void hienThiThongTinKhachHang(int row) {
        txtMaKhachHang.setText(model.getValueAt(row, 0) != null ? model.getValueAt(row, 0).toString() : "");
        txtTenKhachHang.setText(model.getValueAt(row, 1) != null ? model.getValueAt(row, 1).toString() : "");
        txtEmail.setText(model.getValueAt(row, 2) != null ? model.getValueAt(row, 2).toString() : "");
        txtSoDienThoai.setText(model.getValueAt(row, 3) != null ? model.getValueAt(row, 3).toString() : "");
        
        // Xử lý hiển thị ngày sinh từ chuỗi sang Date
        try {
            String ngaySinhStr = model.getValueAt(row, 4) != null ? model.getValueAt(row, 4).toString() : "";
            java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy");
            if (!ngaySinhStr.isEmpty()) {
                Date ngaySinh = dateFormat.parse(ngaySinhStr);
                spnNgaySinh.setValue(ngaySinh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        txtDiaChi.setText(model.getValueAt(row, 5) != null ? model.getValueAt(row, 5).toString() : "");
        cmbTrangThai.setSelectedItem(model.getValueAt(row, 6) != null ? model.getValueAt(row, 6).toString() : "");
    }

    private JPanel taoButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(mauNen);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        btnSua = taoStyledButton("Sửa", new Color(52, 152, 219));

        // Thêm sự kiện cho nút Sửa


        buttonPanel.add(btnSua);

        return buttonPanel;
    }

    private void capNhatThongTinKhachHang() {
        // Xử lý cập nhật thông tin khách hàng
        int selectedRow = tblKhachHang.getSelectedRow();
        if (selectedRow >= 0) {
            // Thực hiện cập nhật thông tin từ form vào model
            // Và lưu vào cơ sở dữ liệu
            JOptionPane.showMessageDialog(this, "Cập nhật thông tin khách hàng thành công!");
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần cập nhật!", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }

    private JButton taoStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(150, 45));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));



        return button;
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

        SwingUtilities.invokeLater(() -> new view_KhachHang("ND01", "AD01"));
    }
}