package dao;

import model.BankClient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BankClientDAO {

    private Connection connection;

    public BankClientDAO(Connection connection) {
        this.connection = connection;
    }

    public List<BankClient> getAllBankClient() {
        List<BankClient> list = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement("select * from bank_client");
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                long id = result.getLong("id");
                String name = result.getString("name");
                String password = result.getString("password");
                Long money = result.getLong("money");
                BankClient client = new BankClient(id, name, password, money);
                list.add(client);
            }
            result.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean validateClient(String name, String password) {
        BankClient client = this.getClientByName(name);
        return client.getPassword().equals(password);
    }

    public void updateClientsMoney(String name, String password, Long transactValue) {
        String selectName = "select * from bank_client where name=?";
        String updateMoney = "UPDATE bank_client SET money = ? WHERE name LIKE ?";
        try {
            if (validateClient(name, password)) {
                PreparedStatement ps = connection.prepareStatement(selectName);
                ps.setString(1, name);
                ResultSet resultSet = ps.executeQuery();
                resultSet.next();
                Long money = resultSet.getLong(4);
                money += transactValue;
                ps = connection.prepareStatement(updateMoney);
                ps.setLong(1, money);
                ps.setString(2, name);
                ps.executeUpdate();
                resultSet.close();
                ps.close();
                System.out.println("Операция прошла успешно!");
            }
        } catch (SQLException e) {
            System.out.println("Не удачно обновленно!");
        }
    }

    public BankClient getClientById(long id) {
        BankClient client = null;
        try {
            PreparedStatement ps = connection.prepareStatement("select * from bank_client where id = ?");
            ps.setLong(1, id);
            ResultSet result = ps.executeQuery();
            result.next();
            String name = result.getString("name");
            String password = result.getString("password");
            Long money = result.getLong("money");
            client = new BankClient(name, password, money);
            result.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return client;
    }

    public boolean isClientHasSum(String name, Long expectedSum) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("select * from bank_client where name = ?");
        preparedStatement.setString(1, name);
        ResultSet result = preparedStatement.executeQuery();
        result.next();
        Long money = result.getLong("money");
        result.close();
        preparedStatement.close();
        return expectedSum <= money;
    }

    public long getClientIdByName(String name) {
        Long id = null;
        try {
            PreparedStatement ps = connection.prepareStatement("select * from bank_clien where name = ?");
            ps.setString(1, name);
            ResultSet result = ps.executeQuery();
            result.next();
            id = result.getLong(1);
            result.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    public BankClient getClientByName(String name) {
        BankClient user = null;
        String sql = "select * from bank_client where name = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, name);
            ResultSet result = ps.executeQuery();
            result.next();
            String userName = result.getString("name");
            String password = result.getString("password");
            Long money = result.getLong("money");
            user = new BankClient(userName, password, money);
            result.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public boolean addClient(BankClient client) {
        try {
            PreparedStatement ps = connection.prepareStatement("insert into bank_client(name, password, money) values (?, ?, ?)");
            ps.setString(1, client.getName());
            ps.setString(2, client.getPassword());
            ps.setLong(3, client.getMoney());
            ps.executeUpdate();
            ps.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void createTable() {
        try {
            PreparedStatement ps = connection.prepareStatement("create table if not exists bank_client (" +
                    "id bigint auto_increment, " +
                    "name varchar(256), " +
                    "password varchar(256), " +
                    "money bigint, " +
                    "primary key (id))");
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropTable() {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("DROP TABLE IF EXISTS bank_client");
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}