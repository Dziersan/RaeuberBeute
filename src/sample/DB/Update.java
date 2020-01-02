package sample.DB;

import java.util.ArrayList;
import java.util.Arrays;

public class Update {
    String sqlStatement = "INSERT INTO ";

    public Update(String name)
    {
        this.sqlStatement += name +" VALUES (";
    }

    public String insertData(Object...attribut)
    {
        ArrayList<Object> attribute = new ArrayList<Object>();

        attribute.addAll(Arrays.asList(attribut));

        for(int i = 0; i < attribute.size()-1; i++)
        {
            sqlStatement += attribute.get(i) + ",";
        }

        sqlStatement += attribute.get(attribute.size()-1)+");";

        return sqlStatement;
    }
}



