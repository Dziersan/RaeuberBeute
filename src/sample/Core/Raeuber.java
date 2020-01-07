package sample.Core;

public class Raeuber {


    double biomasse = 0;
    double gleichgewicht = 0;
    double rate = 0;

    public Raeuber(double Biomasse, double rate, double gleichgewicht)
    {
        this.biomasse = Biomasse;
        this.rate = rate;
        this.gleichgewicht = gleichgewicht;
    }

    public double giveTreffen(Beute tier, double treffen)
    {
        return this.rate/tier.getGleichgewicht()*treffen;
    }

    public double giveBiomasseAenderung()
    {
        return (biomasse*rate);
    }

    public double getBiomasse() {
        return biomasse;
    }

    public void setBiomasse(double biomasse) {
        this.biomasse = biomasse;
    }

    public double getGleichgewicht() {
        return gleichgewicht;
    }

    public void setGleichgewicht(double gleichgewicht) {
        this.gleichgewicht = gleichgewicht;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "Fuchsbiomasse: " + biomasse;
    }
}
