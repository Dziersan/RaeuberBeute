package sample.Core;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Sim {

    private double timestep;
    private double beuteBiomasse;
    private double raeuberBiomasse;
    private double weideData;

    /**
     * Konstruktor für das TableView von Beute_Räuber
     */
    public Sim(double timestep, double beuteBiomasse, double raeuberBiomasse)
    {
        this.timestep = timestep;
        this.beuteBiomasse = beuteBiomasse;
        this.raeuberBiomasse = raeuberBiomasse;
    }
    /**
     * Konstruktor für das TableView von Fuchs_Hase
     */
    public Sim(double timestep, double beuteBiomasse, double raeuberBiomasse, double weideData )
    {
        this.timestep = timestep;
        this.beuteBiomasse = beuteBiomasse;
        this.raeuberBiomasse = raeuberBiomasse;
        this.weideData = weideData;
    }
    static public Double[][] getGraphBH(int length, double timestep, Beute beute, Raeuber raeuber)
    {
        double treffen = 0;
        //Zuerst das befüllen der x-Achse mit den Werten mithilfe der GuiUtil Methode
        Double[][] graphyAxis = GuiUtil.getGraphxAxis(length,timestep);
        length = GuiUtil.getTime(length, timestep);

        for(int i = 0; i < length; i++) {
            // Zusammentreffen der beiden Biomassen
            treffen = Sim.treffenBH(beute, raeuber);

            /** Der neue Wert für die Biomassen der Beute wird wie befolgt berechnet:
             * Alter Wert Beute + Zeitschritt*( Zuwachs Beute - Verlust Beute)
             * Zuwachs der Beute wird prozentual von der gesamten Biomasse berechnet.
             * Verluste sind die Chance pro Treffer getötet zu werden.
             * Die Werte werden der Klasse und dem Array zugewiesen
             */
            beute.setBiomasse(beute.getBiomasse() + timestep *
                    (beute.giveBiomasseAenderung() - beute.giveTreffen(raeuber, treffen)));
            graphyAxis[i][1] = beute.getBiomasse();

            //Es wird mit den neuen Daten der Beutebiomasse gerechnet.
            treffen = Sim.treffenBH(beute, raeuber);
            /**
             * Anders als die Beute erhält der Räuber die zuwächse durch die Chance bei jedem Treffer sich zu vermehren.
             * Der Verlust ist fix und wird prozentual von der Gesamten Biomasse abgezogen.
             */
            raeuber.setBiomasse(raeuber.getBiomasse() + timestep *
                    (raeuber.giveTreffen(beute, treffen) - raeuber.giveBiomasseAenderung()));
            graphyAxis[i][2] = raeuber.getBiomasse();
        }
        return graphyAxis;
    }

    /**
     * Erstellen einer ObservableList für das TableView. Anders als beim Erstellen des Graphen werden die Daten
     * in einer Liste übergeben und in die Klasse Sim zugewiesen. Dies ist Notwendig, damit das TableView auf die Daten
     * zugreifen kann.
     */
    public ObservableList<Sim> getTableDataBH(int length, Beute beute, Raeuber raeuber) {

        ObservableList<Sim> tableData = FXCollections.observableArrayList();

        double treffen = 0;
        double copyTimestep = timestep;

        length = GuiUtil.getTime(length, timestep);
        double step = 0;
        timestep = 0;

        for (int i = 0; i <= length; i++) {

            treffen = Sim.treffenBH(beute, raeuber);

            beute.setBiomasse(beute.getBiomasse() + timestep *
                    (beute.giveBiomasseAenderung() - beute.giveTreffen(raeuber, treffen)));
            beuteBiomasse = GuiUtil.round(beute.getBiomasse());

            treffen = Sim.treffenBH(beute, raeuber);

            raeuber.setBiomasse(raeuber.getBiomasse() + timestep *
                    (raeuber.giveTreffen(beute, treffen) - raeuber.giveBiomasseAenderung()));
            raeuberBiomasse = GuiUtil.round(raeuber.getBiomasse());

            tableData.add(new Sim(step, beuteBiomasse, raeuberBiomasse));
            timestep = copyTimestep;
            step = GuiUtil.round(step+timestep);
        }
        return tableData;
    }

    static double treffenBH(Beute beute, Raeuber raeuber)
    {
        return beute.getBiomasse()* raeuber.getBiomasse();
    }

    /** Ähnlich wie in den Berechnungen zuvor, jedoch spielt jetzt die Weidenbegrenzug hinzu
     *
     */
    static public Double[][] getGraph(int length, double timestep, Hase hase, Fuchs fuchs, Weide weide)
    {
        double treffen = 0;
        Double[][] graphyAxis = GuiUtil.getGraphxAxis(length,timestep);
        length = GuiUtil.getTime(length, timestep);

        double copyTimestep = timestep;
        double step = 0;
        timestep = 0;
        for(int i = 0; i < length; i++) {
            graphyAxis[i][1] = hase.getHaseBiomasse();
            graphyAxis[i][4] = Sim.treffen(hase, fuchs);


            hase.setHaseBiomasse(hase.getHaseBiomasse() + timestep *
                    (hase.giveHaseZuwachs() * Sim.weideflaeche(hase, weide, step) - hase.giveHaseVerlust(treffen,fuchs)));

            treffen = Sim.treffen(hase, fuchs);
            graphyAxis[i][5] = Sim.treffen(hase, fuchs);

            fuchs.setFuchsBiomasse(fuchs.getFuchsBiomasse() + timestep *
                    (fuchs.giveFuchsZuwachs(treffen,hase) - fuchs.giveFuchsVerlust()));

            graphyAxis[i][2] = fuchs.getFuchsBiomasse();
            graphyAxis[i][3] = weideflaeche(hase, weide, step);
            timestep = copyTimestep;
            step += timestep;
        }
        return graphyAxis;
    }

    public ObservableList<Sim> getTableData(int length, Hase hase, Fuchs fuchs, Weide weide) {

        ObservableList<Sim> tableData = FXCollections.observableArrayList();

        double treffen = 0;
        double j = timestep;
        length = GuiUtil.getTime(length, timestep);

        double step = 0;
        timestep = 0;
        for (int i = 0; i <= length; i++) {

            step = Math.round(100.0 * step) / 100.0;
            treffen = Sim.treffen(hase, fuchs);

            hase.setHaseBiomasse(hase.getHaseBiomasse() + timestep *
                    (hase.giveHaseZuwachs() * Sim.weideflaeche(hase, weide, step) - hase.giveHaseVerlust(treffen,fuchs)));
            beuteBiomasse = GuiUtil.round(hase.getHaseBiomasse());

            treffen = Sim.treffen(hase, fuchs);

            fuchs.setFuchsBiomasse(fuchs.getFuchsBiomasse() + timestep *
                    (fuchs.giveFuchsZuwachs(treffen,hase) - fuchs.giveFuchsVerlust()));
            raeuberBiomasse = GuiUtil.round(fuchs.getFuchsBiomasse());
            weideData = Sim.weideflaeche(hase, weide, step);

            tableData.add(new Sim(step, beuteBiomasse, raeuberBiomasse, weideData));
            timestep = j;
            step += timestep;
        }

        return tableData;
    }

    static double treffen(Hase hase, Fuchs fuchs)
    {
        return hase.getHaseBiomasse()*fuchs.getFuchsBiomasse();
    }

    static double weideflaeche(Hase hase, Weide weide, double time)
    {
        return 1-(hase.getHaseBiomasse()/weide.giveKapazitaet(time));
    }

    /** Getter sind essenziell für das TableView. Auch die Bennenung dieser muss dem Schema unten exakt befolgen, da sonst
     * die Daten nicht in die TableColumns erscheinen.
     */
    public double getTimestep() {
        return timestep;
    }
    public double getBeuteBiomasse() {
        return beuteBiomasse;
    }
    public double getRaeuberBiomasse() {
        return raeuberBiomasse;
    }
    public double getWeideData() {
        return weideData;
    }
    public void setWeideData(double weideData) {
        this.weideData = weideData;
    }
}

