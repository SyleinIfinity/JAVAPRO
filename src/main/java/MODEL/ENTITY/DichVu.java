package MODEL.ENTITY;

public class DichVu {
    private String maDichVu;    
    private String tenDichVu;
    private Double giaDichVu;
    private String moTa;

    public DichVu(){

    }
    
    public DichVu(String maDichVu, String tenDichVu, Double giaDichVu, String moTa) {
        this.maDichVu = maDichVu;
        this.tenDichVu = tenDichVu;
        this.giaDichVu = giaDichVu;
        this.moTa = moTa;
    }

        public DichVu(String tenDichVu, Double giaDichVu, String moTa) {
        // this.maDichVu = maDichVu;
        this.tenDichVu = tenDichVu;
        this.giaDichVu = giaDichVu;
        this.moTa = moTa;
    }

    public String getMaDichVu() {
        return maDichVu;
    }

    public void setMaDichVu(String maDichVu) {
        this.maDichVu = maDichVu;
    }

    public String getTenDichVu() {
        return tenDichVu;
    }

    public void setTenDichVu(String tenDichVu) {
        this.tenDichVu = tenDichVu;
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
        return "DichVu{" +
                "maDichVu='" + maDichVu + '\'' +
                ", tenDichVu='" + tenDichVu + '\'' +
                ", giaDichVu=" + giaDichVu +
                ", moTa='" + moTa + '\'' +
                '}';
    }
    
}
