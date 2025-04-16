package CONTROLLER.SERVLET;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import MODEL.ENTITY.NguoiDung;

@WebServlet("/")
public class HomeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            HttpSession session = req.getSession(false);
            if (session != null) {
                NguoiDung loggedInUser = (NguoiDung) session.getAttribute("loggedInUser");
                if (loggedInUser != null) {
                    req.setAttribute("user", loggedInUser);
                    req.getRequestDispatcher("/index.jsp").forward(req, resp);
                    return;
                }
            }
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Đã xảy ra lỗi khi xử lý yêu cầu.");
        }
    }
}