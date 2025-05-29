package VIEW.CLIENT;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.util.Locale;
import VIEW.view_main;

public class view_NapRut extends JPanel {
    private view_main vMain;
    private JTabbedPane tabbedPane;
    
    // Enhanced color scheme
    private static final Color PRIMARY_COLOR = new Color(33, 150, 243);
    private static final Color SUCCESS_COLOR = new Color(76, 175, 80);
    private static final Color ERROR_COLOR = new Color(244, 67, 54);
    private static final Color WARNING_COLOR = new Color(255, 152, 0);
    private static final Color LIGHT_GRAY = new Color(248, 249, 250);
    private static final Color BORDER_COLOR = new Color(224, 224, 224);
    private static final Color TEXT_COLOR = new Color(33, 37, 41);
    private static final Color SECONDARY_TEXT = new Color(108, 117, 125);
    
    // Tab Nạp
    public JTextField txtSoTienNap;
    public JComboBox<String> cbPhuongThucNap;
    public JButton btnNapTien;
    // Tab Rút
    public JTextField txtSoTienRut;
    public JButton btnRutTien;
    // Số dư
    public JLabel lbeSoDu;
    
    // Enhanced components
    private JLabel balanceIcon;
    private JPanel headerPanel;

    public view_NapRut(view_main vMain) {
        this.vMain = vMain;
        setPreferredSize(new Dimension(1080, 880));
        setLayout(new BorderLayout());
        setBackground(LIGHT_GRAY);
        
        initializeComponents();
        setupLayout();
    }
    
    private void initializeComponents() {
        // Create header panel
        headerPanel = createHeaderPanel();
        
        // Create enhanced tabbed pane
        tabbedPane = createEnhancedTabbedPane();
    }
    
    private void setupLayout() {
        // Add components with proper spacing
        add(headerPanel, BorderLayout.NORTH);
        
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(LIGHT_GRAY);
        contentPanel.setBorder(new EmptyBorder(10, 20, 20, 20));
        contentPanel.add(tabbedPane, BorderLayout.CENTER);
        
        add(contentPanel, BorderLayout.CENTER);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 40, 15, 40));
        
        // Title with enhanced styling - smaller font
        JLabel title = new JLabel("NẠP/RÚT TIỀN", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(TEXT_COLOR);
        
        // Balance section with card-like appearance
        JPanel balanceCard = createBalanceCard();
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(Box.createVerticalStrut(10), BorderLayout.CENTER);
        panel.add(balanceCard, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createBalanceCard() {
        JPanel card = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        card.setBackground(new Color(232, 245, 233));
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(SUCCESS_COLOR, 1, true),
            new EmptyBorder(10, 15, 10, 15)
        ));
        
        // Balance icon - smaller
        balanceIcon = new JLabel("💰");
        balanceIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        
        // Balance text - smaller
        lbeSoDu = new JLabel("Số dư hiện tại: 0 VNĐ");
        lbeSoDu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbeSoDu.setForeground(SUCCESS_COLOR);
        
        card.add(balanceIcon);
        card.add(lbeSoDu);
        
        return card;
    }
    
    private JTabbedPane createEnhancedTabbedPane() {
        JTabbedPane pane = new JTabbedPane();
        pane.setFont(new Font("Segoe UI", Font.BOLD, 16));
        pane.setBackground(Color.WHITE);
        pane.setForeground(TEXT_COLOR);
        
        // Enhanced tab styling
        pane.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override
            protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex,
                                        int x, int y, int w, int h, boolean isSelected) {
                if (isSelected) {
                    g.setColor(PRIMARY_COLOR);
                    g.fillRect(x, y + h - 3, w, 3);
                }
            }
        });
        
        pane.addTab("💳 Nạp tiền", createEnhancedNapPanel());
        pane.addTab("💸 Rút tiền", createEnhancedRutPanel());
        
        return pane;
    }

    private JPanel createEnhancedNapPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 30, 30, 30));
        
        // Create scrollable panel for form content
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        
        // Account number section
        JPanel accountSection = createSectionPanel("Thông tin tài khoản", createAccountInfoPanel());
        accountSection.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(accountSection);
        formPanel.add(Box.createVerticalStrut(20));
        
        // Amount section
        JPanel amountInputPanel = new JPanel(new BorderLayout());
        amountInputPanel.setBackground(Color.WHITE);
        
        txtSoTienNap = createEnhancedTextField("Nhập số tiền...", true);
        txtSoTienNap.setPreferredSize(new Dimension(300, 35));
        
        JLabel currencyLabel = new JLabel(" VNĐ");
        currencyLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        currencyLabel.setForeground(SECONDARY_TEXT);
        
        amountInputPanel.add(txtSoTienNap, BorderLayout.WEST);
        amountInputPanel.add(currencyLabel, BorderLayout.CENTER);
        
        JPanel amountSection = createSectionPanel("Số tiền nạp", amountInputPanel);
        amountSection.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(amountSection);
        formPanel.add(Box.createVerticalStrut(20));
        
        // Payment method section
        cbPhuongThucNap = createEnhancedComboBox(new String[]{
            "🏦 Chuyển khoản ngân hàng", 
            "📱 Ví điện tử", 
            "💳 Thẻ tín dụng"
        });
        
        JPanel paymentSection = createSectionPanel("Phương thức thanh toán", cbPhuongThucNap);
        paymentSection.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(paymentSection);
        formPanel.add(Box.createVerticalStrut(20));
        
        // Security section
        JPanel securityPanel = createSecurityPanel();
        JPanel securitySection = createSectionPanel("Xác thực bảo mật", securityPanel);
        securitySection.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(securitySection);
        formPanel.add(Box.createVerticalStrut(25));
        
        // Button section
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        btnNapTien = createEnhancedButton("💰 NẠP TIỀN", SUCCESS_COLOR);
        buttonPanel.add(btnNapTien);
        
        formPanel.add(buttonPanel);
        formPanel.add(Box.createVerticalStrut(20)); // Extra space at bottom
        
        // Add form panel to scroll pane
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.setBackground(Color.WHITE);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        return mainPanel;
    }

    private JPanel createEnhancedRutPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 30, 30, 30));
        
        // Create scrollable panel for form content
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        
        // Account number section
        JPanel accountSection = createSectionPanel("Thông tin tài khoản", createAccountInfoPanel());
        accountSection.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(accountSection);
        formPanel.add(Box.createVerticalStrut(20));
        
        // Amount section
        JPanel amountInputPanel = new JPanel(new BorderLayout());
        amountInputPanel.setBackground(Color.WHITE);
        
        txtSoTienRut = createEnhancedTextField("Nhập số tiền...", true);
        txtSoTienRut.setPreferredSize(new Dimension(300, 35));
        
        JLabel currencyLabel = new JLabel(" VNĐ");
        currencyLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        currencyLabel.setForeground(SECONDARY_TEXT);
        
        amountInputPanel.add(txtSoTienRut, BorderLayout.WEST);
        amountInputPanel.add(currencyLabel, BorderLayout.CENTER);
        
        JPanel amountSection = createSectionPanel("Số tiền rút", amountInputPanel);
        amountSection.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(amountSection);
        formPanel.add(Box.createVerticalStrut(20));
        
        // Payment method section
        JComboBox<String> cbPhuongThucRut = createEnhancedComboBox(new String[]{
            "🏦 Chuyển khoản ngân hàng", 
            "📱 Ví điện tử", 
            "💳 Thẻ tín dụng"
        });
        
        JPanel paymentSection = createSectionPanel("Phương thức rút", cbPhuongThucRut);
        paymentSection.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(paymentSection);
        formPanel.add(Box.createVerticalStrut(20));
        
        // Security section
        JPanel securityPanel = createSecurityPanel();
        JPanel securitySection = createSectionPanel("Xác thực bảo mật", securityPanel);
        securitySection.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(securitySection);
        formPanel.add(Box.createVerticalStrut(25));
        
        // Button section
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        btnRutTien = createEnhancedButton("💸 RÚT TIỀN", ERROR_COLOR);
        buttonPanel.add(btnRutTien);
        
        formPanel.add(buttonPanel);
        formPanel.add(Box.createVerticalStrut(20)); // Extra space at bottom
        
        // Add form panel to scroll pane
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.setBackground(Color.WHITE);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        return mainPanel;
    }
    
    private JPanel createAccountInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(LIGHT_GRAY);
        panel.setBorder(new EmptyBorder(12, 15, 12, 15));
        panel.setPreferredSize(new Dimension(400, 60));
        
        JLabel accountLabel = new JLabel("🏛️ Số tài khoản:");
        accountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        accountLabel.setForeground(SECONDARY_TEXT);
        
        JLabel accountNumber = new JLabel("0123456789");
        accountNumber.setFont(new Font("Segoe UI", Font.BOLD, 14));
        accountNumber.setForeground(TEXT_COLOR);
        
        JPanel content = new JPanel(new GridLayout(2, 1, 0, 3));
        content.setBackground(LIGHT_GRAY);
        content.add(accountLabel);
        content.add(accountNumber);
        
        panel.add(content, BorderLayout.WEST);
        return panel;
    }
    
    private JPanel createSecurityPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        // Password section
        JPanel passwordSection = new JPanel();
        passwordSection.setLayout(new BoxLayout(passwordSection, BoxLayout.Y_AXIS));
        passwordSection.setBackground(Color.WHITE);
        passwordSection.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(300, 35));
        passwordField.setMaximumSize(new Dimension(300, 35));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR, 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        
        JPanel passwordFieldSection = createFieldWithLabel("🔒 Mật khẩu:", passwordField);
        passwordFieldSection.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordSection.add(passwordFieldSection);
        
        panel.add(passwordSection);
        panel.add(Box.createVerticalStrut(15));
        
        // OTP section
        JPanel otpSection = new JPanel();
        otpSection.setLayout(new BoxLayout(otpSection, BoxLayout.Y_AXIS));
        otpSection.setBackground(Color.WHITE);
        otpSection.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextField otpField = createEnhancedTextField("Nhập mã OTP...", false);
        otpField.setPreferredSize(new Dimension(200, 35));
        otpField.setMaximumSize(new Dimension(200, 35));
        
        JButton sendOtpBtn = createSecondaryButton("📧 Gửi mã");
        sendOtpBtn.setPreferredSize(new Dimension(120, 35));
        
        JPanel otpInputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        otpInputPanel.setBackground(Color.WHITE);
        otpInputPanel.add(otpField);
        otpInputPanel.add(sendOtpBtn);
        
        JPanel otpFieldSection = createFieldWithLabel("🔐 Mã OTP:", otpInputPanel);
        otpFieldSection.setAlignmentX(Component.LEFT_ALIGNMENT);
        otpSection.add(otpFieldSection);
        
        panel.add(otpSection);
        
        return panel;
    }
    
    private JPanel createFieldWithLabel(String labelText, JComponent field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(TEXT_COLOR);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Field wrapper with proper alignment
        JPanel fieldWrapper = new JPanel(new BorderLayout());
        fieldWrapper.setBackground(Color.WHITE);
        fieldWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        fieldWrapper.add(field, BorderLayout.WEST);
        
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(fieldWrapper);
        
        return panel;
    }
    
    private JPanel createSectionPanel(String title, JComponent component) {
        JPanel sectionPanel = new JPanel();
        sectionPanel.setLayout(new BoxLayout(sectionPanel, BoxLayout.Y_AXIS));
        sectionPanel.setBackground(Color.WHITE);
        sectionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Section title
        JLabel sectionTitle = new JLabel(title);
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sectionTitle.setForeground(PRIMARY_COLOR);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        sectionPanel.add(sectionTitle);
        sectionPanel.add(Box.createVerticalStrut(10));
        
        // Component wrapper with proper alignment
        JPanel componentWrapper = new JPanel(new BorderLayout());
        componentWrapper.setBackground(Color.WHITE);
        componentWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        componentWrapper.add(component, BorderLayout.WEST);
        
        sectionPanel.add(componentWrapper);
        
        return sectionPanel;
    }
    
    private JTextField createEnhancedTextField(String placeholder, boolean isNumeric) {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR, 1),
            new EmptyBorder(10, 15, 10, 15)
        ));
        
        // Placeholder functionality
        field.setForeground(SECONDARY_TEXT);
        field.setText(placeholder);
        
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(TEXT_COLOR);
                }
                field.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(PRIMARY_COLOR, 2),
                    new EmptyBorder(9, 14, 9, 14)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(SECONDARY_TEXT);
                    field.setText(placeholder);
                }
                field.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(BORDER_COLOR, 1),
                    new EmptyBorder(10, 15, 10, 15)
                ));
            }
        });
        
        return field;
    }
    
    private JComboBox<String> createEnhancedComboBox(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setPreferredSize(new Dimension(350, 40));
        combo.setMaximumSize(new Dimension(350, 40));
        combo.setBorder(new LineBorder(BORDER_COLOR, 1));
        combo.setBackground(Color.WHITE);
        return combo;
    }
    
    private JButton createEnhancedButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(200, 50));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setBorder(null);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setPreferredSize(new Dimension(120, 35));
        button.setBackground(Color.WHITE);
        button.setForeground(PRIMARY_COLOR);
        button.setBorder(new LineBorder(PRIMARY_COLOR, 1));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR);
                button.setForeground(Color.WHITE);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.WHITE);
                button.setForeground(PRIMARY_COLOR);
            }
        });
        
        return button;
    }

    // Getter cho mã người dùng
    public String getMaNguoiDung() {
        return vMain != null ? vMain.getMaNguoiDung() : null;
    }

    // Getter cho mã vai trò
    public String getMaVaiTro() {
        return vMain != null ? vMain.getMaVaiTro() : null;
    }

    // Enhanced setter cho số dư tài khoản with number formatting
    public void setSoDu(String soDu) {
        try {
            // Try to format the number if it's numeric
            long amount = Long.parseLong(soDu.replaceAll("[^0-9]", ""));
            NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
            lbeSoDu.setText("Số dư hiện tại: " + formatter.format(amount) + " VNĐ");
        } catch (NumberFormatException e) {
            // If formatting fails, use original format
            lbeSoDu.setText("Số dư hiện tại: " + soDu + " VNĐ");
        }
    }

    public view_main getMainView() {
        return vMain;
    }

    // public static void main(String[] args) {
    //     SwingUtilities.invokeLater(() -> {
    //         view_main vMain = new view_main("testUser", "testRole");
    //         JFrame frame = new JFrame("Test Nạp/Rút");
    //         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //         frame.setSize(1100, 700);
    //         frame.setLocationRelativeTo(null);
    //         frame.setLayout(new BorderLayout());
    //         frame.add(new view_NapRut(vMain), BorderLayout.CENTER);
    //         frame.setVisible(true);
    //     });
    // }
}