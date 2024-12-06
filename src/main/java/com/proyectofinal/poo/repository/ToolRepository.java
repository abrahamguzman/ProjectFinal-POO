package com.proyectofinal.poo.repository;

import com.proyectofinal.poo.db.DatabaseConnection;
import com.proyectofinal.poo.model.Tool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ToolRepository {

    public List<Tool> findAll() {
        List<Tool> tools = new ArrayList<>();
        String query = "{CALL findAllTool()}";

        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                tools.add(new Tool(
                        resultSet.getInt("id"),
                        resultSet.getString("tool_name"),
                        resultSet.getDouble("price"),
                        resultSet.getString("category_name")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tools;
    }

    public void create(String name, double price, int categoryId) {
        String query = "{CALL createTool(?, ?, ?)}";

        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall(query)) {

            statement.setString(1, name);
            statement.setDouble(2, price);
            statement.setInt(3, categoryId);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(int id, String name, double price, int categoryId) {
        String query = "{CALL updateTool(?, ?, ?, ?)}";

        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall(query)) {

            statement.setInt(1, id);
            statement.setString(2, name);
            statement.setDouble(3, price);
            statement.setInt(4, categoryId);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String query = "{CALL deleteTool(?)}";

        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall(query)) {

            statement.setInt(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Tool findById(int id) {
        String query = "{CALL findByIdTool(?)}";
        Tool tool = null;

        try (Connection connection = DatabaseConnection.getConnection();
             CallableStatement statement = connection.prepareCall(query)) {

            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    tool = new Tool(
                            resultSet.getInt("id"),
                            resultSet.getString("tool_name"),
                            resultSet.getDouble("price"),
                            resultSet.getString("category_name")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tool;
    }
}

