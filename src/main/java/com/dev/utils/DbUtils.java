package com.dev.utils;


import com.dev.User;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class DbUtils {

    private Connection connection;

    @PostConstruct
    public void init () {
        createDbConnection("root", "1234");
    }

    private void createDbConnection (String username, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ash2024", username, password);
            System.out.println("Connection successfull!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkIfUsernameAvailable (String username) {
        boolean available = true;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT username FROM users WHERE username = ?");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                available = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return available;
    }

    public boolean checkCredentials (String username, String password) {
        boolean ok = false;
        if (!checkIfUsernameAvailable(username)) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE password = ? AND username = ?");
                preparedStatement.setString(1, password);
                preparedStatement.setString(2, username);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    ok = true;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return ok;
    }


}
