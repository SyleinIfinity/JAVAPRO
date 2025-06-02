package BLL;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import DLL.DA.NguoiDung;
import DLL.DO.NguoiDungDAO;
import GUI.view_Account;
import GUI.view_main;

public class ctl_Account implements ActionListener {
    private view_main vMain;
    private NguoiDungDAO ndDAO;
    private view_Account view;

    @Override
    public void actionPerformed(ActionEvent e){
        Object source = e.getSource();
        if (source == view.btn_edit) {
            ChinhSua();
        }
        if (source == view.btn_save) {
            saveChanges();
        }
        if (source == view.btn_cancel) {
            cancelChanges();
        }
        if (source == view.btn_edit) {
            // ChinhSua();
        }

    }

    public ctl_Account(view_Account vAccount,view_main vMain) {
        this.view = vAccount;
        this.vMain = vMain;
        this.ndDAO = new NguoiDungDAO();

        this.view.btn_edit.addActionListener(this);
        this.view.btn_save.addActionListener(this);
        this.view.btn_cancel.addActionListener(this);
        this.view.btn_close.addActionListener(this);

        loadAccountDetails();
    }

    private void loadAccountDetails() {
        NguoiDung nd = ndDAO.getNguoiDung(vMain.getMaNguoiDung());
        if (nd != null) {
            view.populateFields(
                nd.getMaNguoiDung(),
                nd.getTenNguoiDung(),
                nd.getNgaySinh(),
                nd.getSDT(),
                nd.getEmail(),
                nd.getMatKhau(),
                String.valueOf(nd.getSoDuTaiKhoan())
            );
        } else {
            // Handle case where user is not found
            view.populateFields("", "", "", "", "", "", "0");
        }
        if (nd.getMaVaiTro().equals("VT001") || nd.getMaVaiTro().equals("VT002")) {
            view.populateFields(nd.getMaNguoiDung(),
                    nd.getTenNguoiDung(),
                    nd.getNgaySinh(),
                    nd.getSDT(),
                    nd.getEmail(),
                    nd.getMatKhau());
            view.txt_maND.setEditable(false);
            view.txt_tenND.setEditable(false);
            view.txt_ngaySinh.setEditable(false);
            view.txt_sdt.setEditable(false);
            view.txt_emailND.setEditable(false);
            view.txt_matKhau.setEditable(false);
            view.lbe_soDuTaiKhoan.setVisible(false);
            view.txt_soDuTaiKhoan.setVisible(false);
        } else if (nd.getMaVaiTro().equals("VT003")) {
            view.populateFields(nd.getMaNguoiDung(),
                    nd.getTenNguoiDung(),
                    nd.getNgaySinh(),
                    nd.getSDT(),
                    nd.getEmail(),
                    nd.getMatKhau(),
                    String.valueOf(nd.getSoDuTaiKhoan()));
                    view.txt_maND.setEditable(false);
            view.txt_tenND.setEditable(false);
            view.txt_ngaySinh.setEditable(false);
            view.txt_sdt.setEditable(false);
            view.txt_emailND.setEditable(false);
            view.txt_matKhau.setEditable(false);
            view.txt_soDuTaiKhoan.setEditable(false);
        }

    }

    private void ChinhSua(){
        view.txt_maND.setEditable(true);
        view.txt_tenND.setEditable(true);
        view.txt_ngaySinh.setEditable(true);
        view.txt_sdt.setEditable(true);
        view.txt_emailND.setEditable(true);
        view.txt_matKhau.setEditable(true);
        view.txt_soDuTaiKhoan.setEditable(true);
    }

    public void saveChanges() {
        String maND = view.txt_maND.getText();
        String tenND = view.txt_tenND.getText();
        String ngaySinh = view.txt_ngaySinh.getText();
        String sdt = view.txt_sdt.getText();
        String email = view.txt_emailND.getText();
        String matKhau = view.txt_matKhau.getText();
        String soDuTaiKhoan = view.txt_soDuTaiKhoan.getText();

        NguoiDung nd = new NguoiDung(maND, tenND, ngaySinh, sdt, email, matKhau, Double.parseDouble(soDuTaiKhoan), vMain.getMaVaiTro(), 1);
        ndDAO.capNhatNguoiDung(nd);

        
        view.txt_maND.setEditable(false);
        view.txt_tenND.setEditable(false);
        view.txt_ngaySinh.setEditable(false);
        view.txt_sdt.setEditable(false);
        view.txt_emailND.setEditable(false);
        view.txt_matKhau.setEditable(false);
        view.txt_soDuTaiKhoan.setEditable(false);

        loadAccountDetails();
    }

    public void cancelChanges() {
        loadAccountDetails();

        view.txt_maND.setEditable(false);
        view.txt_tenND.setEditable(false);
        view.txt_ngaySinh.setEditable(false);
        view.txt_sdt.setEditable(false);
        view.txt_emailND.setEditable(false);
        view.txt_matKhau.setEditable(false);
        view.txt_soDuTaiKhoan.setEditable(false);
    }
    
}
