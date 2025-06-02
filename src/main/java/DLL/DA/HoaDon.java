package DLL.DA;

public class HoaDon {
    private String maHoaDon;
    private String maDatPhong;
    private String maDichVu;
    private String nhanVienPhuTrach;
    private Double tongTien;
    private String ngayGiaoDich;

    public HoaDon(){

    }

    public HoaDon(String maHoaDon, String maDatPhong, String maDichVu, String nhanVienPhuTrach, Double tongTien, String ngayGiaoDich) {
        this.maHoaDon = maHoaDon;
        this.maDatPhong = maDatPhong;
        this.maDichVu = maDichVu;
        this.nhanVienPhuTrach = nhanVienPhuTrach;
        this.tongTien = tongTien;
        this.ngayGiaoDich = ngayGiaoDich;
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

    @Override
    public String toString() {
        return "HoaDon [maHoaDon=" + maHoaDon + ", maDatPhong=" + maDatPhong + ", maDichVu=" + maDichVu
                + ", nhanVienPhuTrach=" + nhanVienPhuTrach + ", tongTien=" + tongTien + ", ngayGiaoDich=" + ngayGiaoDich
                + ", phuongThucThanhToan=" + "]";
    }



}
