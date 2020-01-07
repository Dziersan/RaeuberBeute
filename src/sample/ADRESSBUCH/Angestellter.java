package sample.ADRESSBUCH;

import sample.ADRESSBUCH.Person;

public class Angestellter extends Person {
    private String name;
    private String vorname;
    private String geburtstag;
    private int personalNummer;
    private int managerNummer;

    public Angestellter(String name, String vorname, String geburtstag, int personalNummer, int managerNummer) {
        this.name = name;
        this.vorname = vorname;
        this.geburtstag = geburtstag;
        this.personalNummer = personalNummer;
        this.managerNummer = managerNummer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getGeburtstag() {
        return geburtstag;
    }

    public void setGeburtstag(String geburtstag) {
        this.geburtstag = geburtstag;
    }
}
