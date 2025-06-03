package CONTROLLER.APP.ADMIN;

import java.util.List;
import java.util.stream.Collectors;

import MODEL.DAO.PhongDAO;
import MODEL.DAO.LoaiPhongDAO;
import MODEL.ENTITY.Phong;
import MODEL.ENTITY.LoaiPhong;
import VIEW.view_main;
import VIEW.ADMIN.view_Phong;

public class ctl_Phong {
    private view_Phong view;
    private PhongDAO phongDAO;
    private LoaiPhongDAO loaiPhongDAO;
    view_main vMain;

    public ctl_Phong(view_Phong view, view_main vMain) {
        this.view = view;
        this.vMain = vMain;
        this.phongDAO = new PhongDAO();
        this.loaiPhongDAO = new LoaiPhongDAO();
        
        initializeView();
        attachEventListeners();
        loadInitialData();
    }

    /**
     * Initialize the view with necessary data
     */
    private void initializeView() {
        // Load LoaiPhong data to view
        loadLoaiPhongToView();
    }

    /**
     * Attach all event listeners to view components
     */
    private void attachEventListeners() {
        // Button action listeners
        view.getBtnThem().addActionListener(e -> handleThemPhong());
        view.getBtnSua().addActionListener(e -> handleSuaPhong());
        view.getBtnXoa().addActionListener(e -> handleXoaPhong());
        view.getBtnTimKiem().addActionListener(e -> handleTimKiem());
        view.getBtnLamMoi().addActionListener(e -> handleLamMoi());
        
        // Filter combo box listener
        view.getCmbLoc().addActionListener(e -> handleFilterChange());
        
        // Table selection listener
        view.getTblPhong().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                handleTableSelectionChange();
            }
        });
    }

    /**
     * Load initial data to the view
     */
    private void loadInitialData() {
        loadPhongDataToView();
    }

    /**
     * Load LoaiPhong data to the view's combo box
     */
    private void loadLoaiPhongToView() {
        try {
            List<LoaiPhong> loaiPhongList = loaiPhongDAO.getListLOAIPHONG().values()
                    .stream().collect(Collectors.toList());
            view.setLoaiPhongData(loaiPhongList);
        } catch (Exception e) {
            view.showError("Lỗi khi tải danh sách loại phòng: " + e.getMessage());
        }
    }

    /**
     * Load Phong data to the view's table
     */
    private void loadPhongDataToView() {
        try {
            List<Phong> phongList = phongDAO.listPHONG().values()
                    .stream().collect(Collectors.toList());
            
            // Convert to display format including LoaiPhong name
            Object[][] tableData = convertPhongListToTableData(phongList);
            view.setTableData(tableData);
        } catch (Exception e) {
            view.showError("Lỗi khi tải dữ liệu phòng: " + e.getMessage());
        }
    }

    /**
     * Convert Phong list to table display format
     */
    private Object[][] convertPhongListToTableData(List<Phong> phongList) {
        Object[][] data = new Object[phongList.size()][6];
        
        for (int i = 0; i < phongList.size(); i++) {
            Phong p = phongList.get(i);
            LoaiPhong lp = loaiPhongDAO.getLoaiPhong(p.getMaLoaiPhong());
            
            data[i][0] = p.getMaPhong();
            data[i][1] = p.getSoPhong();
            data[i][2] = lp != null ? lp.getTenLoaiPhong() : p.getMaLoaiPhong();
            data[i][3] = p.getSoTang();
            data[i][4] = p.getMaChiNhanh();
            data[i][5] = p.getTrangThai();
        }
        
        return data;
    }

    /**
     * Handle adding new room
     */
    private void handleThemPhong() {
        try {
            // Validate form data
            if (!view.validateForm()) {
                return;
            }

            // Get data from view
            Phong newPhong = view.getPhongFromForm();
            
            // Validate business rules
            if (isDuplicateRoom(newPhong)) {
                view.showError("Số phòng đã tồn tại trong chi nhánh này!");
                return;
            }

            // Perform add operation
            int result = phongDAO.themPhong(newPhong);
            
            if (result > 0) {
                view.showSuccess("Thêm phòng thành công!");
                refreshData();
                view.clearForm();
            } else {
                view.showError("Thêm phòng thất bại!");
            }
            
        } catch (Exception e) {
            view.showError("Lỗi khi thêm phòng: " + e.getMessage());
        }
    }

    /**
     * Handle updating existing room
     */
    private void handleSuaPhong() {
        try {
            // Check if a room is selected
            String selectedId = view.getSelectedPhongId();
            if (selectedId == null || selectedId.isEmpty()) {
                view.showError("Vui lòng chọn phòng cần sửa!");
                return;
            }

            // Validate form data
            if (!view.validateForm()) {
                return;
            }

            // Confirm action
            if (!view.confirmAction("Bạn có chắc chắn muốn cập nhật phòng này?")) {
                return;
            }

            // Get data from view
            Phong updatedPhong = view.getPhongFromForm();
            updatedPhong.setMaPhong(selectedId);
            
            // Validate business rules (exclude current room from duplicate check)
            if (isDuplicateRoomForUpdate(updatedPhong)) {
                view.showError("Số phòng đã tồn tại trong chi nhánh này!");
                return;
            }

            // Perform update operation
            int result = phongDAO.capNhatPhong(updatedPhong);
            
            if (result > 0) {
                view.showSuccess("Cập nhật phòng thành công!");
                refreshData();
            } else {
                view.showError("Cập nhật phòng thất bại!");
            }
            
        } catch (Exception e) {
            view.showError("Lỗi khi cập nhật phòng: " + e.getMessage());
        }
    }

    /**
     * Handle deleting room
     */
    private void handleXoaPhong() {
        try {
            String maPhong = view.getSelectedPhongId();
            if (maPhong == null || maPhong.isEmpty()) {
                view.showError("Vui lòng chọn phòng cần xóa!");
                return;
            }

            // Get room info for validation
            Phong phong = phongDAO.getPhong(maPhong);
            if (phong == null) {
                view.showError("Không tìm thấy thông tin phòng!");
                return;
            }

            // Check business rules
            if (!canDeleteRoom(phong)) {
                view.showError("Không thể xóa phòng đang được sử dụng hoặc đã đặt!");
                return;
            }

            // Confirm deletion
            if (!view.confirmAction("Bạn có chắc chắn muốn xóa phòng " + phong.getSoPhong() + "?")) {
                return;
            }

            // Perform delete operation
            int result = phongDAO.xoaPhong(maPhong);
            
            if (result > 0) {
                view.showSuccess("Xóa phòng thành công!");
                refreshData();
                view.clearForm();
            } else {
                view.showError("Xóa phòng thất bại!");
            }
            
        } catch (Exception e) {
            view.showError("Lỗi khi xóa phòng: " + e.getMessage());
        }
    }

    /**
     * Handle search functionality
     */
    private void handleTimKiem() {
        try {
            String keyword = view.getSearchKeyword();
            
            if (keyword.isEmpty()) {
                loadPhongDataToView(); // Show all data
                return;
            }

            // Filter data based on search keyword
            List<Phong> filteredList = phongDAO.listPHONG().values().stream()
                .filter(p -> matchesSearchKeyword(p, keyword))
                .collect(Collectors.toList());

            // Update view with filtered data
            Object[][] tableData = convertPhongListToTableData(filteredList);
            view.setTableData(tableData);
            
            if (filteredList.isEmpty()) {
                view.showInfo("Không tìm thấy phòng nào phù hợp với từ khóa: " + keyword);
            }
            
        } catch (Exception e) {
            view.showError("Lỗi khi tìm kiếm: " + e.getMessage());
        }
    }

    /**
     * Handle filter change
     */
    private void handleFilterChange() {
        try {
            String selectedFilter = view.getSelectedFilter();
            List<Phong> filteredList;

            if ("Tất cả".equals(selectedFilter)) {
                filteredList = phongDAO.listPHONG().values().stream()
                        .collect(Collectors.toList());
            } else {
                filteredList = phongDAO.listPHONG().values().stream()
                    .filter(p -> p.getTrangThai().equals(selectedFilter))
                    .collect(Collectors.toList());
            }

            // Update view with filtered data
            Object[][] tableData = convertPhongListToTableData(filteredList);
            view.setTableData(tableData);
            
        } catch (Exception e) {
            view.showError("Lỗi khi lọc dữ liệu: " + e.getMessage());
        }
    }

    /**
     * Handle refresh action
     */
    private void handleLamMoi() {
        refreshData();
        view.clearForm();
    }

    /**
     * Handle table selection change
     */
    private void handleTableSelectionChange() {
        try {
            String selectedId = view.getSelectedPhongId();
            if (selectedId != null && !selectedId.isEmpty()) {
                Phong phong = phongDAO.getPhong(selectedId);
                if (phong != null) {
                    view.setFormData(phong);
                }
            }
        } catch (Exception e) {
            view.showError("Lỗi khi tải thông tin phòng: " + e.getMessage());
        }
    }

    /**
     * Refresh all data from database
     */
    private void refreshData() {
        try {
            // Refresh DAOs to get latest data
            this.phongDAO = new PhongDAO();
            this.loaiPhongDAO = new LoaiPhongDAO();
            
            // Refresh view data
            loadLoaiPhongToView();
            loadPhongDataToView();
            
        } catch (Exception e) {
            view.showError("Lỗi khi làm mới dữ liệu: " + e.getMessage());
        }
    }

    // Business logic validation methods
    
    /**
     * Check if room number already exists in the same branch
     */
    private boolean isDuplicateRoom(Phong phong) {
        return phongDAO.listPHONG().values().stream()
            .anyMatch(p -> p.getSoPhong().equals(phong.getSoPhong()) && 
                          p.getMaChiNhanh().equals(phong.getMaChiNhanh()));
    }

    /**
     * Check if room number already exists in the same branch (excluding current room for update)
     */
    private boolean isDuplicateRoomForUpdate(Phong phong) {
        return phongDAO.listPHONG().values().stream()
            .anyMatch(p -> !p.getMaPhong().equals(phong.getMaPhong()) &&
                          p.getSoPhong().equals(phong.getSoPhong()) && 
                          p.getMaChiNhanh().equals(phong.getMaChiNhanh()));
    }

    /**
     * Check if room can be deleted based on business rules
     */
    private boolean canDeleteRoom(Phong phong) {
        // Cannot delete rooms that are in use or booked
        return !"Đang sử dụng".equals(phong.getTrangThai()) && 
               !"Đã đặt".equals(phong.getTrangThai());
    }

    /**
     * Check if room matches search keyword
     */
    private boolean matchesSearchKeyword(Phong phong, String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        LoaiPhong loaiPhong = loaiPhongDAO.getLoaiPhong(phong.getMaLoaiPhong());
        
        return phong.getMaPhong().toLowerCase().contains(lowerKeyword) ||
               phong.getSoPhong().toLowerCase().contains(lowerKeyword) ||
               phong.getMaLoaiPhong().toLowerCase().contains(lowerKeyword) ||
               phong.getMaChiNhanh().toLowerCase().contains(lowerKeyword) ||
               phong.getTrangThai().toLowerCase().contains(lowerKeyword) ||
               (loaiPhong != null && loaiPhong.getTenLoaiPhong().toLowerCase().contains(lowerKeyword));
    }

    // Public methods for external access (if needed)
    
    public void refreshView() {
        refreshData();
    }

    public List<Phong> getAllPhong() {
        return phongDAO.listPHONG().values().stream().collect(Collectors.toList());
    }

    public Phong getPhongById(String maPhong) {
        return phongDAO.getPhong(maPhong);
    }
}