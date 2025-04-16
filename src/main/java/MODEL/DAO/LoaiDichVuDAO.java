package MODEL.DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

import MODEL.ENTITY.LoaiDichVu;
import UTILS.CONNECTIONDATA.CONNECTIONSQLSERVER;

public class LoaiDichVuDAO {
    HashMap <String, LoaiDichVu> listLOAIDICHVU;
    Connection conn = null;

    public LoaiDichVuDAO(){
        listLOAIDICHVU = new HashMap<>();
        try {
            conn = CONNECTIONSQLSERVER.getConnection();

            CallableStatement stmt = conn.prepareCall("{Call sp_LayDanhSachLoaiDichVu}");

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                LoaiDichVu p = new LoaiDichVu(
                    rs.getString("maLoaiDichVu"),
                    rs.getString("tenLoaiDichVu"),
                    rs.getDouble("giaDichVu"),
                    rs.getString("moTa")
                );
                listLOAIDICHVU.put(p.getMaLoaiDichVu(), p);
            }
            System.out.println("Load thành công");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi");
        }
    }

    public HashMap<String, LoaiDichVu> listLOAIDICHVU(){
        return listLOAIDICHVU;
    }

    public LoaiDichVu getLoaiDichVu(String maLoaiDichVu){
        return listLOAIDICHVU.get(maLoaiDichVu);
    }

    public int themLoaiDicVu(LoaiDichVu ldv){
        try {
            CallableStatement stmt = conn.prepareCall("{Call sp_ThemLoaiDicVu(?,?,?)}");
            stmt.setString(1, ldv.getMaLoaiDichVu());
            stmt.setString(2, ldv.getTenLoaiDichVu());
            stmt.setDouble(3, ldv.getGiaDichVu());
            stmt.setString(4, ldv.getMoTa());

            int row = stmt.executeUpdate();
            return row;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int capNhatLoaiDicVu(LoaiDichVu ldv){
        try {
            CallableStatement stmt = conn.prepareCall("{Call sp_CapNhatLoaiDichVu(?,?,?)}");
            stmt.setString(1, ldv.getMaLoaiDichVu());
            stmt.setString(2, ldv.getTenLoaiDichVu());
            stmt.setDouble(3, ldv.getGiaDichVu());
            stmt.setString(4, ldv.getMoTa());

            int row = stmt.executeUpdate();
            return row;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int xoaLoaiDichVu(String maLoaiDichVu){
        try {
            CallableStatement stmt = conn.prepareCall("{Call sp_XoaLoaiDichVu(?)}");
            stmt.setString(1, maLoaiDichVu);

            int row = stmt.executeUpdate();
            return row;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    
    public static void main(String[] args) {
        LoaiDichVuDAO ldvD = new LoaiDichVuDAO();
    }
}
