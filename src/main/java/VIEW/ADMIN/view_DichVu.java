package VIEW.ADMIN;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import CONTROLLER.APP.ADMIN.ctl_DichVu; 
import VIEW.view_main;

import java.awt.*;
import java.awt.event.*;

public class view_DichVu extends JPanel {
    private JPanel pnForm;
    // Đổi từ private thành public để controller có thể truy cập
    public JTable tblDichVu;
    private JTextField txtMaDichVu, txtTenDichVu, txtGiaDichVu, txtMoTa;
    // Đổi từ private thành public để controller có thể truy cập
    public JButton btnThem, btnSua, btnXoa, btnTimKiem;
    private DefaultTableModel model;
    private Color mauChinh = new Color(219, 112, 147);
    private Color mauPhu = new Color(241, 142, 172);
    private Color mauNhan = new Color(255, 105, 180);
    private Color mauNen = new Color(255, 228, 235);
    public String maNguoiDung;
    public String maVaiTro;
    view_main vMain;
    
    // Thêm controller
    private ctl_DichVu controller;

    public view_DichVu(view_main vMain) {
        setLayout(new BorderLayout());
        setBackground(mauNen);
        this.vMain = vMain;

        JPanel headerPanel = taoHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBackground(mauNen);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel tablePanel = taoTablePanel();
        contentPanel.add(tablePanel, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);

        JPanel buttonPanel = taoButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        // Khởi tạo controller sau khi tạo xong giao diện
        controller = new ctl_DichVu(this);

        setVisible(true);
    }

    private JPanel taoHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(mauChinh);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel lblTitle = new JLabel("QUẢN LÝ DỊCH VỤ", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);

        headerPanel.add(lblTitle, BorderLayout.CENTER);
        return headerPanel;
    }

    private JPanel taoTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(mauNen);

        String[] columnNames = {"Mã dịch vụ", "Tên dịch vụ", "Giá dịch vụ", "Mô tả"};
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Tất cả các cột đều không thể chỉnh sửa trực tiếp trên bảng
                // Sử dụng dialog để chỉnh sửa
                return false;
            }
        };

        tblDichVu = new JTable(model);
        tblDichVu.setRowHeight(40);
        tblDichVu.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblDichVu.setSelectionBackground(new Color(241, 196, 15, 100));
        tblDichVu.setSelectionForeground(Color.BLACK);
        tblDichVu.setShowGrid(true);
        tblDichVu.setGridColor(new Color(189, 195, 199));
        tblDichVu.setRowSelectionAllowed(true);
        tblDichVu.setColumnSelectionAllowed(false);
        tblDichVu.setFillsViewportHeight(true);
        tblDichVu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader header = tblDichVu.getTableHeader();
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

        caiDatTableColumns();

        JScrollPane scrollPane = new JScrollPane(tblDichVu);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        return tablePanel;
    }

    private void caiDatTableColumns() {
        int[] columnWidths = {120, 200, 150, 200};
        TableColumnModel columnModel = tblDichVu.getColumnModel();

        for (int i = 0; i < columnWidths.length; i++) {
            TableColumn col = columnModel.getColumn(i);
            col.setPreferredWidth(columnWidths[i]);
            col.setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                        boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(
                            table, value, isSelected, hasFocus, row, column);
                    if (!isSelected) {
                        c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));
                    }
                    setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                    
                    // Căn giữa cột mã dịch vụ, căn trái các cột khác
                    if (column == 0) {
                        setHorizontalAlignment(JLabel.CENTER);
                    } else if (column == 2) { // Cột giá - căn phải
                        setHorizontalAlignment(JLabel.RIGHT);
                    } else {
                        setHorizontalAlignment(JLabel.LEFT);
                    }
                    
                    return c;
                }
            });
        }
    }

    private JPanel taoButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(mauNen);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        btnThem = taoStyledButton("Thêm", new Color(46, 204, 113));
        btnSua = taoStyledButton("Sửa", new Color(52, 152, 219));
        btnXoa = taoStyledButton("Xóa", new Color(231, 76, 60));
        btnTimKiem = taoStyledButton("Tìm Kiếm", new Color(149, 165, 166));

        // Thêm tooltip cho buttons
        btnThem.setToolTipText("Thêm dịch vụ mới");
        btnSua.setToolTipText("Sửa dịch vụ đã chọn");
        btnXoa.setToolTipText("Xóa dịch vụ đã chọn");
        btnTimKiem.setToolTipText("Tìm kiếm dịch vụ");

        buttonPanel.add(btnThem);
        buttonPanel.add(btnSua);
        buttonPanel.add(btnXoa);
        buttonPanel.add(btnTimKiem);

        // Thêm nút làm mới
        JButton btnLamMoi = taoStyledButton("Làm mới", new Color(155, 89, 182));
        btnLamMoi.setToolTipText("Làm mới dữ liệu");
        btnLamMoi.addActionListener(e -> {
            if (controller != null) {
                controller.refreshData();
                JOptionPane.showMessageDialog(this, "Đã làm mới dữ liệu!", 
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        buttonPanel.add(btnLamMoi);

        return buttonPanel;
    }

    private JButton taoStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(120, 45));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(toiMau(bgColor, 0.1f));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        return button;
    }

    private Color toiMau(Color color, float fraction) {
        int red = Math.max(0, Math.round(color.getRed() * (1 - fraction)));
        int green = Math.max(0, Math.round(color.getGreen() * (1 - fraction)));
        int blue = Math.max(0, Math.round(color.getBlue() * (1 - fraction)));
        return new Color(red, green, blue);
    }

    // Phương thức để controller có thể truy cập DefaultTableModel
    public DefaultTableModel getTableModel() {
        return model;
    }

    // Phương thức để làm mới dữ liệu từ bên ngoài
    public void refreshData() {
        if (controller != null) {
            controller.refreshData();
        }
    }

    // Phương thức để lấy controller (nếu cần)
    public ctl_DichVu getController() {
        return controller;
    }
}