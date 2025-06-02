package BLL;

import javax.swing.*;

import GUI.view_LOGIN;
import GUI.view_TrangChu;

import java.awt.*;

public class ctl_TrangChu {
    private view_TrangChu view;
    private JPanel pnContent;
    private view_LOGIN viewLogin;

    public ctl_TrangChu(view_TrangChu view, JPanel pnContent) {
        this.view = view;
        this.pnContent = pnContent;
    }

    public void hienThiFormDangNhap() {
        // Tạo form đăng nhập mới
        // viewLogin = new view_LOGIN();
        
        // Tạo panel chứa form đăng nhập
        JPanel pnLogin = new JPanel(new BorderLayout());
        pnLogin.setBackground(new Color(30, 32, 36));
        pnLogin.add(viewLogin, BorderLayout.CENTER);
        
        // Thêm form đăng nhập vào panel nội dung
        pnContent.add(pnLogin, BorderLayout.EAST);
        
        // Cập nhật giao diện
        pnContent.repaint();
        pnContent.revalidate();
    }
}
