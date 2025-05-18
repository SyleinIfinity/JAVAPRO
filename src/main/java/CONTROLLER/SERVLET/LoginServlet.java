package CONTROLLER.SERVLET;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
// import javax.servlet.ServletException;
// import javax.servlet.annotation.WebServlet;
// import javax.servlet.http.HttpServlet;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import javax.servlet.http.HttpSession;

import CONTROLLER.SERVLET.Reponsitory.NguoiDungReponsitory;
import MODEL.ENTITY.NguoiDung;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    String email;
    String password;
    List<NguoiDung> users;
    NguoiDungReponsitory user;

    public LoginServlet(){
        user = new NguoiDungReponsitory();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/login.html").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.email = req.getParameter("email").trim();
        this.password = req.getParameter("password").trim();
        System.out.println(email + "  " + password);
        if (email != null && password != null) {
            // NguoiDungReponsitory user = new NguoiDungReponsitory();
            this.users = user.getUserByEmailAndPassword(email, password);
            if (users.size() > 0) {
                HttpSession session = req.getSession();
                session.setAttribute("loggedInUser", users.get(0));
                resp.sendRedirect(req.getContextPath() + "/");
            } else {
                req.getRequestDispatcher("/login.html").forward(req, resp);
            }
        } else {
            req.setAttribute("errorMessage", "Email and password are required.");
            req.getRequestDispatcher("/login.html").forward(req, resp);
        }
    }

    public static void main(String[] args) {
        NguoiDungReponsitory user = new NguoiDungReponsitory();
        LoginServlet lg = new LoginServlet();
        lg.users = user.getUserByEmailAndPassword("a@gmail.com", "khanh123");

        if (lg.users.isEmpty()) {
            System.out.println("Không tìm thấy người dùng.");
        } else {
            for (NguoiDung users : lg.users) {
                System.out.println("Mã người dùng: " + users.getMaNguoiDung());
                System.out.println("Tên: " + users.getTenNguoiDung());
                System.out.println("Ngày sinh: " + users.getNgaySinh());
                System.out.println("SĐT: " + users.getSDT());
                System.out.println("Email: " + users.getEmail());
                System.out.println("Số dư: " + users.getSoDuTaiKhoan());
                System.out.println("Vai trò: " + users.getMaVaiTro());
                System.out.println("-----------");
            }
        }
    }
}