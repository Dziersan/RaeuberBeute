package sample.DB;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

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
                    System.out.println("(W) - Select");
                    System.out.println("(N) - Neues Schema angeben");
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
            System.out.println(" (1) - DECIMAL \n (2) - VARCHAR \n (3) - DATE \n (4) - BOOLEAN \n (5) - INTEGER");
            String type = reader.readLine();
            table.attributes.add(new PrimaryKey(columnPK, getType(type)));

            while (doneBool == false) {
                System.out.println("Geben Sie den Namen der Spalte an");
                String column = reader.readLine();
                System.out.println(" (1) - DECIMAL \n (2) - VARCHAR \n (3) - DATE \n (4) - BOOLEAN \n (5) - INTEGER");
                String cmd = reader.readLine();
                executeCommand(cmd.toUpperCase());
                table.attributes.add(new Attribute(column,getType(cmd)));

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

    private static void selectStatement()
    {
        boolean doneBool = false;
        Select select = new Select();
        try{
            System.out.println("(T) - Spaltennamen angeben");
            System.out.println("(C) - Count nach einer Spalte");
            System.out.println("(S) - SUM nach einer Spalte");
            System.out.println("(A) - Avarage nach einer Spalte");
            String selectStatement = reader.readLine();

            select = selectStatement(selectStatement.toUpperCase(), select);

            if(selectStatement.toUpperCase().equals("C")
            ||selectStatement.toUpperCase().equals("S")
            || selectStatement.toUpperCase().equals("A"))
            {
                doneBool = true;
            }
            System.out.println("Spaltenname angeben");
            while(doneBool == false)
            {

                String tableName = reader.readLine();
                select.selectTable(tableName);
                System.out.println("Fertig? J/N");
                String fertig = reader.readLine();
                if (fertig.equals("J") || fertig.equals("j")) {
                    select.setSqlStatement(select.getSqlStatement().substring(0,select.getSqlStatement().length()-2));
                    doneBool = true;
                }
                else
                    System.out.println("Spaltenname angeben");
            }

            doneBool = false;

            System.out.println("Tabellennamen angeben");
            while(doneBool == false)
            {
                String tableName = reader.readLine();
                select.fromTable(tableName);
                System.out.println("Fertig? J/N");
                String fertig = reader.readLine();

                if (fertig.equals("J") || fertig.equals("j")) {
                    select.setSqlStatement(select.getSqlStatement().substring(0,select.getSqlStatement().length()-2));
                    doneBool = true;
                }
                else
                    System.out.println("Tabellennamen angeben");
            }

            System.out.println("(J) - Join");
            System.out.println("(E) - Equal");
            System.out.println("(L) - Lesser");
            System.out.println("(G) - Greater");
            System.out.println("(I) - Like");
            System.out.println("(N) - Notlike");
            System.out.println("(F) - Fertig");
            doneBool = false;
            String cmd = reader.readLine();
            while(doneBool == false)
            {
                select = whereStatement(cmd.toUpperCase(), select);
                System.out.println("Fertig? J/N");
                String fertig = reader.readLine();

                if (fertig.equals("J") || fertig.equals("j")) {
                    doneBool = true;
                }
                else
                    cmd = reader.readLine();
            }
            System.out.println(select.getSqlStatement());
            //JDBC.updateStatement(connection, select.getSqlStatement());
        }catch (Exception e)
        {
            System.out.println("Beim Lesen ist es zu folgenden Fehler gekommen: " + e.getMessage());
        }
    }

    private static void executeCommand(String cmd) throws SQLException, IOException {
        switch (cmd) {
            case "V": //View erstellen
                createMainView();
                break;
            case "T": //Neue Tabelle erstellen
                createTable();
                break;
            case "E": // Die Anwendung beenden
                exitApplication();
                break;
            case "D": //Table löschen
                deleteTable();
                break;
            case "W":
                selectStatement();
                break;
            case "N":
                System.out.println("Schema Name angeben");
                schemaName = reader.readLine();
                Schema schema = new Schema(schemaName);
                updateStatement(connection, schema.use());
                break;
            default: // Falsche/unbekannte Eingabe
                System.out.println("Unbekanntes Kürzel");
        }
    }

    private static Select whereStatement(String cmd, Select select) throws SQLException, IOException {
        String table1, table2, column1, column2, pattern;
        double value;
        switch (cmd) {
            case "J": //Join erstellen
                System.out.println("Erste Tabelle");
                table1 = reader.readLine();
                System.out.println("Zweite Tabelle");
                table2 = reader.readLine();
                System.out.println("Erste Spalte");
                column1 = reader.readLine();
                System.out.println("Zweite Spalte");
                column2 = reader.readLine();
                select.whereTableJoin(table1, table2, column1, column2);
                return select;

            case "E": //Equal
                System.out.println("Welche Spalte");
                table1 = reader.readLine();
                System.out.println("Value?");
                value = Double.parseDouble(reader.readLine());
                select.whereTableEqual(table1, value);
                return select;

            case "L": // Lesser
                System.out.println("Welche Spalte");
                table1 = reader.readLine();
                System.out.println("Value?");
                value = Double.parseDouble(reader.readLine());
                select.whereTableLesser(table1, value);
                return select;

            case "G": //Greater
                System.out.println("Welche Spalte");
                table1 = reader.readLine();
                System.out.println("Value?");
                value = Double.parseDouble(reader.readLine());
                select.whereTableGreater(table1, value);
                return select;

            case "I": // Like
                System.out.println("Welche Spalte");
                table1 = reader.readLine();
                System.out.println("Welchen Wert?");
                pattern = reader.readLine();
                select.whereTableLike(table1, pattern);
                return select;

            case "N": // Notlike
                System.out.println("Welche Spalte");
                table1 = reader.readLine();
                System.out.println("Welchen Wert?");
                pattern = reader.readLine();
                select.whereTableNotEqual(table1, pattern);
                return select;

            case "F": //Fertig
                return select;

            default: // Falsche/unbekannte Eingabe

                System.out.println("Unbekanntes Kürzel");
                return select;
        }
    }
    private static Select selectStatement(String cmd, Select select) throws IOException {
        switch (cmd) {
            case "T":
            return select;
            case "C":
                System.out.println("Welche Spalte?");
                String command = reader.readLine();
                    select.count(command);
                    return select;
            case "S":
                System.out.println("Welche Spalte?");
                String command1 = reader.readLine();
                select.sum(command1);
                return select;
            case "A":
                System.out.println("Welche Spalte?");
                String command2 = reader.readLine();
                select.avg(command2);
                return select;
            default:
                return select;
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
