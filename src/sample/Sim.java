package sample;

public class Sim {
    public static void main(String[] args) {

    double week = 0.25;
        Beute beute = new Beute(400,0.08, 500);
        Raeuber raeuber = new Raeuber(10,0.2,20);

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
