package MODEL.ENTITY;

public class DatPhong {
    public String maDatPhong;
    public String maNguoiDung;
    public String maPhong;
    public String soNguoi;
    public String dichVuSuDung;
    public String ngayThuePhong;
    public String ngayTraPhong;
    public String trangThai;

    public DatPhong(){

    }

    public DatPhong(String maDatPhong, String maNguoiDung, String maPhong, String soNguoi, String dichVuSuDung,
            String ngayThuePhong, String ngayTraPhong, String trangThai) {
        this.maDatPhong = maDatPhong;
        this.maNguoiDung = maNguoiDung;
        this.maPhong = maPhong;
        this.soNguoi = soNguoi;
        this.dichVuSuDung = dichVuSuDung;
        this.ngayThuePhong = ngayThuePhong;
        this.ngayTraPhong = ngayTraPhong;
        this.trangThai = trangThai;
    }

    public String getMaDatPhong() {
        return maDatPhong;
    }

    public void setMaDatPhong(String maDatPhong) {
        this.maDatPhong = maDatPhong;
    }

    public String getMaNguoiDung() {
        return maNguoiDung;
    }

    public void setMaNguoiDung(String maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }

    public String getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(String maPhong) {
        this.maPhong = maPhong;
    }

    public String getSoNguoi() {
        return soNguoi;
    }

    public void setSoNguoi(String soNguoi) {
        this.soNguoi = soNguoi;
    }

    public String getDichVuSuDung() {
        return dichVuSuDung;
    }

    public void setDichVuSuDung(String dichVuSuDung) {
        this.dichVuSuDung = dichVuSuDung;
    }

    public String getNgayThuePhong() {
        return ngayThuePhong;
    }

    public void setNgayThuePhong(String ngayThuePhong) {
        this.ngayThuePhong = ngayThuePhong;
    }

    public String getNgayTraPhong() {
        return ngayTraPhong;
    }

    public void setNgayTraPhong(String ngayTraPhong) {
        this.ngayTraPhong = ngayTraPhong;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return "datphong [maDatPhong=" + maDatPhong + ", maNguoiDung=" + maNguoiDung + ", maPhong=" + maPhong
                + ", soNguoi=" + soNguoi + ", dichVuSuDung=" + dichVuSuDung + ", ngayThuePhong=" + ngayThuePhong
                + ", ngayTraPhong=" + ngayTraPhong + ", trangThai=" + trangThai + "]";
    }

}
