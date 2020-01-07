package sample.ADRESSBUCH;

import org.mariadb.jdbc.internal.com.read.resultset.SelectResultSet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Main {

    private static Adressbuch adressbuch = new Adressbuch();
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) {
        while(true){
            System.out.println("Geben Sie den Buchstaben für einen der Befehle ein:");
            System.out.println("(S) - Alle Einträge einzeigen");
            System.out.println("(H) - Neuer Eintrag hinzufügen");
            System.out.println("(P) - Suchen");
            System.out.println("(E) - Exit");
            try{
                String cmd = reader.readLine();
                executeCommand(cmd.toUpperCase());
            }catch (Exception e){
                System.out.println("Beim Lesen ist es zu folgenden Fehler gekommen: " + e.getMessage());
            }
        }
    }

    private static void executeCommand(String cmd) {
        switch (cmd) {
            case "S": //alle Einträge anzeigen
                showAllEntries();
                break;
            case "H": //neuen Eintrag hinzufügen
                newEntry();
                break;
            case "E": // Die Anwendung beenden
                exitApplication();
                break;
            case "D": // Einen Eintrag löschen
                deleteUser();
                break;
            case "P": // Nach einem Eintrag suchen
                searchUser();
                break;
            default: // Falsche/unbekannte Eingabe
                System.out.println("Unbekanntes Kürzel");
        }
    }

    private static void searchUser() {

        try {
            System.out.println("Geben Sie die Such parameter ein. Unbekannte Felder leer lassen.");

            System.out.println("Geben Sie den Namen der Person ein: ");
            String name = reader.readLine();

            System.out.println("Geben Sie den Vornamen ein: ");
            String vorname = reader.readLine();

            Person person = new Person();
            if (!name.isEmpty())
                person.setName(name);

            if (!vorname.isEmpty())
                person.setVorname(vorname);

            System.out.println("Gefundene Personen:");
            System.out.println("-----------------------");
            printPersons(adressbuch.suche(person));

        } catch (IOException e) {
            System.out.println("Fehler beim Lesen der Eingabe.");
        }
    }

    private static void deleteUser() {
        System.out.println("Geben Sie den Namen, der zu loeschenden Person ein:");
        try {
            String name = reader.readLine();
            List<Person> personen = adressbuch.getPersonen();

            for (Person p: personen) {
                if(p.getName().equals(name)){
                    adressbuch.loeschen(p);
                    if (personen.size() == 0){
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Folgende Exception ist beim Löschen aufgetreten: " + e.getMessage());
        }
    }

    private static void newEntry() {
        String person = null;
        try {
            System.out.println("Neuen Kunden, Angestellten hinzufügen?");
            person = reader.readLine();
        }catch (Exception e) {
            System.out.println("Falsche Eingabe!");
        }

        if(person.equals("Angestellter")) {
            try {
                System.out.println("Geben Sie den Vornamen ein:");
                String vorname = reader.readLine();
                System.out.println("Geben Sie den Nachnamen ein:");
                String name = reader.readLine();
                System.out.println("Geben Sie die Geburtstag ein:");
                String geburtstag = reader.readLine();
                System.out.println("Geben Sie die Personalnummer ein:");
                int personalnummer = Integer.parseInt(reader.readLine());
                System.out.println("Geben Sie die Managernummer ein:");
                int managernummer = Integer.parseInt(reader.readLine());
                DBcreate.hinzufuegen(new Angestellter(vorname, name, geburtstag, personalnummer, managernummer));
                System.out.println("Nutzer hinzugefügt.");
            } catch (Exception e) {
                System.out.println("Es ist ein Fehler aufgetreten. Der Angestellter konnte nich hinzugefügt werden!");
            }
        }
            if(person.equals("Kunde")) {
                try {
                    System.out.println("Geben Sie den Vornamen ein:");
                    String vorname = reader.readLine();
                    System.out.println("Geben Sie den Nachnamen ein:");
                    String name = reader.readLine();
                    System.out.println("Geben Sie die Telefonnummer ein:");
                    int telefonnummer = Integer.parseInt(reader.readLine());
                    System.out.println("Geben Sie die IBAN ein:");
                    int iban = Integer.parseInt(reader.readLine());
                    DBcreate.hinzufuegen(new Kunde(vorname, name, telefonnummer, iban));
                    System.out.println("Nutzer hinzugefügt.");
                } catch (Exception e) {
                    System.out.println("Es ist ein Fehler aufgetreten. Der Angestellter konnte nich hinzugefügt werden!");
                }
            }
        }


    private static void exitApplication() {
        System.exit(0);
    }

    private static void showAllEntries() {
        List<Person> personen = adressbuch.getPersonen();
        printPersons(personen);
    }

    /***
     * Um Code-Doppelung in searchUser und showAllEntries zu vermeiden wurde
     * der Code in eine eigene Methode-Exctrahiert.
     * @param persons
     */
    private static void printPersons(List<Person> persons) {
        int i = 1;
        for (Person person :persons) {
            System.out.println("Eintrag: " + i++);
            System.out.println("Vorname: " + person.getVorname());
            System.out.println("Name: " + person.getName());
            System.out.println(("Telefonnummer: " + person.getTelefonnummer()));
            System.out.println("------------------------------------------------");
        }
    }
}
