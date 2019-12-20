package sample;

public class Raeuber {
    double raeuberBiomasse = 0;
    double gewinnRate = 0;

    public double getEnergieRate() {
        return energieRate;
    }

    double energieRate = 0;

    public double getGleichgewicht() {
        return gleichgewicht;
    }

    double gleichgewicht = 0;

    public Raeuber(double raeuberBiomasse, double energieRate, double gleichgewicht)
    {
        this.raeuberBiomasse = raeuberBiomasse;
        this.energieRate = energieRate;
        this.gleichgewicht = gleichgewicht;
    }

    double giveRaeuberZuwachs(double treffen, Beute beute)
    {
        return energieRate/beute.getGleichgewicht()*treffen;
    }

    double giveRaeuberVerlust()
    {
        return raeuberBiomasse *energieRate;
    }

    public double getRaeuberBiomasse() {
        return raeuberBiomasse;
    }

    public void setRaeuberBiomasse(double raeuberBiomasse) {
        this.raeuberBiomasse = raeuberBiomasse;
    }

    @Override
    public String toString() {
        return "Fuchsbiomasse: " + raeuberBiomasse;
    }
}
