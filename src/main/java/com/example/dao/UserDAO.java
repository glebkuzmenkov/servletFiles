package com.example.dao;

import com.example.executor.Executor;
import com.example.model.User;

import java.sql.Connection;
import java.sql.SQLException;

public class UserDAO {
    Executor executor;
    public UserDAO(Connection connection){
        this.executor = new Executor(connection);
    }
    public User getUser(String login)
            throws SQLException {
        return executor.execQuery("select * from users where login='" + login+"'", result ->{
            result.next();
            return new User(result.getString("login"), result.getString("passHash"), result.getString("email"));
        });
    }
    public void addUser(User user)
        throws SQLException{
        executor.execUpdate("insert into users (login, passHash, email) values ('" + user.getLogin() + "','" + user.getPassHash() + "','" + user.getEmail() + "')");
    }
}
