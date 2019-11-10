package service;

import dao.BankClientDAO;
import exception.DBException;
import model.BankClient;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BankClientService {

    public BankClientService() {}

    public BankClient getClientById(long id) {
        return getBankClientDAO().getClientById(id);
    }

    public BankClient getClientByName(String name) {
        return getBankClientDAO().getClientByName(name);
    }

    public List<BankClient> getAllClient() {
        return getBankClientDAO().getAllBankClient();
    }

    public boolean clientExist(String name) {
        if (getClientByName(name) != null) {
            return true;
        }
        return false;
    }

    public boolean validateUser(String name, String password) {
        return getBankClientDAO().validateClient(name, password);
    }


    public boolean deleteClient(String name) {
        return false;
    }

    public boolean addClient(BankClient client) {
        if (clientExist(client.getName())) {
            return false;
        }
        return getBankClientDAO().addClient(client);
    }

    public boolean sendMoneyToClient(BankClient sender, String name, Long value) {
        String senderName = sender.getName();
        String senderPass = sender.getPassword();
        if (validateUser(senderName, senderPass) && value > 0) {
            BankClient clientTo = getClientByName(name);
            if (clientTo != null && isClientHasSum(senderName, value)) {
                getBankClientDAO().updateClientsMoney(senderName, senderPass, -value);
                getBankClientDAO().updateClientsMoney(name, clientTo.getPassword(), value);
                return true;
            }
        }
        return false;
    }

    public boolean isClientHasSum(String name, Long money) {
        try {
            return getBankClientDAO().isClientHasSum(name, money);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void cleanUp() throws DBException {
        BankClientDAO dao = getBankClientDAO();
        dao.dropTable();
    }

    public void createTable() throws DBException {
        BankClientDAO dao = getBankClientDAO();
        dao.createTable();
    }

    private static Connection getMysqlConnection() {
        String url = "jdbc:mysql://localhost:3306/db_example?serverTimezone=Europe/Moscow&useSSL=false";
        String username = "root";
        String password = "12345678";
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Соединение с БД установленно!");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private static BankClientDAO getBankClientDAO() {
        return new BankClientDAO(getMysqlConnection());
    }
}
