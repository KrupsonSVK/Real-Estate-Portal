package databaseHandler;

import model.Statistic;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public abstract class AllTablesManager {

    protected List selectQuery(String queryString) throws SQLException {
        List result = new LinkedList();
        Connection conn = null;
        Statement stmt = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", "postgres");
        connectionProps.put("password", "laguna");
        String connectionString = "jdbc:postgresql://localhost:5433/users";
        try {
            conn = DriverManager.getConnection(connectionString, connectionProps);
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(queryString);
            while (rs.next()) {
                result.add(processRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            stmt.close();
            return result;
        }
    }

    protected List selectQuery(String queryString, boolean statistics) throws SQLException {
        List result = new LinkedList();
        Connection conn = null;
        Statement stmt = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", "postgres");
        connectionProps.put("password", "laguna");
        String connectionString = "jdbc:postgresql://localhost:5433/users";
        try {
            conn = DriverManager.getConnection(connectionString, connectionProps);
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(queryString);
            while (rs.next()) {
                result.add(processRowCount(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            stmt.close();
            return result;
        }
    }

    protected List selectQuery(String queryString, Integer id) throws SQLException {
        queryString+=id;
        List result = new LinkedList();
        Connection conn = null;
        Statement stmt = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", "postgres");
        connectionProps.put("password", "laguna");
        String connectionString = "jdbc:postgresql://localhost:5433/users";
        try {
            conn = DriverManager.getConnection(connectionString, connectionProps);
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(queryString);
            while (rs.next()) {
                result.add(processRowProfile(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            stmt.close();
            return result;
        }
    }

    protected Object selectOneQuery(String queryString) throws SQLException {
        Object result = null;
        Connection conn = null;
        Statement stmt = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", "postgres");
        connectionProps.put("password", "laguna");
        String connectionString = "jdbc:postgresql://localhost:5433/users";
        try {
            conn = DriverManager.getConnection(connectionString, connectionProps);
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(queryString);
            result = processRow(rs);
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            stmt.close();
            return result;
        }
    }
    protected Object selectOneQuery(String queryString,boolean statistic) throws SQLException {
        Object result = null;
        Connection conn = null;
        Statement stmt = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", "postgres");
        connectionProps.put("password", "laguna");
        String connectionString = "jdbc:postgresql://localhost:5433/users";
        try {
            conn = DriverManager.getConnection(connectionString, connectionProps);
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(queryString);
            result = processRowCount(rs);
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            stmt.close();
            return result;
        }
    }
    protected abstract Object processRow(ResultSet rs) throws SQLException;
    protected abstract Object processRowCount(ResultSet rs) throws SQLException;
    protected abstract Object processRowProfile(ResultSet rs) throws SQLException;

}

