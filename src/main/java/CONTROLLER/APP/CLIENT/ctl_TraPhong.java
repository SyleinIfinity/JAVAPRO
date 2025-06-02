package CONTROLLER.APP.CLIENT;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import MODEL.DAO.ChiNhanhKhachSanDAO;
import MODEL.DAO.DatPhongDAO;
import MODEL.DAO.LoaiPhongDAO;
import MODEL.DAO.PhongDAO;
import MODEL.ENTITY.ChiNhanhKhachSan;
import MODEL.ENTITY.DatPhong;
import MODEL.ENTITY.LoaiPhong;
import MODEL.ENTITY.Phong;
import VIEW.CLIENT.view_TraPhong;
import VIEW.view_main;

public class ctl_TraPhong implements ActionListener {
    private view_TraPhong view;
    private view_main vMain;
    private DatPhongDAO dpDAO;
    private ChiNhanhKhachSanDAO cnDAO;
    private PhongDAO pDAO;
    private LoaiPhongDAO lpDAO;

    public ctl_TraPhong(view_TraPhong view, view_main vMain) {
        this.view = view;
        this.vMain = vMain;
        this.dpDAO = new DatPhongDAO();
        this.cnDAO = new ChiNhanhKhachSanDAO();
        this.pDAO = new PhongDAO();
        this.lpDAO = new LoaiPhongDAO();

        // Load branch data for the filter combo box
        loadChiNhanhData();

        // Add action listeners
        view.getBranchComboBox().addActionListener(this);
        view.getBtnXem().addActionListener(this);
        view.getBtnTraPhong().addActionListener(this);

        // Add table selection listener
        view.getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = view.getTable().getSelectedRow();
                    if (selectedRow >= 0) {
                        loadDetailData(selectedRow);
                    }
                }
            }
        });

        // Load initial data
        loadTraPhongData();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.getBranchComboBox()) {
            filterTableByBranch();
        } else if (e.getSource() == view.getBtnXem()) {
            loadTraPhongData();
        } else if (e.getSource() == view.getBtnTraPhong()) {
            traPhong();
        }
    }

    private void loadTraPhongData() {
        System.out.println("Loading tra phong data...");
        dpDAO.refreshData();
        pDAO.refreshData();
        lpDAO.getListLOAIPHONG();
        // Refresh data from DAOs
        dpDAO.refreshData();
        pDAO.refreshData();
        lpDAO.getListLOAIPHONG();
        HashMap<String, DatPhong> datPhongs = dpDAO.getListDATPHONG();
        System.out.println("Total bookings found: " + (datPhongs != null ? datPhongs.size() : 0));
        HashMap<String, Phong> phongs = pDAO.listPHONG();
        HashMap<String, LoaiPhong> loaiPhongs = lpDAO.getListLOAIPHONG();

        if (datPhongs == null || datPhongs.isEmpty()) {
            return;
        }

        // Filter bookings with status "Đã đặt"
        List<DatPhong> filteredList = new ArrayList<>();
        for (DatPhong dp : datPhongs.values()) {
            if (dp != null && "Đã đặt".equals(dp.getTrangThai())) {
                filteredList.add(dp);
            }
        }

        // Sort by maDatPhong in ascending order
        Collections.sort(filteredList, (a, b) -> a.getMaDatPhong().compareTo(b.getMaDatPhong()));

        // Prepare table data
        DefaultTableModel tableModel = (DefaultTableModel) view.getTable().getModel();
        tableModel.setRowCount(0); // Clear existing rows

        for (DatPhong dp : filteredList) {
            if (dp == null) continue;

            String maPhong = dp.getMaPhong();
            Phong phong = phongs != null ? phongs.get(maPhong) : null;
            if (phong == null) {
                continue;
            }

            LoaiPhong loaiPhong = loaiPhongs != null ? loaiPhongs.get(phong.getMaLoaiPhong()) : null;
            double giaTien = (loaiPhong != null) ? loaiPhong.getGiaTien() : 0.0;

            // Calculate total cost
            double thanhTien = calculateTotalCost(dp.getNgayThuePhong(), dp.getNgayTraPhong(), giaTien);

            Object[] row = new Object[] {
                dp.getMaDatPhong(),
                dp.getMaNguoiDung(),
                phong.getMaChiNhanh(),
                phong.getSoTang(),
                phong.getSoPhong(),
                dp.getSoNguoi(),
                giaTien,
                thanhTien
            };
            tableModel.addRow(row);
        }
    }

    private void loadChiNhanhData() {
        view.getBranchComboBox().removeAllItems();
        view.getBranchComboBox().addItem("Tất cả");

        HashMap<String, ChiNhanhKhachSan> chiNhanhs = cnDAO.listCHINHANHKHACHSAN();
        if (chiNhanhs != null) {
            for (ChiNhanhKhachSan cn : chiNhanhs.values()) {
                if (cn != null) {
                    String item = cn.getMaChiNhanh() + " | " + cn.getTenChiNhanh();
                    view.getBranchComboBox().addItem(item);
                }
            }
        }
    }

    private void filterTableByBranch() {
        String selectedBranch = (String) view.getBranchComboBox().getSelectedItem();
        if (selectedBranch == null || selectedBranch.equals("Tất cả")) {
            loadTraPhongData();
            return;
        }

        String maChiNhanh = selectedBranch.split(" \\| ")[0];

        DefaultTableModel tableModel = (DefaultTableModel) view.getTable().getModel();
        tableModel.setRowCount(0);

        HashMap<String, DatPhong> datPhongs = dpDAO.getListDATPHONG();
        HashMap<String, Phong> phongs = pDAO.listPHONG();
        HashMap<String, LoaiPhong> loaiPhongs = lpDAO.getListLOAIPHONG();

        List<DatPhong> filteredList = new ArrayList<>();
        for (DatPhong dp : datPhongs.values()) {
            if (dp != null && "Đã đặt".equals(dp.getTrangThai())) {
                Phong phong = phongs != null ? phongs.get(dp.getMaPhong()) : null;
                if (phong != null && phong.getMaChiNhanh().equals(maChiNhanh)) {
                    filteredList.add(dp);
                }
            }
        }

        Collections.sort(filteredList, (a, b) -> a.getMaDatPhong().compareTo(b.getMaDatPhong()));

        for (DatPhong dp : filteredList) {
            Phong phong = phongs.get(dp.getMaPhong());
            LoaiPhong loaiPhong = loaiPhongs != null ? loaiPhongs.get(phong.getMaLoaiPhong()) : null;
            double giaTien = (loaiPhong != null) ? loaiPhong.getGiaTien() : 0.0;
            double thanhTien = calculateTotalCost(dp.getNgayThuePhong(), dp.getNgayTraPhong(), giaTien);

            Object[] row = new Object[] {
                dp.getMaDatPhong(),
                dp.getMaNguoiDung(),
                phong.getMaChiNhanh(),
                phong.getSoTang(),
                phong.getSoPhong(),
                dp.getSoNguoi(),
                giaTien,
                thanhTien
            };
            tableModel.addRow(row);
        }
    }

    private void loadDetailData(int selectedRow) {
        DefaultTableModel model = (DefaultTableModel) view.getTable().getModel();
        String maDatPhong = model.getValueAt(selectedRow, 0).toString();
        String maNguoiDung = model.getValueAt(selectedRow, 1).toString();
        String maChiNhanh = model.getValueAt(selectedRow, 2).toString();
        int soTang = (int) model.getValueAt(selectedRow, 3);
        int soPhong = (int) model.getValueAt(selectedRow, 4);
        String soNguoi = model.getValueAt(selectedRow, 5).toString();
        double giaTien = (double) model.getValueAt(selectedRow, 6);
        double thanhTien = (double) model.getValueAt(selectedRow, 7);

        // Get DatPhong for additional info
        DatPhong dp = dpDAO.getDatPhong(maDatPhong);
        
        StringBuilder detailText = new StringBuilder();
        detailText.append("Thông tin chi tiết đặt phòng:\n\n");
        detailText.append("Mã đặt phòng: ").append(maDatPhong).append("\n");
        detailText.append("Khách hàng: ").append(maNguoiDung).append("\n");
        
        // Get branch info
        ChiNhanhKhachSan cn = cnDAO.getChiNhanhKhachSan(maChiNhanh);
        if (cn != null) {
            detailText.append("Chi nhánh: ").append(cn.getTenChiNhanh()).append("\n");
            detailText.append("Địa chỉ: ").append(cn.getDiaChi()).append("\n");
        }
        
        detailText.append("Tầng: ").append(soTang).append("\n");
        detailText.append("Phòng: ").append(soPhong).append("\n");
        detailText.append("Số người: ").append(soNguoi).append("\n");
        
        // Get room type info
        Phong phong = pDAO.getPhong(dp.getMaPhong());
        if (phong != null) {
            LoaiPhong loaiPhong = lpDAO.getLoaiPhong(phong.getMaLoaiPhong());
            if (loaiPhong != null) {
                detailText.append("Loại phòng: ").append(loaiPhong.getTenLoaiPhong()).append("\n");
                detailText.append("Số người tối đa: ").append(loaiPhong.getSoLuongToiDa()).append("\n");
            }
        }
        
        detailText.append("\nThông tin thuê phòng:\n");
        detailText.append("Ngày thuê: ").append(formatDate(dp.getNgayThuePhong())).append("\n");
        detailText.append("Ngày trả: ").append(formatDate(dp.getNgayTraPhong())).append("\n");
        detailText.append("Giá phòng: ").append(String.format("%,.0f", giaTien)).append(" VND/giờ\n");
        detailText.append("Thành tiền: ").append(String.format("%,.0f", thanhTien)).append(" VND\n");
        
        view.getDetailArea().setText(detailText.toString());
    }

    private void traPhong() {
        int selectedRow = view.getTable().getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(view, "Vui lòng chọn phòng cần trả!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DefaultTableModel model = (DefaultTableModel) view.getTable().getModel();
        String maDatPhong = model.getValueAt(selectedRow, 0).toString();
        String maPhong = model.getValueAt(selectedRow, 4).toString();

        // Get DatPhong to check status
        DatPhong dp = dpDAO.getDatPhong(maDatPhong);
        if (dp == null) {
            JOptionPane.showMessageDialog(view, "Không tìm thấy thông tin đặt phòng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!"Đã đặt".equals(dp.getTrangThai())) {
            JOptionPane.showMessageDialog(view, "Phòng này không ở trạng thái 'Đã đặt'!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            view, 
            "Bạn có chắc chắn muốn trả phòng này?\n\n" +
            "Mã đặt phòng: " + maDatPhong + "\n" +
            "Mã phòng: " + maPhong,
            "Xác nhận trả phòng", 
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            // Update DatPhong status to "Hoàn thành"
            dp.setTrangThai("Hoàn thành");
            int result = dpDAO.capNhatDatPhong(dp);

            if (result > 0) {
                // Update Phong status to "Trống"
                Phong phong = pDAO.getPhong(maPhong);
                if (phong != null) {
                    phong.setTrangThai("Trống");
                    pDAO.capNhatPhong(phong);
                }

                JOptionPane.showMessageDialog(view, 
                    "Trả phòng thành công!\nPhòng " + maPhong + " đã được đặt lại trạng thái thành Trống.", 
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);

                // Refresh data
                loadTraPhongData();
                view.getDetailArea().setText("Chọn một phòng để xem thông tin chi tiết...");
            } else {
                JOptionPane.showMessageDialog(view, "Trả phòng thất bại! Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private double calculateTotalCost(String ngayThue, String ngayTra, double giaTienPerHour) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = sdf.parse(ngayThue);
            Date endDate = sdf.parse(ngayTra);

            long diffInMillies = endDate.getTime() - startDate.getTime();
            long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

            double giaTienPerDay = giaTienPerHour * 24;
            return giaTienPerDay * diffInDays;
        } catch (ParseException e) {
            return 0.0;
        }
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
}