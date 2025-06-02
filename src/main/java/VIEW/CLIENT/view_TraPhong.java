package VIEW.CLIENT;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import CONTROLLER.APP.CLIENT.ctl_TraPhong;

import java.awt.*;
import java.util.Vector;
import VIEW.view_main;

public class view_TraPhong extends JPanel {
    private JTable table;
    private JTextArea detailArea;
    private JButton btnXem, btnTraPhong;
    private JComboBox<String> branchComboBox;
    private Color mauChinh = new Color(52, 152, 219);
    private Color mauPhu = new Color(41, 128, 185);
    private Color mauNen = new Color(236, 240, 241);
    private Color mauNhan = new Color(230, 126, 34);
    private DefaultTableModel tableModel;
    private view_main vMain;

    public view_TraPhong(view_main vMain) {  // Sửa lại tham số từ view_main_whain thành view_main
        setLayout(new BorderLayout());
        setBackground(mauNen);  // Sửa mauMen thành mauNen (theo code gốc)
        this.vMain = vMain;
    
        // Header panel
        JPanel headerPanel = taoHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
    
        // Main content panel
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));  // Sửa lại tham số
        contentPanel.setBackground(mauNen);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));  // Sửa lại cách gọi
    
        // Left panel (table with filter)
        JPanel leftPanel = taoTablePanel();  // Sửa JPanel_leftPanel thành JPanel leftPanel
        contentPanel.add(leftPanel, BorderLayout.CENTER);
    
        // Right panel (detail view)
        JPanel rightPanel = taoDetailPanel();  // Sửa JPanel_rightPanel thành JPanel rightPanel
        contentPanel.add(rightPanel, BorderLayout.EAST);
    
        add(contentPanel, BorderLayout.CENTER);
        
        // Thêm controller - đây là cách đúng
        new ctl_TraPhong(this, vMain);  // Sửa lại dấu ` thành dấu '
    }
    public void refreshData() {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ
        branchComboBox.removeAllItems();
        branchComboBox.addItem("Tất cả");
        detailArea.setText("Chọn một phòng để xem thông tin chi tiết...");
    }
    private JPanel taoHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(mauPhu);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel lblTitle = new JLabel("QUẢN LÝ TRẢ PHÒNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);

        headerPanel.add(lblTitle, BorderLayout.CENTER);
        return headerPanel;
    }

    private JPanel taoTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(mauNen);

        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBackground(mauNen);
        filterPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        JLabel lblFilter = new JLabel("Lọc theo chi nhánh: ");
        lblFilter.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        filterPanel.add(lblFilter);

        branchComboBox = new JComboBox<>();
        branchComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        branchComboBox.setPreferredSize(new Dimension(200, 30));
        filterPanel.add(branchComboBox);

        tablePanel.add(filterPanel, BorderLayout.NORTH);

        // Table setup
        String[] columnNames = {"Mã đặt phòng", "Khách hàng", "Chi nhánh", "Tầng", "Phòng", "Số người", "Giá phòng", "Thành tiền"};
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
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);

        // Hide the "Chi nhánh" column
        TableColumnModel columnModel = table.getColumnModel();
        TableColumn branchColumn = columnModel.getColumn(2); // Chi nhánh is at index 2
        columnModel.removeColumn(branchColumn);

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

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(mauNen);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        btnXem = taoButtonDep("Xem", mauChinh);
        buttonPanel.add(btnXem);

        tablePanel.add(buttonPanel, BorderLayout.SOUTH);

        return tablePanel;
    }

    private JPanel taoDetailPanel() {
        JPanel detailPanel = new JPanel(new BorderLayout());
        detailPanel.setBackground(Color.WHITE);
        detailPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Thông tin chi tiết"),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        detailPanel.setPreferredSize(new Dimension(380, getHeight()));

        detailArea = new JTextArea(10, 30);
        detailArea.setEditable(false);
        detailArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        detailArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        detailArea.setText("Chọn một phòng để xem thông tin chi tiết...");

        JScrollPane scrollPane = new JScrollPane(detailArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        detailPanel.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        btnTraPhong = taoButtonDep("Trả Phòng", mauNhan);
        buttonPanel.add(btnTraPhong);

        detailPanel.add(buttonPanel, BorderLayout.SOUTH);

        return detailPanel;
    }

    private void caiDatTableColumns() {
        int[] columnWidths = {120, 150, 80, 80, 80, 100, 120}; // Adjusted for removed "Chi nhánh" column
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

                    setHorizontalAlignment(SwingConstants.CENTER);
                    return c;
                }
            });
        }
    }

    private JButton taoButtonDep(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                g2.setColor(Color.WHITE);
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

    private Color toiMau(Color color, float fraction) {
        int red = Math.max(0, (int) (color.getRed() * (1 - fraction)));
        int green = Math.max(0, (int) (color.getGreen() * (1 - fraction)));
        int blue = Math.max(0, (int) (color.getBlue() * (1 - fraction)));
        return new Color(red, green, blue);
    }

    // Getter methods for controller to access components
    public JTable getTable() {
        return table;
    }

    public JTextArea getDetailArea() {
        return detailArea;
    }

    public JButton getBtnXem() {
        return btnXem;
    }

    public JButton getBtnTraPhong() {
        return btnTraPhong;
    }

    public JComboBox<String> getBranchComboBox() {
        return branchComboBox;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }
    
}