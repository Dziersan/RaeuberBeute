package sample.DB;


import java.util.ArrayList;

public class Table {


    String name = null;
    ArrayList<Attribute> attributes = new ArrayList<>();

    public Table(String name)
    {
        this.name = name;
    }

    public Table addAttr(String name, Type type)
    {
        attributes.add(new Attribute(name, type));
        return this;
    }

    public Table addPrimaryKey(String name, Type type)
    {
        attributes.add(new PrimaryKey(name, type));
        return this;
    }

    public String create()
    {
        String sqlStatement = "create Table if not exists " + name + "(";
        String sqlStatementPrimaryKeys = "";

        for (int i = 0; i < attributes.size(); i++)
        {
            sqlStatement += attributes.get(i).create();

            if (i < attributes.size() -1)
            {
                sqlStatement += ", ";
            }
            else {
                sqlStatementPrimaryKeys = attributes.get(0).createPrimaryKey(sqlStatementPrimaryKeys);

                sqlStatement += ", PRIMARY KEY (" + sqlStatementPrimaryKeys + "));";
            }
        }
        return sqlStatement;
    }
}
