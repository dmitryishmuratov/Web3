package servlet;

import model.BankClient;
import service.BankClientService;
import util.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class RegistrationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println("Hello world");
        resp.getWriter().println(PageGenerator.getInstance().getPage("registrationPage.html", new HashMap<>()));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(200);
        resp.getWriter().println("Client not add");

        String name = req.getParameter("name");
        String pass = req.getParameter("password");
        String moneyStr = req.getParameter("money");
        if (!name.equals("") && !pass.equals("") && !moneyStr.equals("")) {
            long money = Long.parseLong(moneyStr);
            BankClient bankClient = new BankClient(name, pass, money);
            BankClientService service = new BankClientService();
            if (!service.clientExist(name)) {
                service.addClient(bankClient);
                resp.setStatus(200);
                resp.getWriter().println("Add client successful");
            }
        }
        resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", new HashMap<>()));
    }

}