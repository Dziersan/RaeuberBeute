package sample.ADRESSBUCH;

import java.util.ArrayList;
import java.util.List;

public class DBcreate {
    private static List<Angestellter> angestellterList = new ArrayList<Angestellter>();
    private static List<Kunde> kundeList = new ArrayList<Kunde>();

    public static void hinzufuegen(Kunde kunde){
        kundeList.add(kunde);
    }

    public static void hinzufuegen(Angestellter angestellter){
        angestellterList.add(angestellter);
    }



}
