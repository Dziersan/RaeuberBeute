package sample.DB;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;

import static sample.DB.JDBC.*;

public class Leistung_DB {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    static String schemaName;
    public static Connection connection;

    static {
        try {
            connection = JDBC.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException {
        boolean nameBool = false;

        while (true) {
            while (nameBool == false) {
                System.out.println("Geben Sie den Namen des Schemas an");
                try {
                    schemaName = reader.readLine();
                    nameBool = true;
                } catch (Exception e) {
                    System.out.println("Beim Lesen ist es zu folgenden Fehler gekommen: " + e.getMessage());
                }

                Schema schema = new Schema(schemaName);
                updateStatement(connection, schema.use());

                while (nameBool == true) {
                    System.out.println("Geben Sie den Buchstaben für einen der Befehle ein:");
                    System.out.println("(V) - Mainview erstellen ");
                    System.out.println("(T) - Table erstellen");
                    System.out.println("(D) - Table löschen");
                    System.out.println("(W) - Table löschen");
                    System.out.println("(E) - Exit");
                    try {
                        String cmd = reader.readLine();
                        executeCommand(cmd.toUpperCase());
                    } catch (Exception e) {
                        System.out.println("Beim Lesen ist es zu folgenden Fehler gekommen: " + e.getMessage());
                    }
                }
            }
        }

    }

    private static void exitApplication() {
        System.exit(0);
    }

    private static void createMainView() throws SQLException {


        Select createView = new Select();
        createView.createView("Fuchs_Hase_Weide");
        createView.selectTable("Timestep_Fuchs as Timestep", "Biomasse_Fuchs", "Biomasse_Hase", "Weideflaeche");
        createView.fromTable("Biomasse_Fuchs", "Biomasse_Hase", "Weide");
        createView.whereTableJoin("Biomasse_Fuchs", "Biomasse_Hase", "Timestep_Fuchs", "Timestep_Hase");
        createView.whereTableJoin("Biomasse_Fuchs", "Weide", "Timestep_Fuchs", "Timestep_Weide");
        updateStatement(connection, createView.getSqlStatement());
    }

    private static void createTable() throws SQLException {
        boolean doneBool = false;

        try {
            System.out.println("Geben Sie den Namen ein:");
            String name = reader.readLine();
            Table table = new Table(name);

            System.out.println("Geben Sie den Namen der Spalte mit dem Primärschlüssel an");
            String columnPK = reader.readLine();
            System.out.println(" 1 = DECIMAL \n 2 = VARCHAR \n 3 = DATE \n 4 = BOOLEAN \n 5 = INTEGER");
            String type = reader.readLine();
            table.attributes.add(new PrimaryKey(columnPK, getType(type)));

            while (doneBool == false) {
                System.out.println("Geben Sie den Namen der Spalte an");
                String column = reader.readLine();
                System.out.println(" 1 = DECIMAL \n 2 = VARCHAR \n 3 = DATE \n 4 = BOOLEAN \n 5 = INTEGER");
                type = reader.readLine();

                table.attributes.add(new Attribute(column,getType(type)));

                System.out.println("Fertig? J/N");
                String fertig = reader.readLine();

                if (fertig.equals("J") || fertig.equals("j")) {
                    doneBool = true;
                }
                System.out.println(updateStatement(connection, table.create()));
//                updateStatement(connection, table.create());
            }

        } catch (Exception e) {
            System.out.println("Beim Lesen ist es zu folgenden Fehler gekommen: " + e.getMessage());
        }
    }
public static void deleteTable()
{
    try{
        System.out.println("Geben Sie den Namen des Tables an");
        String tableName = reader.readLine();
        Table table = new Table(tableName);
        table.dropTable();
        updateStatement(connection, table.getSqlStatement());
    }catch (Exception e)
    {
        System.out.println("Beim Lesen ist es zu folgenden Fehler gekommen: " + e.getMessage());
    }

}
    private static void executeCommand(String cmd) throws SQLException {
        switch (cmd) {
            case "V": //View erstellen
                createMainView();
                break;
            case "T": //neuen Eintrag hinzufügen
                createTable();
                break;
            case "E": // Die Anwendung beenden
                exitApplication();
                break;
            case "D": //
                deleteTable();
                break;
            case "W":

                break;
            default: // Falsche/unbekannte Eingabe
                System.out.println("Unbekanntes Kürzel");
        }
    }


    private static Type getType(String cmd) {
        switch (cmd) {
            case "1":
                return Type.DECIMAL;
            case "2":
                return Type.VARCHAR;
            case "3":
                return Type.DATE;
            case "4":
                return Type.BOOLEAN;
            case "5":
                return Type.INTEGER;
            default:
                return Type.DECIMAL;
        }
    }
}
