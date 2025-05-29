package VIEW;

import javax.swing.*;
import CONTROLLER.APP.ctl_main;

import java.awt.*;

import UTILS.FONT.KieuChu;
import UTILS.FONT.PhongChu;
import UTILS.BUTTONS.RoundButton;
import UTILS.BUTTONS.RoundedButton;

public class view_main extends JFrame {
    public JLabel lbe_TennguoiDung, lbe_TieuDe;
    public String maNguoiDung = null;
    public String maVaiTro = null;
    public JPanel pn_top, pn_content, pn_trai, pn_up, pn_down;
    public RoundedButton btn_DangNhap, btn_Account;
    public RoundedButton btn_TrangChu, btn_Phong, btn_KhachSan, btn_DichVu, btn_KhachHang, btn_NhanVien, btn_DoanhThu;
    public RoundedButton btn_DonDatPhong, btn_HoaDon, btn_TrangThaiPhong;
    public RoundedButton btn_NapRut, btn_DatPhong, btn_TraPhong;

    public ctl_main cMain;

    public String getMaNguoiDung(){
        return maNguoiDung;
    }

    public void setMaNguoiDung(String maNguoiDung){
        this.maNguoiDung = maNguoiDung;
    }

    public String getMaVaiTro(){
        return maVaiTro;
    }

    public void setMaVaiTro(String maVaiTro){
        this.maVaiTro = maVaiTro;
    }
    

    public view_main(String maNguoiDung, String maVaiTro) {
        setTitle("Quản lý khách sạn");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 960);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(2, 2)); // padding 10px
        getContentPane().setBackground(new Color(255,245,230,230));
        
        this.maNguoiDung = maNguoiDung;
        this.maVaiTro = maVaiTro;

        initComponents();
        
        this.cMain = new ctl_main(this, maNguoiDung, maVaiTro);
    }

    public void initComponents() {
        // Panel trái (pn_left)
        pn_trai = new JPanel();
        pn_trai.setPreferredSize(new Dimension(200, 960));
        pn_trai.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        pn_trai.setOpaque(true);
        pn_trai.setBackground(new Color(245, 200, 200)); // màu hồng nhạt
        pn_trai.setLayout(new BorderLayout(0, 0));

        // Panel con trên (pn_up)
        pn_up = new JPanel();
        pn_up.setPreferredSize(new Dimension(200, 225));
        pn_up.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        pn_up.setOpaque(true);
        pn_up.setBackground(new Color(255, 255, 255));
        pn_up.setLayout(null);

        JLabel img_icon = new JLabel();
        img_icon.setBounds(35, 10, 129, 151);
        img_icon.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        // img_icon.setIcon(new ImageIcon("src/main/resources/IMAGES/user.png")); // Thay icon nếu có
        pn_up.add(img_icon);

        btn_Account = new RoundedButton("Tài khoản");
        btn_Account.setBounds(35, 170, 129, 33);
        btn_Account.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        pn_up.add(btn_Account);

        btn_DangNhap = new RoundedButton("Đăng nhập");
        btn_DangNhap.setBounds(35, 170, 129, 33);
        btn_DangNhap.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        pn_up.add(btn_DangNhap);

        pn_trai.add(pn_up, BorderLayout.NORTH);

        // Panel con dưới (pn_down)
        pn_down = new JPanel();
        pn_down.setPreferredSize(new Dimension(200, 735));
        pn_down.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        pn_down.setOpaque(true);
        pn_down.setBackground(new Color(245, 200, 200));
        pn_down.setLayout(null);

        String[] btnNames = {"Trang chủ", "Phòng", "Khách sạn", "Dịch vụ", "Khách hàng", "Nhân viên", "Doanh thu", "Đơn đặt phòng", "Hóa đơn", "Trạng thái phòng", "Nạp/Rút", "Đặt phòng", "Trả phòng"};
        RoundedButton[] btnArr = new RoundedButton[]{null, null, null, null, null, null, null, null, null, null, null, null, null};
        for (int i = 0; i < btnNames.length; i++) {
            RoundedButton btn = new RoundedButton(btnNames[i]);
            btn.setBounds(5, 5 + i * 40, 190, 35);
            btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                BorderFactory.createEmptyBorder(1, 3, 1, 3)
            ));
            pn_down.add(btn);
            btnArr[i] = btn;
        }
        
        btn_TrangChu = btnArr[0];
        btn_Phong = btnArr[1];
        btn_KhachSan = btnArr[2];
        btn_DichVu = btnArr[3];
        btn_KhachHang = btnArr[4];
        btn_NhanVien = btnArr[5];
        btn_DoanhThu = btnArr[6];

        btn_DonDatPhong = btnArr[7];
        btn_HoaDon = btnArr[8];
        btn_TrangThaiPhong = btnArr[9];

        btn_NapRut = btnArr[10];
        btn_DatPhong = btnArr[11];
        btn_TraPhong = btnArr[12];

        pn_trai.add(pn_down, BorderLayout.CENTER);
        add(pn_trai, BorderLayout.WEST);

        // Panel trên (pn_top)
        pn_top = new JPanel();
        pn_top.setPreferredSize(new Dimension(1080, 80));
        pn_top.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        pn_top.setOpaque(true);
        pn_top.setBackground(new Color(144, 202, 62)); // xanh lá nhạt
        pn_top.setLayout(null);
        add(pn_top, BorderLayout.NORTH);

        // Panel giữa (pn_center)
        pn_content = new JPanel();
        pn_content.setPreferredSize(new Dimension(1080, 880));
        pn_content.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        pn_content.setOpaque(true);
        pn_content.setBackground(new Color(102, 128, 153)); // xanh xám
        pn_content.setLayout(null);
        add(pn_content, BorderLayout.CENTER);
    }
}
