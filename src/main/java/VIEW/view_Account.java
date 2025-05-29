package VIEW;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import CONTROLLER.APP.ctl_Account;

import java.awt.*;

public class view_Account extends JPanel {
    private String maNguoiDung;
    private String maVaiTro;
    public JLabel lbe_maND, lbe_tenND, lbe_ngaySinh, lbe_sdt, lbe_emailND, lbe_matKhau, lbe_soDuTaiKhoan;
    public JTextField txt_maND, txt_tenND, txt_ngaySinh, txt_sdt, txt_emailND, txt_matKhau, txt_soDuTaiKhoan;
    public JButton btn_edit, btn_save, btn_cancel, btn_close;

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
    view_main vMain;
    ctl_Account controller;


    public view_Account(view_main vMain) {
        this.vMain = vMain;

        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);

        initializeComponents();
        setupLayout();

        controller = new ctl_Account(this, vMain);
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

    public void populateFields(String maND, String tenND, String ngaySinh,
                               String sdt, String email, String matKhau) {
        txt_maND.setText(maND);
        txt_tenND.setText(tenND);
        txt_ngaySinh.setText(ngaySinh);
        txt_sdt.setText(sdt);
        txt_emailND.setText(email);
        txt_matKhau.setText(matKhau);
    }


    private void initializeComponents() {
        lbe_maND = createStyledLabel("Mã người dùng");
        lbe_tenND = createStyledLabel("Tên người dùng");
        lbe_ngaySinh = createStyledLabel("Ngày sinh");
        lbe_sdt = createStyledLabel("Số điện thoại");
        lbe_emailND = createStyledLabel("Email");
        lbe_matKhau = createStyledLabel("Mật khẩu");
        lbe_soDuTaiKhoan = createStyledLabel("Số dư tài khoản");

        txt_maND = createStyledTextField();
        txt_tenND = createStyledTextField();
        txt_ngaySinh = createStyledTextField();
        txt_sdt = createStyledTextField();
        txt_emailND = createStyledTextField();
        txt_matKhau = createStyledTextField();
        txt_soDuTaiKhoan = createStyledTextField();

        setFieldsEditable(true);

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
        JTextField textField = new JTextField();
        textField.setFont(new Font("SF Pro Display", Font.PLAIN, 13));
        textField.setBackground(CARD_COLOR);
        textField.setForeground(TEXT_PRIMARY);
        textField.setCaretColor(PRIMARY_COLOR);
        textField.setPreferredSize(new Dimension(280, 36));
        textField.setMargin(new Insets(8, 12, 8, 12));
        return textField;
    }

    private JButton createModernButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("SF Pro Display", Font.BOLD, 13));
        button.setPreferredSize(new Dimension(120, 36));
        return button;
    }

    private void setupLayout() {
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setOpaque(false);
        mainContainer.setBorder(new EmptyBorder(30, 30, 30, 30));

        mainContainer.add(createHeaderPanel(), BorderLayout.NORTH);
        mainContainer.add(createCardPanel(), BorderLayout.CENTER);
        mainContainer.add(createButtonPanel(), BorderLayout.SOUTH);

        this.add(mainContainer, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Thông tin tài khoản");
        titleLabel.setFont(new Font("SF Pro Display", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_PRIMARY);

        headerPanel.add(titleLabel);
        return headerPanel;
    }

    private JPanel createCardPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(CARD_COLOR);
        formPanel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;
        addFormField(formPanel, lbe_maND, txt_maND, gbc, y++);
        addFormField(formPanel, lbe_tenND, txt_tenND, gbc, y++);
        addFormField(formPanel, lbe_ngaySinh, txt_ngaySinh, gbc, y++);
        addFormField(formPanel, lbe_sdt, txt_sdt, gbc, y++);
        addFormField(formPanel, lbe_emailND, txt_emailND, gbc, y++);
        addFormField(formPanel, lbe_matKhau, txt_matKhau, gbc, y++);
        addFormField(formPanel, lbe_soDuTaiKhoan, txt_soDuTaiKhoan, gbc, y++);

        return formPanel;
    }

    private void addFormField(JPanel panel, JLabel label, JTextField textField, GridBagConstraints gbc, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(label, gbc);

        gbc.gridx = 1;
        panel.add(textField, gbc);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);

        buttonPanel.add(btn_edit);
        buttonPanel.add(btn_save);
        buttonPanel.add(btn_cancel);
        buttonPanel.add(btn_close);

        return buttonPanel;
    }
}