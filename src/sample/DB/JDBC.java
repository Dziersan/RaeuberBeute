package sample.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBC {

    public static Connection getConnection() throws SQLException
    {
        Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306", "root", "mariadb");
        System.out.println("Connection established");
        return connection;
    }

    public static int updateStatement(Connection connection, String sqlStatement) throws SQLException
    {
        Statement statement = connection.createStatement();
        int tuplesUpdate = statement.executeUpdate(sqlStatement);
        System.out.println("SQL update executed : " + sqlStatement);


        return tuplesUpdate;
    }

    public static void main(String[] args) throws SQLException
    {
        try
        {
            Connection connection = getConnection();
            createSchema(connection, "Raeuber_und_Beute");
            createTables(connection);
            updateTable(connection);
            selectTable(connection);
            connection.close();
        }
        catch(SQLException e)
        {
            System.out.println("Fehlermeldung: " + e.getMessage());
            System.out.println("SQL State : " + e.getSQLState());
            e.printStackTrace();
        }
    }

    public static void createSchema(Connection connection, String schemaName) throws SQLException
    {
        Schema schema = new Schema(schemaName);
        updateStatement(connection, schema.drop());
        updateStatement(connection, schema.create());
        updateStatement(connection, schema.use());
    }

    public static void createTables(Connection connection) throws SQLException
    {
        Table biomasse = new Table("Biomasse");

        biomasse.attributes.add(new PrimaryKey("Timestep" , Type.INTEGER));
//        biomasse.attributes.add(new Attribute("SUP_Name" , Type.VARCHAR));
//        biomasse.attributes.add(new Attribute("Street" , Type.VARCHAR));
//        biomasse.attributes.add(new Attribute("City" , Type.VARCHAR));
        biomasse.attributes.add(new Attribute("Biomasse_Hase" , Type.INTEGER));
        biomasse.attributes.add(new Attribute("Biomasse_Fuchs" , Type.INTEGER));
        updateStatement(connection, biomasse.create());
    }

    public static void updateTable(Connection connection) throws SQLException {
        Update biomasseUpdate = new Update("Biomasse");
        updateStatement(connection, biomasseUpdate.insertData(123,22,1));
    }

    public static void selectTable(Connection connection)
    {
        Select select = new Select();
        select.selectTable("Timestep", "Biomasse_Hase");
        select.whereTable("Biomasse");
        System.out.println(select.getSqlStatement());

    }
}
