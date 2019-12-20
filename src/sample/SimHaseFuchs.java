package sample;

public class SimHaseFuchs {

    public static void main(String[] args) {
        double treffen = 0;
        double week = 0.25;
        Hase hase = new Hase(400,0.08, 500);
        Fuchs fuchs = new Fuchs(20,0.2,20);
        Weide weide = new Weide(1000, 100,200);

        Double[][] hasen = SimHaseFuchs.getHaseGraph(5,0.25,hase,fuchs,weide);
        Double[][] fuchse = SimHaseFuchs.getHaseGraph(5,0.25,hase,fuchs,weide);
        for(int i = 0; i<10;i++)
        {
//            System.out.println(hasen[i][0]);
//            System.out.println(hasen[i][1]);
//            System.out.println();
        }

//        for(int i = 0; i < 16; i++)
//        {
//
//            System.out.println(i);
//            System.out.println(hase.toString());
//            System.out.println(fuchs.toString());
//            System.out.println();
//            treffen = treffen(hase,fuchs);
//            hase.setHaseBiomasse(hase.getHaseBiomasse()+week*
//                    (hase.giveHaseZuwachs()*Sim.weideflaeche(hase,weide,week)-hase.giveHaseVerlust(treffen)));
//
//            treffen = treffen(hase,fuchs);
//            fuchs.setFuchsBiomasse(fuchs.getFuchsBiomasse()+week*
//                    (fuchs.giveFuchsZuwachs(treffen)-fuchs.giveFuchsVerlust()));
//        }

    }

    static double treffen(Hase hase, Fuchs fuchs)
    {
        return hase.getHaseBiomasse()*fuchs.getFuchsBiomasse();
    }

    static double weideflaeche(Hase hase, Weide weide, double time)
    {
        return 1-(hase.getHaseBiomasse()/weide.giveKapazitaet(time));
    }

    static public Double[][] getGraphxAxis(int length, double timestep)
    {

        length = SimHaseFuchs.getTime(length, timestep);

        Double[][] graphxAxis = new Double[length][2];

        double time = 0;
        for(int i = 0; i < length; i++)
        {
            graphxAxis[i][0] = time;
            time +=timestep;
        }
        return graphxAxis;
    }
static int getTime(double length, double timestep)
{
    return (int) (length =  (length/timestep));
}
    static public Double[][] getHaseGraph(int length, double timestep, Hase hase, Fuchs fuchs, Weide weide)
    {
        double treffen = 0;
        Double[][] graphyAxis = SimHaseFuchs.getGraphxAxis(length,timestep);
        length = SimHaseFuchs.getTime(length, timestep);
        for(int i = 0; i < length; i++) {
            graphyAxis[i][1] = hase.getHaseBiomasse();
            treffen = SimHaseFuchs.treffen(hase, fuchs);
            System.out.println(i);
            System.out.println(hase.toString());
            hase.setHaseBiomasse(hase.getHaseBiomasse() + timestep *
                    (hase.giveHaseZuwachs() * SimHaseFuchs.weideflaeche(hase, weide, timestep) - hase.giveHaseVerlust(treffen,fuchs)));
            System.out.println(hase.toString());
            System.out.println();
            treffen = SimHaseFuchs.treffen(hase, fuchs);
            fuchs.setFuchsBiomasse(fuchs.getFuchsBiomasse() + timestep *
                    (fuchs.giveFuchsZuwachs(treffen,hase) - fuchs.giveFuchsVerlust()));
        }
        return graphyAxis;
    }

    static public Double[][] getFuchsGraph(int length, double timestep, Hase hase, Fuchs fuchs, Weide weide)
    {
        double treffen = 0;
        Double[][] graphyAxis = SimHaseFuchs.getGraphxAxis(length,timestep);
        length = SimHaseFuchs.getTime(length, timestep);
        for(int i = 0; i < length; i++) {
            graphyAxis[i][1] = fuchs.getFuchsBiomasse();
            treffen = SimHaseFuchs.treffen(hase, fuchs);
            hase.setHaseBiomasse(hase.getHaseBiomasse() + timestep *
                    (hase.giveHaseZuwachs() * SimHaseFuchs.weideflaeche(hase, weide, timestep) - hase.giveHaseVerlust(treffen, fuchs)));

            treffen = SimHaseFuchs.treffen(hase, fuchs);
            fuchs.setFuchsBiomasse(fuchs.getFuchsBiomasse() + timestep *
                    (fuchs.giveFuchsZuwachs(treffen,hase) - fuchs.giveFuchsVerlust()));
        }
        return graphyAxis;
    }
}



