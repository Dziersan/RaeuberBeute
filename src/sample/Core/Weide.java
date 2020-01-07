package sample.Core;

public class Weide {
    double kapazität;
    double abBegrenzung;
    double bisBegrenzung;

    public Weide(double kapazität, double abBegrenzung, double bisBegrenzung)
    {
        this.kapazität = kapazität;
        this.abBegrenzung = abBegrenzung;
        this.bisBegrenzung = bisBegrenzung;
    }

    /** Begrenzung der Weide wurde von Bossel vorgegeben, daher statische Werte
     *
     */
    public double giveKapazitaet(double time)
    {
        if(time < abBegrenzung  || time > bisBegrenzung)
        {
            return 1000;
        }
        else
            return 500;
    }
}
