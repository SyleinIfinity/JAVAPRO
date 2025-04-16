package CONTROLLER.SERVLET;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import CONTROLLER.SERVLET.Reponsitory.NguoiDungReponsitory;
import MODEL.ENTITY.NguoiDung;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/login.html").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email").trim();
        String password = req.getParameter("password").trim();

        if (email != null && password != null) {
            NguoiDungReponsitory user = new NguoiDungReponsitory();
            List<NguoiDung> users = user.getUserByEmailAndPassword(email, password);
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

}