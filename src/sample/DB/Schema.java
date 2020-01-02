package sample.DB;

public class Schema
{
    String name = null;
    public Schema(String name)
    {
        this.name = name;
    }
    public String create()
    {
        return "CREATE SCHEMA " + name + ";";
    }

    public String use()
    {
        return "USE " + name + ";";
    }

    public String drop()
    {
        return "DROP SCHEMA IF EXISTS " + name + ";";
    }


}
