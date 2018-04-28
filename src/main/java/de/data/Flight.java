package de.data;

public class Flight {

    public Flight(){

    }

    private String unicNumber;

    //Flugnummer
    private String fc;

    //Verspaetung
    private int delay;

    //Abflug Flughafen
    private String daid;

    // Ankunft Flughafen
    private String aaid;

    // Abflug Plan
    private String ssd;

    // Abflug Tatsächlich
    private String add;

    //Ankunft Plan
    private String aad;

    //Ankungt Tatsächlich
    private String sad;

    private boolean cgnFlight;

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

    public String getDaid() {
        return daid;
    }

    public void setDaid(String daid) {
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

    public boolean isCgnFlight() {
        return cgnFlight;
    }

    public void setCgnFlight(boolean cgnFlight) {
        this.cgnFlight = cgnFlight;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "unicNumber='" + unicNumber + '\'' +
                ", fc='" + fc + '\'' +
                ", delay=" + delay +
                ", daid='" + daid + '\'' +
                ", aaid='" + aaid + '\'' +
                ", ssd='" + ssd + '\'' +
                ", add='" + add + '\'' +
                ", aad='" + aad + '\'' +
                ", sad='" + sad + '\'' +
                ", cgnFlight=" + cgnFlight +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Flight flight = (Flight) o;

        if (delay != flight.delay) return false;
        if (cgnFlight != flight.cgnFlight) return false;
        if (unicNumber != null ? !unicNumber.equals(flight.unicNumber) : flight.unicNumber != null) return false;
        if (fc != null ? !fc.equals(flight.fc) : flight.fc != null) return false;
        if (daid != null ? !daid.equals(flight.daid) : flight.daid != null) return false;
        if (aaid != null ? !aaid.equals(flight.aaid) : flight.aaid != null) return false;
        if (ssd != null ? !ssd.equals(flight.ssd) : flight.ssd != null) return false;
        if (add != null ? !add.equals(flight.add) : flight.add != null) return false;
        if (aad != null ? !aad.equals(flight.aad) : flight.aad != null) return false;
        return sad != null ? sad.equals(flight.sad) : flight.sad == null;

    }

    @Override
    public int hashCode() {
        int result = unicNumber != null ? unicNumber.hashCode() : 0;
        result = 31 * result + (fc != null ? fc.hashCode() : 0);
        result = 31 * result + delay;
        result = 31 * result + (daid != null ? daid.hashCode() : 0);
        result = 31 * result + (aaid != null ? aaid.hashCode() : 0);
        result = 31 * result + (ssd != null ? ssd.hashCode() : 0);
        result = 31 * result + (add != null ? add.hashCode() : 0);
        result = 31 * result + (aad != null ? aad.hashCode() : 0);
        result = 31 * result + (sad != null ? sad.hashCode() : 0);
        result = 31 * result + (cgnFlight ? 1 : 0);
        return result;
    }
}
