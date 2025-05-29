package VIEW.STAFF;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import MODEL.DAO.PhongDAO;
import VIEW.view_main;

public class view_TrangThaiPhong extends JPanel {
    private JTable tblPhong;
    private JButton btnSua, btnTimKiem;
    private DefaultTableModel model;
    private PhongDAO phongDAO;

    private final Color mauChinh = new Color(179, 157, 219);
    private final Color mauPhu = new Color(209, 196, 233);
    private final Color mauNhan = new Color(149, 117, 205);
    private final Color mauNen = new Color(243, 229, 245);

    private final Color mauBtnSua = new Color(52, 152, 219);
    private final Color mauBtnTimKiem = new Color(149, 165, 166);

    private view_main vMain;

    public view_TrangThaiPhong(view_main vMain) {
        setLayout(new BorderLayout());
        setBackground(mauNen);
        this.vMain = vMain;

        add(taoHeaderPanel(), BorderLayout.NORTH);
        add(taoContentPanel(), BorderLayout.CENTER);
        add(taoButtonPanel(), BorderLayout.SOUTH);

        // themDuLieuMau();
        setVisible(true);
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

        JTextField txtTim = new JTextField();
        txtTim.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtTim.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        searchInput.add(lblSearch, BorderLayout.WEST);
        searchInput.add(txtTim, BorderLayout.CENTER);

        JPanel filter = new JPanel(new BorderLayout(10, 0));
        filter.setBackground(mauNen);

        JLabel lblFilter = new JLabel("Lọc theo:");
        lblFilter.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JComboBox<String> cmb = new JComboBox<>(new String[]{"Tất cả", "Trống", "Đã đặt", "Đang sử dụng"});
        cmb.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        filter.add(lblFilter, BorderLayout.WEST);
        filter.add(cmb, BorderLayout.CENTER);

        pnl.add(searchInput, BorderLayout.CENTER);
        pnl.add(filter, BorderLayout.EAST);
        return pnl;
    }

    private JPanel taoTablePanel() {
        JPanel pnl = new JPanel(new BorderLayout());
        pnl.setBackground(mauNen);

        String[] cols = {"Mã phòng", "Tên phòng", "Tầng", "Loại phòng", "Tình trạng"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return "NV".equalsIgnoreCase(null) && c == 4;
            }
        };

        tblPhong = new JTable(model);
        tblPhong.setRowHeight(40);
        tblPhong.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblPhong.setGridColor(new Color(200, 200, 200));

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
        int[] w = {100, 200, 80, 150, 150};
        TableColumnModel cm = tblPhong.getColumnModel();

        for (int i = 0; i < w.length; i++) {
            TableColumn col = cm.getColumn(i);
            col.setPreferredWidth(w[i]);

            col.setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    label.setHorizontalAlignment(column == 0 ? CENTER : LEFT);
                    label.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                    label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    label.setOpaque(true);
                    if (isSelected) {
                        label.setBackground(new Color(255, 204, 102)); // màu vàng cam khi được chọn
                        label.setForeground(Color.BLACK);
                    } else {
                        label.setBackground(Color.WHITE); // mặc định là trắng
                        label.setForeground(Color.BLACK);
                    }
                    return label;
                }
            });
        }

        String[] tinhTrang = {"Trống", "Đã đặt", "Đang sử dụng"};
        cm.getColumn(4).setCellEditor(new DefaultCellEditor(new JComboBox<>(tinhTrang)));
    }

    private JPanel taoButtonPanel() {
        JPanel pnl = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        pnl.setBackground(mauNen);
        pnl.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        btnSua = taoStyledButton("Sửa", mauBtnSua);
        btnTimKiem = taoStyledButton("Tìm Kiếm", mauBtnTimKiem);

        pnl.add(btnSua);
        pnl.add(btnTimKiem);
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
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(toiMau(bg, 0.1f)); }
            @Override public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
        });
        return btn;
    }

    private Color toiMau(Color c, float frac) {
        int r = Math.max(0, Math.round(c.getRed() * (1 - frac)));
        int g = Math.max(0, Math.round(c.getGreen() * (1 - frac)));
        int b = Math.max(0, Math.round(c.getBlue() * (1 - frac)));
        return new Color(r, g, b);
    }

    // private void themDuLieuMau() {
    //     model.addRow(new Object[]{"PH001", "Phòng đơn không view", "2", "Standard Room", "Trống"});
    //     model.addRow(new Object[]{"PH002", "Phòng Deluxe có giường cỡ King", "3", "King Room", "Đã đặt"});
    // }
}
