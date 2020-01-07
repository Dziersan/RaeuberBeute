package sample.DB;

import java.util.ArrayList;

public class AlterTable {
    String sqlStatement = "ALTER TABLE ";

    public void selectTable(String table)
    {
        sqlStatement += table;
    }

    public void addColumn(String columnName, Type type)
    {
        sqlStatement += "\nADD " + columnName +" " + type.getSqlTypeString();
    }

    public void dropColumn(String columnName)
    {
        sqlStatement += "\nDROP COLUMN IF EXISTS " + columnName +" ";
    }
    public void alterColumn(String columnName,  Type type)
    {
        sqlStatement += "\nALTER COLUMN " + columnName + " " + type.getSqlTypeString();
    }

    public void addConstraint(String table, Constraint constraint)
    {
        sqlStatement += "\nADD CONSTRAINT " + table +" " + constraint.getSqlTypeString();
    }

    public void addForeignKey(String table1, String table2, String column1, String column2)
    {
        sqlStatement += " " + table1 + " foreign key" + "(" + column1 + ")" + "references " + table2 + "(" + column2 + ")";
    }
    public String getSqlStatement() {
        return sqlStatement + ";";
    }
}
