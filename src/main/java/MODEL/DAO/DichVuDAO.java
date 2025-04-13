package MODEL.DAO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

import MODEL.ENTITY.DichVu;
import UTILS.CONNECTIONDATA.CONNECTIONSQLSERVER;

public class DichVuDAO {
    HashMap<String, DichVu> listDICHVU;
    Connection conn = null;

    public DichVuDAO(){
        listDICHVU = new HashMap<>();
        try {
            conn = CONNECTIONSQLSERVER.GetConnection();

            CallableStatement stmt = conn.prepareCall("{Call sp_LayDanhSachDichVu}");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                DichVu dv = new DichVu(
                    rs.getString("maDichVu"),
                    rs.getString("tenDichVu"),
                    rs.getString("maLoaiDichVu")
                );
                listDICHVU.put(dv.getMaDichVu(), dv);
            }
            System.out.println("Load thanh cong");
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi");
        }
    }

    public HashMap<String, DichVu> getListDICHVU(){
        return listDICHVU;
    }

    // public DichVu getDichVu(String maDichvu){

    // }

    public static void main(String[] args) {
        new DichVuDAO();
    }

}
