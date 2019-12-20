package sample;

public class Sim {
    public static void main(String[] args) {
    double treffen = 0;
    double week = 0.25;
        Beute beute = new Beute(400,0.08, 500);
        Raeuber raeuber = new Raeuber(10,0.2,20);

    for(double i = 0; i < 18; i+= 0.25)

    {
        System.out.println(i);
        System.out.println(beute.toString());
        System.out.println(raeuber.toString());
        System.out.println();

        treffen = treffen(beute, raeuber);
        beute.setBeuteBiomasse(beute.getBeuteBiomasse()+week*
                (beute.giveBeuteZuwachs()- beute.giveBeuteVerlust(treffen,raeuber)));

        treffen = treffen(beute, raeuber);

        raeuber.setRaeuberBiomasse(raeuber.getRaeuberBiomasse()+week*
        (raeuber.giveRaeuberZuwachs(treffen,beute)- raeuber.giveRaeuberVerlust()));

    }

    }
    static double treffen(Beute beute, Raeuber raeuber)
    {
        return beute.getBeuteBiomasse()* raeuber.getRaeuberBiomasse();
    }


}
