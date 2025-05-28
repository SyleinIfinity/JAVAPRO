package CONTROLLER.APP.ADMIN;

// import java.awt.List;
import java.awt.event.ActionListener;

import MODEL.DAO.ChiNhanhKhachSanDAO;
import MODEL.ENTITY.ChiNhanhKhachSan;
import VIEW.view_main;
import VIEW.ADMIN.view_ChiNhanh;
import java.util.List;

import javax.swing.table.DefaultTableModel;

public class ctl_ChiNhanh implements ActionListener {
    private view_ChiNhanh view;
    private view_main vMain;
    private ChiNhanhKhachSanDAO cnDAO;
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        Object source = e.getSource();
        // if (source == view.btn_add) {
        //     addChiNhanh();
        // } else if (source == view.btn_edit) {
        //     editChiNhanh();
        // } else if (source == view.btn_delete) {
        //     deleteChiNhanh();
        // } else if (source == view.btn_save) {
        //     saveChanges();
        // } else if (source == view.btn_cancel) {
        //     cancelChanges();
        // }
    }

    public ctl_ChiNhanh(view_ChiNhanh view, view_main vMain) {
        this.view = view;
        this.vMain = vMain;
        this.cnDAO = new ChiNhanhKhachSanDAO();

        // Add action listeners for buttons
        // view.btn_add.addActionListener(this);
        // view.btn_edit.addActionListener(this);
        // view.btn_delete.addActionListener(this);
        // view.btn_save.addActionListener(this);
        // view.btn_cancel.addActionListener(this);

        // Load initial data
        loadChiNhanhData();
    }

    private void loadChiNhanhData() {
        List<ChiNhanhKhachSan> chiNhanhs = cnDAO.listCHINHANHKHACHSAN().values().stream().toList();
        DefaultTableModel model =  new DefaultTableModel(
            new String[]{"Mã Chi Nhánh", "Tên Chi Nhánh", "Địa Chỉ", "Số Điện Thoại"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Integer.class : String.class;
            }
        };
        for (ChiNhanhKhachSan cn : chiNhanhs) {
            model.addRow(new Object[]{
                cn.getMaChiNhanh(),
                cn.getTenChiNhanh(),
                cn.getDiaChi(),
                cn.getSDT()
            });
        }
        view.table.setModel(model);
    }

}
