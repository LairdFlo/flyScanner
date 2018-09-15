package de.data;

import org.joda.time.DateTime;

import java.util.Objects;

public class Flight {

    public Flight(){

    }

    private String unicNumber;

    //Flugnummer
    private String fc;

    //Verspaetung
    private int delay;

    //Abflug Flughafen
    private Airport daid;

    // Ankunft Flughafen
    private String aaid;

    // Abflug Plan
    private String ssd;
    private DateTime planFlugzeit;

    // Abflug Tatsächlich
    private String add;
    private DateTime neueFlugzeit;

    //Ankunft Plan
    private String aad;

    //Ankungt Tatsächlich
    private String sad;

    //Herkunft
    private Quelle quelle;

    //Preis
    private String preis = "";

    public String getUnicNumber() {
        return unicNumber;
    }

    public void setUnicNumber(String unicNumber) {
        this.unicNumber = unicNumber;
    }

    public String getFc() {
        return fc;
    }

    public void setFc(String fc) {
        this.fc = fc;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public Airport getDaid() {
        return daid;
    }

    public void setDaid(Airport daid) {
        this.daid = daid;
    }

    public String getAaid() {
        return aaid;
    }

    public void setAaid(String aaid) {
        this.aaid = aaid;
    }

    public String getSsd() {
        return ssd;
    }

    public void setSsd(String ssd) {
        this.ssd = ssd;
    }

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }

    public String getAad() {
        return aad;
    }

    public void setAad(String aad) {
        this.aad = aad;
    }

    public String getSad() {
        return sad;
    }

    public void setSad(String sad) {
        this.sad = sad;
    }

    public Quelle getQuelle() {
        return quelle;
    }

    public void setQuelle(Quelle quelle) {
        this.quelle = quelle;
    }

    public String getPreis() {
        return preis;
    }

    public void setPreis(String preis) {
        this.preis = preis;
    }

    public DateTime getPlanFlugzeit() {
        return planFlugzeit;
    }

    public void setPlanFlugzeit(DateTime planFlugzeit) {
        this.planFlugzeit = planFlugzeit;
    }

    public DateTime getNeueFlugzeit() {
        return neueFlugzeit;
    }

    public void setNeueFlugzeit(DateTime neueFlugzeit) {
        this.neueFlugzeit = neueFlugzeit;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "unicNumber='" + unicNumber + '\'' +
                ", fc='" + fc + '\'' +
                ", delay=" + delay +
                ", daid=" + daid +
                ", aaid='" + aaid + '\'' +
                ", ssd='" + ssd + '\'' +
                ", planFlugzeit=" + planFlugzeit +
                ", add='" + add + '\'' +
                ", neueFlugzeit=" + neueFlugzeit +
                ", aad='" + aad + '\'' +
                ", sad='" + sad + '\'' +
                ", quelle=" + quelle +
                ", preis='" + preis + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flight flight = (Flight) o;
        return Objects.equals(unicNumber, flight.unicNumber) &&
                Objects.equals(fc, flight.fc) &&
                daid == flight.daid &&
                Objects.equals(aaid, flight.aaid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(unicNumber, fc, daid, aaid);
    }
}
