package sample.DB;

import java.util.ArrayList;
import java.util.Arrays;

public class Select {
    String sqlStatement = "SELECT ";
    private int whereCounter = 0;

    public void selectTable(String... columnName)
    {
        ArrayList<String> columns = new ArrayList<String>();
        columns.addAll(Arrays.asList(columnName));

        for(int i = 0; i < columns.size()-1;i++)
        {
            sqlStatement += columns.get(i) + ", ";
        }
        sqlStatement += columns.get(columns.size()-1);
    }

    public void selectTableDistinct(String... columnName)
    {
        ArrayList<String> columns = new ArrayList<String>();
        columns.addAll(Arrays.asList(columnName));
        sqlStatement += " distinct ";
        for(int i = 0; i < columns.size()-1;i++)
        {
            sqlStatement += columns.get(i) + ",";
        }
        sqlStatement += columns.get(columns.size()-1);
    }

    public void fromTable(String...tableName)
    {
        ArrayList<String> tableNames = new ArrayList<String>();
        tableNames.addAll(Arrays.asList(tableName));

        sqlStatement += " from ";
        for(int i = 0; i < tableNames.size()-1;i++)
        {
            sqlStatement += tableNames.get(i)+", ";
        }
        sqlStatement += tableNames.get(tableNames.size()-1);
    }

public void whereTableJoin(String table1, String table2, String column1, String column2) {
        if(whereCounter == 0)
            sqlStatement += " where ";
        else
            sqlStatement += " AND ";

    whereCounter++;
    sqlStatement += table1 + "." + column1 + " = " + table2+"." + column2;
}
    public void whereTableBetween(String column, double value1, double value2) {
        if(whereCounter == 0)
            sqlStatement += " where ";
        else
            sqlStatement += " AND ";

        whereCounter++;
        sqlStatement += column + " BETWEEN " + value1 + " AND " + value2;
    }

    public void whereTableEqual(String column, double value1) {
        if(whereCounter == 0)
            sqlStatement += " where ";
        else
            sqlStatement += " AND ";

        whereCounter++;
        sqlStatement += column + " = " + value1;
    }
    public void whereTableGreater(String column, double value1) {
        if(whereCounter == 0)
            sqlStatement += " where ";
        else
            sqlStatement += " AND ";

        whereCounter++;
        sqlStatement += column + " > " + value1;
    }

    public void whereTableLesser(String column, double value1) {
        if(whereCounter == 0)
            sqlStatement += " where ";
        else
            sqlStatement += " AND ";

        whereCounter++;
        sqlStatement += column + " < " + value1;
    }

    public void whereTableLike(String column, String pattern) {
        if(whereCounter == 0)
            sqlStatement += " where ";
        else
            sqlStatement += " AND ";

        whereCounter++;
        sqlStatement += column + " like " + pattern;
    }

    public void whereTableIn(String column, String pattern) {
        if(whereCounter == 0)
            sqlStatement += " where ";
        else
            sqlStatement += " AND ";

        whereCounter++;
        sqlStatement += column + " in " + pattern;
    }

    public void createView(String viewName)
    {
        sqlStatement = "CREATE VIEW IF NOT EXISTS " + viewName + " AS " + "SELECT ";
    }
    public String getSqlStatement() {
        return sqlStatement + ";";
    }
}
