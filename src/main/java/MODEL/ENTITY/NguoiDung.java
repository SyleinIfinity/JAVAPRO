package MODEL.ENTITY;

public class NguoiDung {
    private String maNguoiDung;
    private String tenNguoiDung;
    private String ngaySinh;
    private String SDT;
    private String email;
    private String matKhau;
    private Double soDuTaiKhoan;
    private String maVaiTro;
    private int trangThai;

    public NguoiDung(){

    }

    public NguoiDung(String maNguoiDung, String tenNguoiDung, String ngaySinh, String sDT, String email, String matKhau,
            Double soDuTaiKhoan, String maVaiTro, int trangThai) {
        this.maNguoiDung = maNguoiDung;
        this.tenNguoiDung = tenNguoiDung;
        this.ngaySinh = ngaySinh;
        this.SDT = sDT;
        this.email = email;
        this.matKhau = matKhau;
        this.soDuTaiKhoan = soDuTaiKhoan;
        this.maVaiTro = maVaiTro;
        this.trangThai = trangThai;
    }

        public NguoiDung(String tenNguoiDung, String ngaySinh, String sDT, String email, String matKhau,
            Double soDuTaiKhoan, String maVaiTro, int trangThai) {
        // this.maNguoiDung = maNguoiDung;
        this.tenNguoiDung = tenNguoiDung;
        this.ngaySinh = ngaySinh;
        this.SDT = sDT;
        this.email = email;
        this.matKhau = matKhau;
        this.soDuTaiKhoan = soDuTaiKhoan;
        this.maVaiTro = maVaiTro;
        this.trangThai = trangThai;
    }

    public int isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
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

    public Double getSoDuTaiKhoan() {
        return soDuTaiKhoan;
    }

    public void setSoDuTaiKhoan(Double soDuTaiKhoan) {
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
