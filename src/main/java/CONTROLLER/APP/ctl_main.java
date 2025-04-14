package CONTROLLER.APP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import MODEL.DAO.NguoiDungDAO;
import MODEL.ENTITY.NguoiDung;
import VIEW.view_LOGIN;
import VIEW.view_main;

public class ctl_main {
    private view_main vMain;
    private NguoiDungDAO nguoiDungDAO;
    private NguoiDung nguoiDung;

    public ctl_main(view_main vMain, String maNguoiDung){
        this.vMain = vMain;
        this.nguoiDungDAO = new NguoiDungDAO();
        this.nguoiDung = nguoiDungDAO.getNguoiDung(maNguoiDung);
    }

    public void XinChao(){
        vMain.lblTenNguoiDung.setText("Xin chào: " + nguoiDung.getTenNguoiDung());
    }

    public void DangNhap(){
        new view_LOGIN().setVisible(true);
        vMain.dispose();
    }

    public void DangXuat(){

    }
}
