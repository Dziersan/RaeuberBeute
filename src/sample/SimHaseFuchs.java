package sample;

public class SimHaseFuchs {

    public static void main(String[] args) {
        double treffen = 0;
        double week = 0.25;

    }
    static double treffen(Hase hase, Fuchs fuchs)
    {
        return hase.getHaseBiomasse()*fuchs.getFuchsBiomasse();
    }

    static double weideflaeche(Hase hase, Weide weide, double time)
    {
        return 1-(hase.getHaseBiomasse()/weide.giveKapazitaet(time));
    }

    static public Double[][][] getGraph(int length, double timestep, Hase hase, Fuchs fuchs, Weide weide)
    {
        double treffen = 0;
        Double[][][] graphyAxis = XYGraph.getGraphxAxis(length,timestep);
        length = XYGraph.getTime(length, timestep);

        for(int i = 0; i < length; i++) {
            graphyAxis[i][1][0] = hase.getHaseBiomasse();
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
            graphyAxis[i][0][1] = fuchs.getFuchsBiomasse();
        }
        return graphyAxis;
    }
}



