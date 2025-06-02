package CONTROLLER.APP.CLIENT;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import MODEL.DAO.NguoiDungDAO;
import MODEL.ENTITY.NguoiDung;
import UTILS.API.GMAIL.GMailer;
import VIEW.view_main;
import VIEW.CLIENT.view_NapRut;

public class ctl_NapRut implements ActionListener {
    private view_NapRut giaoDienNapRut;
    private view_main giaoDienChinh;
    private NguoiDungDAO nguoiDungDAO;
    
    private String maOTP = "";
    private long thoiGianHetHanOTP = 0; // Thời điểm OTP hết hạn (milliseconds)
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object nguon = e.getSource();
        if (nguon == giaoDienNapRut.btnNapTien) {
            xuLyNapTien();
        }
        if (nguon == giaoDienNapRut.btnRutTien) {
            xuLyRutTien();
        }
        if (nguon == giaoDienNapRut.btnGuiMaOTP) {
            guiMaOTP();
        }
    }

    public ctl_NapRut(view_NapRut giaoDienNapRut, view_main giaoDienChinh) {
        this.giaoDienNapRut = giaoDienNapRut;
        this.giaoDienChinh = giaoDienChinh;
        this.nguoiDungDAO = new NguoiDungDAO();
        
        // Đăng ký sự kiện cho các nút
        this.giaoDienNapRut.btnNapTien.addActionListener(this);
        this.giaoDienNapRut.btnRutTien.addActionListener(this);
        this.giaoDienNapRut.btnGuiMaOTP.addActionListener(this);
        
        // Cập nhật thông tin người dùng ban đầu
        capNhatThongTinNguoiDung();
    }
    
    private void capNhatThongTinNguoiDung() {
        NguoiDung nguoiDung = nguoiDungDAO.getNguoiDung(giaoDienChinh.getMaNguoiDung());
        giaoDienNapRut.capNhatSoDu(nguoiDung.getSoDuTaiKhoan().toString());
    }


    public void guiMaOTP() {
        maOTP = taoMaOTP(6);
        String noiDungEmail = "Mã OTP xác thực: " + maOTP;

        NguoiDung nguoiDung = nguoiDungDAO.getNguoiDung(giaoDienChinh.getMaNguoiDung());

        try {
            GMailer.sendMain(noiDungEmail, nguoiDung.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(giaoDienNapRut, "Gửi OTP thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        thoiGianHetHanOTP = System.currentTimeMillis() + 60_000;
        giaoDienNapRut.btnGuiMaOTP.setEnabled(false);
        final int[] demNguoc = {60};

        Timer boDem = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (demNguoc[0] > 0) {
                    giaoDienNapRut.btnGuiMaOTP.setText("Gửi lại (" + demNguoc[0]-- + "s)");
                } else {
                    ((Timer) evt.getSource()).stop();
                    giaoDienNapRut.btnGuiMaOTP.setEnabled(true);
                    giaoDienNapRut.btnGuiMaOTP.setText("Gửi OTP");
                    maOTP = "";
                    thoiGianHetHanOTP = 0;
                }
            }
        });
        boDem.start();
    }

    public void xuLyNapTien() {
        // Kiểm tra số tiền
        if (giaoDienNapRut.txtSoTienNap.getText().isEmpty()) {
            hienThiThongBao("Vui lòng nhập số tiền cần nạp.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Kiểm tra OTP
        if (!kiemTraOTPHopLe()) {
            return;
        }
        
        // Kiểm tra mật khẩu
        if (!kiemTraMatKhau()) {
            return;
        }
        
        // Thực hiện nạp tiền
        thucHienNapTien();
    }

    private boolean kiemTraOTPHopLe() {
        String otpNhap = giaoDienNapRut.txtMaOTP.getText();
        System.out.println(giaoDienNapRut.txtSoTienNap.getText()); // Debug
        System.out.println(otpNhap + " - " + maOTP); // Debug
        // Kiểm tra nếu người dùng chưa nhập gì (vẫn còn placeholder)
        if (otpNhap.equals("Nhập mã OTP...") || otpNhap.isEmpty()) {
            hienThiThongBao("Vui lòng nhập mã OTP để xác thực.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // Kiểm tra OTP hết hạn
        if (maOTP.isEmpty() || System.currentTimeMillis() > thoiGianHetHanOTP) {
            hienThiThongBao("Mã OTP đã hết hạn hoặc không hợp lệ. Vui lòng gửi lại mã OTP.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        System.out.println(otpNhap + " - " + maOTP); // Debug

        if (!otpNhap.equals(maOTP)) {
            hienThiThongBao("Mã OTP không đúng. Vui lòng kiểm tra lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }


    private boolean kiemTraMatKhau() {
        char[] matKhauChars = giaoDienNapRut.txtMatKhau.getPassword();
        String matKhau = new String(matKhauChars).trim();
        java.util.Arrays.fill(matKhauChars, '\0'); // Xóa mật khẩu khỏi bộ nhớ
        
        if (matKhau.isEmpty()) {
            hienThiThongBao("Vui lòng nhập mật khẩu để xác thực.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        NguoiDung nguoiDung = nguoiDungDAO.getNguoiDung(giaoDienChinh.getMaNguoiDung());
        if (!nguoiDung.getMatKhau().equals(matKhau)) {
            hienThiThongBao("Mật khẩu không đúng. Vui lòng kiểm tra lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }

    private void thucHienNapTien() {
        try {
            double soTienNap = Double.parseDouble(giaoDienNapRut.txtSoTienNap.getText());
            NguoiDung nguoiDung = nguoiDungDAO.getNguoiDung(giaoDienChinh.getMaNguoiDung());
            
            nguoiDung.setSoDuTaiKhoan(nguoiDung.getSoDuTaiKhoan() + soTienNap);
            nguoiDungDAO.capNhatNguoiDung(nguoiDung);
            
            // Cập nhật lại số dư hiển thị
            capNhatThongTinNguoiDung();
            
            hienThiThongBao("Nạp tiền thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            hienThiThongBao("Số tiền không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void xuLyRutTien() {
        // TODO: Triển khai chức năng rút tiền
    }

    private String taoMaOTP(int doDai) {
        String kyTuChoPhep = "0123456789";
        SecureRandom random = new SecureRandom();
        
        if (doDai > kyTuChoPhep.length()) {
            throw new IllegalArgumentException("Độ dài vượt quá số lượng ký tự cho phép");
        }
        
        Set<Character> kyTuDaDung = new HashSet<>();
        StringBuilder sb = new StringBuilder(doDai);

        while (kyTuDaDung.size() < doDai) {
            char kyTuNgauNhien = kyTuChoPhep.charAt(random.nextInt(kyTuChoPhep.length()));
            if (kyTuDaDung.add(kyTuNgauNhien)) {
                sb.append(kyTuNgauNhien);
            }
        }
        return sb.toString();
    }

    private void hienThiThongBao(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(giaoDienNapRut, message, title, messageType);
    }
}