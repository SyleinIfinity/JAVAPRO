package MODEL.ENTITY;

public class LoaiPhong {
    public String maLoaiPhong;
    public String tenLoaiPhong;
    public int soLuongToiDa;
    public String moTa;
    public Double giaTien;

    public LoaiPhong(){

    }

    public LoaiPhong(String maLoaiPhong, String tenLoaiPhong, int soLuongToiDa, String moTa, Double giaTien) {
        this.maLoaiPhong = maLoaiPhong;
        this.tenLoaiPhong = tenLoaiPhong;
        this.soLuongToiDa = soLuongToiDa;
        this.moTa = moTa;
        this.giaTien = giaTien;
    }

    public String getMaLoaiPhong() {
        return maLoaiPhong;
    }

    public void setMaLoaiPhong(String maLoaiPhong) {
        this.maLoaiPhong = maLoaiPhong;
    }

    public String getTenLoaiPhong() {
        return tenLoaiPhong;
    }

    public void setTenLoaiPhong(String tenLoaiPhong) {
        this.tenLoaiPhong = tenLoaiPhong;
    }

    public int getSoLuongToiDa() {
        return soLuongToiDa;
    }

    public void setSoLuongToiDa(int soLuongToiDa) {
        this.soLuongToiDa = soLuongToiDa;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public Double getGiaTien() {
        return giaTien;
    }

    public void setGiaTien(Double giaTien) {
        this.giaTien = giaTien;
    }

    @Override
    public String toString() {
        return "loaiphong [maLoaiPhong=" + maLoaiPhong + ", tenLoaiPhong=" + tenLoaiPhong + ", soLuongToiDa="
                + soLuongToiDa + ", moTa=" + moTa + ", giaTien=" + giaTien + "]";
    }

    

}
