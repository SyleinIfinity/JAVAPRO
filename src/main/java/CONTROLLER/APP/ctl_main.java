package CONTROLLER.APP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Panel;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import MODEL.DAO.NguoiDungDAO;
import MODEL.ENTITY.NguoiDung;
import VIEW.view_TrangChu;
import VIEW.view_LOGIN;
// import VIEW.view_LOGIN;
import VIEW.view_main;

public class ctl_main implements ActionListener {
    private view_main vMain;
    // private NguoiDungDAO nguoiDungDAO;
    // private NguoiDung nguoiDung;
    // private String maVaiTro;
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == vMain.btn_DangNhap) {
            DangNhap();
        } else if (source == vMain.btn_TrangChu) {
            TrangChu();
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
        vMain.pn_content.add(new view_TrangChu());
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
