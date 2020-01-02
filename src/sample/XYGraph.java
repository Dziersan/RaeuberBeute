package sample;

public class XYGraph {

    public static Double[][][] getGraphxAxis(int length, double timestep)
    {

        length = getTime(length, timestep);

        Double[][][] graphxAxis = new Double[length][2][2];

        double time = 0;
        for(int i = 0; i < length; i++)
        {
            graphxAxis[i][0][0] = time;
            time +=timestep;
        }
        return graphxAxis;
    }
    static int getTime(double length, double timestep)
    {
        return (int) (length =  (length/timestep));
    }
}
