package CONTROLLER.APP.CLIENT;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
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

        // Load initial data
        loadChiNhanhData();
        setupEventListeners();
        loadTraPhongData();
    }

    private void setupEventListeners() {
        // Branch filter combo box
        view.getBranchComboBox().addActionListener(this);

        // Return room button
        view.getBtnTraPhong().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleReturnRoomButtonClick();
            }
        });

        // View details button
        view.getBtnXem().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleViewButtonClick();
            }
        });

        // Table selection listener
        view.getTable().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    handleTableSelectionChange();
                }
            }
        });
    }

    private void loadChiNhanhData() {
        view.getBranchComboBox().removeAllItems();
        view.getBranchComboBox().addItem("Tất cả");

        HashMap<String, ChiNhanhKhachSan> chiNhanhs = cnDAO.listCHINHANHKHACHSAN();
        if (chiNhanhs != null) {
            Vector<String> branches = new Vector<>();
            for (ChiNhanhKhachSan cn : chiNhanhs.values()) {
                if (cn != null) {
                    String item = cn.getMaChiNhanh() + " | " + cn.getTenChiNhanh();
                    branches.add(item);
                }
            }
            view.updateBranchList(branches);
        }
    }

    private void loadTraPhongData() {
        dpDAO.refreshData();
        pDAO.refreshData();
        lpDAO.getListLOAIPHONG();
        
        HashMap<String, DatPhong> datPhongs = dpDAO.getListDATPHONG();
        HashMap<String, Phong> phongs = pDAO.listPHONG();
        HashMap<String, LoaiPhong> loaiPhongs = lpDAO.getListLOAIPHONG();

        if (datPhongs == null || datPhongs.isEmpty()) {
            view.updateTableData(new Vector<>());
            return;
        }

        List<DatPhong> filteredList = new ArrayList<>();
        for (DatPhong dp : datPhongs.values()) {
            if (dp != null && "Đã đặt".equals(dp.getTrangThai())) {
                filteredList.add(dp);
            }
        }

        Collections.sort(filteredList, (a, b) -> a.getMaDatPhong().compareTo(b.getMaDatPhong()));

        Vector<Vector<Object>> data = new Vector<>();
        for (DatPhong dp : filteredList) {
            if (dp == null) continue;

            String maPhong = dp.getMaPhong();
            Phong phong = phongs != null ? phongs.get(maPhong) : null;
            if (phong == null) continue;

            LoaiPhong loaiPhong = loaiPhongs != null ? loaiPhongs.get(phong.getMaLoaiPhong()) : null;
            double giaTien = (loaiPhong != null) ? loaiPhong.getGiaTien() : 0.0;
            double thanhTien = calculateTotalCost(dp.getNgayThuePhong(), dp.getNgayTraPhong(), giaTien);

            Vector<Object> row = new Vector<>();
            row.add(dp.getMaDatPhong());
            row.add(dp.getMaNguoiDung());
            row.add(phong.getMaChiNhanh());
            row.add(phong.getSoTang());
            row.add(phong.getSoPhong());
            row.add(dp.getSoNguoi());
            row.add(String.format("%,.0f VND", giaTien));
            row.add(String.format("%,.0f VND", thanhTien));
            data.add(row);
        }
        view.updateTableData(data);
    }

    private void handleViewButtonClick() {
        JOptionPane.showMessageDialog(view, 
            "Đang cập nhật chức năng", 
            "Thông báo", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleReturnRoomButtonClick() {
        int selectedRow = view.getTable().getSelectedRow();
        if (selectedRow >= 0) {
            DefaultTableModel model = (DefaultTableModel) view.getTable().getModel();
            String bookingID = model.getValueAt(selectedRow, 0).toString();
            DatPhong dp = dpDAO.getDatPhong(bookingID);
            if (dp == null) {
                JOptionPane.showMessageDialog(view, "Không tìm thấy thông tin đặt phòng!", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!"Đã đặt".equals(dp.getTrangThai())) {
                JOptionPane.showMessageDialog(view, "Phòng này không ở trạng thái 'Đã đặt'!", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(view, 
                "Bạn có chắc chắn muốn trả phòng " + bookingID + "?", 
                "Xác nhận trả phòng", 
                JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                returnRoom(bookingID);
            }
        } else {
            JOptionPane.showMessageDialog(view, 
                "Vui lòng chọn một phòng trước khi trả", 
                "Thông báo", 
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private void handleTableSelectionChange() {
        int selectedRow = view.getTable().getSelectedRow();
        if (selectedRow >= 0) {
            loadDetailData(selectedRow);
        }
    }

    private void handleBranchFilterChange() {
        String selectedBranch = (String) view.getBranchComboBox().getSelectedItem();
        if (selectedBranch == null || selectedBranch.equals("Tất cả")) {
            loadTraPhongData();
            return;
        }

        String maChiNhanh = selectedBranch.split(" \\| ")[0];
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

        Vector<Vector<Object>> data = new Vector<>();
        for (DatPhong dp : filteredList) {
            Phong phong = phongs.get(dp.getMaPhong());
            LoaiPhong loaiPhong = loaiPhongs != null ? loaiPhongs.get(phong.getMaLoaiPhong()) : null;
            double giaTien = (loaiPhong != null) ? loaiPhong.getGiaTien() : 0.0;
            double thanhTien = calculateTotalCost(dp.getNgayThuePhong(), dp.getNgayTraPhong(), giaTien);

            Vector<Object> row = new Vector<>();
            row.add(dp.getMaDatPhong());
            row.add(dp.getMaNguoiDung());
            row.add(phong.getMaChiNhanh());
            row.add(phong.getSoTang());
            row.add(phong.getSoPhong());
            row.add(dp.getSoNguoi());
            row.add(String.format("%,.0f VND", giaTien));
            row.add(String.format("%,.0f VND", thanhTien));
            data.add(row);
        }
        view.updateTableData(data);
    }

    private void loadDetailData(int selectedRow) {
        DefaultTableModel model = (DefaultTableModel) view.getTable().getModel();
        String maDatPhong = model.getValueAt(selectedRow, 0).toString();
        String maNguoiDung = model.getValueAt(selectedRow, 1).toString();
        String maChiNhanh = model.getValueAt(selectedRow, 2).toString();
        int soTang = (int) model.getValueAt(selectedRow, 3);
        int soPhong = (int) model.getValueAt(selectedRow, 4);
        String soNguoi = model.getValueAt(selectedRow, 5).toString();
        String giaTienStr = model.getValueAt(selectedRow, 6).toString();
        String thanhTienStr = model.getValueAt(selectedRow, 7).toString();

        DatPhong dp = dpDAO.getDatPhong(maDatPhong);
        if (dp == null) return;

        StringBuilder detailText = new StringBuilder();
        detailText.append("THÔNG TIN CHI TIẾT ĐẶT PHÒNG\n\n");
        detailText.append("Mã đặt phòng: ").append(maDatPhong).append("\n");
        detailText.append("Khách hàng: ").append(maNguoiDung).append("\n");

        ChiNhanhKhachSan cn = cnDAO.getChiNhanhKhachSan(maChiNhanh);
        if (cn != null) {
            detailText.append("Chi nhánh: ").append(cn.getTenChiNhanh()).append("\n");
            detailText.append("Địa chỉ: ").append(cn.getDiaChi()).append("\n");
        }

        detailText.append("Tầng: ").append(soTang).append("\n");
        detailText.append("Phòng: ").append(soPhong).append("\n");
        detailText.append("Số người: ").append(soNguoi).append("\n");

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
        detailText.append("Giá phòng: ").append(giaTienStr).append("/giờ\n");
        detailText.append("Thành tiền: ").append(thanhTienStr).append("\n");

        view.getDetailArea().setText(detailText.toString());
    }

    private void returnRoom(String bookingID) {
        DatPhong dp = dpDAO.getDatPhong(bookingID);
        if (dp != null) {
            dp.setTrangThai("Hoàn thành");
            int result = dpDAO.capNhatDatPhong(dp);

            if (result > 0) {
                Phong phong = pDAO.getPhong(dp.getMaPhong());
                if (phong != null) {
                    phong.setTrangThai("Trống");
                    pDAO.capNhatPhong(phong);
                }

                JOptionPane.showMessageDialog(view, 
                    "Đã trả phòng thành công: " + bookingID, 
                    "Thông báo", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                view.refreshData();
                loadTraPhongData();
                view.getDetailArea().setText("Chọn một phòng để xem thông tin chi tiết...");
            } else {
                JOptionPane.showMessageDialog(view, 
                    "Trả phòng thất bại! Vui lòng thử lại.", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.getBranchComboBox()) {
            handleBranchFilterChange();
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