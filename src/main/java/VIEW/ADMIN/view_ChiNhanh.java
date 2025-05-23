package VIEW.ADMIN;


import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class view_ChiNhanh extends JFrame {
    private JPanel mainPanel;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, deleteButton, refreshButton, clearButton;
    
    // Input fields for ChiNhanhKhachSan
    private JTextField txtMaChiNhanh;
    private JTextField txtTenChiNhanh;
    private JTextField txtDiaChi;
    private JTextField txtSDT;
    
    public String maNguoiDung;
    public String maVaiTro;

    // Modern color scheme
    private final Color PRIMARY_COLOR = new Color(52, 152, 219);      // Blue
    private final Color SECONDARY_COLOR = new Color(46, 204, 113);    // Green
    private final Color DANGER_COLOR = new Color(231, 76, 60);        // Red
    private final Color WARNING_COLOR = new Color(241, 196, 15);      // Yellow
    private final Color BACKGROUND_COLOR = new Color(248, 249, 250);  // Light gray
    private final Color CARD_COLOR = Color.WHITE;
    private final Color TEXT_COLOR = new Color(52, 58, 64);
    private final Color BORDER_COLOR = new Color(222, 226, 230);

    public view_ChiNhanh(String maNguoiDung, String maVaiTro) {
        this.maNguoiDung = maNguoiDung;
        this.maVaiTro = maVaiTro;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        applyModernStyling();
    }

    private void initializeComponents() {
        setTitle("🏨 Quản lý Chi Nhánh Khách Sạn");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(true);

        // Set application icon
        try {
            setIconImage(createIconImage());
        } catch (Exception e) {
            // Fallback if icon creation fails
        }

        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Initialize table with modern styling
        String[] columnNames = {"🏢 Mã Chi Nhánh", "🏨 Tên Chi Nhánh", "📍 Địa Chỉ", "📞 Số Điện Thoại"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);

        // Initialize input fields with modern styling
        txtMaChiNhanh = createStyledTextField(15);
        txtTenChiNhanh = createStyledTextField(25);
        txtDiaChi = createStyledTextField(35);
        txtSDT = createStyledTextField(15);

        // Initialize buttons with modern styling
        addButton = createStyledButton("➕ Thêm", SECONDARY_COLOR);
        editButton = createStyledButton("✏️ Sửa", PRIMARY_COLOR);
        deleteButton = createStyledButton("🗑️ Xóa", DANGER_COLOR);
        refreshButton = createStyledButton("🔄 Làm mới", WARNING_COLOR);
        clearButton = createStyledButton("🧹 Xóa trắng", new Color(108, 117, 125));
    }

    private JTextField createStyledTextField(int columns) {
        JTextField textField = new JTextField(columns);
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        textField.setBackground(Color.WHITE);
        textField.setForeground(TEXT_COLOR);
        
        // Add focus effects
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                    BorderFactory.createEmptyBorder(7, 11, 7, 11)
                ));
            }
            
            @Override
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
    button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    button.setContentAreaFilled(false); // Thêm dòng này
    button.setOpaque(true);             // Thêm dòng này

        // Add hover effects
    button.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            button.setBackground(color.darker());
        }
        @Override
        public void mouseExited(MouseEvent e) {
            button.setBackground(color);
        }
    });

    return button;
    }

    private void setupLayout() {
        // Header panel
        JPanel headerPanel = createHeaderPanel();
        
        // Input panel with modern card design
        JPanel inputPanel = createInputPanel();
        
        // Button panel
        JPanel buttonPanel = createButtonPanel();
        
        // Table panel with modern styling
        JPanel tablePanel = createTablePanel();

        // Main layout
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new BorderLayout(0, 15));
        centerPanel.setBackground(BACKGROUND_COLOR);
        centerPanel.add(inputPanel, BorderLayout.NORTH);
        centerPanel.add(tablePanel, BorderLayout.CENTER);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        JLabel titleLabel = new JLabel("🏨 QUẢN LÝ CHI NHÁNH KHÁCH SẠN", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(255, 102, 102) );
        
        JLabel subtitleLabel = new JLabel("Hệ thống quản lý thông tin chi nhánh", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(108, 117, 125));
        
        JPanel titleContainer = new JPanel(new BorderLayout());
        titleContainer.setBackground(BACKGROUND_COLOR);
        titleContainer.add(titleLabel, BorderLayout.CENTER);
        titleContainer.add(subtitleLabel, BorderLayout.SOUTH);
        
        headerPanel.add(titleContainer, BorderLayout.CENTER);
        
        return headerPanel;
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(CARD_COLOR);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        
        // Add shadow effect (simulated with multiple borders)
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 2, new Color(0, 0, 0, 20)),
                BorderFactory.createLineBorder(BORDER_COLOR, 3)
            ),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);

        // Section title
        JLabel sectionTitle = new JLabel("📝 Thông tin Chi Nhánh");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sectionTitle.setForeground(new Color(0, 120, 215)); // Blue
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4; gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(sectionTitle, gbc);

        // Row 1
        gbc.gridwidth = 1; gbc.gridy = 1;
        gbc.gridx = 0; gbc.anchor = GridBagConstraints.WEST;
        inputPanel.add(createLabel("🏢 Mã Chi Nhánh:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(txtMaChiNhanh, gbc);

        gbc.gridx = 2;
        inputPanel.add(createLabel("🏨 Tên Chi Nhánh:"), gbc);
        gbc.gridx = 3;
        inputPanel.add(txtTenChiNhanh, gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(createLabel("📍 Địa Chỉ:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(txtDiaChi, gbc);

        // Row 3
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(createLabel("📞 Số Điện Thoại:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(txtSDT, gbc);

        return inputPanel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(TEXT_COLOR);
        return label;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(clearButton);
        
        return buttonPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(CARD_COLOR);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 2, new Color(0, 0, 0, 20)),
                BorderFactory.createLineBorder(BORDER_COLOR, 1)
            ),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Table title
        JLabel tableTitle = new JLabel("📋 Danh sách Chi Nhánh");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tableTitle.setForeground(PRIMARY_COLOR);
        tableTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        tablePanel.add(tableTitle, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        return tablePanel;
    }

    // ...existing code...
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
// ...existing code...


    private Image createIconImage() {
        // Create a simple icon programmatically
        int size = 32;
        BufferedImage icon = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = icon.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw a simple building icon
        g2d.setColor(PRIMARY_COLOR);
        g2d.fillRect(6, 10, 20, 20);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(8, 12, 4, 4);
        g2d.fillRect(14, 12, 4, 4);
        g2d.fillRect(20, 12, 4, 4);
        g2d.fillRect(8, 18, 4, 4);
        g2d.fillRect(14, 18, 4, 4);
        g2d.fillRect(20, 18, 4, 4);
        g2d.fillRect(8, 24, 4, 4);
        g2d.fillRect(20, 24, 4, 4);
        
        g2d.dispose();
        return icon;
    }

    private void setupEventHandlers() {
        addButton.addActionListener(e -> handleButtonClick("Thêm"));
        editButton.addActionListener(e -> handleButtonClick("Sửa"));
        deleteButton.addActionListener(e -> handleButtonClick("Xóa"));
        refreshButton.addActionListener(e -> handleButtonClick("Làm mới"));
        clearButton.addActionListener(e -> clearFields());
        
        // Table selection handler
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    populateFieldsFromTable(selectedRow);
                }
            }
        });
    }

    private void populateFieldsFromTable(int row) {
        txtMaChiNhanh.setText(table.getValueAt(row, 0).toString().replace("🏢 ", ""));
        txtTenChiNhanh.setText(table.getValueAt(row, 1).toString().replace("🏨 ", ""));
        txtDiaChi.setText(table.getValueAt(row, 2).toString().replace("📍 ", ""));
        txtSDT.setText(table.getValueAt(row, 3).toString().replace("📞 ", ""));
        txtMaChiNhanh.setEditable(false);
    }

    private void handleButtonClick(String buttonName) {
        if (validateInput()) {
            showModernMessage("Thao tác '" + buttonName + "' được thực hiện thành công! ✅", 
                           "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void clearFields() {
        txtMaChiNhanh.setText("");
        txtTenChiNhanh.setText("");
        txtDiaChi.setText("");
        txtSDT.setText("");
        txtMaChiNhanh.setEditable(true);
        table.clearSelection();
    }

    private void showModernMessage(String message, String title, int messageType) {
        // Create custom message dialog with modern styling
        JDialog dialog = new JDialog(this, title, true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        JPanel contentPanel = new JPanel(new BorderLayout(15, 15));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.WHITE);
        
        // Icon based on message type
        String iconText = messageType == JOptionPane.ERROR_MESSAGE ? "❌" : 
                         messageType == JOptionPane.WARNING_MESSAGE ? "⚠️" : "✅";
        JLabel iconLabel = new JLabel(iconText, JLabel.CENTER);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        
        JLabel messageLabel = new JLabel("<html><div style='text-align: center;'>" + message + "</div></html>", JLabel.CENTER);
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setForeground(TEXT_COLOR);
        
        JButton okButton = createStyledButton("OK", PRIMARY_COLOR);
        okButton.addActionListener(e -> dialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(okButton);
        
        contentPanel.add(iconLabel, BorderLayout.WEST);
        contentPanel.add(messageLabel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(contentPanel);
        dialog.setVisible(true);
    }

    private boolean validateInput() {
        String maChiNhanh = txtMaChiNhanh.getText().trim();
        String tenChiNhanh = txtTenChiNhanh.getText().trim();
        String diaChi = txtDiaChi.getText().trim();
        String sdt = txtSDT.getText().trim();

        // Validate Mã Chi Nhánh
        if (maChiNhanh.isEmpty()) {
            showModernMessage("Mã chi nhánh không được để trống! ❌", "Lỗi Validation", JOptionPane.ERROR_MESSAGE);
            txtMaChiNhanh.requestFocus();
            return false;
        }

        if (maChiNhanh.length() != 5) {
            showModernMessage("Mã chi nhánh phải có đúng 5 ký tự! ❌", "Lỗi Validation", JOptionPane.ERROR_MESSAGE);
            txtMaChiNhanh.requestFocus();
            return false;
        }

        if (!maChiNhanh.matches("^[A-Z]{2}\\d{3}$")) {
            showModernMessage("Mã chi nhánh phải có định dạng: 2 chữ cái hoa + 3 chữ số (VD: CN001)! ❌", "Lỗi Validation", JOptionPane.ERROR_MESSAGE);
            txtMaChiNhanh.requestFocus();
            return false;
        }

        // Validate Tên Chi Nhánh
        if (tenChiNhanh.isEmpty()) {
            showModernMessage("Tên chi nhánh không được để trống! ❌", "Lỗi Validation", JOptionPane.ERROR_MESSAGE);
            txtTenChiNhanh.requestFocus();
            return false;
        }

        if (tenChiNhanh.length() < 5 || tenChiNhanh.length() > 100) {
            showModernMessage("Tên chi nhánh phải có từ 5 đến 100 ký tự! ❌", "Lỗi Validation", JOptionPane.ERROR_MESSAGE);
            txtTenChiNhanh.requestFocus();
            return false;
        }

        // Validate Địa Chỉ
        if (diaChi.isEmpty()) {
            showModernMessage("Địa chỉ không được để trống! ❌", "Lỗi Validation", JOptionPane.ERROR_MESSAGE);
            txtDiaChi.requestFocus();
            return false;
        }

        if (diaChi.length() < 10 || diaChi.length() > 200) {
            showModernMessage("Địa chỉ phải có từ 10 đến 200 ký tự! ❌", "Lỗi Validation", JOptionPane.ERROR_MESSAGE);
            txtDiaChi.requestFocus();
            return false;
        }

        // Validate Số Điện Thoại (optional field)
        if (!sdt.isEmpty()) {
            if (!sdt.matches("^(0[3|5|7|8|9])\\d{8}$")) {
                showModernMessage("Số điện thoại phải có định dạng hợp lệ (10 chữ số, bắt đầu bằng 03, 05, 07, 08, 09)! ❌", "Lỗi Validation", JOptionPane.ERROR_MESSAGE);
                txtSDT.requestFocus();
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel for better native integration
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                
                // Enable anti-aliasing for better text rendering
                System.setProperty("awt.useSystemAAFontSettings", "on");
                System.setProperty("swing.aatext", "true");
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            String defaultMaNguoiDung = "ND01";
            String defaultMaVaiTro = "AD01";
            
            view_ChiNhanh frame = new view_ChiNhanh(defaultMaNguoiDung, defaultMaVaiTro);
            frame.setVisible(true);
        });
    }
}