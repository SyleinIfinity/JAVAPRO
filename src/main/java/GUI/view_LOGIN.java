package GUI;

import javax.swing.*;

import BLL.ctl_LOGIN;

import java.awt.*;

public class view_LOGIN extends JPanel {
    private JPanel pnForm;
    public JTextField txtEmail;
    public JPasswordField txtMatKhau;
    public JButton btnDangNhap;
    // Các nút chuyển tab
    public JButton btnHienThiDangNhap, btnHienThiDangKy, btnHienThiQuenMatKhau;
    // Các nút chức năng trong từng form
    public JButton btnDangKy, btnQuenMatKhau;
    // Các trường nhập liệu
    public JTextField txtEmailDangKy, txtHoTen, txtEmailQuenMatKhau;
    public JPasswordField txtMatKhauDangKy, txtXacNhanMatKhau;
    private ctl_LOGIN controller;
    private view_main vMain;

    public view_LOGIN(view_main vMain) {
        setPreferredSize(new Dimension(1080, 880));
        setLayout(new GridBagLayout());
        setBackground(new Color(40, 40, 40)); // nền tối
        this.vMain = vMain;

        // Panel nền mờ bên trái (có thể dùng JLabel với ImageIcon)
        JLabel lbeHinhNen = new JLabel();
        lbeHinhNen.setPreferredSize(new Dimension(500, 500));
        lbeHinhNen.setIcon(new ImageIcon("src/main/resources/IMAGES/signup_bg.jpg")); // Đổi đường dẫn ảnh phù hợp
        lbeHinhNen.setHorizontalAlignment(SwingConstants.CENTER);

        // Panel chứa form động bên phải
        pnForm = new JPanel();
        pnForm.setPreferredSize(new Dimension(500, 500));
        pnForm.setBackground(new Color(30, 32, 36));
        pnForm.setLayout(null);
        pnForm.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

        // Thêm 2 panel vào layout chính
        JPanel pnChinh = new JPanel(new GridLayout(1, 2));
        pnChinh.setOpaque(false);
        pnChinh.add(lbeHinhNen);
        pnChinh.add(pnForm);

        add(pnChinh);

        // Khởi tạo controller
        controller = new ctl_LOGIN(this, pnForm, this.vMain);

        // Hiển thị mặc định form đăng nhập
        hienThiFormDangNhap();
    }
    
    public void hienThiFormDangNhap() {
        pnForm.removeAll();
        pnForm.setLayout(null);

        JLabel lbeTieuDe = new JLabel("ĐĂNG NHẬP");
        lbeTieuDe.setForeground(Color.WHITE);
        lbeTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lbeTieuDe.setBounds(150, 30, 200, 40);
        pnForm.add(lbeTieuDe);

        JLabel lbeEmail = new JLabel("Email:");
        lbeEmail.setForeground(Color.WHITE);
        lbeEmail.setBounds(60, 100, 100, 30);
        pnForm.add(lbeEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(60, 135, 380, 35);
        pnForm.add(txtEmail);

        JLabel lbeMatKhau = new JLabel("Mật khẩu:");
        lbeMatKhau.setForeground(Color.WHITE);
        lbeMatKhau.setBounds(60, 185, 100, 30);
        pnForm.add(lbeMatKhau);

        txtMatKhau = new JPasswordField();
        txtMatKhau.setBounds(60, 220, 380, 35);
        pnForm.add(txtMatKhau);

        btnDangNhap = new JButton("Đăng nhập");
        btnDangNhap.setBounds(60, 280, 380, 40);
        btnDangNhap.setBackground(new Color(33, 150, 243));
        btnDangNhap.setForeground(Color.WHITE);
        btnDangNhap.setFocusPainted(false);
        pnForm.add(btnDangNhap);

        btnHienThiDangKy = new JButton("Đăng ký");
        btnHienThiDangKy.setBounds(60, 340, 180, 30);
        pnForm.add(btnHienThiDangKy);

        btnHienThiQuenMatKhau = new JButton("Quên mật khẩu");
        btnHienThiQuenMatKhau.setBounds(260, 340, 180, 30);
        pnForm.add(btnHienThiQuenMatKhau);

        pnForm.repaint();
        pnForm.revalidate();

        // Thêm sự kiện cho các nút trong form đăng nhập
        controller.themSuKien();
    }

    public void hienThiFormDangKy() {
        pnForm.removeAll();
        pnForm.setLayout(null);
    
        JLabel lbeTieuDe = new JLabel("ĐĂNG KÝ");
        lbeTieuDe.setForeground(Color.WHITE);
        lbeTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lbeTieuDe.setBounds(190, 30, 200, 40);
        pnForm.add(lbeTieuDe);
    
        JLabel lbeHoTen = new JLabel("Họ tên:");
        lbeHoTen.setForeground(Color.WHITE);
        lbeHoTen.setBounds(60, 80, 100, 30);
        pnForm.add(lbeHoTen);
    
        txtHoTen = new JTextField();
        txtHoTen.setBounds(60, 110, 380, 35);
        pnForm.add(txtHoTen);
    
        JLabel lbeEmail = new JLabel("Email:");
        lbeEmail.setForeground(Color.WHITE);
        lbeEmail.setBounds(60, 150, 100, 30);
        pnForm.add(lbeEmail);
    
        txtEmailDangKy = new JTextField();
        txtEmailDangKy.setBounds(60, 180, 380, 35);
        pnForm.add(txtEmailDangKy);
    
        JLabel lbeMatKhau = new JLabel("Mật khẩu:");
        lbeMatKhau.setForeground(Color.WHITE);
        lbeMatKhau.setBounds(60, 230, 100, 30);
        pnForm.add(lbeMatKhau);
    
        txtMatKhauDangKy = new JPasswordField();
        txtMatKhauDangKy.setBounds(60, 260, 380, 35);
        pnForm.add(txtMatKhauDangKy);
    
        JLabel lbeXacNhanMatKhau = new JLabel("Nhập lại mật khẩu:");
        lbeXacNhanMatKhau.setForeground(Color.WHITE);
        lbeXacNhanMatKhau.setBounds(60, 310, 200, 30);
        pnForm.add(lbeXacNhanMatKhau);
    
        txtXacNhanMatKhau = new JPasswordField();
        txtXacNhanMatKhau.setBounds(60, 340, 380, 35);
        pnForm.add(txtXacNhanMatKhau);
    
        btnDangKy = new JButton("Đăng ký");
        btnDangKy.setBounds(60, 400, 380, 40);
        btnDangKy.setBackground(new Color(33, 150, 243));
        btnDangKy.setForeground(Color.WHITE);
        btnDangKy.setFocusPainted(false);
        pnForm.add(btnDangKy);
    
        btnHienThiDangNhap = new JButton("Đã có tài khoản? Đăng nhập");
        btnHienThiDangNhap.setBounds(60, 450, 380, 30);
        pnForm.add(btnHienThiDangNhap);
    
        pnForm.repaint();
        pnForm.revalidate();

        // Thêm sự kiện cho các nút trong form đăng ký
        controller.themSuKien();
    }

    public void hienThiFormQuenMatKhau() {
        pnForm.removeAll();
        pnForm.setLayout(null);

        JLabel lbeTieuDe = new JLabel("QUÊN MẬT KHẨU");
        lbeTieuDe.setForeground(Color.WHITE);
        lbeTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lbeTieuDe.setBounds(120, 30, 300, 40);
        pnForm.add(lbeTieuDe);

        JLabel lbeEmail = new JLabel("Nhập email:");
        lbeEmail.setForeground(Color.WHITE);
        lbeEmail.setBounds(60, 120, 100, 30);
        pnForm.add(lbeEmail);

        txtEmailQuenMatKhau = new JTextField();
        txtEmailQuenMatKhau.setBounds(60, 160, 380, 35);
        pnForm.add(txtEmailQuenMatKhau);

        btnQuenMatKhau = new JButton("Gửi yêu cầu");
        btnQuenMatKhau.setBounds(60, 220, 380, 40);
        btnQuenMatKhau.setBackground(new Color(33, 150, 243));
        btnQuenMatKhau.setForeground(Color.WHITE);
        btnQuenMatKhau.setFocusPainted(false);
        pnForm.add(btnQuenMatKhau);

        btnHienThiDangNhap = new JButton("Quay lại đăng nhập");
        btnHienThiDangNhap.setBounds(60, 280, 380, 30);
        pnForm.add(btnHienThiDangNhap);

        pnForm.repaint();
        pnForm.revalidate();

        controller.themSuKien();
    }

}
