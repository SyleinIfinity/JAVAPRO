package VIEW;

import javax.swing.*;

import CONTROLLER.APP.ctl_main;

import java.awt.event.*;

import MODEL.ENTITY.NguoiDung;

public class view_main extends JFrame implements ActionListener {
    public JLabel lblTenNguoiDung;
    public JButton btnXinChao, btnDangNhap, btnDangXuat;
    public String maNguoiDung = null;
    ctl_main cMain;

    // public view_main() {
    // }

    public view_main(String maNguoiDung) {
        setTitle("Quản lý khách sạn");
        setSize(430, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        this.maNguoiDung = maNguoiDung;
        this.cMain = new ctl_main(this, maNguoiDung);
        initComponents();
    }

    public void initComponents() {
        btnXinChao = new JButton("Xin chào");
        btnXinChao.setBounds(150, 50, 120, 30);
        btnXinChao.addActionListener(this);
        add(btnXinChao);

        lblTenNguoiDung = new JLabel();
        lblTenNguoiDung.setBounds(100, 100, 300, 30);
        add(lblTenNguoiDung);

        btnDangNhap = new JButton("Đăng nhập");
        btnDangNhap.setBounds(150, 150, 120, 30);
        btnDangNhap.addActionListener(this);
        add(btnDangNhap);

        btnDangXuat = new JButton("Đăng Xuất");
        btnDangXuat.setBounds(150, 200, 120, 30);
        btnDangXuat.addActionListener(this);
        add(btnDangXuat);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnXinChao && maNguoiDung != null) {
            cMain.XinChao();
        } else if (e.getSource() == btnDangNhap) {
            new view_LOGIN().setVisible(true);
            this.dispose();
        }
    }

    public static void main(String[] args) {
        new view_main(null).setVisible(true);
    }
}
