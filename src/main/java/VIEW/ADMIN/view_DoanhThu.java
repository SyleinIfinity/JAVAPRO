package VIEW.ADMIN;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import CONTROLLER.APP.ADMIN.ctl_DoanhThu;
import VIEW.view_main;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class view_DoanhThu extends JPanel{
    public JTable tableDoanhThu;
    // public DefaultTableModel tableModel;
    public JSpinner dateFrom, dateTo;
    public JComboBox<String> cboLoaiThoiGian, cboChiNhanh;
    public JButton btnTraCuu, btnXuatExcel;
    public JLabel lblTongDoanhThu;
    private Color mauChinh = new Color(41, 128, 185);
    private Color mauPhu = new Color(52, 152, 219);
    private Color mauNhan = new Color(230, 126, 34);
    private Color mauNen = new Color(236, 240, 241);
    public String maNguoiDung;
    public String maVaiTro;
    view_main vMain;
    ctl_DoanhThu controller;
    
    public view_DoanhThu(view_main vMain) {
        setLayout(new BorderLayout());
        setBackground(mauNen);
        this.vMain = vMain;

        
        initComponents();
        controller = new ctl_DoanhThu(this, vMain);
    }
    
    private void initComponents() {
        // Panel chính
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(mauNen);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Panel tiêu đề
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(mauNen);
        JLabel lblTitle = new JLabel("QUẢN LÝ DOANH THU");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(mauChinh);
        titlePanel.add(lblTitle);
        
        // Panel nội dung chứa topPanel và scrollPane
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBackground(mauNen);
        
        // Panel chứa searchPanel và toolPanel
        JPanel topPanel = new JPanel(new BorderLayout(15, 0));
        topPanel.setBackground(mauNen);
        
        // Panel tìm kiếm
        JPanel searchPanel = new JPanel(new GridLayout(2, 1, 0, 15));
        searchPanel.setBackground(mauNen);
        searchPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(mauChinh, 2),
            "Tìm kiếm",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            null,
            mauChinh
        ));
        
        // Panel cho loại thời gian và từ ngày
        JPanel panelRow1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        panelRow1.setBackground(mauNen);
        
        // Panel cho chi nhánh và đến ngày
        JPanel panelRow2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        panelRow2.setBackground(mauNen);
        
        // Thêm date spinner từ ngày
        JLabel lblTuNgay = new JLabel("Từ ngày:");
        lblTuNgay.setForeground(mauChinh);
        lblTuNgay.setPreferredSize(new Dimension(100, 30));
        panelRow1.add(lblTuNgay);
        
        SpinnerDateModel modelFrom = new SpinnerDateModel();
        dateFrom = new JSpinner(modelFrom);
        dateFrom.setEditor(new JSpinner.DateEditor(dateFrom, "dd/MM/yyyy HH:mm:ss"));
        dateFrom.setPreferredSize(new Dimension(200, 30));
        dateFrom.setBackground(Color.WHITE);
        panelRow1.add(dateFrom);
        
        // Thêm date spinner đến ngày
        JLabel lblDenNgay = new JLabel("Đến ngày:");
        lblDenNgay.setForeground(mauChinh);
        lblDenNgay.setPreferredSize(new Dimension(100, 30));
        panelRow2.add(lblDenNgay);
        
        SpinnerDateModel modelTo = new SpinnerDateModel();
        dateTo = new JSpinner(modelTo);
        dateTo.setEditor(new JSpinner.DateEditor(dateTo, "dd/MM/yyyy HH:mm:ss"));
        dateTo.setPreferredSize(new Dimension(200, 30));
        dateTo.setBackground(Color.WHITE);
        panelRow2.add(dateTo);
        
        // Thêm combobox chi nhánh
        JLabel lblChiNhanh = new JLabel("Chi nhánh:");
        lblChiNhanh.setForeground(mauChinh);
        lblChiNhanh.setPreferredSize(new Dimension(100, 30));
        panelRow2.add(lblChiNhanh);
        
        String[] defaultChiNhanh = {"Tất cả"};
        cboChiNhanh = new JComboBox<>(defaultChiNhanh);
        cboChiNhanh.setPreferredSize(new Dimension(200, 30));        
        cboChiNhanh.setBackground(Color.WHITE);
        panelRow2.add(cboChiNhanh);
        
        // Thêm các panel vào searchPanel
        searchPanel.add(panelRow1);
        searchPanel.add(panelRow2);
        
        // Panel công cụ
        JPanel toolPanel = new JPanel(new GridLayout(2, 1, 0, 15));
        toolPanel.setBackground(mauNen);
        toolPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(mauChinh, 2),
            "Công cụ",
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
            javax.swing.border.TitledBorder.DEFAULT_POSITION,
            null,
            mauChinh
        ));
        
        // Panel cho nút tra cứu
        JPanel traCuuPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        traCuuPanel.setBackground(mauNen);
        btnTraCuu = new JButton("Tra cứu");
        styleButton(btnTraCuu);
        traCuuPanel.add(btnTraCuu);
        
        // Panel cho nút xuất excel
        JPanel xuatExcelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        xuatExcelPanel.setBackground(mauNen);
        btnXuatExcel = new JButton("Xuất Excel");
        styleButton(btnXuatExcel);
        xuatExcelPanel.add(btnXuatExcel);
        
        toolPanel.add(traCuuPanel);
        toolPanel.add(xuatExcelPanel);
        
        // Thêm searchPanel và toolPanel vào topPanel
        topPanel.add(searchPanel, BorderLayout.CENTER);
        topPanel.add(toolPanel, BorderLayout.EAST);
        
        // Bảng doanh thu
        String[] columns = {"Mã hóa đơn", "khách đặt", "Số người", "ngày thuê", "ngày trả", "nhân viên phụ trách", "tổng tiền"};
        // tableModel = new DefaultTableModel(columns, 0) {
        //     @Override
        //     public boolean isCellEditable(int row, int column) {
        //         return false;
        //     }
        // };
        tableDoanhThu = new JTable(new DefaultTableModel(columns, 0));
        tableDoanhThu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        styleTable(tableDoanhThu);
        
        JScrollPane scrollPane = new JScrollPane(tableDoanhThu);
        scrollPane.setBorder(BorderFactory.createLineBorder(mauChinh, 2));
        
        // Panel tổng doanh thu
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.setBackground(mauNen);
        lblTongDoanhThu = new JLabel("Tổng doanh thu: 0 VNĐ");
        lblTongDoanhThu.setFont(new Font("Arial", Font.BOLD, 14));
        lblTongDoanhThu.setForeground(mauNhan);
        totalPanel.add(lblTongDoanhThu);
        
        // Thêm các panel vào frame
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        contentPanel.add(topPanel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(totalPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void styleButton(JButton button) {
        button.setPreferredSize(new Dimension(160, 40));
        button.setBackground(mauChinh);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 15));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(mauPhu);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(mauChinh);
            }
        });
    }
    
    private void styleTable(JTable table) {
        // Style cho header
        JTableHeader header = table.getTableHeader();
        header.setBackground(mauChinh);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 12));
        
        // Style cho table
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setSelectionBackground(mauPhu);
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(mauChinh);
        table.setShowGrid(true);
        
        // Căn giữa các cột
        table.getColumnModel().getColumn(0).setPreferredWidth(50);  // STT
        table.getColumnModel().getColumn(1).setPreferredWidth(100); // Ngày
        table.getColumnModel().getColumn(2).setPreferredWidth(150); // Mã hóa đơn
        table.getColumnModel().getColumn(3).setPreferredWidth(150); // Số tiền
        table.getColumnModel().getColumn(4).setPreferredWidth(200); // Ghi chú
        
        // Căn giữa cho các cột
        javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }
}