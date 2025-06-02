package CONTROLLER.APP.CLIENT;

import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import MODEL.DAO.ChiNhanhKhachSanDAO;
import MODEL.DAO.DatPhongDAO;
import MODEL.DAO.DichVuDAO;
import MODEL.DAO.LoaiPhongDAO;
import MODEL.DAO.NguoiDungDAO;
import MODEL.DAO.PhongDAO;
import VIEW.view_main;
import VIEW.CLIENT.view_DatPhong;

import java.util.ArrayList;
import java.util.Date;
// import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import MODEL.ENTITY.ChiNhanhKhachSan;
import MODEL.ENTITY.DatPhong;
import MODEL.ENTITY.DichVu;
import MODEL.ENTITY.LoaiPhong;
import MODEL.ENTITY.NguoiDung;
import MODEL.ENTITY.Phong;

public class ctl_DatPhong implements ActionListener {
    private view_main vMain;
    private view_DatPhong vDatPhong;
    private DatPhongDAO datPhongDAO;
    private ChiNhanhKhachSanDAO chiNhanhDAO;
    private PhongDAO phongDAO;
    private DichVuDAO dichVuDAO;
    private LoaiPhongDAO loaiPhongDAO;
    private NguoiDungDAO nguoiDungDAO;

    @Override
    public void actionPerformed(ActionEvent e) {
        // String command = e.getActionCommand();
        Object source = e.getSource();
        if (source == vDatPhong.btnTim) {
            loadPhongTheoChiNhanh();
        }
        // if (source == vDatPhong.btnXacNhan) {
        //     datPhong();
        // }
    }

    public ctl_DatPhong(view_DatPhong vDatPhong, view_main vMain) {
        this.vDatPhong = vDatPhong;
        this.vMain = vMain;
        this.datPhongDAO = new DatPhongDAO();
        this.chiNhanhDAO = new ChiNhanhKhachSanDAO();
        this.phongDAO = new PhongDAO();
        this.dichVuDAO = new DichVuDAO();
        this.loaiPhongDAO = new LoaiPhongDAO();
        this.nguoiDungDAO = new NguoiDungDAO();
        
        LoadChiNhanh();
        loadPhongTheoChiNhanh();
        // LoadDichVu();
        
        
        vDatPhong.btnTim.addActionListener(this);
        vDatPhong.radioChon.addActionListener(this);
        // vDatPhong.btnXacNhan.addActionListener(this);

        vDatPhong.tfPhong.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                ClickPhong();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                ClickPhong();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                ClickPhong();
            }
        });

        // thayDoiDuLieu();

    }


    private void LoadChiNhanh() {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        model.addElement("Tất cả");
        
        chiNhanhDAO.listCHINHANHKHACHSAN().values().forEach(cn -> model.addElement(cn.getTenChiNhanh()));
        vDatPhong.cbChiNhanh.setModel(model);
    }

    public void LoadDichVu(){
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        // model.addElement("Tất cả");
        // Giả sử bạn có một danh sách dịch vụ, thêm vào model
        dichVuDAO.getListDICHVU().values().forEach(dv -> model.addElement(dv.getTenDichVu()));
        vDatPhong.cbDichVu.setModel(model);
    }

    private void loadPhongTheoChiNhanh() {
        String tenChiNhanh = (String) vDatPhong.cbChiNhanh.getSelectedItem();
        
        // Xóa các phòng hiện tại
        vDatPhong.panelPhongList.removeAll();
        
        if (tenChiNhanh == null || tenChiNhanh.equals("Tất cả")) {
            // Hiển thị tất cả phòng nếu chọn "Tất cả"
            List<Phong> allPhong = new ArrayList<>(phongDAO.listPHONG().values());
            for (Phong phong : allPhong) {
                String p = phong.getMaPhong() + " - " + phong.getSoPhong();
                if (phong.getTrangThai().equals("Trống")) {
                    vDatPhong.panelPhongList.add(vDatPhong.createModernRoomPanel(p));
                } 
            }
        } else {
            // Lấy chi nhánh theo tên
            ChiNhanhKhachSan chiNhanh = chiNhanhDAO.getChiNhanhByTen(tenChiNhanh);
            if (chiNhanh == null) {
                System.out.println("Không tìm thấy chi nhánh: " + tenChiNhanh);
                return;
            }
            
            // Lấy danh sách phòng theo chi nhánh, không lọc trạng thái
            List<Phong> dsPhong = new ArrayList<>(phongDAO.listPHONGByChiNhanh(chiNhanh.getMaChiNhanh()).values());
            
            // Thêm các phòng mới vào panel

            for (Phong phong : dsPhong) {
                // String phong
                vDatPhong.panelPhongList.add(vDatPhong.createModernRoomPanel(phong.getMaPhong() + " - " + phong.getSoPhong()));
            }
        }
        
        vDatPhong.panelPhongList.revalidate();
        vDatPhong.panelPhongList.repaint();
    }

    String maP;

    private void ClickPhong() {

        String selectedRoom = vDatPhong.tfPhong.getText();
        if (selectedRoom != null) {
            // Tách mã phòng từ chuỗi
            String maPhong = selectedRoom.split(" - ")[0];
            maP = maPhong;
            Phong phong = phongDAO.getPhong(maPhong);
            LoaiPhong loaiPhong = loaiPhongDAO.getLoaiPhong(phong.getMaLoaiPhong());
            try {
                // Hiển thị thông tin phòng trong view_DatPhong
                vDatPhong.tangMay.setText(String.valueOf(phong.getSoTang()));

                // vDatPhong.loaiPhong.setText(phong.getMaLoaiPhong());
                vDatPhong.loaiPhong.setText(loaiPhong.getTenLoaiPhong());
                // TODO: handle exception
                
            } catch (Exception e) {
                System.out.println("");
            }
        } else {
            System.out.println("Vui lòng chọn một phòng.");
        }
    }

    public Double tinhTien() {
        String Phong = vDatPhong.tfPhongBooking.getText();
        String ma = Phong.split(" - ")[0].trim();
        Phong phong = phongDAO.getPhong(ma);
        LoaiPhong loaiPhong = loaiPhongDAO.getLoaiPhong(phong.getMaLoaiPhong());

        double soGio = tinhSoGioLuuTru();
        double giaTien = loaiPhong.getGiaTien();

        double tongTien = soGio * giaTien;

        return tongTien;
    }

    public double tinhSoGioLuuTru() {
        Date checkIn = (Date) vDatPhong.spinnerCheckIn.getValue();
        Date checkOut = (Date) vDatPhong.spinnerCheckOut.getValue();

        long milliseconds = checkOut.getTime() - checkIn.getTime();
        double soGio = milliseconds / (1000.0 * 60 * 60); // chia cho mili giây để ra giờ

        return soGio;
    }

    public void datPhong() {
        Phong phong = phongDAO.getPhong(maP);
        // LoaiPhong loaiPhong = loaiPhongDAO.getLoaiPhong(phong.getMaLoaiPhong());

        String tenKhachHang = vMain.getMaNguoiDung();
        String soNguoi = vDatPhong.tfSoNguoi.getText();
        Date checkIn = (Date) vDatPhong.spinnerCheckIn.getValue();
        Date checkOut = (Date) vDatPhong.spinnerCheckOut.getValue();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String checkInStr = dateFormat.format(checkIn);
        String checkOutStr = dateFormat.format(checkOut);


        DichVu dichVu = dichVuDAO.getDichVuByTen((String) vDatPhong.cbDichVu.getSelectedItem());

        // Thực hiện đặt phòng
        DatPhong datPhong = new DatPhong(tenKhachHang, phong.getMaPhong(), soNguoi, dichVu.getMaDichVu(), checkInStr, checkOutStr);

        int result = datPhongDAO.themDatPhong(datPhong);
        if (result > 0) {
            JOptionPane.showMessageDialog(vDatPhong, "Đặt phòng thành công!");
            // Cập nhật trạng thái phòng
            NguoiDung nguoiDung = nguoiDungDAO.getNguoiDung(vMain.getMaNguoiDung());
            nguoiDung.setSoDuTaiKhoan(tinhTien());
            nguoiDungDAO.capNhatNguoiDung(nguoiDung);
            phong.setTrangThai("Có người ở");
            phongDAO.capNhatPhong(phong);
            // Load lại danh sách phòng
            loadPhongTheoChiNhanh();
        } else {
            JOptionPane.showMessageDialog(vDatPhong, "Đặt phòng thất bại. Vui lòng thử lại.");
            
        }
    }

    public void TaoHoaDon(){
        
    }
}