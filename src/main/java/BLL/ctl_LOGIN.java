package BLL;

import javax.swing.*;

import DLL.DA.NguoiDung;
import DLL.DO.NguoiDungDAO;
import GUI.view_LOGIN;
import GUI.view_main;

import java.awt.event.*;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

import UTILS.API.GMAIL.GMailer;

public class ctl_LOGIN implements ActionListener {
    private view_LOGIN view;
    private JPanel pnForm;
    private NguoiDungDAO nguoiDungDAO;
    private view_main vMain;

    public ctl_LOGIN(view_LOGIN view, JPanel pnForm, view_main vMain) {
        this.view = view;
        this.pnForm = pnForm;
        nguoiDungDAO = new NguoiDungDAO();
        this.vMain = vMain;
        themSuKien();
    }

    public void themSuKien() {
        // Sự kiện cho nút đăng nhập
        if (view.btnDangNhap != null) {
            view.btnDangNhap.addActionListener(this);
        }

        // Sự kiện cho nút đăng ký
        if (view.btnDangKy != null) {
            view.btnDangKy.addActionListener(this);
        }

        // Sự kiện cho nút quên mật khẩu
        if (view.btnQuenMatKhau != null) {
            view.btnQuenMatKhau.addActionListener(this);
        }

        // Sự kiện chuyển form
        if (view.btnHienThiDangNhap != null) {
            view.btnHienThiDangNhap.addActionListener(this);
        }

        if (view.btnHienThiDangKy != null) {
            view.btnHienThiDangKy.addActionListener(this);
        }

        if (view.btnHienThiQuenMatKhau != null) {
            view.btnHienThiQuenMatKhau.addActionListener(this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.btnDangNhap) {
            xuLyDangNhap();
        } else if (e.getSource() == view.btnDangKy) {
            xuLyDangKy();
        } else if (e.getSource() == view.btnQuenMatKhau) {
            xuLyQuenMatKhau();
        } else if (e.getSource() == view.btnHienThiDangNhap) {
            view.hienThiFormDangNhap();
        } else if (e.getSource() == view.btnHienThiDangKy) {
            view.hienThiFormDangKy();
        } else if (e.getSource() == view.btnHienThiQuenMatKhau) {
            view.hienThiFormQuenMatKhau();
        }
    }

    private void xuLyDangNhap() {
        String email = view.txtEmail.getText();
        String matKhau = new String(view.txtMatKhau.getPassword());

        if (email.isEmpty() || matKhau.isEmpty()) {
            JOptionPane.showMessageDialog(pnForm, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        NguoiDung nguoiDung = nguoiDungDAO.checkLogin(email, matKhau);
        if (nguoiDung != null) {
            JOptionPane.showMessageDialog(pnForm, "Đăng nhập thành công!");
            vMain.setMaNguoiDung(nguoiDung.getMaNguoiDung());
            vMain.setMaVaiTro(nguoiDung.getMaVaiTro());
            
            // Load lại giao diện view_main
            vMain.dispose(); // Đóng cửa sổ hiện tại
            view_main newMain = new view_main(nguoiDung.getMaNguoiDung(), nguoiDung.getMaVaiTro()); // Tạo cửa sổ mới
            newMain.setVisible(true); // Hiển thị cửa sổ mới
        } else {
            JOptionPane.showMessageDialog(pnForm, "Email hoặc mật khẩu không đúng!");
        }
    }

    private void xuLyDangKy() {
        String hoTen = view.txtHoTen.getText();
        String email = view.txtEmailDangKy.getText();
        String matKhau = new String(view.txtMatKhauDangKy.getPassword());
        String xacNhanMatKhau = new String(view.txtXacNhanMatKhau.getPassword());

        if (hoTen.isEmpty() || email.isEmpty() || matKhau.isEmpty() || xacNhanMatKhau.isEmpty()) {
            JOptionPane.showMessageDialog(pnForm, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        if (!matKhau.equals(xacNhanMatKhau)) {
            JOptionPane.showMessageDialog(pnForm, "Mật khẩu xác nhận không khớp!");
            return;
        }

        if (nguoiDungDAO.checkGmail(email) == false) {
            JOptionPane.showMessageDialog(pnForm, "Email đã tồn tại!");
            return;
        }

        NguoiDung nguoiDung = new NguoiDung(hoTen, null, null, email, matKhau, 0.0, "VT003", 1);
        if (nguoiDungDAO.themNguoiDung(nguoiDung) > 0) {
            JOptionPane.showMessageDialog(pnForm, "Đăng ký thành công!");
            view.hienThiFormDangNhap();
        } else {
            JOptionPane.showMessageDialog(pnForm, "Đăng ký thất bại!");
        }
    }

    private void xuLyQuenMatKhau() {
        String email = view.txtEmailQuenMatKhau.getText();

        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(pnForm, "Vui lòng nhập email!");
            return;
        }

        if (nguoiDungDAO.checkGmail(email)) {
            JOptionPane.showMessageDialog(pnForm, "Email không tồn tại trong hệ thống!");
            return;
        }

        try {
            // Tạo mã OTP ngẫu nhiên 6 số
            String matKhau = "Mật khẩu của bạn là: " + nguoiDungDAO.getPass(email);
            // Gửi email chứa mã OTP
            GMailer.sendMain(matKhau, email);
            JOptionPane.showMessageDialog(pnForm, "Mật khẩu đã được gửi. Vui lòng kiểm tra email của bạn!");
            view.hienThiFormDangNhap();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(pnForm, e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(pnForm, "Có lỗi xảy ra khi gửi email. Vui lòng thử lại sau!");
            e.printStackTrace();
        }
    }
}
