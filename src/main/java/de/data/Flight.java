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
                '}';
    }
}
