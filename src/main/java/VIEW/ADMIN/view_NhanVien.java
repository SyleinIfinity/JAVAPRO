package VIEW.ADMIN;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import CONTROLLER.APP.ADMIN.ctl_NhanVien;
import VIEW.view_main;
import MODEL.DAO.NguoiDungDAO;
import MODEL.DAO.VaiTroDAO;
import MODEL.ENTITY.NguoiDung;
import MODEL.ENTITY.VaiTro;

import java.awt.*;
import java.awt.event.*;

public class view_NhanVien extends JPanel {
    private JPanel pnForm;
    private JTable tblNhanVien;
    private JTextField txtMaNhanVien, txtTenNhanVien, txtNgaySinh, txtSDT, txtEmail, txtMatKhau, txtTrangThai;
    private JButton btnThem, btnSua, btnXoa, btnTimKiem;
    private DefaultTableModel model;
    private Color mauChinh = new Color(219, 112, 147);
    private Color mauPhu = new Color(241, 142, 172);
    private Color mauNhan = new Color(255, 105, 180);
    private Color mauNen = new Color(255, 228, 235);
    public String maNguoiDung;
    public String maVaiTro;
    view_main vMain;
    
    // Thêm DAO
    private NguoiDungDAO nguoiDungDAO;
    private VaiTroDAO vaiTroDAO;

    public view_NhanVien(view_main vMain) {
        setLayout(new BorderLayout());
        setBackground(mauNen);
        this.vMain = vMain;
        
        // Khởi tạo DAO
        initializeDAO();

        JPanel headerPanel = taoHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBackground(mauNen);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        JPanel searchPanel = taoSearchFilterPanel();
        contentPanel.add(searchPanel, BorderLayout.NORTH);

        JPanel tablePanel = taoTablePanel();
        contentPanel.add(tablePanel, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);

        JPanel buttonPanel = taoButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        // Load dữ liệu từ database thay vì dữ liệu mẫu
        loadDataFromDatabase();

        setVisible(true);
        new ctl_NhanVien(this);
    }
    
    // Khởi tạo DAO
    private void initializeDAO() {
        nguoiDungDAO = new NguoiDungDAO();
        vaiTroDAO = new VaiTroDAO();
    }

    private JPanel taoHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(mauChinh);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel lblTitle = new JLabel("QUẢN LÝ NHÂN VIÊN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);

        headerPanel.add(lblTitle, BorderLayout.CENTER);
        return headerPanel;
    }

    private JPanel taoSearchFilterPanel() {
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBackground(mauNen);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JPanel searchInputPanel = new JPanel(new BorderLayout(10, 0));
        searchInputPanel.setBackground(mauNen);

        JLabel lblSearch = new JLabel("Tìm kiếm:");
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JTextField txtTimKiem = new JTextField();
        txtTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtTimKiem.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        searchInputPanel.add(lblSearch, BorderLayout.WEST);
        searchInputPanel.add(txtTimKiem, BorderLayout.CENTER);

        JPanel filterPanel = new JPanel(new BorderLayout(10, 0));
        filterPanel.setBackground(mauNen);

        searchPanel.add(searchInputPanel, BorderLayout.CENTER);
        searchPanel.add(filterPanel, BorderLayout.EAST);

        return searchPanel;
    }

    private JPanel taoTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(mauNen);

        String[] columnNames = {"Mã nhân viên", "Tên nhân viên", "Ngày sinh", "Số điện thoại", "Email", "Trạng thái"};
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Cột 0 (Mã nhân viên) không chỉnh sửa, các cột còn lại chỉnh sửa được
                return column != 0;
            }
        };

        tblNhanVien = new JTable(model);
        tblNhanVien.setRowHeight(40);
        tblNhanVien.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblNhanVien.setSelectionBackground(new Color(241, 196, 15, 100));
        tblNhanVien.setSelectionForeground(Color.BLACK);
        tblNhanVien.setShowGrid(true);
        tblNhanVien.setGridColor(new Color(189, 195, 199));
        tblNhanVien.setRowSelectionAllowed(true);
        tblNhanVien.setColumnSelectionAllowed(false);
        tblNhanVien.setFillsViewportHeight(true);

        JTableHeader header = tblNhanVien.getTableHeader();
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

        JScrollPane scrollPane = new JScrollPane(tblNhanVien);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        return tablePanel;
    }

    private void caiDatTableColumns() {
        int[] columnWidths = {120, 200, 100, 140, 200, 140};
        TableColumnModel columnModel = tblNhanVien.getColumnModel();

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
                    setHorizontalAlignment(column == 0 ? JLabel.CENTER : JLabel.LEFT);
                    return c;
                }
            });
        }

        // Gán editor cho từng cột
        columnModel.getColumn(1).setCellEditor(taoTextFieldEditor()); // Tên nhân viên
        columnModel.getColumn(2).setCellEditor(taoTextFieldEditor()); // Ngày sinh
        columnModel.getColumn(3).setCellEditor(taoTextFieldEditor()); // Số điện thoại
        columnModel.getColumn(4).setCellEditor(taoTextFieldEditor()); // Email

        // Trạng thái dùng JComboBox
        String[] trangThaiOptions = {"Đang hoạt động", "Không hoạt động", "Bị khóa"};
        columnModel.getColumn(5).setCellEditor(taoStyledComboBoxEditor(trangThaiOptions));
    }

    private DefaultCellEditor taoTextFieldEditor() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(2, 10, 2, 10)
        ));
        return new DefaultCellEditor(textField);
    }

    private DefaultCellEditor taoStyledComboBoxEditor(String[] options) {
        JComboBox<String> comboBox = new JComboBox<>(options);
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setBackground(Color.WHITE);
        DefaultCellEditor editor = new DefaultCellEditor(comboBox);
        editor.setClickCountToStart(1);
        return editor;
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

        buttonPanel.add(btnThem);
        buttonPanel.add(btnSua);
        buttonPanel.add(btnXoa);
        buttonPanel.add(btnTimKiem);

        return buttonPanel;
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

    // ========== CÁC PHƯƠNG THỨC LOAD DỮ LIỆU TỪ DATABASE ==========
    
    // Phương thức load dữ liệu từ database vào bảng
    public void loadDataFromDatabase() {
        try {
            // Khởi tạo DAO nếu chưa có
            if (nguoiDungDAO == null) {
                nguoiDungDAO = new NguoiDungDAO();
            }
            if (vaiTroDAO == null) {
                vaiTroDAO = new VaiTroDAO();
            }
            
            // Xóa dữ liệu cũ trong bảng
            model.setRowCount(0);
            
            // Lấy dữ liệu từ DAO và đổ vào bảng
            for (NguoiDung nd : nguoiDungDAO.getListNGUOIDUNG().values()) {
                if (nd.getMaVaiTro().equals("VT002")) {
                    
                                    // Chuyển đổi trạng thái từ int sang String
                                    String trangThai;
                                    switch (nd.isTrangThai()) {
                                        case 1:
                                            trangThai = "Đang hoạt động";
                                            break;
                                        case 0:
                                            trangThai = "Không hoạt động";
                                            break;
                                        case -1:
                                            trangThai = "Bị khóa";
                                            break;
                                        default:
                                            trangThai = "Không xác định";
                                            break;
                                    }
                                    
                                    // Tạo dòng dữ liệu mới
                                    Object[] row = {
                                        nd.getMaNguoiDung(),        // Mã nhân viên
                                        nd.getTenNguoiDung(),       // Tên nhân viên
                                        nd.getNgaySinh(),           // Ngày sinh
                                        nd.getSDT(),                // Số điện thoại
                                        nd.getEmail(),              // Email
                                        trangThai                   // Trạng thái
                                    };
                    
                                    // Thêm dòng vào model
                                    model.addRow(row);
                }
                
            }
            
            // Thông báo load thành công
            System.out.println("Đã load " + model.getRowCount() + " nhân viên vào bảng.");
            
            // Refresh table để hiển thị dữ liệu mới
            tblNhanVien.revalidate();
            tblNhanVien.repaint();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Lỗi khi tải dữ liệu từ database: " + e.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // Phương thức refresh dữ liệu (tải lại từ database)
    public void refreshData() {
        try {
            // Tạo lại DAO để lấy dữ liệu mới nhất
            nguoiDungDAO = new NguoiDungDAO();
            vaiTroDAO = new VaiTroDAO();
            
            // Load lại dữ liệu
            loadDataFromDatabase();
            
            JOptionPane.showMessageDialog(this, 
                "Đã làm mới dữ liệu thành công!", 
                "Thông báo", 
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Lỗi khi làm mới dữ liệu: " + e.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // Phương thức lấy NguoiDung được chọn trong bảng
    public NguoiDung getSelectedNguoiDung() {
        int selectedRow = tblNhanVien.getSelectedRow();
        if (selectedRow == -1) {
            return null;
        }
        
        // Lấy mã nhân viên từ dòng được chọn
        String maNguoiDung = (String) model.getValueAt(selectedRow, 0);
        
        // Trả về đối tượng NguoiDung
        return nguoiDungDAO.getNguoiDung(maNguoiDung);
    }

    // ========== GETTER METHODS ==========
    
    public JTable getTblNhanVien() {
        return tblNhanVien;
    }

    public DefaultTableModel getModel() {
        return model;
    }

    public JButton getBtnThem() {
        return btnThem;
    }

    public JButton getBtnSua() {
        return btnSua;
    }

    public JButton getBtnXoa() {
        return btnXoa;
    }

    public JButton getBtnTimKiem() {
        return btnTimKiem;
    }
    
    public NguoiDungDAO getNguoiDungDAO() {
        return nguoiDungDAO;
    }

    public VaiTroDAO getVaiTroDAO() {
        return vaiTroDAO;
    }
}