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
            conn = CONNECTIONSQLSERVER.getConnection();

            CallableStatement stmt = conn.prepareCall("{Call sp_LayDanhSachDichVu}");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                DichVu dv = new DichVu(
                    rs.getString("maDichVu"),
                    rs.getString("tenDichVu"),
                    rs.getDouble("giaDichVu"),
                    rs.getString("moTa")
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

    public DichVu getDichVu(String maDichvu){
        return listDICHVU.get(maDichvu);
    }

    public int themDichVu(DichVu dv){
        try {
            CallableStatement stmt = conn.prepareCall("{Call sp_ThemDichVu(?,?,?)}");
            // stmt.setString(1, dv.getMaDichVu());
            stmt.setString(1, dv.getTenDichVu());
            stmt.setDouble(2, dv.getGiaDichVu());
            stmt.setString(3, dv.getMoTa());

            int row = stmt.executeUpdate();
            return row;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int capNhatDichVu(DichVu dv){
        try {
            CallableStatement stmt = conn.prepareCall("{Call sp_CapNhatDichVu(?,?,?,?)}");
            stmt.setString(1, dv.getMaDichVu());
            stmt.setString(2, dv.getTenDichVu());
            stmt.setDouble(3, dv.getGiaDichVu());
            stmt.setString(4, dv.getMoTa());

            int row = stmt.executeUpdate();

            return row;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int xoaDichVu(String maDichVu){
        try {
            CallableStatement stmt = conn.prepareCall("{Call sp_XoaDichVu(?)}");
            stmt.setString(1, maDichVu);

            int row = stmt.executeUpdate();
            return row;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public DichVu getDichVuByTen(String tenDichVu) {
        for (DichVu dv : listDICHVU.values()) {
            if (dv.getTenDichVu().equalsIgnoreCase(tenDichVu)) {
                return dv;
            }
        }
        return null; // Trả về null nếu không tìm thấy
    }

    public static void main(String[] args) {
        DichVuDAO dvD = new DichVuDAO();

        // DichVu dv = new DichVu("DV003","Nope","LDV01");

        // try {
        //     int row = dvD.themDichVu(dv);
        //     if (row > 0) {
        //         System.out.println("Thành công");
        //     }


        //     int row1 = dvD.xoaDichVu("DV003");
        //     if (row1 > 0) {
        //         System.out.println("Thành công");
        //     }

        // } catch (Exception e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // }

    }

}
