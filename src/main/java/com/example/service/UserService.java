package com.example.service;

import com.example.dao.UserDAO;
import com.example.model.User;
import org.mindrot.jbcrypt.BCrypt;


import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class UserService {
    private final Connection connection;
    private static UserService instance;
    public UserService() {
        this.connection = getMysqlConnection();
    }
    @SuppressWarnings("UnusedDeclaration")
    public static Connection getMysqlConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            DriverManager.registerDriver((Driver) Class.forName("com.mysql.jdbc.Driver").newInstance());
            Connection connection = DriverManager.getConnection("jdbc:sqlite:D:\\Demo\\servlet\\usersdb.db");
            return connection;
        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static synchronized UserService getInstance() throws SQLException {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }
    public synchronized boolean register(String login, String password, String email) throws DBException {
        UserDAO dao = new UserDAO(connection);
        try {
            connection.setAutoCommit(false);
            try{
                User user = dao.getUser(login);
            } catch (SQLException e){
                String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
                try {
                    dao.addUser(new User(login, hashed, email));
                }catch (SQLException sql){
                    return false;
                }
                connection.commit();
                return true;
            }
            return false;
        }catch (SQLException e){
            try{
                connection.rollback();
            } catch (SQLException ignore) {}
            throw new DBException(e);
        }finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ignore) {}
        }
    }
    public synchronized User authenticate(String login, String password) throws DBException {
        UserDAO dao = new UserDAO(connection);
        try{
            User user = dao.getUser(login);
            boolean match = BCrypt.checkpw(password, user.getPassHash());
            return match ? user : null;
        }catch (SQLException e){
            return null;
        }
    }
}
