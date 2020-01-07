package sample.ADRESSBUCH;

public class Person {

    private String Vorname;
    private String Name;
    private String Telefonnummer;

    public Person(){
    }

    public Person(String vorname, String name, String telefonnummer){
        this.Vorname = vorname;
        this.Name = name;
        this.Telefonnummer = telefonnummer;
    }

    public String getName() {
        return Name;
    }

    public String getTelefonnummer() {
        return Telefonnummer;
    }

    public String getVorname() {
        return Vorname;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setTelefonnummer(String telefonnummer) {
        Telefonnummer = telefonnummer;
    }

    public void setVorname(String vorname) {
        Vorname = vorname;
    }
}

