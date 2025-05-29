package VIEW.CLIENT;

import java.util.Date;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import VIEW.view_main;


public class view_DatPhong extends JPanel {
    private view_main vMain;
    private JComboBox<String> cbChiNhanh;
    private JButton btnTim, btnKiemTra, btnDat;
    private JPanel panelPhongList;
    private JTextField tfPhong;
    private ButtonGroup roomButtonGroup;
    
    // Color scheme
    private static final Color PRIMARY_COLOR = new Color(46, 125, 50);
    private static final Color SECONDARY_COLOR = new Color(33, 150, 243);
    private static final Color ACCENT_COLOR = new Color(244, 67, 54);
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private static final Color CARD_BACKGROUND = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(228, 230, 235);
    private static final Color TEXT_PRIMARY = new Color(33, 37, 41);
    private static final Color TEXT_SECONDARY = new Color(108, 117, 125);
    private static final Color SELECTED_COLOR = new Color(227, 242, 253);
    private static final Color HOVER_COLOR = new Color(240, 248, 255);
    
    // Main content panel and booking panel
    private JPanel mainContentPanel;
    private JPanel bookingPanel;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public view_DatPhong(view_main vMain) {
        this.vMain = vMain;
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(BACKGROUND_COLOR);

        roomButtonGroup = new ButtonGroup();
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        
        mainContentPanel = new JPanel(new BorderLayout(15, 15));
        mainContentPanel.setBackground(BACKGROUND_COLOR);
        
        initializeComponents();
        
        cardPanel.add(mainContentPanel, "main");
        add(cardPanel, BorderLayout.CENTER);
    }

    private void initializeComponents() {
        // Header panel with modern styling
        JPanel headerPanel = createModernPanel();
        headerPanel.setLayout(new BorderLayout(15, 0));
        headerPanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        // Branch selection with improved styling
        JPanel branchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        branchPanel.setOpaque(false);
        
        JLabel lblBranch = new JLabel("Chi nhánh:");
        lblBranch.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblBranch.setForeground(TEXT_PRIMARY);
        lblBranch.setBorder(new EmptyBorder(0, 0, 0, 10));
        
        cbChiNhanh = createModernComboBox(new String[]{"Chi nhánh 1", "Chi nhánh 2"});
        
        branchPanel.add(lblBranch);
        branchPanel.add(cbChiNhanh);

        btnTim = createModernButton("🔍 Tìm kiếm", PRIMARY_COLOR);
        btnKiemTra = createModernButton("📋 Kiểm tra", SECONDARY_COLOR);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(btnTim);
        buttonPanel.add(btnKiemTra);

        headerPanel.add(branchPanel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        mainContentPanel.add(headerPanel, BorderLayout.NORTH);

        // Main content area
        JPanel mainPanel = new JPanel(new BorderLayout(20, 0));
        mainPanel.setOpaque(false);

        // Left panel: Room grid with modern cards
        JPanel leftContainer = createModernPanel();
        leftContainer.setLayout(new BorderLayout());
        leftContainer.setBorder(new EmptyBorder(25, 25, 25, 25));
        
        JLabel titleLabel = new JLabel("Danh sách phòng");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        leftContainer.add(titleLabel, BorderLayout.NORTH);

        panelPhongList = new JPanel();
        panelPhongList.setLayout(new GridLayout(0, 4, 15, 15)); // 4 columns for better spacing
        panelPhongList.setBackground(CARD_BACKGROUND);
        panelPhongList.setBorder(new EmptyBorder(10, 0, 10, 0));

        JScrollPane scrollPane = new JScrollPane(panelPhongList);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(CARD_BACKGROUND);
        scrollPane.getViewport().setBackground(CARD_BACKGROUND);
        
        // Custom scrollbar styling
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());

        // Add sample rooms
        for (int i = 0; i < 15; i++) {
            panelPhongList.add(createModernRoomPanel("10" + String.format("%02d", i + 1)));
        }

        leftContainer.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(leftContainer, BorderLayout.CENTER);

        // Right panel: Room information with modern design
        JPanel rightContainer = createModernPanel();
        rightContainer.setPreferredSize(new Dimension(280, 400));
        rightContainer.setLayout(new BorderLayout());
        rightContainer.setBorder(new EmptyBorder(25, 25, 25, 25));
        
        JLabel infoTitle = new JLabel("Thông tin phòng");
        infoTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        infoTitle.setForeground(TEXT_PRIMARY);
        infoTitle.setBorder(new EmptyBorder(0, 0, 20, 0));
        rightContainer.add(infoTitle, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        // Room field
        infoPanel.add(createInfoField("Phòng", tfPhong = createModernTextField("Chưa chọn phòng")));
        tfPhong.setEditable(false);
        
        // Floor combobox
        JComboBox<String> cbTangMay = createModernComboBox(new String[]{"Tầng 1", "Tầng 2", "Tầng 3"});
        infoPanel.add(createInfoField("Tầng", cbTangMay));

        // Room type combobox
        JComboBox<String> cbLoaiPhong = createModernComboBox(new String[]{"Loại A", "Loại B", "Loại C"});
        infoPanel.add(createInfoField("Loại phòng", cbLoaiPhong));

        // Book button
        btnDat = createModernButton("🛏️ Đặt phòng", ACCENT_COLOR);
        btnDat.setPreferredSize(new Dimension(200, 45));
        btnDat.setMaximumSize(new Dimension(200, 45));
        btnDat.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        btnDat.addActionListener(e -> {
            if (tfPhong.getText().equals("Chưa chọn phòng") || tfPhong.getText().trim().isEmpty()) {
                showModernMessage("Vui lòng chọn một phòng trước khi đặt!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            showBookingPanel();
        });

        infoPanel.add(Box.createVerticalStrut(25));
        infoPanel.add(btnDat);

        rightContainer.add(infoPanel, BorderLayout.CENTER);
        mainPanel.add(rightContainer, BorderLayout.EAST);

        mainContentPanel.add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createInfoField(String labelText, JComponent component) {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        fieldPanel.setOpaque(false);
        fieldPanel.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT_SECONDARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(new EmptyBorder(0, 0, 5, 0));
        
        component.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        fieldPanel.add(label);
        fieldPanel.add(component);
        
        return fieldPanel;
    }

    private JPanel createModernRoomPanel(String tenPhong) {
        JPanel panel = new ModernCardPanel();
        panel.setLayout(new BorderLayout(5, 5));
        panel.setPreferredSize(new Dimension(120, 110));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Room icon and name
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        
        JLabel lblIcon = new JLabel("🛏️", JLabel.CENTER);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        
        JLabel lblTenPhong = new JLabel(tenPhong, JLabel.CENTER);
        lblTenPhong.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTenPhong.setForeground(TEXT_PRIMARY);
        
        topPanel.add(lblIcon, BorderLayout.CENTER);
        topPanel.add(lblTenPhong, BorderLayout.SOUTH);

        // Selection radio button with modern styling
        JRadioButton radioChon = new JRadioButton("Chọn");
        radioChon.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        radioChon.setOpaque(false);
        radioChon.setFocusPainted(false);
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 5));
        bottomPanel.setOpaque(false);
        bottomPanel.add(radioChon);
        
        roomButtonGroup.add(radioChon);
        
        radioChon.addActionListener(e -> {
            if (radioChon.isSelected()) {
                tfPhong.setText(tenPhong);
                updateRoomSelection(panel);
            }
        });
        
        // Enhanced mouse interaction
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                radioChon.setSelected(true);
                tfPhong.setText(tenPhong);
                updateRoomSelection(panel);
            }
            
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!radioChon.isSelected()) {
                    panel.setBackground(HOVER_COLOR);
                    panel.repaint();
                }
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!radioChon.isSelected()) {
                    panel.setBackground(CARD_BACKGROUND);
                    panel.repaint();
                }
            }
        });

        panel.add(topPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void updateRoomSelection(JPanel selectedPanel) {
        // Reset all panels
        for (Component comp : panelPhongList.getComponents()) {
            if (comp instanceof JPanel) {
                comp.setBackground(CARD_BACKGROUND);
            }
        }
        // Highlight selected panel
        selectedPanel.setBackground(SELECTED_COLOR);
        panelPhongList.repaint();
    }

    private void showBookingPanel() {
        bookingPanel = new JPanel(new BorderLayout(20, 20));
        bookingPanel.setBackground(BACKGROUND_COLOR);
        bookingPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Header with back button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JButton btnQuayLai = createModernButton("← Quay lại", TEXT_SECONDARY);
        btnQuayLai.addActionListener(e -> cardLayout.show(cardPanel, "main"));

        JLabel bookingTitle = new JLabel("Xác nhận đặt phòng");
        bookingTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        bookingTitle.setForeground(TEXT_PRIMARY);
        bookingTitle.setHorizontalAlignment(JLabel.CENTER);

        headerPanel.add(btnQuayLai, BorderLayout.WEST);
        headerPanel.add(bookingTitle, BorderLayout.CENTER);

        // Main form panel with adjusted size
        JPanel formContainer = createModernPanel();
        formContainer.setLayout(new BorderLayout());
        formContainer.setBorder(new EmptyBorder(30, 40, 30, 40));
        formContainer.setPreferredSize(new Dimension(700, 600)); // Further adjusted size

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);

        // Form fields
        JTextField tfPhongBooking = createModernTextField(tfPhong.getText());
        tfPhongBooking.setEditable(false);
        formPanel.add(createInfoField("Phòng đã chọn", tfPhongBooking));

        JComboBox<String> cbTangBooking = createModernComboBox(new String[]{"Tầng 1", "Tầng 2", "Tầng 3"});
        formPanel.add(createInfoField("Tầng", cbTangBooking));

        JComboBox<String> cbLoaiPhongBooking = createModernComboBox(new String[]{"Loại A", "Loại B", "Loại C"});
        formPanel.add(createInfoField("Loại phòng", cbLoaiPhongBooking));

        JTextField tfSoNguoi = createModernTextField("");
        formPanel.add(createInfoField("Số người", tfSoNguoi));

        JComboBox<String> cbDichVu = createModernComboBox(new String[]{"Không có", "Dịch vụ A", "Dịch vụ B", "Dịch vụ C"});
        formPanel.add(createInfoField("Dịch vụ", cbDichVu));

        // Add Date Pickers using JSpinner
        JSpinner spinnerCheckIn = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorCheckIn = new JSpinner.DateEditor(spinnerCheckIn, "dd/MM/yyyy");
        spinnerCheckIn.setEditor(editorCheckIn);
        formPanel.add(createInfoField("Ngày nhận phòng", spinnerCheckIn));

        JSpinner spinnerCheckOut = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor editorCheckOut = new JSpinner.DateEditor(spinnerCheckOut, "dd/MM/yyyy");
        spinnerCheckOut.setEditor(editorCheckOut);
        formPanel.add(createInfoField("Ngày trả phòng", spinnerCheckOut));

        formContainer.add(formPanel, BorderLayout.CENTER);

        // Center the form
        JPanel centerWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerWrapper.setOpaque(false);
        centerWrapper.add(formContainer);

        // Bottom panel with total and confirm
        JPanel bottomPanel = new JPanel(new BorderLayout(30, 20)); // Use BorderLayout for better alignment
        bottomPanel.setOpaque(false);

        JLabel lblTongTien = new JLabel("💰 Tổng tiền: 0 VND");
        lblTongTien.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTongTien.setForeground(ACCENT_COLOR);

        JButton btnXacNhan = createModernButton("✓ Xác nhận đặt phòng", PRIMARY_COLOR);
        btnXacNhan.setPreferredSize(new Dimension(200, 45));
        btnXacNhan.addActionListener(e -> {
            // Confirmation logic...
        });

        // Add lblTongTien to the left and btnXacNhan to the right
        bottomPanel.add(lblTongTien, BorderLayout.WEST);
        bottomPanel.add(btnXacNhan, BorderLayout.EAST);

        bookingPanel.add(headerPanel, BorderLayout.NORTH);
        bookingPanel.add(centerWrapper, BorderLayout.CENTER);
        bookingPanel.add(bottomPanel, BorderLayout.SOUTH);

        cardPanel.add(bookingPanel, "booking");
        cardLayout.show(cardPanel, "booking");
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    // Modern UI helper methods
    private JPanel createModernPanel() {
        JPanel panel = new ModernCardPanel();
        return panel;
    }

    private JButton createModernButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2.setColor(bgColor.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(
                        Math.min(255, bgColor.getRed() + 20),
                        Math.min(255, bgColor.getGreen() + 20),
                        Math.min(255, bgColor.getBlue() + 20)
                    ));
                } else {
                    g2.setColor(bgColor);
                }
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(130, 35));
        
        return button;
    }

    private JTextField createModernTextField(String placeholder) {
        JTextField field = new JTextField(placeholder) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(BORDER_COLOR, 1, 6),
            new EmptyBorder(8, 12, 8, 12)
        ));
        field.setBackground(Color.WHITE);
        field.setPreferredSize(new Dimension(200, 35));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        
        return field;
    }

    private JComboBox<String> createModernComboBox(String[] items) {
        JComboBox<String> combo = new JComboBox<String>(items) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setBorder(new RoundedBorder(BORDER_COLOR, 1, 6));
        combo.setBackground(Color.WHITE);
        combo.setPreferredSize(new Dimension(200, 35));
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        
        return combo;
    }

    private void showModernMessage(String message, String title, int messageType) {
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("OptionPane.buttonFont", new Font("Segoe UI", Font.BOLD, 12));
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    // Custom panel class with rounded corners and shadow
    private static class ModernCardPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw shadow
            g2.setColor(new Color(0, 0, 0, 20));
            g2.fillRoundRect(2, 2, getWidth() - 2, getHeight() - 2, 12, 12);
            
            // Draw background
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 2, 12, 12);
            
            g2.dispose();
        }
        
        public ModernCardPanel() {
            setBackground(CARD_BACKGROUND);
            setOpaque(false);
        }
    }

    // Custom border class for rounded borders
    private static class RoundedBorder extends LineBorder {
        private int radius;
        
        public RoundedBorder(Color color, int thickness, int radius) {
            super(color, thickness);
            this.radius = radius;
        }
        
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(lineColor);
            g2.setStroke(new BasicStroke(thickness));
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }
    }

    // Custom scrollbar UI
    private static class ModernScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = new Color(200, 200, 200);
            this.trackColor = new Color(245, 245, 245);
        }
        
        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }
        
        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }
        
        private JButton createZeroButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            return button;
        }
        
        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(thumbColor);
            g2.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2, 
                           thumbBounds.width - 4, thumbBounds.height - 4, 6, 6);
            g2.dispose();
        }
    }

    // Existing methods remain the same
    public String getMaNguoiDung() {
        return vMain != null ? vMain.getMaNguoiDung() : null;
    }

    public String getMaVaiTro() {
        return vMain != null ? vMain.getMaVaiTro() : null;
    }

    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Modern Room Booking System");
            view_main vMain = new view_main(null, null);
            view_DatPhong datPhongPanel = new view_DatPhong(vMain);
            
            frame.setContentPane(datPhongPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1080, 880);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}