package GUI.STAFF;

import javax.swing.*;
import javax.swing.table.*;

import BLL.STAFF.ctl_TrangThaiPhong;

import java.awt.*;
import java.awt.event.*;

import DLL.DO.PhongDAO;
import GUI.view_main;

public class view_TrangThaiPhong extends JPanel {
    private JTable tblPhong;
    private JButton btnSua, btnTimKiem, btnRefresh;
    private JTextField txtTim;
    private JComboBox<String> cmbFilter;
    private DefaultTableModel model;
    private PhongDAO phongDAO;
    private ctl_TrangThaiPhong controller;

    private final Color mauChinh = new Color(179, 157, 219);
    private final Color mauPhu = new Color(209, 196, 233);
    private final Color mauNhan = new Color(149, 117, 205);
    private final Color mauNen = new Color(243, 229, 245);

    private final Color mauBtnSua = new Color(52, 152, 219);
    private final Color mauBtnTimKiem = new Color(149, 165, 166);
    private final Color mauBtnRefresh = new Color(46, 204, 113);

    private view_main vMain;

    public view_TrangThaiPhong(view_main vMain) {
        setLayout(new BorderLayout());
        setBackground(mauNen);
        this.vMain = vMain;

        // Initialize components first
        initializeComponents();
        
        // Initialize controller
        this.controller = new ctl_TrangThaiPhong(this);
        
        // Setup event listeners
        setupEventListeners();
        
        setVisible(true);
    }
    
    private void initializeComponents() {
        add(taoHeaderPanel(), BorderLayout.NORTH);
        add(taoContentPanel(), BorderLayout.CENTER);
        add(taoButtonPanel(), BorderLayout.SOUTH);
    }
    
    private void setupEventListeners() {
        // Search button event
        btnTimKiem.addActionListener(e -> {
            String keyword = txtTim.getText().trim();
            controller.timKiemPhong(keyword);
        });
        
        // Enter key for search
        txtTim.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String keyword = txtTim.getText().trim();
                    controller.timKiemPhong(keyword);
                }
            }
        });
        
        // Filter combo box event
        cmbFilter.addActionListener(e -> {
            String selectedFilter = (String) cmbFilter.getSelectedItem();
            controller.locTheoTrangThai(selectedFilter);
        });
        
        // Edit button event
        btnSua.addActionListener(e -> {
            capNhatTrangThaiPhong();
        });
        
        // Refresh button event
        btnRefresh.addActionListener(e -> {
            controller.refreshData();
            txtTim.setText("");
            cmbFilter.setSelectedIndex(0);
            JOptionPane.showMessageDialog(this, "Dữ liệu đã được làm mới!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        });
        
        // Table selection event
        tblPhong.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                btnSua.setEnabled(tblPhong.getSelectedRow() != -1);
            }
        });
    }

    private JPanel taoHeaderPanel() {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setBackground(mauChinh);
        pnl.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel lbl = new JLabel("QUẢN LÝ TRẠNG THÁI PHÒNG", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lbl.setForeground(Color.WHITE);

        pnl.add(lbl, BorderLayout.CENTER);
        return pnl;
    }

    private JPanel taoContentPanel() {
        JPanel pnl = new JPanel(new BorderLayout(15, 15));
        pnl.setBackground(mauNen);
        pnl.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        pnl.add(taoSearchFilterPanel(), BorderLayout.NORTH);
        pnl.add(taoTablePanel(), BorderLayout.CENTER);
        return pnl;
    }

    private JPanel taoSearchFilterPanel() {
        JPanel pnl = new JPanel(new BorderLayout(10, 0));
        pnl.setBackground(mauNen);
        pnl.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JPanel searchInput = new JPanel(new BorderLayout(10, 0));
        searchInput.setBackground(mauNen);

        JLabel lblSearch = new JLabel("Tìm kiếm:");
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));

        txtTim = new JTextField();
        txtTim.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtTim.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        txtTim.setToolTipText("Nhập mã phòng, số phòng hoặc loại phòng để tìm kiếm");

        searchInput.add(lblSearch, BorderLayout.WEST);
        searchInput.add(txtTim, BorderLayout.CENTER);

        JPanel filter = new JPanel(new BorderLayout(10, 0));
        filter.setBackground(mauNen);

        JLabel lblFilter = new JLabel("Lọc theo:");
        lblFilter.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Updated filter options to match new status values
        cmbFilter = new JComboBox<>(new String[]{"Tất cả", "Trống", "Đã đặt trước", "Có người ở", "Bảo trì"});
        cmbFilter.setSelectedIndex(0);
        cmbFilter.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        filter.add(lblFilter, BorderLayout.WEST);
        filter.add(cmbFilter, BorderLayout.CENTER);

        pnl.add(searchInput, BorderLayout.CENTER);
        pnl.add(filter, BorderLayout.EAST);
        return pnl;
    }

    private JPanel taoTablePanel() {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setBackground(mauNen);

        String[] cols = {"Mã phòng", "Số phòng", "Tầng", "Loại phòng", "Tình trạng"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                // Only allow editing the status column
                return c == 4;
            }
        };

        tblPhong = new JTable(model);
        tblPhong.setRowHeight(40);
        tblPhong.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblPhong.setGridColor(new Color(200, 200, 200));
        tblPhong.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader hdr = tblPhong.getTableHeader();
        hdr.setPreferredSize(new Dimension(hdr.getWidth(), 45));
        hdr.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
                JLabel lb = new JLabel(v.toString(), SwingConstants.CENTER);
                lb.setFont(new Font("Segoe UI", Font.BOLD, 14));
                lb.setOpaque(true);
                lb.setBackground(mauChinh);
                lb.setForeground(Color.WHITE);
                lb.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, mauNhan));
                return lb;
            }
        });

        caiDatTableColumns();

        JScrollPane sp = new JScrollPane(tblPhong);
        sp.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        pnl.add(sp, BorderLayout.CENTER);
        return pnl;
    }

    private void caiDatTableColumns() {
        int[] w = {100, 150, 80, 200, 150};
        TableColumnModel cm = tblPhong.getColumnModel();

        for (int i = 0; i < w.length; i++) {
            TableColumn col = cm.getColumn(i);
            col.setPreferredWidth(w[i]);

            col.setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    label.setHorizontalAlignment(column == 0 || column == 2 ? CENTER : LEFT);
                    label.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                    label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    label.setOpaque(true);
                    
                    if (isSelected) {
                        label.setBackground(new Color(255, 204, 102));
                        label.setForeground(Color.BLACK);
                    } else {
                        // Updated color coding for new status values
                        if (column == 4 && value != null) {
                            String status = value.toString();
                            switch (status) {
                                case "Trống":
                                    label.setBackground(new Color(212, 237, 218)); // Light green
                                    label.setForeground(new Color(21, 87, 36));     // Dark green
                                    break;
                                case "Đã đặt trước":
                                    label.setBackground(new Color(255, 243, 205)); // Light yellow
                                    label.setForeground(new Color(133, 100, 4));   // Dark yellow
                                    break;
                                case "Có người ở":
                                    label.setBackground(new Color(248, 215, 218)); // Light red
                                    label.setForeground(new Color(114, 28, 36));   // Dark red
                                    break;
                                case "Bảo trì":
                                    label.setBackground(new Color(229, 229, 229)); // Light gray
                                    label.setForeground(new Color(64, 64, 64));    // Dark gray
                                    break;
                                default:
                                    label.setBackground(Color.WHITE);
                                    label.setForeground(Color.BLACK);
                            }
                        } else {
                            label.setBackground(Color.WHITE);
                            label.setForeground(Color.BLACK);
                        }
                    }
                    return label;
                }
            });
        }

        // Updated combo box editor for status column with new options
        String[] tinhTrang = {"Trống", "Đã đặt trước", "Có người ở", "Bảo trì"};
        cm.getColumn(4).setCellEditor(new DefaultCellEditor(new JComboBox<>(tinhTrang)));
    }

    private JPanel taoButtonPanel() {
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        pnl.setBackground(mauNen);
        pnl.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        btnSua = taoStyledButton("Cập nhật", mauBtnSua);
        btnSua.setEnabled(false); // Initially disabled
        
        btnTimKiem = taoStyledButton("Tìm Kiếm", mauBtnTimKiem);
        btnRefresh = taoStyledButton("Làm mới", mauBtnRefresh);

        pnl.add(btnSua);
        pnl.add(btnTimKiem);
        pnl.add(btnRefresh);
        return pnl;
    }

    private JButton taoStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(150, 45));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { 
                if (btn.isEnabled()) {
                    btn.setBackground(toiMau(bg, 0.1f)); 
                }
            }
            @Override public void mouseExited(MouseEvent e) { 
                if (btn.isEnabled()) {
                    btn.setBackground(bg); 
                }
            }
        });
        return btn;
    }

    private Color toiMau(Color c, float frac) {
        int r = Math.max(0, Math.round(c.getRed() * (1 - frac)));
        int g = Math.max(0, Math.round(c.getGreen() * (1 - frac)));
        int b = Math.max(0, Math.round(c.getBlue() * (1 - frac)));
        return new Color(r, g, b);
    }
    
    private void capNhatTrangThaiPhong() {
        int selectedRow = tblPhong.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng cần cập nhật!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String maPhong = model.getValueAt(selectedRow, 0).toString();
        String trangThaiHienTai = model.getValueAt(selectedRow, 4).toString();
        
        // Use controller's available status options for consistency
        String[] options = controller.getAvailableStatusOptions();
        String trangThaiMoi = (String) JOptionPane.showInputDialog(
            this,
            "Chọn trạng thái mới cho phòng " + model.getValueAt(selectedRow, 1) + ":",
            "Cập nhật trạng thái phòng",
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            trangThaiHienTai
        );
        
        if (trangThaiMoi != null && !trangThaiMoi.equals(trangThaiHienTai)) {
            // Validate status before updating
            if (controller.isValidStatus(trangThaiMoi)) {
                boolean success = controller.capNhatTrangThaiPhong(maPhong, trangThaiMoi);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Cập nhật trạng thái phòng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật trạng thái phòng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Trạng thái không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // Getter methods for controller access
    public DefaultTableModel getTableModel() {
        return model;
    }
    
    public JTable getTable() {
        return tblPhong;
    }
    
    public ctl_TrangThaiPhong getController() {
        return controller;
    }
}