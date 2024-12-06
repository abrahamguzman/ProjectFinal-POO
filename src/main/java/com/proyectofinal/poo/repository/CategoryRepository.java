package com.proyectofinal.poo.repository;

import com.proyectofinal.poo.db.DatabaseConnection;
import com.proyectofinal.poo.model.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {

    public List<Category> findAll() {
        List<Category> categories = new ArrayList<>();
        String query = "{CALL findAllCategory()}";

        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");

                categories.add(new Category(id, name));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }
}

