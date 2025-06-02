package DLL.DO;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;

import DLL.DA.VaiTro;
import UTILS.CONNECTIONDATA.CONNECTIONSQLSERVER;

public class VaiTroDAO {
    HashMap<String, VaiTro> listVAITRO;
    Connection conn = null;

    public VaiTroDAO(){
        listVAITRO = new HashMap<>();
        try {
            conn = CONNECTIONSQLSERVER.getConnection();

            CallableStatement stmt = conn.prepareCall("{Call sp_LayDanhSachVaiTro}");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                VaiTro vt = new VaiTro(
                    rs.getString("maVaiTro"),
                    rs.getString("tenVaiTro"),
                    rs.getString("moTa")
                );
                listVAITRO.put(vt.getMaVaiTro(), vt);
            }
            System.out.println("Load thanh cong");
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Lỗi");
        }
    }
    public HashMap<String, VaiTro> getListVAITRO(){
        return listVAITRO;
    }
    public VaiTro getVaiTro(String maVaiTro){
        return listVAITRO.get(maVaiTro);
    }
    public int themVaiTro(VaiTro vt){
        try {
            CallableStatement stmt = conn.prepareCall("{Call sp_ThemVaiTro(?,?,?)}");
            stmt.setString(1, vt.getMaVaiTro());
            stmt.setString(2, vt.getTenVaiTro());
            stmt.setString(3, vt.getMoTa());

            int row = stmt.executeUpdate();
            return row;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    public int xoaVaiTro(String maVaiTro){
        try {
            CallableStatement stmt = conn.prepareCall("{Call sp_XoaVaiTro(?)}");
            stmt.setString(1, maVaiTro);

            int row = stmt.executeUpdate();
            return row;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    public int capNhatVaiTro(VaiTro vt){
        try {
            CallableStatement stmt = conn.prepareCall("{Call sp_CapNhatVaiTro(?,?,?)}");
            stmt.setString(1, vt.getMaVaiTro());
            stmt.setString(2, vt.getTenVaiTro());
            stmt.setString(3, vt.getMoTa());

            int row = stmt.executeUpdate();
            return row;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    public static void main(String[] args) {
        VaiTroDAO vt = new VaiTroDAO();
        // VaiTro vt1 = new VaiTro("VT01", "Quản lý", "Quản lý hệ thống");
        // try {
        //     int row = vt.themVaiTro(vt1);
        //     if (row > 0) {
        //         System.out.println("Thêm thành công");
        //     } else {
        //         System.out.println("Thêm thất bại");
        //     }

        //     int row1 = vt.xoaVaiTro("VT01");
        //     if (row1 > 0) {
        //         System.out.println("Xóa thành công");
        //     } else {
        //         System.out.println("Xóa thất bại");
        //     }
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
    }
}
