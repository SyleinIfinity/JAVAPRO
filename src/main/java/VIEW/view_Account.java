package VIEW;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class view_Account extends Frame {
    private String maNguoiDung;
    private JLabel lbe_maND, lbe_tenND, lbe_ngaySinh, lbe_sdt, lbe_emailND, lbe_matKhau, lbe_soDuTaiKhoan;
    public JTextField txt_maND, txt_tenND, txt_ngaySinh, txt_sdt, txt_emailND, txt_matKhau, txt_soDuTaiKhoan;
    
    public view_Account(String maNguoiDung) {
        lbe_maND = new JLabel("Mã quản lý:");
        lbe_maND.setBounds(20, 20, 100, 30);
        add(lbe_maND);

        txt_maND = new JTextField();
        txt_maND.setBounds(120, 20, 200, 30);
        txt_maND.setEditable(false); // Disable editing for the ID field
        add(txt_maND);

        lbe_tenND = new JLabel("Tên quản lý");
        lbe_tenND.setBounds(120, 60, 200, 30);
        add(lbe_tenND);

        txt_tenND = new JTextField();
        txt_tenND.setBounds(120, 60, 200, 30);
        txt_tenND.setEditable(false); // Disable editing for the name field
        add(txt_tenND);

        lbe_ngaySinh = new JLabel("Ngày sinh:");
        lbe_ngaySinh.setBounds(120, 100, 200, 30);
        add(lbe_ngaySinh);

        txt_ngaySinh = new JTextField();
        txt_ngaySinh.setBounds(120, 100, 200, 30);
        txt_ngaySinh.setEditable(false); // Disable editing for the date of birth field
        add(txt_ngaySinh);

        lbe_sdt = new JLabel("Số điện thoại:");
        lbe_sdt.setBounds(120, 140, 200, 30);
        add(lbe_sdt);

        txt_sdt = new JTextField();
        txt_sdt.setBounds(120, 140, 200, 30);
        txt_sdt.setEditable(false); // Disable editing for the phone number field
        add(txt_sdt);

        lbe_emailND = new JLabel("Email:");
        lbe_emailND.setBounds(120, 180, 200, 30);
        add(lbe_emailND);

        txt_emailND = new JTextField();
        txt_emailND.setBounds(120, 180, 200, 30);
        txt_emailND.setEditable(false); // Disable editing for the email field
        add(txt_emailND);

        lbe_matKhau = new JLabel("Mật khẩu:");
        lbe_matKhau.setBounds(120, 220, 200, 30);
        add(lbe_matKhau);

        txt_matKhau = new JTextField();
        txt_matKhau.setBounds(120, 220, 200, 30);
        txt_matKhau.setEditable(false); // Disable editing for the password field
        add(txt_matKhau);
    }

}
