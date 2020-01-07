package sample.Core;

public class Beute {
    double biomasse = 0;
    double rate = 0;
    double gleichgewicht = 0;


    public Beute(double biomasse, double rate, double gleichgewicht)
    {
        this.biomasse = biomasse;
        this.rate = rate;
        this.gleichgewicht = gleichgewicht;
    }

    public double giveBiomasseAenderung()
    {
        return (biomasse*rate);
    }

    public double giveTreffen(Raeuber tier, double treffen)
    {
        return this.rate/tier.getGleichgewicht()*treffen;
    }

    public double getBiomasse() {
        return biomasse;
    }


    public void setBiomasse(double biomasse) {
        this.biomasse = biomasse;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getGleichgewicht() {
        return gleichgewicht;
    }

    public void setGleichgewicht(double gleichgewicht) {
        this.gleichgewicht = gleichgewicht;
    }

    @Override
    public String toString() {
        return "Hasebiomasse: " + biomasse;
    }
}
