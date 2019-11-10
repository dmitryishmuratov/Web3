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

public class MoneyTransactionServlet extends HttpServlet {

    BankClientService service = new BankClientService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().println("Hell o Yea");
        getServletContext().getRequestDispatcher("/transaction").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String senderName = req.getParameter("senderName");
        String senderPass = req.getParameter("senderPass");
        String countStr = req.getParameter("count");
        String nameTo = req.getParameter("nameTo");

        if (!senderName.isEmpty() && !senderPass.isEmpty() && !countStr.isEmpty() && !nameTo.isEmpty()) {
            long count = Long.parseLong(countStr);
            BankClientService service = new BankClientService();
            BankClient client = service.getClientByName(senderName);

            if (service.validateUser(senderName, senderPass) && count > 0 && service.sendMoneyToClient(client, nameTo, count)) {
                resp.setStatus(200);
                resp.getWriter().println("The transaction was successful");

            } else {
                resp.getWriter().println("transaction rejected");
            }
        }
        resp.getWriter().println(PageGenerator.getInstance().getPage("resultPage.html", new HashMap<>()));
    }
}