package sample;

public class Beute {
    double beuteBiomasse = 0;
    double zuwachsRate = 0;
    double verlustRate = 0;

    public double getGleichgewicht() {
        return gleichgewicht;
    }

    double gleichgewicht = 0;

    public Beute(double beuteBiomasse, double zuwachsRate, double gleichgewicht)
    {
        this.beuteBiomasse = beuteBiomasse;
        this.zuwachsRate = zuwachsRate;
        this.gleichgewicht = gleichgewicht;
    }
    double giveBeuteVerlust(double treffen, Raeuber raeuber)
    {
        return zuwachsRate/raeuber.getGleichgewicht()*treffen;
    }

    double giveBeuteZuwachs()
    {
        return zuwachsRate* beuteBiomasse;
    }

    public double getBeuteBiomasse() {
        return beuteBiomasse;
    }

    public void setBeuteBiomasse(double beuteBiomasse) {
        this.beuteBiomasse = beuteBiomasse;
    }

    @Override
    public String toString() {
        return "Hasebiomasse: " + beuteBiomasse;
    }
}
