package sample.ADRESSBUCH;

import sample.ADRESSBUCH.Person;

public class Kunde extends Person {
    private String name;
    private String vorname;
    private int telefonnummer;

    public Kunde(String name, String vorname, int telefonnummer, int iBAN) {
        this.name = name;
        this.vorname = vorname;
        this.telefonnummer = telefonnummer;
        this.iBAN = iBAN;
    }

    public int getiBAN() {
        return iBAN;
    }

    public void setiBAN(int iBAN) {
        this.iBAN = iBAN;
    }

    private int iBAN;

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

    public void setTelefonnummer(int telefonnummer) {
        this.telefonnummer = telefonnummer;
    }

}
