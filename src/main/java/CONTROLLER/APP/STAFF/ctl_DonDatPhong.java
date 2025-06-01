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
        loadPhongData();
        
        // Load initial data for table
        loadDonDatPhongData();
        
        // Clear form fields to start with empty form
        view.clearFormFields();
    }

    // Method to handle the "Đặt" (Book/Add) button click
    private void datPhong() {
        try {
            // Get values from form
            String maChiNhanh = "";
            String maPhong = "";
            String maKhachHang = "";
            String dichVuSuDung = "DV001"; // Default service
            
            // Get selected values from combo boxes
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
                
                // Check if the room is available
                Phong phong = pDAO.getPhong(maPhong);
                if (phong != null && !phong.getTrangThai().equals("Trống") && !phong.getTrangThai().equals("Đã đặt trước")) {
                    JOptionPane.showMessageDialog(view, 
                        "Phòng này hiện không khả dụng (Trạng thái: " + phong.getTrangThai() + ")!", 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
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
            
            // Get số người from text field
            String soNguoi = view.getTxtSoNguoi().getText().trim();
            if (soNguoi.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Vui lòng nhập số người!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int soNguoiValue = 0;
            try {
                soNguoiValue = Integer.parseInt(soNguoi);
                if (soNguoiValue <= 0) {
                    JOptionPane.showMessageDialog(view, "Số người phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Check if the room can accommodate this many people
                Phong phong = pDAO.getPhong(maPhong);
                if (phong != null) {
                    // Get the room type for capacity check
                    // This would require LoaiPhongDAO to be properly implemented
                    // For now, just a placeholder check
                    if (soNguoiValue > 6) {
                        JOptionPane.showMessageDialog(view, 
                            "Số người vượt quá sức chứa tối đa của phòng!", 
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(view, "Số người phải là số nguyên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Get ngày thuê and ngày trả from spinners
            Date ngayThueDate = (Date) view.getSpinnerNgayThue().getValue();
            Date ngayTraDate = (Date) view.getSpinnerNgayTra().getValue();
            
            // Check if ngày thuê is today or in future
            Date today = new Date();
            today.setHours(0);
            today.setMinutes(0);
            today.setSeconds(0);
            
            if (ngayThueDate.before(today)) {
                JOptionPane.showMessageDialog(view, "Ngày thuê phải từ ngày hôm nay trở đi!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Check if ngày trả is after ngày thuê
            if (!ngayTraDate.after(ngayThueDate)) {
                JOptionPane.showMessageDialog(view, "Ngày trả phải sau ngày thuê!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Format dates for database
            SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");
            String ngayThue = dbFormat.format(ngayThueDate);
            String ngayTra = dbFormat.format(ngayTraDate);
            
            // Default value for status
            String trangThai = "Đã đặt"; 
            
            // Create new DatPhong object
            DatPhong datPhong = new DatPhong(
                null, // maDatPhong will be generated by database
                maKhachHang,
                maPhong,
                soNguoi,
                dichVuSuDung,
                ngayThue,
                ngayTra,
                trangThai
            );
            
            // Show a processing message and disable the button to prevent multiple clicks
            btnDat.setEnabled(false);
            JOptionPane optionPane = new JOptionPane("Đang xử lý...", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
            JDialog dialog = optionPane.createDialog("Thông báo");
            
            // Use a timer to close the dialog after 1 second
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
                // Add to database
                int result = dpDAO.themDatPhong(datPhong);
                
                if (result > 0) {
                    // Update room status to "Đã đặt trước"
                    Phong phong = pDAO.getPhong(maPhong);
                    if (phong != null) {
                        phong.setTrangThai("Đã đặt trước");
                        pDAO.capNhatPhong(phong);
                    }
                    
                    JOptionPane.showMessageDialog(view, 
                        "Đặt phòng thành công!\nMã phòng: " + maPhong + "\nNgày thuê: " + 
                        formatDate(ngayThue) + "\nNgày trả: " + formatDate(ngayTra),
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Refresh table data
                    loadDonDatPhongData();
                    
                    // Refresh room data as status may have changed
                    loadPhongData();
                    
                    // Clear form for next entry
                    view.clearFormFields();
                } else {
                    JOptionPane.showMessageDialog(view, "Đặt phòng thất bại! Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } finally {
                // Re-enable the button
                btnDat.setEnabled(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Lỗi xử lý: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Method to handle the "Hủy" (Cancel/Delete) button click
    private void huyPhong() {
        // Check if a row is selected
        int selectedRow = view.table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn đơn đặt phòng cần hủy!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Get information from selected row
        String maDatPhong = view.table.getValueAt(selectedRow, 0).toString();
        String maPhong = view.table.getValueAt(selectedRow, 3).toString();
        String trangThaiDon = "";
        
        // Get the booking information
        DatPhong datPhong = dpDAO.getDatPhong(maDatPhong);
        if (datPhong != null) {
            trangThaiDon = datPhong.getTrangThai();
            // Check if the booking can be canceled
            if ("Hoàn thành".equals(trangThaiDon)) {
                JOptionPane.showMessageDialog(view, 
                    "Không thể hủy đơn đặt phòng đã hoàn thành!", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            JOptionPane.showMessageDialog(view, "Không tìm thấy thông tin đơn đặt phòng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Confirm deletion with detailed information
        String message = "Bạn có chắc chắn muốn hủy đơn đặt phòng này không?\n\n" +
                         "Mã đặt phòng: " + maDatPhong + "\n" +
                         "Mã phòng: " + maPhong + "\n" +
                         "Ngày thuê: " + formatDate(datPhong.getNgayThuePhong()) + "\n" +
                         "Ngày trả: " + formatDate(datPhong.getNgayTraPhong());
        
        int confirm = JOptionPane.showConfirmDialog(
            view, 
            message, 
            "Xác nhận hủy", 
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            // Disable the button to prevent multiple clicks
            btnHuy.setEnabled(false);
            
            // Show processing dialog
            JOptionPane optionPane = new JOptionPane("Đang xử lý hủy đơn...", JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
            JDialog dialog = optionPane.createDialog("Thông báo");
            
            // Use a timer to close the dialog after 1 second
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
                // Delete from database
                int result = dpDAO.xoaDatPhong(maDatPhong);
                
                if (result > 0) {
                    // Update room status back to "Trống"
                    Phong phong = pDAO.getPhong(maPhong);
                    if (phong != null) {
                        phong.setTrangThai("Trống");
                        pDAO.capNhatPhong(phong);
                    }
                    
                    JOptionPane.showMessageDialog(view, 
                        "Hủy đơn đặt phòng thành công!\nPhòng " + maPhong + " đã được đặt lại trạng thái thành Trống.", 
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Refresh table data
                    loadDonDatPhongData();
                    
                    // Refresh room data as status may have changed
                    loadPhongData();
                    
                    // Clear form
                    view.clearFormFields();
                } else {
                    JOptionPane.showMessageDialog(view, "Hủy đơn đặt phòng thất bại! Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(view, "Lỗi xử lý: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            } finally {
                // Re-enable the button
                btnHuy.setEnabled(true);
            }
        }
    }

    private void loadDonDatPhongData() {
        // Refresh data from database first to ensure we have the latest records
        dpDAO.refreshData();
        
        HashMap<String, DatPhong> datPhongs = dpDAO.getListDATPHONG();
        
        // Convert to list and sort by maDatPhong in ascending order
        List<DatPhong> sortedList = new ArrayList<>(datPhongs.values());
        Collections.sort(sortedList, (a, b) -> a.getMaDatPhong().compareTo(b.getMaDatPhong()));
        
        // Create data for the table with sorted data
        Object[][] data = new Object[sortedList.size()][8];
        int index = 0;
        
        for (DatPhong dp : sortedList) {
            data[index][0] = dp.getMaDatPhong();
            data[index][1] = dp.getMaNguoiDung();    // This will serve as MaKhachHang
            data[index][2] = getChiNhanhFromPhong(dp.getMaPhong());
            data[index][3] = dp.getMaPhong();
            data[index][4] = dp.getSoNguoi();
            data[index][5] = dp.getDichVuSuDung();
            data[index][6] = formatDate(dp.getNgayThuePhong());
            data[index][7] = formatDate(dp.getNgayTraPhong());
            index++;
        }
        
        // Define column names to match the view's table
        String[] columnNames = {"Mã đặt phòng", "Mã khách hàng", "Mã chi nhánh", "Mã phòng", 
                                "Số người", "Dịch vụ", "Ngày thuê", "Ngày trả"};
        
        // Update the table in the view
        view.updateTableData(data, columnNames);
        
        // Force the table to repaint to ensure it shows the new data
        view.table.repaint();
    }
    
    // Helper method to format dates in dd/MM/yyyy format
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
            // If the date is already in dd/MM/yyyy format, return it as is
            return dateStr;
        }
    }
    
    // Load detailed data for the selected row and format it according to requirements
    public void loadDetailedDataForRow(String maDatPhong, String maKhachHang, String maChiNhanh, 
                                      String maPhong, String ngayThueStr, String ngayTraStr) {
        // Format and set date values
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd"); // Assuming database format
            SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy");
            
            // Parse and set the dates
            Date ngayThue = null;
            Date ngayTra = null;
            
            try {
                ngayThue = inputFormat.parse(ngayThueStr);
            } catch (ParseException e) {
                // Try the display format if the input format fails
                try {
                    ngayThue = displayFormat.parse(ngayThueStr);
                } catch (ParseException e2) {
                    // If both fail, use current date
                    ngayThue = new Date();
                }
            }
            
            try {
                ngayTra = inputFormat.parse(ngayTraStr);
            } catch (ParseException e) {
                // Try the display format if the input format fails
                try {
                    ngayTra = displayFormat.parse(ngayTraStr);
                } catch (ParseException e2) {
                    // If both fail, use current date
                    ngayTra = new Date();
                }
            }
            
            view.setNgayThue(ngayThue);
            view.setNgayTra(ngayTra);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Get and set Chi Nhanh with details
        ChiNhanhKhachSan chiNhanh = cnDAO.getChiNhanhKhachSan(maChiNhanh);
        if (chiNhanh != null) {
            view.setChiNhanhWithDetail(chiNhanh.getMaChiNhanh(), chiNhanh.getTenChiNhanh());
        } else {
            view.setChiNhanhWithDetail(maChiNhanh, "Không tìm thấy");
        }
        
        // Get and set Khach Hang (Nguoi Dung) with details
        NguoiDung khachHang = ndDAO.getNguoiDung(maKhachHang);
        if (khachHang != null) {
            view.setKhachHangWithDetail(khachHang.getMaNguoiDung(), khachHang.getTenNguoiDung());
        } else {
            view.setKhachHangWithDetail(maKhachHang, "Không tìm thấy");
        }
        
        // Get and set Phong with details
        Phong phong = pDAO.getPhong(maPhong);
        if (phong != null) {
            view.setPhongWithDetail(
                phong.getMaPhong(), 
                "Phòng " + phong.getSoPhong(), 
                "Tầng " + phong.getSoTang()
            );
        } else {
            view.setPhongWithDetail(maPhong, "Phòng " + maPhong, "Tầng 1");
        }
    }
    
    // Helper method to get MaChiNhanh from MaPhong
    private String getChiNhanhFromPhong(String maPhong) {
        // Get the room information from PhongDAO
        Phong phong = pDAO.getPhong(maPhong);
        if (phong != null) {
            return phong.getMaChiNhanh();
        }
        return "CN001";  // Default if not found
    }

    private void loadChiNhanhData() {
        view.clearChiNhanhComboBox();
        HashMap<String, ChiNhanhKhachSan> chiNhanhs = cnDAO.listCHINHANHKHACHSAN();
        
        for (ChiNhanhKhachSan cn : chiNhanhs.values()) {
            String[] data = new String[] {
                cn.getMaChiNhanh(),
                cn.getTenChiNhanh()
            };
            view.addChiNhanhItem(data);
        }
    }

    private void loadKhachHangData() {
        view.clearKhachHangComboBox();
        HashMap<String, NguoiDung> nguoiDungs = ndDAO.getListNGUOIDUNG();
        
        for (NguoiDung nd : nguoiDungs.values()) {
            // Only include customers (Role check would be better but using all for now)
            String[] data = new String[] {
                nd.getMaNguoiDung(),
                nd.getTenNguoiDung()
            };
            view.addKhachHangItem(data);
        }
    }

    private void loadPhongData() {
        // Refresh Phong data from database first
        pDAO.refreshData();
        
        view.clearPhongComboBox();
        
        // Use the PhongDAO to get real room data
        HashMap<String, Phong> phongs = pDAO.listPHONG();
        
        for (Phong p : phongs.values()) {
            String[] data = new String[] {
                p.getMaPhong(),
                "Phòng " + p.getSoPhong(),
                String.valueOf(p.getSoTang())
            };
            view.addPhongItem(data);
        }
    }
}
