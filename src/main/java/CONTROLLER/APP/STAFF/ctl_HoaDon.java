package CONTROLLER.APP.STAFF;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Date;
import java.util.stream.Collectors;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


import MODEL.DAO.ChiNhanhKhachSanDAO;
import MODEL.DAO.DatPhongDAO;
import MODEL.DAO.HoaDonDAO;
import MODEL.DAO.NguoiDungDAO;
import MODEL.DAO.PhongDAO;
import MODEL.ENTITY.ChiNhanhKhachSan;
import MODEL.ENTITY.DatPhong;
import MODEL.ENTITY.HoaDon;
import MODEL.ENTITY.NguoiDung;
import MODEL.ENTITY.Phong;
import VIEW.view_main;
import VIEW.STAFF.view_HoaDon;

public class ctl_HoaDon implements ActionListener {
    private view_HoaDon view;
    private HoaDonDAO hoaDonDAO;
    private DatPhongDAO datPhongDAO;
    private NguoiDungDAO nguoiDungDAO;
    private ChiNhanhKhachSanDAO chiNhanhDAO;
    private PhongDAO phongDAO;
    view_main vMain;

    // Sửa định dạng ngày đúng với dữ liệu SQL Server trả về
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == view.btnTraCuu) {
            searchByDateRange();
        } else if (source == view.btnXuatHoaDon) {
            int selectedRow = view.tableHoaDon.getSelectedRow();
            if (selectedRow != -1) {
                String maHD = view.tableHoaDon.getValueAt(selectedRow, 1).toString();
                HoaDon hd = hoaDonDAO.getHoaDon(maHD);  // Cần viết hàm này nếu chưa có
                // exportHoaDonToPDF(hd);
            } else {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn một hóa đơn để xuất PDF.");
            }
        }
    }


    public ctl_HoaDon(view_HoaDon view, view_main vMain) {
        this.view = view;
        this.hoaDonDAO = new HoaDonDAO();
        this.datPhongDAO = new DatPhongDAO();
        this.nguoiDungDAO = new NguoiDungDAO();
        this.vMain = vMain;
        this.chiNhanhDAO = new ChiNhanhKhachSanDAO();
        this.phongDAO = new PhongDAO();

        view.btnTraCuu.addActionListener(this);
        view.btnXuatHoaDon.addActionListener(this);

        loadData();
    }
    
    private void loadData() {
        List<HoaDon> listHoaDon = hoaDonDAO.getListHOADON().values().stream().toList();
        updateTable(listHoaDon);
    }

    private void searchByDateRange() {
        Date fromDate = (Date) view.dateFrom.getValue();
        Date toDate = (Date) view.dateTo.getValue();

        List<HoaDon> listHoaDon = hoaDonDAO.getListHOADON().values().stream()
            .filter(hd -> {
                try {
                    Date ngayGiaoDich = dateFormat.parse(hd.getNgayGiaoDich());
                    return !ngayGiaoDich.before(fromDate) && !ngayGiaoDich.after(toDate);
                } catch (ParseException e) {
                    System.err.println("Lỗi định dạng ngày cho hóa đơn: " + hd.getMaHoaDon() + " -> " + hd.getNgayGiaoDich());
                    return false;
                }
            })
            .collect(Collectors.toList());

        updateTable(listHoaDon);
    }

    private void updateTable(List<HoaDon> listHoaDon) {
        DefaultTableModel model = new DefaultTableModel(new String[] {"STT", "Mã hóa đơn", "Ngày tạo", "Khách đặt", "Số người", "Tổng tiền", "Chi nhánh"}, 0);
        model.setRowCount(0);
        int stt = 1;
        for (HoaDon hd : listHoaDon) {
            DatPhong datPhong = datPhongDAO.getDatPhong(hd.getMaDatPhong());
            NguoiDung nguoiDung = nguoiDungDAO.getNguoiDung(datPhong.getMaNguoiDung());
            Phong phong = phongDAO.getPhong(datPhong.getMaPhong());
            ChiNhanhKhachSan chiNhanh = chiNhanhDAO.getChiNhanhKhachSan(phong.getMaChiNhanh());

            model.addRow(new Object[] {
                stt++, 
                hd.getMaHoaDon(),
                hd.getNgayGiaoDich(), 
                nguoiDung.getTenNguoiDung(), 
                datPhong.getSoNguoi(), 
                hd.getTongTien(), 
                chiNhanh.getTenChiNhanh()
            });
        }
        view.tableHoaDon.setModel(model);
    }




}
