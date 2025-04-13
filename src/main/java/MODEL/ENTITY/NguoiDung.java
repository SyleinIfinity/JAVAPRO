package MODEL.ENTITY;

public class NguoiDung {
    public String maNguoiDung;
    public String tenNguoiDung;
    public String ngaySinh;
    public String SDT;
    public String email;
    public String matKhau;
    public String soDuTaiKhoan;
    public String maVaiTro;

    public NguoiDung(){

    }

    public NguoiDung(String maNguoiDung, String tenNguoiDung, String ngaySinh, String sDT, String email, String matKhau,
            String soDuTaiKhoan, String maVaiTro) {
        this.maNguoiDung = maNguoiDung;
        this.tenNguoiDung = tenNguoiDung;
        this.ngaySinh = ngaySinh;
        SDT = sDT;
        this.email = email;
        this.matKhau = matKhau;
        this.soDuTaiKhoan = soDuTaiKhoan;
        this.maVaiTro = maVaiTro;
    }

    public String getMaNguoiDung() {
        return maNguoiDung;
    }

    public void setMaNguoiDung(String maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }

    public String getTenNguoiDung() {
        return tenNguoiDung;
    }

    public void setTenNguoiDung(String tenNguoiDung) {
        this.tenNguoiDung = tenNguoiDung;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String sDT) {
        SDT = sDT;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getSoDuTaiKhoan() {
        return soDuTaiKhoan;
    }

    public void setSoDuTaiKhoan(String soDuTaiKhoan) {
        this.soDuTaiKhoan = soDuTaiKhoan;
    }

    public String getMaVaiTro() {
        return maVaiTro;
    }

    public void setMaVaiTro(String maVaiTro) {
        this.maVaiTro = maVaiTro;
    }

    @Override
    public String toString() {
        return "nguoidung [maNguoiDung=" + maNguoiDung + ", tenNguoiDung=" + tenNguoiDung + ", ngaySinh=" + ngaySinh
                + ", SDT=" + SDT + ", email=" + email + ", matKhau=" + matKhau + ", soDuTaiKhoan=" + soDuTaiKhoan
                + ", maVaiTro=" + maVaiTro + "]";
    }

}
