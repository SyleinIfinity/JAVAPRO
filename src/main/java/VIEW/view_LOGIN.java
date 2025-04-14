package VIEW;

import javax.swing.*;

import CONTROLLER.APP.ctl_LOGIN;

import java.awt.*;
import java.awt.event.*;
import UTILS.FONT.*;

public class view_LOGIN extends JFrame implements ActionListener {
    private JLabel lblLogo;
    public JTextField txt_TenDangNhap;
    public JPasswordField txt_MatKhau;
    public JButton btn_DangNhap, btn_QuenMatKhau, btn_TaoTaiKhoan;
    ctl_LOGIN cLogin;

    @Override
    public void actionPerformed(ActionEvent e) {
        this.cLogin.KiemTraVaiTro();
    }

    public view_LOGIN() {
        setTitle("K-Team Hotel - Đăng nhập");
        setSize(430, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        initComponents();
        this.cLogin = new ctl_LOGIN(this);
    }

    public void initComponents() {
        // Font mặc định
        Font fontTitle = new Font("Times New Roman", Font.BOLD, 38);
        Font fontNormal = new Font("Times New Roman", Font.PLAIN, 16);
        Font fontButton = new Font("Times New Roman", Font.BOLD, 16);
        Font fontSmall = new Font("Times New Roman", Font.PLAIN, 14);

        // Logo
        lblLogo = new JLabel("K-Team Hotel");
        lblLogo.setFont(fontTitle);
        lblLogo.setForeground(new Color(24, 119, 242));
        lblLogo.setBounds(80, 40, 300, 50);
        add(lblLogo);

        // Tên đăng nhập
        txt_TenDangNhap = new JTextField();
        txt_TenDangNhap.setFont(fontNormal);
        txt_TenDangNhap.setBounds(60, 120, 300, 40);
        txt_TenDangNhap.setBorder(BorderFactory.createTitledBorder("Số điện thoại hoặc email"));
        add(txt_TenDangNhap);

        // Mật khẩu
        txt_MatKhau = new JPasswordField();
        txt_MatKhau.setFont(fontNormal);
        txt_MatKhau.setBounds(60, 180, 300, 40);
        txt_MatKhau.setBorder(BorderFactory.createTitledBorder("Mật khẩu"));
        add(txt_MatKhau);

        // Nút Đăng nhập
        btn_DangNhap = new JButton("Đăng nhập");
        btn_DangNhap.setBackground(new Color(24, 119, 242));
        btn_DangNhap.setForeground(Color.WHITE);
        btn_DangNhap.setFont(fontButton);
        btn_DangNhap.setBounds(60, 240, 300, 40);
        btn_DangNhap.addActionListener(this);
        add(btn_DangNhap);

        // Nút Quên mật khẩu
        btn_QuenMatKhau = new JButton("Quên mật khẩu?");
        btn_QuenMatKhau.setFont(fontSmall);
        btn_QuenMatKhau.setBounds(140, 290, 150, 30);
        btn_QuenMatKhau.setBorderPainted(false);
        btn_QuenMatKhau.setContentAreaFilled(false);
        btn_QuenMatKhau.setForeground(new Color(24, 119, 242));
        add(btn_QuenMatKhau);

        // Đường kẻ ngang
        JSeparator separator = new JSeparator();
        separator.setBounds(60, 340, 300, 1);
        add(separator);

        // Nút Tạo tài khoản mới
        btn_TaoTaiKhoan = new JButton("Tạo tài khoản mới");
        btn_TaoTaiKhoan.setBackground(new Color(45, 183, 87));
        btn_TaoTaiKhoan.setForeground(Color.WHITE);
        btn_TaoTaiKhoan.setFont(fontButton);
        btn_TaoTaiKhoan.setBounds(120, 360, 180, 40);
        add(btn_TaoTaiKhoan);
    }

    public static void main(String[] args) {
        new view_LOGIN().setVisible(true);
    }
}
