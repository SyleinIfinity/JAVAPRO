package MODEL.ENTITY;

public class DichVu {
    private String maDichVu;    
    private String tenDichVu;
    private String maLoaiDichVu;

    public DichVu(){

    }
    
    public DichVu(String maDichVu, String tenDichVu, String maLoaiDichVu) {
        this.maDichVu = maDichVu;
        this.tenDichVu = tenDichVu;
        this.maLoaiDichVu = maLoaiDichVu;
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

    public String getMaLoaiDichVu() {
        return maLoaiDichVu;
    }

    public void setMaLoaiDichVu(String maLoaiDichVu) {
        this.maLoaiDichVu = maLoaiDichVu;
    }

    @Override
    public String toString() {
        return "dichvu [maDichVu=" + maDichVu + ", tenDichVu=" + tenDichVu + ", maLoaiDichVu=" + maLoaiDichVu + "]";
    }
    
}
