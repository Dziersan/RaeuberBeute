package sample.Core;

public class Fuchs {
    double fuchsBiomasse = 0;
    double gewinnRate = 0;
    double verlustRate = 0;
    double gleichgewicht = 0;

    public Fuchs(double fuchsBiomasse,double verlustRate, double gleichgewicht)
    {
        this.fuchsBiomasse = fuchsBiomasse;
        this.verlustRate = verlustRate;
        this.gleichgewicht = gleichgewicht;
    }

    double giveFuchsZuwachs(double treffen)
    {
        return gewinnRate*treffen;
    }

    double giveFuchsVerlust()
    {
        return fuchsBiomasse* verlustRate;
    }

    public double getFuchsBiomasse() {
        return fuchsBiomasse;
    }

    public void setFuchsBiomasse(double fuchsBiomasse) {
        this.fuchsBiomasse = fuchsBiomasse;
    }

    @Override
    public String toString() {
        return "Fuchsbiomasse: " + fuchsBiomasse;
    }

    public double getGleichgewicht() {
        return gleichgewicht;
    }
public double giveFuchsZuwachs(double treffen, Hase hase)
{
    return verlustRate/hase.getGleichgewicht()*treffen;
}

}
