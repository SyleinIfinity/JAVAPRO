package CONTROLLER.APP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Panel;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import MODEL.DAO.NguoiDungDAO;
import MODEL.ENTITY.NguoiDung;
import VIEW.*;
// import VIEW.STAFF.*;
import VIEW.ADMIN.*;
// import VIEW.CLIENT.*;
import VIEW.view_main;

public class ctl_main implements ActionListener {
    private view_main vMain;
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == vMain.btn_DangNhap) {
            show(new view_LOGIN(vMain));
        }
        if (source == vMain.btn_TrangChu) {
            show(new view_TrangChu(vMain));
        }
        if (source == vMain.btn_Account) {
            // show(new view_Account(vMain));
        }
        if (source == vMain.btn_Phong) {
            // show(new view_Phong(vMain));
        }
        if (source == vMain.btn_KhachSan) {
            // show(new view_KhachSan(vMain));
        }
        if (source == vMain.btn_DichVu) {
            // show(new view_DichVu(vMain));
        }
        if (source == vMain.btn_KhachHang) {
            // show(new view_KhachHang(vMain));
        }
        if (source == vMain.btn_NhanVien) {
            // show(new view_NhanVien(vMain));
        }
        if (source == vMain.btn_DoanhThu) {
            // show(new view_DoanhThu(vMain));
        }
        if (source == vMain.btn_DonDatPhong) {
            // show(new view_DonDatPhong(vMain));
        }
        if (source == vMain.btn_HoaDon) {
            // show(new view_HoaDon(vMain));
        }
        if (source == vMain.btn_TrangThaiPhong) {
            // show(new view_TrangThaiPhong(vMain));
        }
        if (source == vMain.btn_NapRut) {
            // show(new view_NapRut(vMain));
        }
        if (source == vMain.btn_DatPhong) {
            // show(new view_DatPhong(vMain));
        }
        if (source == vMain.btn_TraPhong) {
            // show(new view_TraPhong(vMain));
        }
    }

    public ctl_main(view_main vMain, String maNguoiDung, String maVaiTro){
        this.vMain = vMain;
        // this.nguoiDungDAO = new NguoiDungDAO();
        // this.nguoiDung = nguoiDungDAO.getNguoiDung(maNguoiDung);
        // this.maVaiTro = maVaiTro;

        if (this.vMain.maNguoiDung == null && this.vMain.maVaiTro == null) {
            this.vMain.btn_DangNhap.setVisible(true);
            this.vMain.btn_Account.setVisible(false);
        } else {
            if (this.vMain.maVaiTro.equals("VT001")) {
                this.vMain.btn_DonDatPhong.setVisible(false);
                this.vMain.btn_HoaDon.setVisible(false);
                this.vMain.btn_TrangThaiPhong.setVisible(false);
                this.vMain.btn_NapRut.setVisible(false);
                this.vMain.btn_DatPhong.setVisible(false);
                this.vMain.btn_TraPhong.setVisible(false);
            } else if (this.vMain.maVaiTro.equals("TV002")) {
                this.vMain.btn_NapRut.setVisible(false);
                this.vMain.btn_DatPhong.setVisible(false);
                this.vMain.btn_TraPhong.setVisible(false);
                this.vMain.btn_Phong.setVisible(false);
                this.vMain.btn_KhachSan.setVisible(false);
                this.vMain.btn_DichVu.setVisible(false);
                this.vMain.btn_KhachHang.setVisible(false);
                this.vMain.btn_NhanVien.setVisible(false);
                this.vMain.btn_DoanhThu.setVisible(false);
            } else if (this.vMain.maVaiTro.equals("TV003")) {
                this.vMain.btn_DonDatPhong.setVisible(false);
                this.vMain.btn_HoaDon.setVisible(false);
                this.vMain.btn_TrangThaiPhong.setVisible(false);
                this.vMain.btn_Phong.setVisible(false);
                this.vMain.btn_KhachSan.setVisible(false);
                this.vMain.btn_DichVu.setVisible(false);
                this.vMain.btn_KhachHang.setVisible(false);
                this.vMain.btn_NhanVien.setVisible(false);
                this.vMain.btn_DoanhThu.setVisible(false);
            } else {

            }
            this.vMain.btn_DangNhap.setVisible(false);
            this.vMain.btn_Account.setVisible(true);
        }


        this.vMain.btn_DangNhap.addActionListener(this);
        this.vMain.btn_TrangChu.addActionListener(this);
    }

    public void DangNhap(){
        vMain.pn_content.removeAll();
        vMain.pn_content.setLayout(new BorderLayout());
        vMain.pn_content.add(new view_LOGIN(this.vMain));
        vMain.pn_content.revalidate();
        vMain.pn_content.repaint();
    }

    public void TrangChu(){
        vMain.pn_content.removeAll();
        vMain.pn_content.add(new view_TrangChu(this.vMain));
        vMain.pn_content.revalidate();
        vMain.pn_content.repaint();
    }

    public void show(JPanel panel){
        // panel = new JPanel();
        vMain.pn_content.removeAll();
        vMain.pn_content.setLayout(new BorderLayout());
        vMain.pn_content.add(panel);
        vMain.pn_content.revalidate();
        vMain.pn_content.repaint();
    }

}
