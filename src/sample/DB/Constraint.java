package sample.DB;

public enum Constraint {
    UPDATECASCADE("ON UPDATE CASCADE"),
    DELETERESTRICT("ON DELETE DISTRICT"),
    ONDELETESETNULL("ON DELETE SET NULL");

    private String sqlTypeString;

    Constraint(String sqlTypeString) {
        this.sqlTypeString = sqlTypeString;
    }

    public String getSqlTypeString()
    {
        return sqlTypeString;
    }
}
