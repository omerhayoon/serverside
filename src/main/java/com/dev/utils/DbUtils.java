package com.dev.utils;

import com.dev.models.User;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

@Component
public class DbUtils {

    private Connection connection;

    @PostConstruct
    public void init() {
        createDbConnection("root", "1234"); // Update username and password as needed
    }

    private void createDbConnection(String username, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/ash2024", username, password);
            System.out.println("Connection successful!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkIfUsernameAvailable(String username) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT username FROM users WHERE username = ?"
            );
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            return !resultSet.next(); // If no result, username is available
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkCredentials(String username, String password) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM users WHERE username = ? AND password = ?"
            );
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next(); // If result exists, credentials are valid
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addUser(String username, String hashedPassword, String email, boolean isAdmin) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO users (username, password, email, is_admin) VALUES (?, ?, ?, ?)"
            );
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, hashedPassword);
            preparedStatement.setString(3, email);
            preparedStatement.setBoolean(4, isAdmin);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User getUserByUsername(String username) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM users WHERE username = ?"
            );
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new User(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("email"),
                        resultSet.getBoolean("is_admin")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String hashPasswordMD5(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));

            // Convert the hash bytes to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error: MD5 algorithm not found.", e);
        }
    }
}
