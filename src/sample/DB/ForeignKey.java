package sample.DB;

public class ForeignKey extends Attribute{

    Table tableReferenced;
    Attribute attributeReferenced;
    boolean isPrimaryKey;

    public ForeignKey(String name, Type type, Table tableReferenced, Attribute attributeReferenced, boolean isPrimaryKey) {
        super(name, type);
        this.tableReferenced = tableReferenced;
        this.attributeReferenced = attributeReferenced;
        this.isPrimaryKey = isPrimaryKey;

    }

    public String create()
    {
        return super.create() + ", "
                + "FOREIGN KEY(" + name + ") REFERENCED "
                + tableReferenced.name
                + "(" + attributeReferenced.name + ")";
    }

    public String createPrimaryKey(String sqlStatementPrimaryKey)
    {
        if(isPrimaryKey)
            return sqlStatementPrimaryKey.isEmpty() ? name : sqlStatementPrimaryKey + ", name";
        else
            return sqlStatementPrimaryKey;
    }

}
