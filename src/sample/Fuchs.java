package sample;

public class Fuchs {
    double fuchsBiomasse = 0;
    double gewinnRate = 0;
    double energieRate = 0;
    double gleichgewicht = 0;

    public Fuchs(double fuchsBiomasse, double gewinnRate, double energieRate, double gleichgewicht)
    {
        this.fuchsBiomasse = fuchsBiomasse;
        this.gewinnRate = gewinnRate;
        this.energieRate = energieRate;
        this.gleichgewicht = gleichgewicht;
    }

    double giveFuchsZuwachs(double treffen)
    {
        return gewinnRate*treffen;
    }

    double giveFuchsVerlust()
    {
        return fuchsBiomasse*energieRate;
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
}
