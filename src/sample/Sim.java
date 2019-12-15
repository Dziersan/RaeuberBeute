package sample;

public class Sim {
    public static void main(String[] args) {
    double treffen = 0;
    double week = 0.25;
        Hase hase = new Hase(500,0.08,0.002, 500);
        Fuchs fuchs = new Fuchs(10,0.0004,0.2,20);
        Weide weide = new Weide(1000);

    for(double i = 0; i < 1; i+= 0.25)
    {
        System.out.println(i);
        System.out.println(hase.toString());
        System.out.println(fuchs.toString());
        System.out.println();
        treffen = treffen(hase,fuchs);
        hase.setHaseBiomasse(hase.getHaseBiomasse()+week*
                (hase.giveHaseZuwachs()*Sim.weideflaeche(hase,weide,0.25)-hase.giveHaseVerlust(treffen)));

        treffen = treffen(hase,fuchs);
        fuchs.setFuchsBiomasse(fuchs.getFuchsBiomasse()+week*
        (fuchs.giveFuchsZuwachs(treffen)-fuchs.giveFuchsVerlust()));
    }


    }
    static double treffen(Hase hase, Fuchs fuchs)
    {
        return hase.getHaseBiomasse()*fuchs.getFuchsBiomasse();
    }

    static double weideflaeche(Hase hase, Weide weide, double time)
    {
        return 1-(hase.getHaseBiomasse()/weide.giveKapazitaet(time));
    }
}
