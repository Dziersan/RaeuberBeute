package sample.ADRESSBUCH;

import java.util.ArrayList;
import java.util.List;

class Adressbuch {

    private List<Person> Personen = new ArrayList<Person>();

    public List<Person> getPersonen() {
        return Personen;
    }

    public void hinzufuegen(Person person){
        this.Personen.add(person);
    }

    public void loeschen(Person person){
        this.Personen.remove(person);
    }

    public List<Person> suche(Person gesuchtePerson){
        List<Person> gefundenePersonen = new ArrayList<>();

        for (Person p: this.Personen) {
            if(p.getName().equals(gesuchtePerson.getName())){
                gefundenePersonen.add(p);
            } else if(p.getVorname().equals(gesuchtePerson.getVorname())){
                gefundenePersonen.add(p);
            }else if (p.getTelefonnummer().equals(gesuchtePerson.getTelefonnummer())){
                gefundenePersonen.add(p);
            }
        }

        return gefundenePersonen;
    }
}
