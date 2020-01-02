package sample;

public class Sim {
    public static void main(String[] args) {

    double week = 0.25;
        Beute beute = new Beute(400,0.08, 500);
        Raeuber raeuber = new Raeuber(10,0.2,20);

//    for(double i = 0; i < 1; i+= 0.25)
//
//    {
//        System.out.println(i);
//        System.out.println(beute.toString());
//        System.out.println(raeuber.toString());
//        System.out.println();
//
//        treffen = Sim.treffen(beute, raeuber);
//
//        beute.setBiomasse(beute.getBiomasse()+week*
//                (beute.giveBiomasseAenderung()- beute.giveTreffen(raeuber,treffen)));
//
//        treffen = Sim.treffen(beute, raeuber);
//
//        raeuber.setBiomasse(raeuber.getBiomasse()+week*
//        (raeuber.giveTreffen(beute,treffen)- raeuber.giveBiomasseAenderung()));
//
//    }
    }
    static public Double[][][] getGraph(int length, double timestep, Beute beute, Raeuber raeuber)
    {
        double treffen = 0;
        Double[][][] graphyAxis = XYGraph.getGraphxAxis(length,timestep);
        length = XYGraph.getTime(length, timestep);
        for(int i = 0; i < length; i++) {

            treffen = Sim.treffen(beute, raeuber);

            beute.setBiomasse(beute.getBiomasse() + timestep *
                    (beute.giveBiomasseAenderung() - beute.giveTreffen(raeuber, treffen)));
            graphyAxis[i][1][0] = beute.getBiomasse();
            treffen = Sim.treffen(beute, raeuber);

            raeuber.setBiomasse(raeuber.getBiomasse() + timestep *
                    (raeuber.giveTreffen(beute, treffen) - raeuber.giveBiomasseAenderung()));
            graphyAxis[i][0][1] = raeuber.getBiomasse();
        }
        return graphyAxis;
    }

    static double treffen(Beute beute, Raeuber raeuber)
    {
        return beute.getBiomasse()* raeuber.getBiomasse();
    }
}
