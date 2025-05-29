package CONTROLLER.APP.ADMIN;

// import java.awt.List;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import MODEL.DAO.ChiNhanhKhachSanDAO;
import MODEL.ENTITY.ChiNhanhKhachSan;
import VIEW.view_main;
import VIEW.ADMIN.view_ChiNhanh;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import com.google.api.services.gmail.model.Message;

public class ctl_ChiNhanh implements ActionListener {
    private view_ChiNhanh view;
    private view_main vMain;
    private ChiNhanhKhachSanDAO cnDAO;
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        Object source = e.getSource();
        if (source == view.addButton) {
            addChiNhanh();
        } else if (source == view.editButton) {
            updateChiNhanh();
        } else if (source == view.deleteButton) {
            deleteChiNhanh();
        } else if (source == view.refreshButton) {
            // saveChanges();
        } else if (source == view.clearButton) {
            // cancelChanges();
        }
    }

    public ctl_ChiNhanh(view_ChiNhanh view, view_main vMain) {
        this.view = view;
        this.vMain = vMain;
        this.cnDAO = new ChiNhanhKhachSanDAO();

        this.view.addButton.addActionListener(this);
        this.view.editButton.addActionListener(this);
        this.view.deleteButton.addActionListener(this);
        this.view.refreshButton.addActionListener(this);
        this.view.clearButton.addActionListener(this);
        this.view.table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                MouseClicked();
            }
        });
        loadChiNhanhData();
    }

    private void loadChiNhanhData() {
        List<ChiNhanhKhachSan> chiNhanhs = cnDAO.listCHINHANHKHACHSAN().values().stream().toList();
        DefaultTableModel model =  new DefaultTableModel(
            new String[]{"STT","Mã Chi Nhánh", "Tên Chi Nhánh", "Địa Chỉ", "Số Điện Thoại"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Integer.class : String.class;
            }
        };

        int stt = 1;

        for (ChiNhanhKhachSan cn : chiNhanhs) {
            model.addRow(new Object[]{
                stt,
                cn.getMaChiNhanh(),
                cn.getTenChiNhanh(),
                cn.getDiaChi(),
                cn.getSDT()
            });
            stt++;
        }
        view.table.setModel(model);
        view.txtMaChiNhanh.setEditable(false);
    }

    private void MouseClicked() {
        int selectedRow = view.table.getSelectedRow();
        if (selectedRow != -1) {
            String maChiNhanh = (String) view.table.getValueAt(selectedRow, 1);
            ChiNhanhKhachSan cn = cnDAO.getChiNhanhKhachSan(maChiNhanh);
            if (cn != null) {
                view.txtMaChiNhanh.setText(cn.getMaChiNhanh());
                view.txtTenChiNhanh.setText(cn.getTenChiNhanh());
                view.txtDiaChi.setText(cn.getDiaChi());
                view.txtSDT.setText(cn.getSDT());
            }
        }
    }

    private void addChiNhanh() {
        String maChiNhanh = view.txtMaChiNhanh.getText();
        String tenChiNhanh = view.txtTenChiNhanh.getText();
        String diaChi = view.txtDiaChi.getText();
        String sdt = view.txtSDT.getText();

        ChiNhanhKhachSan cn = new ChiNhanhKhachSan(maChiNhanh, tenChiNhanh, diaChi, sdt);
        int result = cnDAO.themChiNhanhKhachSan(cn);
        if (result > 0) {
            loadChiNhanhData();
            JOptionPane.showMessageDialog(view, "Thêm chi nhánh thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            // hoặc dùng view.showMessage("Thêm chi nhánh thành công!");
        } else {
            JOptionPane.showMessageDialog(view, "Lỗi khi thêm chi nhánh!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            // hoặc dùng view.showMessage("Lỗi khi thêm chi nhánh!");
        }
    }

    private void updateChiNhanh(){
        if (view.table.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn chi nhánh để cập nhật!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maChiNhanh = view.txtMaChiNhanh.getText();
        String tenChiNhanh = view.txtTenChiNhanh.getText();
        String diaChi = view.txtDiaChi.getText();
        String sdt = view.txtSDT.getText();

        ChiNhanhKhachSan cn = new ChiNhanhKhachSan(maChiNhanh, tenChiNhanh, diaChi, sdt);
        int result = cnDAO.capNhatChiNhanhKhachSan(cn);
        if (result > 0) {
            loadChiNhanhData();
            JOptionPane.showMessageDialog(view, "Cập nhật chi nhánh thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            // hoặc dùng view.showMessage("Thêm chi nhánh thành công!");
        } else {
            JOptionPane.showMessageDialog(view, "Lỗi khi Cập nhật chi nhánh!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            // hoặc dùng view.showMessage("Lỗi khi thêm chi nhánh!");
        }
    }

    private void deleteChiNhanh(){
        if (view.table.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn chi nhánh để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String maChiNhanh = view.txtMaChiNhanh.getText();

        int result = cnDAO.xoaChiNhanhKhachSan(maChiNhanh);
        if (result > 0) {
            loadChiNhanhData();
            JOptionPane.showMessageDialog(view, "Xóa chi nhánh thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            // hoặc dùng view.showMessage("Thêm chi nhánh thành công!");
        } else {
            JOptionPane.showMessageDialog(view, "Lỗi khi xóa chi nhánh!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            // hoặc dùng view.showMessage("Lỗi khi thêm chi nhánh!");
        }
    }

}
