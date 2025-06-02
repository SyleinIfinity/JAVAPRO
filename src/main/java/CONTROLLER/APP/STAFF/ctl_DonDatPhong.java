package CONTROLLER.APP.STAFF;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import javax.swing.table.DefaultTableModel;

import MODEL.DAO.ChiNhanhKhachSanDAO;
import MODEL.DAO.DatPhongDAO;
import MODEL.DAO.NguoiDungDAO;
import MODEL.DAO.PhongDAO;
import MODEL.ENTITY.ChiNhanhKhachSan;
import MODEL.ENTITY.DatPhong;
import MODEL.ENTITY.NguoiDung;
import MODEL.ENTITY.Phong;
import VIEW.view_main;
import VIEW.STAFF.view_DonDatPhong;
import VIEW.STAFF.view_DonDatPhong.Record;

public class ctl_DonDatPhong implements ActionListener {
    private view_DonDatPhong view;
    private view_main vMain;
    private DatPhongDAO dpDAO;
    private ChiNhanhKhachSanDAO cnDAO;
    private NguoiDungDAO ndDAO;
    private PhongDAO pDAO;
    private JButton btnDat, btnHuy;
    
    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        Object source = e.getSource();
        
        if (source == btnDat) {
            datPhong();
        } else if (source == btnHuy) {
            huyPhong();
        }
    }

    public ctl_DonDatPhong(view_DonDatPhong view, view_main vMain) {
        this.view = view;
        this.vMain = vMain;
        this.dpDAO = new DatPhongDAO();
        this.cnDAO = new ChiNhanhKhachSanDAO();
        this.ndDAO = new NguoiDungDAO();
        this.pDAO = new PhongDAO();

        // Get references to buttons
        btnDat = view.getBtnDat();
        btnHuy = view.getBtnHuy();

        // Add action listeners to buttons
        btnDat.addActionListener(this);
        btnHuy.addActionListener(this);

        // Initialize data for dropdowns
        loadChiNhanhData();
        loadKhachHangData();
        
        // Add action listener to the Chi Nhanh combobox
        view.cbMaChiNhanh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePhongByChiNhanh();
            }
        });
        
        // Load initial phong data (empty until a chi nhanh is selected)
        loadPhongData(null);
        
        // Load initial data for table
        loadDonDatPhongData();
        
        // Clear form fields to start with empty form
        view.clearFormFields();
    }

    private void datPhong() {
        try {
            String maChiNhanh = "";
            String maPhong = "";
            String maKhachHang = "";
            String dichVuSuDung = "DV001";

            if (view.cbMaChiNhanh.getSelectedItem() != null) {
                Record chiNhanhRecord = (Record) view.cbMaChiNhanh.getSelectedItem();
                maChiNhanh = chiNhanhRecord.getColumns()[0];
            } else {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn chi nhánh!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (view.cbMaPhong.getSelectedItem() != null) {
                Record phongRecord = (Record) view.cbMaPhong.getSelectedItem();
                maPhong = phongRecord.getColumns()[0];
                Phong phong = pDAO.getPhong(maPhong);
                if (phong != null && !phong.getTrangThai().equals("Trống") && !phong.getTrangThai().equals("Đã đặt trước")) {
                    JOptionPane.showMessageDialog(view, "Phòng này hiện không khả dụng (Trạng thái: " + phong.getTrangThai() + ")!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn phòng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (view.cbDichVu.getSelectedItem() != null) {
                Record khachHangRecord = (Record) view.cbDichVu.getSelectedItem();
                maKhachHang = khachHangRecord.getColumns()[0];
            } else {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String soNguoi = view.getTxtSoNguoi().getText().trim();
            if (soNguoi.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Vui lòng nhập số người!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int soNguoiValue = Integer.parseInt(soNguoi);
            if (soNguoiValue <= 0) {
                JOptionPane.showMessageDialog(view, "Số người phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Phong phong = pDAO.getPhong(maPhong);
            if (phong != null && soNguoiValue > 6) {
                JOptionPane.showMessageDialog(view, "Số người vượt quá sức chứa tối đa của phòng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Date ngayThueDate = (Date) view.getSpinnerNgayThue().getValue();
            Date ngayTraDate = (Date) view.getSpinnerNgayTra().getValue();
            
            Date today = new Date();
            today.setHours(0);
            today.setMinutes(0);
            today.setSeconds(0);
            
            if (ngayThueDate.before(today)) {
                JOptionPane.showMessageDialog(view, "Ngày thuê phải từ ngày hôm nay trở đi!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!ngayTraDate.after(ngayThueDate)) {
                JOptionPane.showMessageDialog(view, "Ngày trả phải sau ngày thuê!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");
            String ngayThue = dbFormat.format(ngayThueDate);
            String ngayTra = dbFormat.format(ngayTraDate);
            
            String trangThai = "Đã đặt";
            
            DatPhong datPhong = new DatPhong(null, maKhachHang, maPhong, soNguoi, dichVuSuDung, ngayThue, ngayTra, trangThai);
            
            btnDat.setEnabled(false);
            JOptionPane optionPane = new JOptionPane("Đang xử lý...", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
            JDialog dialog = optionPane.createDialog("Thông báo");
            Timer timer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dialog.dispose();
                }
            });
            timer.setRepeats(false);
            timer.start();
            dialog.setVisible(true);
            
            try {
                int result = dpDAO.themDatPhong(datPhong);
                if (result > 0) {
                    Phong phongToUpdate = pDAO.getPhong(maPhong);
                    if (phongToUpdate != null) {
                        phongToUpdate.setTrangThai("Đã đặt trước");
                        int updateResult = pDAO.capNhatPhong(phongToUpdate);
                        if (updateResult > 0) {
                            System.out.println("Cập nhật trạng thái phòng thành công: " + maPhong + " -> Đã đặt trước");
                        } else {
                            System.out.println("Không thể cập nhật trạng thái phòng: " + maPhong);
                        }
                    }
                    JOptionPane.showMessageDialog(view, "Đặt phòng thành công!\nMã phòng: " + maPhong + "\nNgày thuê: " + formatDate(ngayThue) + "\nNgày trả: " + formatDate(ngayTra) + "\nTrạng thái phòng đã chuyển sang 'Đã đặt trước'", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    loadDonDatPhongData();
                    loadPhongData();
                    view.clearFormFields();
                } else {
                    JOptionPane.showMessageDialog(view, "Đặt phòng thất bại! Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } finally {
                btnDat.setEnabled(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi xử lý: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void huyPhong() {
        int selectedRow = view.table.getSelectedRow();
        if (selectedRow < 0 || view.table.getRowCount() == 0) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn đơn đặt phòng cần hủy!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maDatPhong = view.table.getValueAt(selectedRow, 0).toString();
        String maPhong = view.table.getValueAt(selectedRow, 3).toString();
        String trangThaiDon = "";

        DatPhong datPhong = dpDAO.getDatPhong(maDatPhong);
        if (datPhong == null) {
            JOptionPane.showMessageDialog(view, "Không tìm thấy thông tin đơn đặt phòng với mã: " + maDatPhong, "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        trangThaiDon = datPhong.getTrangThai();

        if ("Hoàn thành".equals(trangThaiDon)) {
            JOptionPane.showMessageDialog(view, "Không thể hủy đơn đặt phòng đã hoàn thành!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String message = "Bạn có chắc chắn muốn hủy đơn đặt phòng này không?\n\n" +
                         "Mã đặt phòng: " + maDatPhong + "\n" +
                         "Mã phòng: " + maPhong + "\n" +
                         "Ngày thuê: " + formatDate(datPhong.getNgayThuePhong()) + "\n" +
                         "Ngày trả: " + formatDate(datPhong.getNgayTraPhong());
        
        int confirm = JOptionPane.showConfirmDialog(view, message, "Xác nhận hủy", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        btnHuy.setEnabled(false);

        JOptionPane optionPane = new JOptionPane("Đang xử lý hủy đơn...", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
        JDialog dialog = optionPane.createDialog("Thông báo");
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        timer.setRepeats(false);
        timer.start();
        dialog.setVisible(true);

        try {
            int result = dpDAO.xoaDatPhong(maDatPhong);
            if (result > 0) {
                Phong phong = pDAO.getPhong(maPhong);
                if (phong != null) {
                    phong.setTrangThai("Trống");
                    int updateResult = pDAO.capNhatPhong(phong);
                    if (updateResult > 0) {
                        System.out.println("Cập nhật trạng thái phòng thành công: " + maPhong + " -> Trống");
                    } else {
                        System.out.println("Không thể cập nhật trạng thái phòng: " + maPhong);
                    }
                }

                JOptionPane.showMessageDialog(view, "Hủy đơn đặt phòng thành công!\nPhòng " + maPhong + " đã được đặt lại trạng thái thành Trống.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadDonDatPhongData();
                loadPhongData();
                view.clearFormFields();
            } else {
                JOptionPane.showMessageDialog(view, "Hủy đơn đặt phòng thất bại! Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi xử lý: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } finally {
            btnHuy.setEnabled(true);
        }
    }

    private void loadDonDatPhongData() {
        dpDAO.refreshData();
        HashMap<String, DatPhong> datPhongs = dpDAO.getListDATPHONG();
        List<DatPhong> sortedList = new ArrayList<>(datPhongs.values());
        Collections.sort(sortedList, (a, b) -> a.getMaDatPhong().compareTo(b.getMaDatPhong()));
        
        Object[][] data = new Object[sortedList.size()][8];
        int index = 0;
        for (DatPhong dp : sortedList) {
            data[index][0] = dp.getMaDatPhong();
            data[index][1] = dp.getMaNguoiDung();
            data[index][2] = getChiNhanhFromPhong(dp.getMaPhong());
            data[index][3] = dp.getMaPhong();
            data[index][4] = dp.getSoNguoi();
            data[index][5] = dp.getDichVuSuDung();
            data[index][6] = formatDate(dp.getNgayThuePhong());
            data[index][7] = formatDate(dp.getNgayTraPhong());
            index++;
        }
        
        String[] columnNames = {"Mã đặt phòng", "Mã khách hàng", "Mã chi nhánh", "Mã phòng", "Số người", "Dịch vụ", "Ngày thuê", "Ngày trả"};
        view.updateTableData(data, columnNames);
        view.table.repaint();
    }
    
    private String formatDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return "";
        }
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = inputFormat.parse(dateStr);
            return outputFormat.format(date);
        } catch (ParseException e) {
            return dateStr;
        }
    }
    
    public void loadDetailedDataForRow(String maDatPhong, String maKhachHang, String maChiNhanh, 
                                      String maPhong, String ngayThueStr, String ngayTraStr) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date ngayThue = inputFormat.parse(ngayThueStr);
            Date ngayTra = inputFormat.parse(ngayTraStr);
            view.setNgayThue(ngayThue);
            view.setNgayTra(ngayTra);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        ChiNhanhKhachSan chiNhanh = cnDAO.getChiNhanhKhachSan(maChiNhanh);
        if (chiNhanh != null) {
            view.setChiNhanhWithDetail(chiNhanh.getMaChiNhanh(), chiNhanh.getTenChiNhanh());
        } else {
            view.setChiNhanhWithDetail(maChiNhanh, "Không tìm thấy");
        }
        
        NguoiDung khachHang = ndDAO.getNguoiDung(maKhachHang);
        if (khachHang != null && "VT003".equals(khachHang.getMaVaiTro())) {
            view.setKhachHangWithDetail(khachHang.getMaNguoiDung(), khachHang.getTenNguoiDung());
        } else {
            view.setKhachHangWithDetail(maKhachHang, "Không tìm thấy hoặc không phải khách hàng");
        }
        
        Phong phong = pDAO.getPhong(maPhong);
        if (phong != null) {
            view.setPhongWithDetail(phong.getMaPhong(), "Phòng " + phong.getSoPhong(), "Tầng " + phong.getSoTang());
        } else {
            view.setPhongWithDetail(maPhong, "Phòng " + maPhong, "Tầng 1");
        }
    }
    
    private String getChiNhanhFromPhong(String maPhong) {
        Phong phong = pDAO.getPhong(maPhong);
        if (phong != null) {
            return phong.getMaChiNhanh();
        }
        return "CN001";
    }

    private void loadChiNhanhData() {
        view.clearChiNhanhComboBox();
        HashMap<String, ChiNhanhKhachSan> chiNhanhs = cnDAO.listCHINHANHKHACHSAN();
        for (ChiNhanhKhachSan cn : chiNhanhs.values()) {
            String[] data = new String[] {cn.getMaChiNhanh(), cn.getTenChiNhanh()};
            view.addChiNhanhItem(data);
        }
    }

    private void loadKhachHangData() {
        view.clearKhachHangComboBox();
        HashMap<String, NguoiDung> nguoiDungs = ndDAO.getListNGUOIDUNG();
        for (NguoiDung nd : nguoiDungs.values()) {
            if ("VT003".equals(nd.getMaVaiTro())) {
                String[] data = new String[] {nd.getMaNguoiDung(), nd.getTenNguoiDung()};
                view.addKhachHangItem(data);
            }
        }
    }

    private void loadPhongData() {
        loadPhongData(null);
    }

    private void loadPhongData(String maChiNhanh) {
        pDAO.refreshData();
        view.clearPhongComboBox();
        HashMap<String, Phong> phongs = pDAO.listPHONG();
        for (Phong p : phongs.values()) {
            if (maChiNhanh == null || p.getMaChiNhanh().equals(maChiNhanh)) {
                if (p.getTrangThai().equals("Trống")) {
                    String[] data = new String[] {p.getMaPhong(), "Phòng " + p.getSoPhong(), String.valueOf(p.getSoTang())};
                    view.addPhongItem(data);
                }
            }
        }
        if (view.cbMaPhong.getItemCount() == 0) {
            view.addPhongItem(new String[] {"", "Không có phòng trống", ""});
        }
    }

    private void updatePhongByChiNhanh() {
        String selectedChiNhanh = "";
        if (view.cbMaChiNhanh.getSelectedItem() != null) {
            Record chiNhanhRecord = (Record) view.cbMaChiNhanh.getSelectedItem();
            selectedChiNhanh = chiNhanhRecord.getColumns()[0];
            loadPhongData(selectedChiNhanh);
        }
    }
}