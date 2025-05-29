package CONTROLLER.APP.CLIENT;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.Timer;

import MODEL.DAO.NguoiDungDAO;
import MODEL.ENTITY.NguoiDung;
import UTILS.API.GMAIL.GMailer;
import VIEW.view_main;
import VIEW.CLIENT.view_NapRut;

public class ctl_NapRut implements ActionListener{
    private view_NapRut vNapRut;
    private view_main vMain;
    private NguoiDungDAO nDao;

    @Override
    public void actionPerformed(ActionEvent e){
        Object source = e.getSource();
        if (source == vNapRut.btnNapTien) {

        } else if (source == vNapRut.btnRutTien) {

        }
    }

    public ctl_NapRut(view_NapRut vNapRut, view_main vMain) {
        this.vNapRut = vNapRut;
        this.vMain = vMain;
        this.nDao = new NguoiDungDAO();
        
        // Add action listeners to buttons
        this.vNapRut.btnNapTien.addActionListener(this);
        this.vNapRut.btnRutTien.addActionListener(this);
        this.vNapRut.sendOtpBtn.addActionListener(this);
    }

    String otp = "";
    long otpExpireTime = 0; // thời điểm OTP hết hạn tính theo milliseconds


    public void GuiOTP(JButton sendOtpBtn){
        otp = randomOTP(6);
        otp = "Mã OTP xác thực: " + otp;

        NguoiDung nguoiDung = nDao.getNguoiDung(vMain.getMaNguoiDung());

        try {
            GMailer.sendMain(otp, nguoiDung.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
            return; // nếu lỗi, không cần set đếm ngược
        }

        // Cập nhật thời điểm hết hạn OTP sau 60 giây
        otpExpireTime = System.currentTimeMillis() + 60_000;

        // Disable nút và đếm ngược
        sendOtpBtn.setEnabled(false);
        final int[] countdown = {60};

        Timer timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (countdown[0] > 0) {
                    sendOtpBtn.setText("Gửi lại (" + countdown[0]-- + "s)");
                } else {
                    ((Timer) evt.getSource()).stop();
                    sendOtpBtn.setEnabled(true);
                    sendOtpBtn.setText("Gửi OTP");

                    // Hết hiệu lực sau 60s
                    otp = "";
                    otpExpireTime = 0;
                }
            }
        });
        timer.start();
    }

    public void NapTien(){

    }

    public String randomOTP(int dodai) {
        String kitusotaikhoan = "0123456789";
        SecureRandom chuoingaunhien = new SecureRandom();
        if (dodai > kitusotaikhoan.length()) {
            throw new IllegalArgumentException("Do dai vuot qua soluong duy nhat co san");
        }
        Set<Character> sotaikhoanDuyNhat = new HashSet<>();
        StringBuilder sb = new StringBuilder(dodai);

        while (sotaikhoanDuyNhat.size() < dodai) {
            char KituRamDom = kitusotaikhoan.charAt(chuoingaunhien.nextInt(kitusotaikhoan.length()));
            if (sotaikhoanDuyNhat.add(KituRamDom)) {
                sb.append(KituRamDom);
            }
        }
        return sb.toString();
    }

}
