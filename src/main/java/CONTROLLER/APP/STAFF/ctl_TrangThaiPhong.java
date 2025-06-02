package CONTROLLER.APP.STAFF;

import MODEL.DAO.PhongDAO;
import MODEL.DAO.LoaiPhongDAO;
import MODEL.DAO.DatPhongDAO;
import MODEL.ENTITY.Phong;
import MODEL.ENTITY.LoaiPhong;
import MODEL.ENTITY.DatPhong;
import VIEW.STAFF.view_TrangThaiPhong;

import javax.swing.table.DefaultTableModel;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class ctl_TrangThaiPhong {
    private view_TrangThaiPhong view;
    private PhongDAO phongDAO;
    private LoaiPhongDAO loaiPhongDAO;
    private DatPhongDAO datPhongDAO;
    
    public ctl_TrangThaiPhong(view_TrangThaiPhong view) {
        this.view = view;
        this.phongDAO = new PhongDAO();
        this.loaiPhongDAO = new LoaiPhongDAO();
        this.datPhongDAO = new DatPhongDAO();
        
        // Load initial data
        loadDataToTable();
    }
    
    /**
     * Load all room data to the table
     */
    public void loadDataToTable() {
        DefaultTableModel model = view.getTableModel();
        model.setRowCount(0); // Clear existing data
        
        HashMap<String, Phong> phongMap = phongDAO.listPHONG();
        HashMap<String, LoaiPhong> loaiPhongMap = loaiPhongDAO.getListLOAIPHONG();
        
        for (Phong phong : phongMap.values()) {
            String tenLoaiPhong = "";
            if (loaiPhongMap.containsKey(phong.getMaLoaiPhong())) {
                tenLoaiPhong = loaiPhongMap.get(phong.getMaLoaiPhong()).getTenLoaiPhong();
            }
            
            String trangThai = determineTrangThai(phong.getMaPhong());
            
            Object[] rowData = {
                phong.getMaPhong(),
                phong.getSoPhong(),
                phong.getSoTang(),
                tenLoaiPhong,
                trangThai
            };
            model.addRow(rowData);
        }
    }
    
    /**
     * Determine room status based on booking data
     * Returns status that matches the view's expected values
     */
    private String determineTrangThai(String maPhong) {
        // First check booking status
        HashMap<String, DatPhong> datPhongMap = datPhongDAO.getListDATPHONG();
        
        for (DatPhong datPhong : datPhongMap.values()) {
            if (datPhong.getMaPhong().equals(maPhong)) {
                String trangThaiDat = datPhong.getTrangThai();
                if ("Đã xác nhận".equals(trangThaiDat)) {
                    return "Đã đặt trước";
                } else if ("Đang thực hiện".equals(trangThaiDat) || "Đang sử dụng".equals(trangThaiDat)) {
                    return "Có người ở";
                }
            }
        }
        
        // If no active booking found, get status from room entity
        Phong phong = phongDAO.getPhong(maPhong);
        if (phong != null) {
            String trangThaiPhong = phong.getTrangThai();
            // Map database status to view status
            return mapDatabaseStatusToViewStatus(trangThaiPhong);
        }
        
        return "Trống"; // Default status
    }
    
    /**
     * Map database status values to view status values
     */
    private String mapDatabaseStatusToViewStatus(String dbStatus) {
        if (dbStatus == null) return "Trống";
        
        switch (dbStatus.toLowerCase().trim()) {
            case "trống":
            case "empty":
            case "available":
                return "Trống";
            case "đã đặt":
            case "đã đặt trước":
            case "reserved":
            case "booked":
                return "Đã đặt trước";
            case "đang sử dụng":
            case "có người ở":
            case "occupied":
            case "in use":
                return "Có người ở";
            case "bảo trì":
            case "maintenance":
                return "Bảo trì";
            default:
                return "Trống";
        }
    }
    
    /**
     * Map view status values to database status values
     */
    private String mapViewStatusToDatabaseStatus(String viewStatus) {
        if (viewStatus == null) return "Trống";
        
        switch (viewStatus.trim()) {
            case "Trống":
                return "Trống";
            case "Đã đặt trước":
                return "Đã đặt trước";
            case "Có người ở":
                return "Có người ở";
            case "Bảo trì":
                return "Bảo trì";
            default:
                return "Trống";
        }
    }
    
    /**
     * Search rooms by room number or room type
     */
    public void timKiemPhong(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            loadDataToTable();
            return;
        }
        
        DefaultTableModel model = view.getTableModel();
        model.setRowCount(0);
        
        HashMap<String, Phong> phongMap = phongDAO.listPHONG();
        HashMap<String, LoaiPhong> loaiPhongMap = loaiPhongDAO.getListLOAIPHONG();
        
        String searchKeyword = keyword.trim().toLowerCase();
        
        for (Phong phong : phongMap.values()) {
            String tenLoaiPhong = "";
            if (loaiPhongMap.containsKey(phong.getMaLoaiPhong())) {
                tenLoaiPhong = loaiPhongMap.get(phong.getMaLoaiPhong()).getTenLoaiPhong();
            }
            
            // Check if room matches search criteria
            boolean matches = phong.getSoPhong().toLowerCase().contains(searchKeyword) ||
                            phong.getMaPhong().toLowerCase().contains(searchKeyword) ||
                            tenLoaiPhong.toLowerCase().contains(searchKeyword);
            
            if (matches) {
                String trangThai = determineTrangThai(phong.getMaPhong());
                
                Object[] rowData = {
                    phong.getMaPhong(),
                    phong.getSoPhong(),
                    phong.getSoTang(),
                    tenLoaiPhong,
                    trangThai
                };
                model.addRow(rowData);
            }
        }
    }
    
    /**
     * Filter rooms by status
     */
    public void locTheoTrangThai(String trangThai) {
        if ("Tất cả".equals(trangThai)) {
            loadDataToTable();
            return;
        }
        
        DefaultTableModel model = view.getTableModel();
        model.setRowCount(0);
        
        HashMap<String, Phong> phongMap = phongDAO.listPHONG();
        HashMap<String, LoaiPhong> loaiPhongMap = loaiPhongDAO.getListLOAIPHONG();
        
        for (Phong phong : phongMap.values()) {
            String trangThaiPhong = determineTrangThai(phong.getMaPhong());
            
            if (trangThai.equals(trangThaiPhong)) {
                String tenLoaiPhong = "";
                if (loaiPhongMap.containsKey(phong.getMaLoaiPhong())) {
                    tenLoaiPhong = loaiPhongMap.get(phong.getMaLoaiPhong()).getTenLoaiPhong();
                }
                
                Object[] rowData = {
                    phong.getMaPhong(),
                    phong.getSoPhong(),
                    phong.getSoTang(),
                    tenLoaiPhong,
                    trangThaiPhong
                };
                model.addRow(rowData);
            }
        }
    }
    
    /**
     * Update room status - Fixed with better error handling
     */
    public boolean capNhatTrangThaiPhong(String maPhong, String trangThaiMoi) {
        try {
            System.out.println("Updating room: " + maPhong + " to status: " + trangThaiMoi);
            
            Phong phong = phongDAO.getPhong(maPhong);
            if (phong == null) {
                System.out.println("Room not found: " + maPhong);
                return false;
            }
            
            // Convert view status to database status before saving
            String dbStatus = mapViewStatusToDatabaseStatus(trangThaiMoi);
            System.out.println("Mapped status: " + trangThaiMoi + " -> " + dbStatus);
            
            phong.setTrangThai(dbStatus);
            
            int result = phongDAO.capNhatPhong(phong);
            System.out.println("Database update result: " + result);
            
            if (result > 0) {
                // Refresh table data to reflect changes
                loadDataToTable();
                return true;
            } else {
                System.out.println("Database update failed - no rows affected");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Exception in capNhatTrangThaiPhong: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get room statistics
     */
    public Map<String, Integer> getThongKePhong() {
        Map<String, Integer> thongKe = new HashMap<>();
        thongKe.put("Tổng số phòng", 0);
        thongKe.put("Trống", 0);
        thongKe.put("Đã đặt trước", 0);
        thongKe.put("Có người ở", 0);
        thongKe.put("Bảo trì", 0);
        
        HashMap<String, Phong> phongMap = phongDAO.listPHONG();
        
        for (Phong phong : phongMap.values()) {
            String trangThai = determineTrangThai(phong.getMaPhong());
            
            thongKe.put("Tổng số phòng", thongKe.get("Tổng số phòng") + 1);
            
            // Safely increment status count
            Integer currentCount = thongKe.get(trangThai);
            if (currentCount != null) {
                thongKe.put(trangThai, currentCount + 1);
            } else {
                // Handle unexpected status
                thongKe.put(trangThai, 1);
            }
        }
        
        return thongKe;
    }
    
    /**
     * Get rooms by floor
     */
    public List<Phong> getPhongTheoTang(int soTang) {
        List<Phong> phongTheoTang = new ArrayList<>();
        HashMap<String, Phong> phongMap = phongDAO.listPHONG();
        
        for (Phong phong : phongMap.values()) {
            if (phong.getSoTang() == soTang) {
                phongTheoTang.add(phong);
            }
        }
        
        return phongTheoTang;
    }
    
    /**
     * Refresh data from database
     */
    public void refreshData() {
        // Refresh all DAOs to get fresh data from database
        this.phongDAO.refreshFromDatabase();
        this.loaiPhongDAO = new LoaiPhongDAO();
        this.datPhongDAO = new DatPhongDAO();
        
        // Reload table data
        loadDataToTable();
    }
    
    /**
     * Validate room status before update
     */
    public boolean isValidStatus(String status) {
        return status != null && 
               (status.equals("Trống") || 
                status.equals("Đã đặt trước") || 
                status.equals("Có người ở") || 
                status.equals("Bảo trì"));
    }
    
    /**
     * Get available status options for the view
     */
    public String[] getAvailableStatusOptions() {
        return new String[]{"Trống", "Đã đặt trước", "Có người ở", "Bảo trì"};
    }
}