package VIEW.ADMIN;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import MODEL.DAO.PhongDAO;
import MODEL.ENTITY.Phong;

public class view_Phong extends JFrame {
    private JPanel pnForm;
    private JTable tblPhong;
    private JTextField txtMaPhong, txtTenPhong, txtLoaiPhong, txtGiaPhong, txtTrangThai;
    private JButton btnThem, btnSua, btnXoa, btnTimKiem;
    private DefaultTableModel model;
    private PhongDAO phongDAO;
    private Color mauChinh = new Color(41, 128, 185);
    private Color mauPhu = new Color(52, 152, 219);
    private Color mauNhan = new Color(230, 126, 34);
    private Color mauNen = new Color(236, 240, 241);
    public String maNguoiDung;
    public String maVaiTro;

    public view_Phong(String maNguoiDung, String maVaiTro) {
        setTitle("Quản lý Phòng");
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

        JPanel searchPanel = taoSearchFilterPanel();
        contentPanel.add(searchPanel, BorderLayout.NORTH);

        JPanel tablePanel = taoTablePanel();
        contentPanel.add(tablePanel, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);

        JPanel buttonPanel = taoButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        themDuLieuMau();

        setVisible(true);
    }
    
    private JPanel taoHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(mauChinh);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel lblTitle = new JLabel("QUẢN LÝ PHÒNG", SwingConstants.CENTER);
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

        JLabel lblFilter = new JLabel("Lọc theo:");
        lblFilter.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JComboBox<String> cmbLoc = new JComboBox<>(new String[]{"Tất cả", "Trống", "Đã đặt", "Đang sử dụng"});
        cmbLoc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbLoc.setBackground(Color.WHITE);

        filterPanel.add(lblFilter, BorderLayout.WEST);
        filterPanel.add(cmbLoc, BorderLayout.CENTER);

        searchPanel.add(searchInputPanel, BorderLayout.CENTER);
        searchPanel.add(filterPanel, BorderLayout.EAST);

        return searchPanel;
    }

    private JPanel taoTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(mauNen);

        String[] columnNames = {"Mã phòng", "Tên phòng", "Tầng", "Loại phòng", "Tình trạng"};
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };

        tblPhong = new JTable(model);
        tblPhong.setRowHeight(40);
        tblPhong.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblPhong.setSelectionBackground(new Color(241, 196, 15, 100));
        tblPhong.setSelectionForeground(Color.BLACK);
        tblPhong.setShowGrid(true);
        tblPhong.setGridColor(new Color(189, 195, 199));
        tblPhong.setRowSelectionAllowed(true);
        tblPhong.setColumnSelectionAllowed(false);
        tblPhong.setFillsViewportHeight(true);

        JTableHeader header = tblPhong.getTableHeader();
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

        JScrollPane scrollPane = new JScrollPane(tblPhong);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        scrollPane.getViewport().setBackground(Color.WHITE);

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        return tablePanel;
    }

    private void caiDatTableColumns() {
        int[] columnWidths = {100, 200, 80, 150, 150};
        TableColumnModel columnModel = tblPhong.getColumnModel();

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
                    setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                    setHorizontalAlignment(column == 0 ? JLabel.CENTER : JLabel.LEFT);
                    return c;
                }
            });
        }

        String[] tangOptions = {"1", "2", "3", "4", "5"};
        String[] loaiPhongOptions = {"Đơn", "Đôi", "VIP"};
        String[] tinhTrangOptions = {"Trống", "Đã đặt", "Đang sử dụng"};

        JTextField txtTenPhong = new JTextField();
        txtTenPhong.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtTenPhong.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(2, 10, 2, 10)
        ));
        columnModel.getColumn(1).setCellEditor(new DefaultCellEditor(txtTenPhong));

        columnModel.getColumn(2).setCellEditor(taoStyledComboBoxEditor(tangOptions));
        columnModel.getColumn(3).setCellEditor(taoStyledComboBoxEditor(loaiPhongOptions));
        columnModel.getColumn(4).setCellEditor(taoStyledComboBoxEditor(tinhTrangOptions));
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

    private void themDuLieuMau() {
        // Thêm dữ liệu mẫu nếu cần
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new view_Phong("ND01", "AD01"));
    }
}