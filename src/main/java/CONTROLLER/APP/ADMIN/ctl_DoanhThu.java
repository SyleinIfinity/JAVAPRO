package CONTROLLER.APP.ADMIN;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import MODEL.DAO.ChiNhanhKhachSanDAO;
import MODEL.DAO.DatPhongDAO;
import MODEL.DAO.HoaDonDAO;
import MODEL.DAO.NguoiDungDAO;
import MODEL.ENTITY.HoaDon;
import VIEW.view_main;
import VIEW.ADMIN.view_DoanhThu;
import MODEL.ENTITY.*;

public class ctl_DoanhThu implements ActionListener {
    private view_DoanhThu vDoanhThu;
    private JPanel form;
    private HoaDonDAO hdDAO;
    private NguoiDungDAO nDungDAO;
    private DatPhongDAO dPhongDAO;
    private ChiNhanhKhachSanDAO chiNhanhDAO;
    private view_main vMain;
    // private Hoa

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        
        if (source == vDoanhThu.btnTraCuu) {
            TraCuu();
        }
        if (source == vDoanhThu.btnXuatExcel) {
            XuatExcel();
        }
    }

    public ctl_DoanhThu(view_DoanhThu vDoanhThu, view_main vMain) {
        this.vDoanhThu = vDoanhThu;
        this.form = form;
        this.vMain = vMain;
        this.hdDAO = new HoaDonDAO();
        this.nDungDAO = new NguoiDungDAO();
        this.dPhongDAO = new DatPhongDAO();
        chiNhanhDAO = new ChiNhanhKhachSanDAO();
        
        // Load initial data
        LoadthongTin();
        LoadChiNhanh();
        vDoanhThu.btnTraCuu.addActionListener(this);
        vDoanhThu.btnXuatExcel.addActionListener(this);

    }

    private void LoadthongTin() {
        List<HoaDon> hoaDon = hdDAO.getListHOADON().values().stream().toList();
        DefaultTableModel model =  new DefaultTableModel(
            new String[]{"STT", "Mã hóa đơn", "khách đặt", "Số người", "ngày thuê", "ngày trả", "nhân viên phụ trách", "tổng tiền"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 0 ? Integer.class : String.class;
            }
        };

        long tongTien = 0;
        int i = 1;
        for(HoaDon hd: hoaDon){
            NguoiDung nhanvien = nDungDAO.getNguoiDung(hd.getNhanVienPhuTrach());
            DatPhong dp = dPhongDAO.getDatPhong(hd.getMaDatPhong());
            NguoiDung khachHang = nDungDAO.getNguoiDung(dp.getMaNguoiDung());

            model.addRow(new Object[]{
                i,
                hd.getMaHoaDon(),
                khachHang.getTenNguoiDung(),
                dp.getSoNguoi(),
                dp.getNgayThuePhong(),
                dp.getNgayTraPhong(),
                nhanvien.getTenNguoiDung(),
                hd.getTongTien()
            });
            i++;
            tongTien +=hd.getTongTien();
        }

        // vDoanhThu.tableDoanhThu = new JTable(model);
        vDoanhThu.tableDoanhThu.setModel(model);
        vDoanhThu.lblTongDoanhThu.setText("Tổng doanh thu: " + String.format("%,d VNĐ", tongTien));
    }

    private void LoadChiNhanh() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("Tất cả"); // Tùy chọn hiển thị tất cả doanh thu
    
        // Lấy danh sách chi nhánh từ DAO và thêm tên chi nhánh vào model
        chiNhanhDAO.listCHINHANHKHACHSAN().values().forEach(cn -> model.addElement(cn.getTenChiNhanh()));
    
        vDoanhThu.cboChiNhanh.setModel(model); // gán model cho combobox
    }
    


    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private void TraCuu() {
        try {
            java.util.Date fromUtilDate = (java.util.Date) vDoanhThu.dateFrom.getValue();
            java.util.Date toUtilDate = (java.util.Date) vDoanhThu.dateTo.getValue();
            
            java.sql.Date fromDate = fromUtilDate != null ? new java.sql.Date(fromUtilDate.getTime()) : null;
            java.sql.Date toDate = toUtilDate != null ? new java.sql.Date(toUtilDate.getTime()) : null;
            
            String chiNhanh = (String) vDoanhThu.cboChiNhanh.getSelectedItem();
    
            List<HoaDon> danhSach = hdDAO.getHoaDon(fromDate, toDate, chiNhanh);
            DefaultTableModel model = (DefaultTableModel) vDoanhThu.tableDoanhThu.getModel();
            model.setRowCount(0); // clear old data
    
            long tongTien = 0;
            int i = 1;
            for (HoaDon hd : danhSach) {
                NguoiDung nhanvien = nDungDAO.getNguoiDung(hd.getNhanVienPhuTrach());
                DatPhong dp = dPhongDAO.getDatPhong(hd.getMaDatPhong());
                NguoiDung khachHang = nDungDAO.getNguoiDung(dp.getMaNguoiDung());
    
                Object[] row = (new Object[]{
                    i,
                    hd.getMaHoaDon(),
                    khachHang.getTenNguoiDung(),
                    dp.getSoNguoi(),
                    dp.getNgayThuePhong(),
                    dp.getNgayTraPhong(),
                    nhanvien.getTenNguoiDung(),
                    hd.getTongTien()
                });
                i++;
                model.addRow(row);
                tongTien += hd.getTongTien();
            }
    
            vDoanhThu.lblTongDoanhThu.setText("Tổng doanh thu: " + String.format("%,d VNĐ", tongTien));
    
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tra cứu doanh thu: " + ex.getMessage());
        }
    }
    

    private void XuatExcel() {
        try {
            // Hỏi người dùng nhập tên file
            String tenFile = JOptionPane.showInputDialog(null, "Nhập tên file Excel (không cần .xlsx):", "Xuất Excel", JOptionPane.PLAIN_MESSAGE);
            
            if (tenFile == null || tenFile.trim().isEmpty()) {
                return; // Người dùng hủy
            }
    
            // Đảm bảo tên hợp lệ và có đuôi .xlsx
            tenFile = tenFile.trim();
            if (!tenFile.endsWith(".xlsx")) {
                tenFile += ".xlsx";
            }
    
            // Đường dẫn lưu file (trong thư mục dự án)
            String duongDanThuMuc = "src/main/resources/EXCELs/";
            java.io.File thuMuc = new java.io.File(duongDanThuMuc);
            if (!thuMuc.exists()) {
                thuMuc.mkdirs(); // Tạo thư mục nếu chưa có
            }
    
            java.io.File fileLuu = new java.io.File(thuMuc, tenFile);
    
            // Tạo workbook và sheet
            org.apache.poi.ss.usermodel.Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("DoanhThu");
    
            // Lấy model từ bảng
            DefaultTableModel model = (DefaultTableModel) vDoanhThu.tableDoanhThu.getModel();
    
            // Ghi tiêu đề cột
            org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
            for (int col = 0; col < model.getColumnCount(); col++) {
                headerRow.createCell(col).setCellValue(model.getColumnName(col));
            }
    
            // Ghi dữ liệu
            for (int row = 0; row < model.getRowCount(); row++) {
                org.apache.poi.ss.usermodel.Row excelRow = sheet.createRow(row + 1);
                for (int col = 0; col < model.getColumnCount(); col++) {
                    Object value = model.getValueAt(row, col);
                    excelRow.createCell(col).setCellValue(value != null ? value.toString() : "");
                }
            }
    
            // Ghi ra file
            try (java.io.FileOutputStream fos = new java.io.FileOutputStream(fileLuu)) {
                workbook.write(fos);
            }
    
            workbook.close();
            JOptionPane.showMessageDialog(null, "Đã xuất Excel vào: " + fileLuu.getPath());
    
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi xuất Excel: " + ex.getMessage());
        }
    }

}
