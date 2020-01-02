package sample.DB;

import java.util.ArrayList;
import java.util.Arrays;

public class Select {
    String sqlStatement = "select ";

    public void selectTable(String... columnName)
    {
        ArrayList<String> columns = new ArrayList<String>();
        columns.addAll(Arrays.asList(columnName));

        for(int i = 0; i < columns.size()-1;i++)
        {
            sqlStatement += columns.get(i) + ",";
        }
        sqlStatement += columns.get(columns.size()-1);
    }

    public void whereTable(String...tableName)
    {
        ArrayList<String> tableNames = new ArrayList<String>();
        tableNames.addAll(Arrays.asList(tableName));

        sqlStatement += " from ";
        for(int i = 0; i < tableNames.size()-1;i++)
        {
            sqlStatement += tableNames.get(i) + ",";
        }
        sqlStatement += tableNames.get(tableNames.size()-1) + ";";
    }

    public String getSqlStatement() {
        return sqlStatement;
    }
}
