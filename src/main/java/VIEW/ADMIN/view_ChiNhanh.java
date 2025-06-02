package VIEW.ADMIN;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;

import CONTROLLER.APP.ADMIN.ctl_ChiNhanh;
import VIEW.view_main;

public class view_ChiNhanh extends JPanel {
    public JTable table;
    // private DefaultTableModel tableModel;
    public JButton addButton, editButton, deleteButton, refreshButton, clearButton;
    public JTextField txtMaChiNhanh, txtTenChiNhanh, txtDiaChi, txtSDT;

    public String maNguoiDung;
    public String maVaiTro;
    public view_main vMain;
    ctl_ChiNhanh controller;

    private final Color PRIMARY_COLOR = new Color(52, 152, 219);
    private final Color SECONDARY_COLOR = new Color(46, 204, 113);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color WARNING_COLOR = new Color(241, 196, 15);
    private final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private final Color CARD_COLOR = Color.WHITE;
    private final Color TEXT_COLOR = new Color(52, 58, 64);
    private final Color BORDER_COLOR = new Color(222, 226, 230);

    public view_ChiNhanh(view_main vMain) {
        // this.maNguoiDung = maNguoiDung;
        // this.maVaiTro = maVaiTro;
        this.vMain = vMain;

        initializeComponents();
        setupLayout();
        applyModernStyling();
        controller = new ctl_ChiNhanh(this, vMain);
    }

    private void initializeComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        String[] columnNames = {"🏢 Mã Chi Nhánh", "🏨 Tên Chi Nhánh", "📍 Địa Chỉ", "📞 Số Điện Thoại"};
        table = new JTable(new DefaultTableModel(columnNames, 0));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);

        txtMaChiNhanh = createStyledTextField(15);
        txtTenChiNhanh = createStyledTextField(25);
        txtDiaChi = createStyledTextField(35);
        txtSDT = createStyledTextField(15);

        addButton = createStyledButton("➕ Thêm", SECONDARY_COLOR);
        editButton = createStyledButton("✏️ Sửa", PRIMARY_COLOR);
        deleteButton = createStyledButton("🗑️ Xóa", DANGER_COLOR);
        refreshButton = createStyledButton("🔄 Làm mới", WARNING_COLOR);
        clearButton = createStyledButton("🧹 Xóa trắng", new Color(108, 117, 125));
    }

    private JTextField createStyledTextField(int columns) {
        JTextField textField = new JTextField(columns);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setForeground(TEXT_COLOR);
        textField.setBackground(Color.WHITE);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        textField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                    BorderFactory.createEmptyBorder(7, 11, 7, 11)
                ));
            }
            public void focusLost(FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR, 1),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
        });

        return textField;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(false);
        button.setOpaque(true);

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });

        return button;
    }

    private void setupLayout() {
        JPanel headerPanel = createHeaderPanel();
        JPanel inputPanel = createInputPanel();
        JPanel tablePanel = createTablePanel();
        JPanel buttonPanel = createButtonPanel();

        add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 15));
        centerPanel.setBackground(BACKGROUND_COLOR);
        centerPanel.add(inputPanel, BorderLayout.NORTH);
        centerPanel.add(tablePanel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel("🏨 QUẢN LÝ CHI NHÁNH KHÁCH SẠN", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(255, 102, 102));

        JLabel subtitleLabel = new JLabel("Hệ thống quản lý thông tin chi nhánh", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(108, 117, 125));

        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(BACKGROUND_COLOR);
        container.add(titleLabel, BorderLayout.CENTER);
        container.add(subtitleLabel, BorderLayout.SOUTH);

        headerPanel.add(container, BorderLayout.CENTER);
        return headerPanel;
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 2, new Color(0, 0, 0, 20)),
                BorderFactory.createLineBorder(BORDER_COLOR, 3)
            ),
            new EmptyBorder(25, 25, 25, 25)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        JLabel sectionTitle = new JLabel("📝 Thông tin Chi Nhánh");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sectionTitle.setForeground(new Color(0, 120, 215));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4; gbc.anchor = GridBagConstraints.WEST;
        panel.add(sectionTitle, gbc);

        gbc.gridwidth = 1; gbc.gridy = 1;
        gbc.gridx = 0; panel.add(createLabel("🏢 Mã Chi Nhánh:"), gbc);
        gbc.gridx = 1; panel.add(txtMaChiNhanh, gbc);
        gbc.gridx = 2; panel.add(createLabel("🏨 Tên Chi Nhánh:"), gbc);
        gbc.gridx = 3; panel.add(txtTenChiNhanh, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(createLabel("📍 Địa Chỉ:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(txtDiaChi, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        panel.add(createLabel("📞 Số Điện Thoại:"), gbc);
        gbc.gridx = 1;
        panel.add(txtSDT, gbc);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 2, new Color(0, 0, 0, 20)),
                BorderFactory.createLineBorder(BORDER_COLOR, 1)
            ),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel label = new JLabel("📋 Danh sách Chi Nhánh");
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
        label.setForeground(PRIMARY_COLOR);
        label.setBorder(new EmptyBorder(0, 0, 15, 0));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);

        panel.add(label, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(new EmptyBorder(10, 0, 0, 0));

        panel.add(addButton);
        panel.add(editButton);
        panel.add(deleteButton);
        panel.add(refreshButton);
        panel.add(clearButton);

        return panel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(TEXT_COLOR);
        return label;
    }

private void applyModernStyling() {
    // Table styling
    table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    table.setRowHeight(35);
    table.setShowGrid(true);
    table.setGridColor(BORDER_COLOR);
    table.setSelectionBackground(PRIMARY_COLOR.brighter());
    table.setSelectionForeground(Color.WHITE);
    table.setBackground(Color.WHITE);

    // Header styling
    JTableHeader header = table.getTableHeader();
    header.setFont(new Font("Segoe UI", Font.BOLD, 14));
    header.setPreferredSize(new Dimension(header.getPreferredSize().width, 40));
    header.setDefaultRenderer(new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel lbl = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            lbl.setBackground(new Color(100, 149, 237)); // Cornflower Blue
            lbl.setForeground(Color.WHITE);
            lbl.setHorizontalAlignment(JLabel.CENTER);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lbl.setOpaque(true);
            return lbl;
        }
    });

    // Alternating row colors
    table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!isSelected) {
                c.setBackground(row % 2 == 0 ? new Color(220, 240, 255) : new Color(200, 230, 250));
            }
            if (c instanceof JComponent) {
                ((JComponent) c).setOpaque(true);
            }
            return c;
        }
    });
}
}
