package MODEL.ENTITY;

public class HoaDon {
    public String maHoaDon;
    public String maDatPhong;
    public String maDichVu;
    public String nhanVienPhuTrach;
    public Double tongTien;
    public String ngayGiaoDich;
    public String phuongThucThanhToan;

    public HoaDon(){

    }

    public HoaDon(String maHoaDon, String maDatPhong, String maDichVu, String nhanVienPhuTrach, Double tongTien, String ngayGiaoDich,
            String phuongThucThanhToan) {
        this.maHoaDon = maHoaDon;
        this.maDatPhong = maDatPhong;
        this.maDichVu = maDichVu;
        this.nhanVienPhuTrach = nhanVienPhuTrach;
        this.tongTien = tongTien;
        this.ngayGiaoDich = ngayGiaoDich;
        this.phuongThucThanhToan = phuongThucThanhToan;
    }

    public String getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(String maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public String getMaDatPhong() {
        return maDatPhong;
    }

    public void setMaDatPhong(String maDatPhong) {
        this.maDatPhong = maDatPhong;
    }

    public String getMaDichVu() {
        return maDichVu;
    }

    public void setMaDichVu(String maDichVu) {
        this.maDichVu = maDichVu;
    }

    public String getNhanVienPhuTrach() {
        return nhanVienPhuTrach;
    }

    public void setNhanVienPhuTrach(String nhanVienPhuTrach) {
        this.nhanVienPhuTrach = nhanVienPhuTrach;
    }

    public Double getTongTien() {
        return tongTien;
    }

    public void setTongTien(Double tongTien) {
        this.tongTien = tongTien;
    }

    public String getNgayGiaoDich() {
        return ngayGiaoDich;
    }

    public void setNgayGiaoDich(String ngayGiaoDich) {
        this.ngayGiaoDich = ngayGiaoDich;
    }

    public String getPhuongThucThanhToan() {
        return phuongThucThanhToan;
    }

    public void setPhuongThucThanhToan(String phuongThucThanhToan) {
        this.phuongThucThanhToan = phuongThucThanhToan;
    }

    @Override
    public String toString() {
        return "HoaDon [maHoaDon=" + maHoaDon + ", maDatPhong=" + maDatPhong + ", maDichVu=" + maDichVu
                + ", nhanVienPhuTrach=" + nhanVienPhuTrach + ", tongTien=" + tongTien + ", ngayGiaoDich=" + ngayGiaoDich
                + ", phuongThucThanhToan=" + phuongThucThanhToan + "]";
    }



}
