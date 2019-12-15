package sample;

public class Weide {
    double kapazität = 0;
    double beschraenkung = 0;

    public Weide(double kapazität)
    {
        this.kapazität = kapazität;
    }

    double giveKapazitaet(double time)
    {
        if(time < 99 || time >199)
        {
            return 1000;
        }
        else
            return 500;
    }


}
