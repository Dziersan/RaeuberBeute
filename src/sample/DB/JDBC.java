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
            createSchema(connection, "hase_fuchs18_01_07_01_20");

//            createTables(connection);
//            updateTable(connection);
//            selectTable(connection);
            selectTable(connection);
            selectTableDistinct(connection);
            alterTable(connection);
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
        updateStatement(connection, schema.use());
    }

    public static void createTables(Connection connection) throws SQLException
    {
        Table biomasse = new Table("Biomasse");

        biomasse.attributes.add(new PrimaryKey("Timestep" , Type.DECIMAL));
//        biomasse.attributes.add(new Attribute("SUP_Name" , Type.VARCHAR));
//        biomasse.attributes.add(new Attribute("Street" , Type.VARCHAR));
//        biomasse.attributes.add(new Attribute("City" , Type.VARCHAR));
        biomasse.attributes.add(new Attribute("Biomasse_Hase" , Type.DECIMAL));
        biomasse.attributes.add(new Attribute("Biomasse_Fuchs" , Type.DECIMAL));
        updateStatement(connection, biomasse.create());
    }

    public static void updateTable(Connection connection) throws SQLException {
        Update biomasseUpdate = new Update("Biomasse");
        updateStatement(connection, biomasseUpdate.insertData(123,22,1));
    }

    public static void selectTableDistinct(Connection connection)
    {
        Select select = new Select();
        select.selectTableDistinct("Timestep");
        select.fromTable("Biomasse");

        System.out.println(select.getSqlStatement());

    }
    public static void selectTable(Connection connection) throws SQLException {
        Select select = new Select();
        select.selectTable("Timestep", "Biomasse_Hase");
        select.fromTable("Biomasse");
        updateStatement(connection, select.getSqlStatement());
//        System.out.println(select.getSqlStatement());
    }
    public static void alterTable(Connection connection)
    {
        AlterTable alterTable = new AlterTable();

        alterTable.selectTable("hase_fuchs18_01_07_01_20");
        alterTable.addColumn("Weide", Type.DECIMAL);
        alterTable.dropColumn("Biomasse_Hase");
        alterTable.addConstraint("Biomasse", Constraint.ONDELETESETNULL);
        alterTable.addForeignKey("Biomasse", "Weide", "Timestep","Steps");
        System.out.println(alterTable.getSqlStatement());
    }
}
