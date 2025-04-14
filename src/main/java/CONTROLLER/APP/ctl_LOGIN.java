package CONTROLLER.APP;

import javax.swing.JOptionPane;

import MODEL.DAO.NguoiDungDAO;
import MODEL.ENTITY.NguoiDung;
import VIEW.view_LOGIN;
import VIEW.view_main;

public class ctl_LOGIN {
    view_LOGIN vLogin;
    NguoiDungDAO nguoiDungDAO;

    public ctl_LOGIN(view_LOGIN vLogin){
        this.vLogin = vLogin;
        nguoiDungDAO = new NguoiDungDAO();
    }

    public void KiemTraVaiTro(){
        String tenDangNhap = vLogin.txt_TenDangNhap.getText();
        String matKhau = new String(vLogin.txt_MatKhau.getPassword());

        if (tenDangNhap.isEmpty() || matKhau.isEmpty()) {
            JOptionPane.showMessageDialog(vLogin, "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu!");
            return;
        }

        boolean dangNhapThanhCong = false;

        for (NguoiDung nd : nguoiDungDAO.getListNGUOIDUNG().values()) {
            if ((nd.getEmail().equals(tenDangNhap) || nd.getSDT().equals(tenDangNhap)) && nd.getMatKhau().equals(matKhau)) {
                dangNhapThanhCong = true;

                String vaiTro = nd.getMaVaiTro().trim();
                System.out.println(vaiTro);
                switch (vaiTro) {
                    case "R001":
                        JOptionPane.showMessageDialog(vLogin, "Chào Khách Hàng: " + nd.getTenNguoiDung());
                        new view_main(nd.getMaNguoiDung()).setVisible(true);
                        vLogin.dispose();
                        break;
                    case "R002":
                        JOptionPane.showMessageDialog(vLogin, "Chào Nhân viên: " + nd.getTenNguoiDung());
                        new view_main(nd.getMaNguoiDung()).setVisible(true);
                        break;
                    case "R003":
                        JOptionPane.showMessageDialog(vLogin, "Chào Quản lý: " + nd.getTenNguoiDung());
                        new view_main(nd.getMaNguoiDung()).setVisible(true);
                        vLogin.dispose();
                        break;
                    default:
                        JOptionPane.showMessageDialog(vLogin, "Vai trò không xác định!");
                }
                // vLogin.dispose();
                break;
            }
        }

        if (!dangNhapThanhCong) {
            JOptionPane.showMessageDialog(vLogin, "Sai tên đăng nhập hoặc mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
