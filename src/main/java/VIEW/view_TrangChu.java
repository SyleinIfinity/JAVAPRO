package VIEW;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import CONTROLLER.APP.ctl_TrangChu;

public class view_TrangChu extends JFrame {
    private JPanel pnContent;
    // private ctl_TrangChu controller;

    public view_TrangChu() {
        setTitle("Khách sạn K-Team");
        setSize(1080, 880);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(40, 40, 40));

        // Panel chứa nội dung chính
        pnContent = new JPanel();
        pnContent.setLayout(new BorderLayout());
        pnContent.setBackground(new Color(30, 32, 36));
        add(pnContent, BorderLayout.CENTER);

        // Panel chứa thông tin giới thiệu
        JPanel pnGioiThieu = new JPanel();
        pnGioiThieu.setLayout(new BorderLayout(20, 20));
        pnGioiThieu.setBackground(new Color(30, 32, 36));
        pnGioiThieu.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Tiêu đề
        JLabel lblTieuDe = new JLabel("Chào mừng đến với Khách sạn K-Team", JLabel.CENTER);
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTieuDe.setForeground(new Color(33, 150, 243));
        pnGioiThieu.add(lblTieuDe, BorderLayout.NORTH);

        // Panel trung tâm
        JPanel centerPanel = new JPanel(new BorderLayout(20, 20));
        centerPanel.setOpaque(false);

        // Hình ảnh minh họa
        JLabel lblImage = new JLabel();
        lblImage.setHorizontalAlignment(JLabel.CENTER);
        lblImage.setIcon(new ImageIcon("src/main/resources/IMAGES/bg_home.png"));
        lblImage.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));
        centerPanel.add(lblImage, BorderLayout.CENTER);

        // Card chứa phần mô tả
        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBackground(new Color(255, 255, 255, 200));
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JTextArea txtGioiThieu = new JTextArea();
        txtGioiThieu.setEditable(false);
        txtGioiThieu.setWrapStyleWord(true);
        txtGioiThieu.setLineWrap(true);
        txtGioiThieu.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        txtGioiThieu.setOpaque(false);
        txtGioiThieu.setText(
            "Khách sạn K-Team mang đến cho bạn không gian nghỉ dưỡng hiện đại, sang trọng và thân thiện.\n\n" +
            "✔️ Phòng nghỉ đạt chuẩn ★★★★\n" +
            "✔️ Nhà hàng, hồ bơi, spa cao cấp\n" +
            "✔️ Vị trí trung tâm thành phố – thuận tiện đi lại\n\n" +
            "📍 123 Java Street, Quận Swing, TP. Code\n" +
            "📞 0123 456 789 | 🌐 www.kteamhotel.vn"
        );

        cardPanel.add(txtGioiThieu, BorderLayout.CENTER);
        centerPanel.add(cardPanel, BorderLayout.SOUTH);

        pnGioiThieu.add(centerPanel, BorderLayout.CENTER);

        // Thêm panel giới thiệu vào panel nội dung
        pnContent.add(pnGioiThieu, BorderLayout.CENTER);

        // Khởi tạo controller
        // controller = new ctl_TrangChu(this, pnContent);

        // Hiển thị mặc định form đăng nhập
        // controller.hienThiFormDangNhap();
    }

    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> {
            new view_TrangChu().setVisible(true);
        });
    }
}
