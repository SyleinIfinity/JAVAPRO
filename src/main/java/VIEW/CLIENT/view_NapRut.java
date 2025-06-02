package VIEW.CLIENT;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.Locale;
import VIEW.view_main;
import CONTROLLER.APP.CLIENT.ctl_NapRut;

public class view_NapRut extends JPanel {
    // ========== KHAI BÁO MÀU SẮC ==========
    private static final Color MAU_CHINH = new Color(33, 150, 243);
    private static final Color MAU_THANH_CONG = new Color(76, 175, 80);
    private static final Color MAU_LOI = new Color(244, 67, 54);
    private static final Color MAU_CANH_BAO = new Color(255, 152, 0);
    private static final Color MAU_NEN_NHAT = new Color(248, 249, 250);
    private static final Color MAU_VIEN = new Color(224, 224, 224);
    private static final Color MAU_VAN_BAN = new Color(33, 37, 41);
    private static final Color MAU_VAN_BAN_PHU = new Color(108, 117, 125);
    
    // ========== KHAI BÁO THÀNH PHẦN GIAO DIỆN ==========
    // Thành phần chung
    private view_main giaoDienChinh;
    private JTabbedPane tabChucNang;
    private JLabel lblBieuTuongSoDu, lblSoDu;
    private JPanel pnlTieuDe;
    ctl_NapRut boDieuKhien;
    
    // Tab Nạp tiền
    public JTextField txtSoTienNap;
    public JComboBox<String> cboPhuongThucNap;
    public JButton btnNapTien;
    public JButton btnGuiMaOTP;
    public JTextField txtMaOTP;
    public JPasswordField txtMatKhau;
    public JLabel lblSoTaiKhoan;
    
    // Tab Rút tiền
    public JTextField txtSoTienRut;
    public JButton btnRutTien;

    public view_NapRut(view_main giaoDienChinh) {
        this.giaoDienChinh = giaoDienChinh;
        thietLapKichThuoc();
        thietLapGiaoDien();

        // boDieuKhien = new ctl_NapRut(this, giaoDienChinh);
    }
    
    private void thietLapKichThuoc() {
        setPreferredSize(new Dimension(1080, 880));
        setLayout(new BorderLayout());
        setBackground(MAU_NEN_NHAT);
    }
    
    private void thietLapGiaoDien() {
        khoiTaoThanhPhan();
        cauHinhLayout();
    }
    
    private void khoiTaoThanhPhan() {
        pnlTieuDe = taoPanelTieuDe();
        tabChucNang = taoTabChucNang();
    }
    
    private void cauHinhLayout() {
        add(pnlTieuDe, BorderLayout.NORTH);
        
        JPanel pnlNoiDung = new JPanel(new BorderLayout());
        pnlNoiDung.setBackground(MAU_NEN_NHAT);
        pnlNoiDung.setBorder(new EmptyBorder(10, 20, 20, 20));
        pnlNoiDung.add(tabChucNang, BorderLayout.CENTER);
        
        add(pnlNoiDung, BorderLayout.CENTER);
    }
    
    private JPanel taoPanelTieuDe() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 40, 15, 40));
        
        JLabel tieuDe = new JLabel("NẠP/RÚT TIỀN", JLabel.CENTER);
        tieuDe.setFont(new Font("Segoe UI", Font.BOLD, 20));
        tieuDe.setForeground(MAU_VAN_BAN);
        
        JPanel pnlSoDu = taoPanelSoDu();
        
        panel.add(tieuDe, BorderLayout.NORTH);
        panel.add(Box.createVerticalStrut(10), BorderLayout.CENTER);
        panel.add(pnlSoDu, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel taoPanelSoDu() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        panel.setBackground(new Color(232, 245, 233));
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(MAU_THANH_CONG, 1, true),
            new EmptyBorder(10, 15, 10, 15)
        ));
        
        lblBieuTuongSoDu = new JLabel("💰");
        lblBieuTuongSoDu.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        
        lblSoDu = new JLabel("Số dư hiện tại: 0 VNĐ");
        lblSoDu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSoDu.setForeground(MAU_THANH_CONG);
        
        panel.add(lblBieuTuongSoDu);
        panel.add(lblSoDu);
        
        return panel;
    }
    
    private JTabbedPane taoTabChucNang() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabbedPane.setBackground(Color.WHITE);
        tabbedPane.setForeground(MAU_VAN_BAN);
        
        tabbedPane.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override
            protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex,
                                        int x, int y, int w, int h, boolean isSelected) {
                if (isSelected) {
                    g.setColor(MAU_CHINH);
                    g.fillRect(x, y + h - 3, w, 3);
                }
            }
        });
        
        tabbedPane.addTab("💳 Nạp tiền", taoTabNapTien());
        tabbedPane.addTab("💸 Rút tiền", taoTabRutTien());
        
        return tabbedPane;
    }

    private JPanel taoTabNapTien() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 30, 30, 30));
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        
        // Thông tin tài khoản
        formPanel.add(taoSection("Thông tin tài khoản", taoPanelThongTinTaiKhoan()));
        formPanel.add(Box.createVerticalStrut(20));
        
        // Số tiền nạp
        JPanel pnlSoTien = new JPanel(new BorderLayout());
        pnlSoTien.setBackground(Color.WHITE);
        
        txtSoTienNap = new JTextField();
        txtSoTienNap.setPreferredSize(new Dimension(300, 35));
        
        JLabel lblDonVi = new JLabel(" VNĐ");
        lblDonVi.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDonVi.setForeground(MAU_VAN_BAN_PHU);
        
        pnlSoTien.add(txtSoTienNap, BorderLayout.WEST);
        pnlSoTien.add(lblDonVi, BorderLayout.CENTER);
        
        formPanel.add(taoSection("Số tiền nạp", pnlSoTien));
        formPanel.add(Box.createVerticalStrut(20));
        
        // Phương thức thanh toán
        cboPhuongThucNap = taoComboBox(new String[]{
            "🏦 Chuyển khoản ngân hàng", 
            "📱 Ví điện tử", 
            "💳 Thẻ tín dụng"
        });
        
        formPanel.add(taoSection("Phương thức thanh toán", cboPhuongThucNap));
        formPanel.add(Box.createVerticalStrut(20));
        
        // Xác thực bảo mật
        formPanel.add(taoSection("Xác thực bảo mật", taoPanelBaoMat()));
        formPanel.add(Box.createVerticalStrut(25));
        
        // Nút nạp tiền
        JPanel pnlNut = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlNut.setBackground(Color.WHITE);
        pnlNut.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        btnNapTien = taoNut("💰 NẠP TIỀN", MAU_THANH_CONG);
        pnlNut.add(btnNapTien);
        
        formPanel.add(pnlNut);
        formPanel.add(Box.createVerticalStrut(20));
        
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.setBackground(Color.WHITE);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        return mainPanel;
    }

    private JPanel taoTabRutTien() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 30, 30, 30));
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        
        // Thông tin tài khoản
        formPanel.add(taoSection("Thông tin tài khoản", taoPanelThongTinTaiKhoan()));
        formPanel.add(Box.createVerticalStrut(20));
        
        // Số tiền rút
        JPanel pnlSoTien = new JPanel(new BorderLayout());
        pnlSoTien.setBackground(Color.WHITE);
        
        txtSoTienRut = new JTextField();
        txtSoTienRut.setPreferredSize(new Dimension(300, 35));
        
        JLabel lblDonVi = new JLabel(" VNĐ");
        lblDonVi.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblDonVi.setForeground(MAU_VAN_BAN_PHU);
        
        pnlSoTien.add(txtSoTienRut, BorderLayout.WEST);
        pnlSoTien.add(lblDonVi, BorderLayout.CENTER);
        
        formPanel.add(taoSection("Số tiền rút", pnlSoTien));
        formPanel.add(Box.createVerticalStrut(20));
        
        // Phương thức rút
        JComboBox<String> cboPhuongThucRut = taoComboBox(new String[]{
            "🏦 Chuyển khoản ngân hàng", 
            "📱 Ví điện tử", 
            "💳 Thẻ tín dụng"
        });
        
        formPanel.add(taoSection("Phương thức rút", cboPhuongThucRut));
        formPanel.add(Box.createVerticalStrut(20));
        
        // Xác thực bảo mật
        formPanel.add(taoSection("Xác thực bảo mật", taoPanelBaoMat()));
        formPanel.add(Box.createVerticalStrut(25));
        
        // Nút rút tiền
        JPanel pnlNut = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlNut.setBackground(Color.WHITE);
        pnlNut.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        btnRutTien = taoNut("💸 RÚT TIỀN", MAU_LOI);
        pnlNut.add(btnRutTien);
        
        formPanel.add(pnlNut);
        formPanel.add(Box.createVerticalStrut(20));
        
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.setBackground(Color.WHITE);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        return mainPanel;
    }
    
    private JPanel taoPanelThongTinTaiKhoan() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(MAU_NEN_NHAT);
        panel.setBorder(new EmptyBorder(12, 15, 12, 15));
        panel.setPreferredSize(new Dimension(400, 60));
        
        JLabel lblTieuDe = new JLabel("🏛️ Số tài khoản:");
        lblTieuDe.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTieuDe.setForeground(MAU_VAN_BAN_PHU);
        
        lblSoTaiKhoan = new JLabel("0123456789");
        lblSoTaiKhoan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSoTaiKhoan.setForeground(MAU_VAN_BAN);
        
        JPanel content = new JPanel(new GridLayout(2, 1, 0, 3));
        content.setBackground(MAU_NEN_NHAT);
        content.add(lblTieuDe);
        content.add(lblSoTaiKhoan);
        
        panel.add(content, BorderLayout.WEST);
        return panel;
    }
    
    private JPanel taoPanelBaoMat() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        // Phần mật khẩu
        JPanel pnlMatKhau = new JPanel();
        pnlMatKhau.setLayout(new BoxLayout(pnlMatKhau, BoxLayout.Y_AXIS));
        pnlMatKhau.setBackground(Color.WHITE);
        pnlMatKhau.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtMatKhau = new JPasswordField();
        txtMatKhau.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtMatKhau.setPreferredSize(new Dimension(300, 35));
        txtMatKhau.setMaximumSize(new Dimension(300, 35));
        txtMatKhau.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(MAU_VIEN, 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        
        pnlMatKhau.add(taoFieldVoiNhan("🔒 Mật khẩu:", txtMatKhau));
        panel.add(pnlMatKhau);
        panel.add(Box.createVerticalStrut(15));
        
        // Phần OTP
        JPanel pnlOTP = new JPanel();
        pnlOTP.setLayout(new BoxLayout(pnlOTP, BoxLayout.Y_AXIS));
        pnlOTP.setBackground(Color.WHITE);
        pnlOTP.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtMaOTP = new JTextField();
        txtMaOTP.setPreferredSize(new Dimension(200, 35));
        txtMaOTP.setMaximumSize(new Dimension(200, 35));
        
        btnGuiMaOTP = taoNutPhu("📧 Gửi mã");
        btnGuiMaOTP.setPreferredSize(new Dimension(120, 35));
        // btnGuiMaOTP.addActionListener(e -> {
        //     if (boDieuKhien != null) {
        //         // boDieuKhien.guiMaOTP(btnGuiMaOTP);
        //         boDieuKhien.guiMaOTP();
        //     }
        // });
        
        JPanel pnlNhapOTP = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlNhapOTP.setBackground(Color.WHITE);
        pnlNhapOTP.add(txtMaOTP);
        pnlNhapOTP.add(btnGuiMaOTP);
        
        pnlOTP.add(taoFieldVoiNhan("🔐 Mã OTP:", pnlNhapOTP));
        panel.add(pnlOTP);
        
        return panel;
    }
    
    private JPanel taoFieldVoiNhan(String nhan, JComponent field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblNhan = new JLabel(nhan);
        lblNhan.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblNhan.setForeground(MAU_VAN_BAN);
        lblNhan.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.add(field, BorderLayout.WEST);
        
        panel.add(lblNhan);
        panel.add(Box.createVerticalStrut(5));
        panel.add(wrapper);
        
        return panel;
    }
    
    private JPanel taoSection(String tieuDe, JComponent component) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblTieuDe = new JLabel(tieuDe);
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTieuDe.setForeground(MAU_CHINH);
        lblTieuDe.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(lblTieuDe);
        panel.add(Box.createVerticalStrut(10));
        
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.add(component, BorderLayout.WEST);
        
        panel.add(wrapper);
        
        return panel;
    }
    
    private JComboBox<String> taoComboBox(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setPreferredSize(new Dimension(350, 40));
        combo.setMaximumSize(new Dimension(350, 40));
        combo.setBorder(new LineBorder(MAU_VIEN, 1));
        combo.setBackground(Color.WHITE);
        return combo;
    }
    
    private JButton taoNut(String text, Color mauNen) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(200, 50));
        button.setBackground(mauNen);
        button.setForeground(Color.WHITE);
        button.setBorder(null);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(mauNen.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(mauNen);
            }
        });
        
        return button;
    }
    
    private JButton taoNutPhu(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setPreferredSize(new Dimension(120, 35));
        button.setBackground(Color.WHITE);
        button.setForeground(MAU_CHINH);
        button.setBorder(new LineBorder(MAU_CHINH, 1));
        button.setFocusPainted(false);
        // button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(MAU_CHINH);
                button.setForeground(Color.WHITE);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.WHITE);
                button.setForeground(MAU_CHINH);
            }
        });
        
        return button;
    }

    private void khoiTaoBoDieuKhien() {
        boDieuKhien = new ctl_NapRut(this, giaoDienChinh);
    }

    // ========== CÁC PHƯƠNG THỨC GETTER ==========
    public String getMaNguoiDung() {
        return giaoDienChinh != null ? giaoDienChinh.getMaNguoiDung() : null;
    }

    public String getMaVaiTro() {
        return giaoDienChinh != null ? giaoDienChinh.getMaVaiTro() : null;
    }

    public void capNhatSoDu(String soDu) {
        try {
            long amount = Long.parseLong(soDu.replaceAll("[^0-9]", ""));
            NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
            lblSoDu.setText("Số dư hiện tại: " + formatter.format(amount) + " VNĐ");
        } catch (NumberFormatException e) {
            lblSoDu.setText("Số dư hiện tại: " + soDu + " VNĐ");
        }
    }

    public view_main getGiaoDienChinh() {
        return giaoDienChinh;
    }
}