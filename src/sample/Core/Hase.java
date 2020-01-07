package sample.Core;

public class Hase {
    double haseBiomasse = 0;
    double zuwachsRate = 0;
    double verlustRate = 0;
    double gleichgewicht = 0;

    public Hase(double haseBiomasse, double zuwachsRate, double gleichgewicht)
    {
        this.haseBiomasse = haseBiomasse;
        this.zuwachsRate = zuwachsRate;
        this.gleichgewicht = gleichgewicht;
    }
    double giveHaseVerlust(double treffen, Fuchs fuchs)
    {
        return zuwachsRate/fuchs.getGleichgewicht()*treffen;
    }

    double giveHaseZuwachs()
    {
        return zuwachsRate*haseBiomasse;
    }

    public double getHaseBiomasse() {
        return haseBiomasse;
    }

    public void setHaseBiomasse(double haseBiomasse) {
        this.haseBiomasse = haseBiomasse;
    }

    @Override
    public String toString() {
        return "Hasebiomasse: " + haseBiomasse;
    }

    public double getZuwachsRate() {
        return zuwachsRate;
    }

    public double getGleichgewicht() {
        return gleichgewicht;
    }
}
