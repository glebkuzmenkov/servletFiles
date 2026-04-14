package com.example;

import com.example.service.DBException;
import com.example.service.UserService;

import java.sql.SQLException;

public class Main {


    public void main(String[] args) throws DBException, SQLException {
        String login = "123";
        String password = "123";
        String email = "123";
        boolean success = UserService.getInstance().register(login, password, email);
        System.out.println(success);
    }
}
