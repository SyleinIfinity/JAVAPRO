package DLL.DA;

public class Phong {
    private String maPhong;
    private String soPhong;
    private String maLoaiPhong;
    private int soTang;
    private String maChiNhanh;
    private String trangThai;

    public Phong(){

    }

    public Phong(String maPhong, String soPhong, String maLoaiPhong, int soTang, String maChiNhanh, String trangThai) {
        this.maPhong = maPhong;
        this.soPhong = soPhong;
        this.maLoaiPhong = maLoaiPhong;
        this.soTang = soTang;
        this.maChiNhanh = maChiNhanh;
        this.trangThai = trangThai;
    }

    public String getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(String maPhong) {
        this.maPhong = maPhong;
    }

    public String getSoPhong() {
        return soPhong;
    }

    public void setSoPhong(String soPhong) {
        this.soPhong = soPhong;
    }

    public String getMaLoaiPhong() {
        return maLoaiPhong;
    }

    public void setMaLoaiPhong(String maLoaiPhong) {
        this.maLoaiPhong = maLoaiPhong;
    }

    public int getSoTang() {
        return soTang;
    }

    public void setSoTang(int soTang) {
        this.soTang = soTang;
    }

    public String getMaChiNhanh() {
        return maChiNhanh;
    }

    public void setMaChiNhanh(String maChiNhanh) {
        this.maChiNhanh = maChiNhanh;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return "phong [maPhong=" + maPhong + ", soPhong=" + soPhong + ", maLoaiPhong=" + maLoaiPhong + ", soTang="
                + soTang + ", maChiNhanh=" + maChiNhanh + ", trangThai=" + trangThai + "]";
    }

}
