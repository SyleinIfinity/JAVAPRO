package VIEW;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class view_Account extends JFrame {
    private String maNguoiDung;
    private String maVaiTro;
    private JLabel lbe_maND, lbe_tenND, lbe_ngaySinh, lbe_sdt, lbe_emailND, lbe_matKhau, lbe_soDuTaiKhoan;
    public JTextField txt_maND, txt_tenND, txt_ngaySinh, txt_sdt, txt_emailND, txt_matKhau, txt_soDuTaiKhoan;

    private JButton btn_edit, btn_save, btn_cancel, btn_close;

    // Modern color palette
    private static final Color PRIMARY_COLOR = new Color(74, 144, 226);
    private static final Color SUCCESS_COLOR = new Color(72, 207, 173);
    private static final Color DANGER_COLOR = new Color(245, 101, 101);
    private static final Color SECONDARY_COLOR = new Color(108, 117, 125);
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color TEXT_PRIMARY = new Color(33, 37, 41);
    private static final Color TEXT_SECONDARY = new Color(108, 117, 125);
    private static final Color BORDER_COLOR = new Color(222, 226, 230);
    private static final Color ACCENT_COLOR = new Color(102, 126, 234);

    public view_Account(String maNguoiDung, String maVaiTro) {
        this.maNguoiDung = maNguoiDung;
        this.maVaiTro = maVaiTro;
        initializeComponents();
        setupLayout();
        setupFrame();
    }

    public void populateFields(String maND, String tenND, String ngaySinh,
                              String sdt, String email, String matKhau, String soDuTaiKhoan) {
        txt_maND.setText(maND);
        txt_tenND.setText(tenND);
        txt_ngaySinh.setText(ngaySinh);
        txt_sdt.setText(sdt);
        txt_emailND.setText(email);
        txt_matKhau.setText(matKhau);
        txt_soDuTaiKhoan.setText(soDuTaiKhoan);
    }

    private void initializeComponents() {
        // Create labels with modern styling
        lbe_maND = createStyledLabel("Mã quản lý");
        lbe_tenND = createStyledLabel("Tên quản lý");
        lbe_ngaySinh = createStyledLabel("Ngày sinh");
        lbe_sdt = createStyledLabel("Số điện thoại");
        lbe_emailND = createStyledLabel("Email");
        lbe_matKhau = createStyledLabel("Mật khẩu");
        lbe_soDuTaiKhoan = createStyledLabel("Số dư tài khoản");

        // Create text fields with modern styling
        txt_maND = createStyledTextField();
        txt_tenND = createStyledTextField();
        txt_ngaySinh = createStyledTextField();
        txt_sdt = createStyledTextField();
        txt_emailND = createStyledTextField();
        txt_matKhau = createStyledTextField();
        txt_soDuTaiKhoan = createStyledTextField();

        // Set all fields as editable
        setFieldsEditable(true);

        // Create buttons with modern design
        btn_edit = createModernButton("✏️ Chỉnh sửa", PRIMARY_COLOR);
        btn_save = createModernButton("💾 Lưu", SUCCESS_COLOR);
        btn_cancel = createModernButton("❌ Hủy", DANGER_COLOR);
        btn_close = createModernButton("🚪 Đóng", SECONDARY_COLOR);
    }

    private void setFieldsEditable(boolean editable) {
        txt_maND.setEditable(editable);
        txt_tenND.setEditable(editable);
        txt_ngaySinh.setEditable(editable);
        txt_sdt.setEditable(editable);
        txt_emailND.setEditable(editable);
        txt_matKhau.setEditable(editable);
        txt_soDuTaiKhoan.setEditable(editable);
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SF Pro Display", Font.BOLD, 13));
        label.setForeground(TEXT_PRIMARY);
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new ModernTextField();
        textField.setFont(new Font("SF Pro Display", Font.PLAIN, 13));
        textField.setBackground(CARD_COLOR);
        textField.setForeground(TEXT_PRIMARY);
        textField.setCaretColor(PRIMARY_COLOR);
        textField.setPreferredSize(new Dimension(280, 36)); // giảm chiều cao
        textField.setMargin(new Insets(8, 12, 8, 12)); // giảm margin trên/dưới
        return textField;
    }

    private JButton createModernButton(String text, Color color) {
        return new ModernButton(text, color);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Main container with gradient background
        JPanel mainContainer = new GradientPanel();
        mainContainer.setLayout(new BorderLayout());
        mainContainer.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Header panel with user info
        JPanel headerPanel = createHeaderPanel();
        
        // Card panel for form
        JPanel cardPanel = createCardPanel();
        
        // Button panel
        JPanel buttonPanel = createButtonPanel();

        // Assembly
        mainContainer.add(headerPanel, BorderLayout.NORTH);
        mainContainer.add(cardPanel, BorderLayout.CENTER);
        mainContainer.add(buttonPanel, BorderLayout.SOUTH);

        add(mainContainer);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 25, 0));

        // Main title
        JLabel titleLabel = new JLabel("Quản lý Tài khoản");
        titleLabel.setFont(new Font("SF Pro Display", Font.BOLD, 28));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtitle with user info
        JLabel subtitleLabel = new JLabel("ID: " + maNguoiDung + " • Vai trò: " + maVaiTro);
        subtitleLabel.setFont(new Font("SF Pro Display", Font.PLAIN, 14));
        subtitleLabel.setForeground(TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Decorative line
        JPanel linePanel = new JPanel();
        linePanel.setPreferredSize(new Dimension(100, 3));
        linePanel.setMaximumSize(new Dimension(100, 3));
        linePanel.setBackground(ACCENT_COLOR);
        linePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(8));
        headerPanel.add(subtitleLabel);
        headerPanel.add(Box.createVerticalStrut(15));
        headerPanel.add(linePanel);

        return headerPanel;
    }

    private JPanel createCardPanel() {
        JPanel cardPanel = new RoundedPanel(20);
        cardPanel.setBackground(CARD_COLOR);
        cardPanel.setLayout(new BorderLayout());
        cardPanel.setBorder(new EmptyBorder(30, 35, 30, 35));
        
        // Add subtle shadow effect
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            new ShadowBorder(5, new Color(0, 0, 0, 15)),
            new EmptyBorder(30, 35, 30, 35)
        ));

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 0, 12, 20);
        gbc.anchor = GridBagConstraints.WEST;

        // Add form fields with icons
        addFormFieldWithIcon(formPanel, "👤", lbe_maND, txt_maND, 0, gbc);
        addFormFieldWithIcon(formPanel, "🏷️", lbe_tenND, txt_tenND, 1, gbc);
        addFormFieldWithIcon(formPanel, "📅", lbe_ngaySinh, txt_ngaySinh, 2, gbc);
        addFormFieldWithIcon(formPanel, "📱", lbe_sdt, txt_sdt, 3, gbc);
        addFormFieldWithIcon(formPanel, "📧", lbe_emailND, txt_emailND, 4, gbc);
        addFormFieldWithIcon(formPanel, "🔒", lbe_matKhau, txt_matKhau, 5, gbc);
        addFormFieldWithIcon(formPanel, "💰", lbe_soDuTaiKhoan, txt_soDuTaiKhoan, 6, gbc);

        cardPanel.add(formPanel, BorderLayout.CENTER);
        return cardPanel;
    }

    private void addFormFieldWithIcon(JPanel panel, String icon, JLabel label, JTextField textField, 
                                 int row, GridBagConstraints gbc) {
    // Icon
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0.0;
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        panel.add(iconLabel, gbc);

        // Label
        gbc.gridx = 1;
        gbc.insets = new Insets(4, 10, 4, 20); // giảm insets trên/dưới
        panel.add(label, gbc);

        // Text field
        gbc.gridx = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(4, 0, 4, 0); // giảm insets trên/dưới
        panel.add(textField, gbc);
        
        // Reset for next iteration
        gbc.weightx = 0.0;
        gbc.insets = new Insets(4, 0, 4, 20); // giảm insets trên/dưới
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        buttonPanel.setOpaque(false);
        
        buttonPanel.add(btn_edit);
        buttonPanel.add(btn_save);
        buttonPanel.add(btn_cancel);
        buttonPanel.add(btn_close);

        return buttonPanel;
    }

    private void setupFrame() {
        setTitle("Hệ thống Quản lý Tài khoản");
        setSize(650, 750);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            // Continue with default
        }

        // Set minimum size
        setMinimumSize(new Dimension(600, 700));
    }

    // Custom components for modern UI
    private static class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            
            int w = getWidth(), h = getHeight();
            GradientPaint gp = new GradientPaint(
                0, 0, BACKGROUND_COLOR,
                0, h, new Color(240, 242, 247)
            );
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, w, h);
        }
    }

    private static class RoundedPanel extends JPanel {
        private int radius;
        
        public RoundedPanel(int radius) {
            this.radius = radius;
            setOpaque(false);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class ShadowBorder implements javax.swing.border.Border {
        private int shadowSize;
        private Color shadowColor;
        
        public ShadowBorder(int shadowSize, Color shadowColor) {
            this.shadowSize = shadowSize;
            this.shadowColor = shadowColor;
        }
        
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            for (int i = 0; i < shadowSize; i++) {
                g2.setColor(new Color(shadowColor.getRed(), shadowColor.getGreen(), 
                                    shadowColor.getBlue(), shadowColor.getAlpha() / shadowSize));
                g2.drawRoundRect(x + i, y + i, width - 2*i - 1, height - 2*i - 1, 20, 20);
            }
            g2.dispose();
        }
        
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(shadowSize, shadowSize, shadowSize, shadowSize);
        }
        
        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }

    private static class ModernTextField extends JTextField {
        private boolean focused = false;
        
        public ModernTextField() {
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
            
            addFocusListener(new java.awt.event.FocusAdapter() {
                @Override
                public void focusGained(java.awt.event.FocusEvent e) {
                    focused = true;
                    repaint();
                }
                
                @Override
                public void focusLost(java.awt.event.FocusEvent e) {
                    focused = false;
                    repaint();
                }
            });
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Background
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            
            // Border
            if (focused) {
                g2.setColor(PRIMARY_COLOR);
                g2.setStroke(new BasicStroke(2));
            } else {
                g2.setColor(BORDER_COLOR);
                g2.setStroke(new BasicStroke(1));
            }
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
            
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private static class ModernButton extends JButton {
        private Color baseColor;
        private boolean hovered = false;
        private boolean pressed = false;

        public ModernButton(String text, Color baseColor) {
            super(text);
            this.baseColor = baseColor;
            
            setForeground(Color.WHITE);
            setFont(new Font("SF Pro Display", Font.BOLD, 13));
            setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
            setFocusPainted(false);
            setContentAreaFilled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(120, 42));
            
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    hovered = true;
                    repaint();
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    hovered = false;
                    repaint();
                }
                
                @Override
                public void mousePressed(MouseEvent e) {
                    pressed = true;
                    repaint();
                }
                
                @Override
                public void mouseReleased(MouseEvent e) {
                    pressed = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            Color bgColor = baseColor;
            if (pressed) {
                bgColor = darkenColor(baseColor, 0.2f);
            } else if (hovered) {
                bgColor = brightenColor(baseColor, 0.1f);
            }
            
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            
            // Text
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(getText())) / 2;
            int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
            g2.setColor(getForeground());
            g2.setFont(getFont());
            g2.drawString(getText(), x, y);
            
            g2.dispose();
        }
        
        private Color brightenColor(Color color, float factor) {
            int r = Math.min(255, (int) (color.getRed() + (255 - color.getRed()) * factor));
            int g = Math.min(255, (int) (color.getGreen() + (255 - color.getGreen()) * factor));
            int b = Math.min(255, (int) (color.getBlue() + (255 - color.getBlue()) * factor));
            return new Color(r, g, b, color.getAlpha());
        }
        
        private Color darkenColor(Color color, float factor) {
            int r = (int) (color.getRed() * (1 - factor));
            int g = (int) (color.getGreen() * (1 - factor));
            int b = (int) (color.getBlue() * (1 - factor));
            return new Color(r, g, b, color.getAlpha());
        }
    }

    // Main method for testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            view_Account frame = new view_Account("USER001", "ADMIN");
            frame.populateFields("USER001", "Nguyễn Văn A", "01/01/1990",
                                "0123456789", "nguyenvana@email.com", "password123", "1,000,000 VND");
            frame.setVisible(true);
        });
    }
}
