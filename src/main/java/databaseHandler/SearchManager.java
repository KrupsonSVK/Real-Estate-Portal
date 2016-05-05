package databaseHandler;

import model.Inzerat;
import model.Statistic;

import java.sql.*;
import java.util.*;

public class SearchManager extends AllTablesManager {

    protected Inzerat processRowProfile(ResultSet rs) throws SQLException {
        return (new Inzerat(rs.getString("nazov"), rs.getInt("id"), rs.getInt("cena"), rs.getString("mesto")));
    }

    protected Statistic processRowCount(ResultSet rs) throws SQLException {
        return (new Statistic(rs.getInt("total_ads"),rs.getInt("total_users"),rs.getString("favorite_ad"),rs.getInt("ad_liked"),rs.getString("biggest_seller"), rs.getInt("ads_created")));
    }

    protected Inzerat processRow(ResultSet rs) throws SQLException {
        return (new Inzerat(rs.getString("nazov"), rs.getInt("id"), rs.getString("info"), rs.getInt("cena"),
                rs.getString("mesto"), rs.getString("email") +", "+ rs.getString("phone"), rs.getString("keywords"),rs.getString("autor")));
    }

    public List<Inzerat> searchInzeratSimple(String phrase) throws SQLException {
        return (selectQuery("SELECT *, mesta.nazov AS mesto , uzivatelia.id AS autor FROM inzeraty " +
                "JOIN mesta ON inzeraty.mesto_id=mesta.id " +
                "JOIN uzivatelia ON inzeraty.autor_id=uzivatelia.id " +
                "WHERE inzeraty.nazov LIKE '%" + phrase + "%' OR typ LIKE '%" + phrase + "%' OR info LIKE '%" + phrase + "%' " +
                "OR keywords LIKE '%" + phrase + "%' OR phone LIKE '%" + phrase + "%' OR email LIKE '%" + phrase + "%'"));
    }

    public List<Inzerat> searchInzeratAdvanced(String phrase,String type, String city, Integer min, Integer max, Integer distance) throws SQLException {
        return (selectQuery("SELECT inzeraty.nazov, inzeraty.id, inzeraty.cena, inzeraty.info, uzivatelia.email, uzivatelia.phone, inzeraty.keywords, mesta.nazov " +
                "AS mesto, cesty.vzdialenost AS vzdialenost, uzivatelia.username AS autor FROM inzeraty " +
                "JOIN mesta ON inzeraty.mesto_id = mesta.id " +
                "JOIN uzivatelia ON uzivatelia.id=inzeraty.autor_id " +
                "JOIN cesty ON mesta.id = cesty.mesta_from AND cesty.mesta_to = (SELECT mesta.id FROM mesta " +
                "WHERE mesta.nazov = '" + city + "') " +
                "WHERE typ = '" + type + "' AND(cena BETWEEN " + min + " AND " + max + " ) AND cesty.vzdialenost <=" + distance+ " AND (inzeraty.nazov LIKE '%" + phrase + "%' OR typ LIKE '%" + phrase + "%' OR info LIKE '%" + phrase + "%' " +
        "OR keywords LIKE '%" + phrase + "%' OR uzivatelia.phone LIKE '%" + phrase + "%' OR uzivatelia.email LIKE '%" + phrase + "%')"));
    }

    public List<Inzerat> searchInzeratID(Integer id) throws SQLException {
        return (selectQuery("SELECT *,mesta.nazov AS mesto, uzivatelia.username AS autor FROM inzeraty " +
                "JOIN mesta ON inzeraty.mesto_id=mesta.id " +
                "JOIN uzivatelia ON inzeraty.autor_id=uzivatelia.id " +
                "WHERE inzeraty.id=" + id));
    }


    public List<Inzerat> getAllStudents() throws SQLException {
        return (selectQuery("SELECT * FROM inzeraty"));
    }

    public List<Inzerat> searchFavoriteAds(Integer user_id) throws SQLException {
        return (selectQuery("SELECT inzeraty.id, inzeraty.nazov, inzeraty.cena, mesta.nazov AS mesto, uzivatelia.email, uzivatelia.phone FROM inzeraty" +
                " JOIN oblubenost ON inzeraty.id = oblubenost.inzeraty_id JOIN uzivatelia ON oblubenost.uzivatelia_id  = uzivatelia.id " +
                " JOIN  mesta ON inzeraty.mesto_id = mesta.id " +
                " WHERE oblubenost.uzivatelia_id =", user_id));
    }

    public List<Inzerat> searchMyAds(Integer user_id) throws SQLException {
        return (selectQuery("SELECT inzeraty.id, inzeraty.nazov, inzeraty.cena , uzivatelia.email, uzivatelia.phone, mesta.nazov AS mesto" +
                " FROM inzeraty " +
                "JOIN autorstvo ON inzeraty.id=autorstvo.inzeraty_id " +
                "JOIN uzivatelia ON autorstvo.uzivatelia_id  = uzivatelia.id   " +
                "JOIN mesta ON inzeraty.mesto_id = mesta.id " +
                "WHERE autorstvo.uzivatelia_id =", user_id));
    }

    public List<Statistic> getStatistic() throws SQLException {
        return (selectQuery("SELECT COUNT(*) AS total_ads,\n" +
                "       (SELECT COUNT(*) FROM uzivatelia ) as total_users,\n" +
                "       (SELECT  username  FROM uzivatelia JOIN oblubenost ON oblubenost.uzivatelia_id =uzivatelia.id GROUP BY username  ORDER BY COUNT(username) DESC LIMIT 1) as favorite_user,\n" +
                "       (SELECT nazov FROM inzeraty JOIN oblubenost ON oblubenost.inzeraty_id=inzeraty.id GROUP BY nazov ORDER BY COUNT(nazov) DESC LIMIT 1) AS favorite_ad,\n" +
                "       (SELECT count(inzeraty.id) FROM inzeraty JOIN oblubenost ON inzeraty.id = oblubenost.inzeraty_id WHERE inzeraty.nazov =\n" +
                "          (SELECT nazov FROM inzeraty JOIN oblubenost ON oblubenost.inzeraty_id=inzeraty.id GROUP BY nazov ORDER BY COUNT(nazov) DESC LIMIT 1)) AS ad_liked,\n" +
                "       (SELECT  username  FROM uzivatelia JOIN autorstvo ON autorstvo.uzivatelia_id =uzivatelia.id GROUP BY username  ORDER BY COUNT(username) DESC LIMIT 1) as biggest_seller,\n" +
                "       (SELECT count(inzeraty.id) FROM inzeraty JOIN autorstvo ON inzeraty.id = autorstvo.inzeraty_id JOIN uzivatelia ON autorstvo.uzivatelia_id  = uzivatelia.id WHERE uzivatelia.username =\n" +
                "          (SELECT username FROM uzivatelia JOIN autorstvo ON autorstvo.uzivatelia_id =uzivatelia.id GROUP BY username ORDER BY COUNT(username) DESC LIMIT 1)) AS ads_created\n" +
                "FROM inzeraty",true));
    }


    public void updateAds(String nazov, String typ, String info,Integer mesto_id,Integer cena, Integer uzivatel_id, String keywords){
        Connection conn = null;
        PreparedStatement stmt = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", "postgres");
        connectionProps.put("password", "laguna");
        String connectionString = "jdbc:postgresql://localhost:5433/users";

        try {
            conn = DriverManager.getConnection(connectionString, connectionProps);
            conn.setAutoCommit(false);
            String createStatementString = "INSERT INTO inzerat(nazov,typ,info,mesto_id,cena,uzivatel_id,keywords) VALUES(?,?,?,?,?,?)";
            stmt = conn.prepareStatement(createStatementString);
            stmt.setString(1, nazov);
            stmt.setString(2, typ);
            stmt.setString(3, info);
            stmt.setInt(4, mesto_id);
            stmt.setInt(5, cena);
            stmt.setInt(6, uzivatel_id);
            stmt.setString(7,keywords);
            stmt.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    System.err.println(e.getMessage());
                    System.err.print("Transaction wll be rolled back");
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
