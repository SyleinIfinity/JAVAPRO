package MODEL.ENTITY;

public class LoaiDichVu {
    private String maLoaiDichVu;
    private String tenLoaiDichVu;
    private Double giaDichVu;
    private String moTa;

    public LoaiDichVu(){

    }

    public LoaiDichVu(String maLoaiDichVu, String tenLoaiDichVu, Double giaDichVu, String moTa) {
        this.maLoaiDichVu = maLoaiDichVu;
        this.tenLoaiDichVu = tenLoaiDichVu;
        this.giaDichVu = giaDichVu;
        this.moTa = moTa;
    }

    public String getMaLoaiDichVu() {
        return maLoaiDichVu;
    }

    public void setMaLoaiDichVu(String maLoaiDichVu) {
        this.maLoaiDichVu = maLoaiDichVu;
    }

    public String getTenLoaiDichVu() {
        return tenLoaiDichVu;
    }

    public void setTenLoaiDichVu(String tenLoaiDichVu) {
        this.tenLoaiDichVu = tenLoaiDichVu;
    }

    public Double getGiaDichVu() {
        return giaDichVu;
    }

    public void setGiaDichVu(Double giaDichVu) {
        this.giaDichVu = giaDichVu;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    @Override
    public String toString() {
        return "loaidichvu [maLoaiDichVu=" + maLoaiDichVu + ", tenLoaiDichVu=" + tenLoaiDichVu + ", giaDichVu="
                + giaDichVu + ", moTa=" + moTa + "]";
    }

}
