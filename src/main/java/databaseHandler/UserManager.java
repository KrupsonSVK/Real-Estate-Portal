package databaseHandler;

import model.Inzerat;
import model.Statistic;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class UserManager extends AllTablesManager {

    protected User processRow(ResultSet rs) throws SQLException {
        return (new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("name"), rs.getString("surname"), rs.getString("email"), rs.getString("phone")));
    }

    @Override
    protected Statistic processRowCount(ResultSet rs) throws SQLException {
        return (new Statistic(rs.getInt("total_ads"), rs.getInt("total_users"), rs.getString("favorite_ad"), rs.getInt("ad_liked"), rs.getString("best_user"), rs.getInt("ads_created")));
    }

    protected Inzerat processRowProfile(ResultSet rs) throws SQLException {
        return (new Inzerat(rs.getString("nazov"), rs.getInt("id"), rs.getInt("cena"), rs.getString("kontakt")));
    }

    public List <String> searchFavoriteUser(Integer inzerat_id) throws SQLException {
        List <String> result = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        Properties connectionProps = new Properties();
        String aha = null;

        connectionProps.put("user", "postgres");
        connectionProps.put("password", "laguna");
        String connectionString = "jdbc:postgresql://localhost:5433/users";
        conn = DriverManager.getConnection(connectionString, connectionProps);
        stmt = conn.createStatement();

        ResultSet rs = stmt.executeQuery("SELECT uzivatelia.username FROM uzivatelia JOIN oblubenost ON uzivatelia.id=oblubenost.uzivatelia_id WHERE oblubenost.inzeraty_id=" + inzerat_id);
        while (rs.next()) {
            aha = (String) rs.getString("username").toString();
            result.add(aha);
        }

        stmt.close();
        return result;

    }


    public List verificateUser(String usr, String pwd) throws SQLException {
        return (selectQuery("SELECT * FROM uzivatelia WHERE username LIKE '" + usr + "' AND password LIKE '" + pwd + "'"));
    }

    public void  updateProfile(String username, String usr, String pwd){
        Connection conn = null;
        PreparedStatement stmt = null;

        Properties connectionProps = new Properties();
        connectionProps.put("user", "postgres");
        connectionProps.put("password", "laguna");
        String connectionString = "jdbc:postgresql://localhost:5433/users";

        try {
            conn = DriverManager.getConnection(connectionString, connectionProps);
            conn.setAutoCommit(false);
            String createStatementString = "UPDATE uzivatelia SET username=? , password=? WHERE uzivatelia.username='" + username + "'";
            stmt = conn.prepareStatement(createStatementString);
            stmt.setString(1, usr);
            stmt.setString(2, pwd);
            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    System.err.println(e.getMessage());
                    System.err.print("Transaction is being rolled back");
                    conn.rollback();
                } catch (SQLException excep) {

                }
            }
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    public void updateFavorites(Integer inzerat, Integer uzivatel) {
        Connection conn = null;
        PreparedStatement stmt = null;

        Properties connectionProps = new Properties();
        connectionProps.put("user", "postgres");
        connectionProps.put("password", "laguna");
        String connectionString = "jdbc:postgresql://localhost:5433/users";

        try {
            conn = DriverManager.getConnection(connectionString, connectionProps);
            conn.setAutoCommit(false);
            String createStatementString = "INSERT INTO oblubenost(inzeraty_id,uzivatelia_id) VALUES(?,?)";
            stmt = conn.prepareStatement(createStatementString);
            stmt.setInt(1, inzerat);
            stmt.setInt(2, uzivatel);
            //tu dame breakpoint a skusime nieco spravit s tabulkou
            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    System.err.println(e.getMessage());
                    System.err.print("Transaction will be rolled back");
                    conn.rollback();
                } catch (SQLException excep) {

                }
            }
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

