package DLL.DA;

public class ChiNhanhKhachSan {
    private String maChiNhanh;
    private String tenChiNhanh;
    private String diaChi;
    private String SDT;

    public ChiNhanhKhachSan(){

    }

    public ChiNhanhKhachSan(String maChiNhanh, String tenChiNhanh, String diaChi, String sDT) {
        this.maChiNhanh = maChiNhanh;
        this.tenChiNhanh = tenChiNhanh;
        this.diaChi = diaChi;
        SDT = sDT;
    }

    public String getMaChiNhanh() {
        return maChiNhanh;
    }

    public void setMaChiNhanh(String maChiNhanh) {
        this.maChiNhanh = maChiNhanh;
    }

    public String getTenChiNhanh() {
        return tenChiNhanh;
    }

    public void setTenChiNhanh(String tenChiNhanh) {
        this.tenChiNhanh = tenChiNhanh;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String sDT) {
        SDT = sDT;
    }

    @Override
    public String toString() {
        return "chinhanhkhachsan [maChiNhanh=" + maChiNhanh + ", tenChiNhanh=" + tenChiNhanh + ", diaChi=" + diaChi
                + ", SDT=" + SDT + "]";
    }

}
