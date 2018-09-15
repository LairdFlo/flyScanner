package de.data;

public enum Airport {
    DUS("DUS", "Düsseldorf"),
    CGN("CGN", "Köln-Bonn"),
    FRA("FRA", "Frankfurt-Main"),
    MUC("MUC", "München"),
    TXL("TXL", "Berlin-Tegel"),
    SXF("SXF", "Berlin-Schönefeld"),
    HAM("HAM", "Hamburg "),
    STR("STR", "Stuttgart"),
    HAJ("HAJ", "Hannover"),
    NUE("NUE", "Nürnberg"),
    BRE("BRE", "Bremen"),
    FMM("FMM", "Memmingen"),
    LEJ("LEJ", "Leipzig-Halle"),
    DTM("DTM", "Dortmund"),
    DRS("DRS", "Dresden International"),
    HHN("HHN", "Frankfurt-Hahn"),
    FMO("FMO", "Münster-Osnabrück"),
    FKB("FKB", "Karlsruhe"),
    NRN("NRN", "Weeze"),
    FDH("FDH", "Friedrichshafen"),
    PAD("PAD", "Paderborn-Lippstadt"),
    GWT("GWT", "Westerland / Sylt"),
    SCN("SCN", "Saarbrücken"),
    RLG("RLG", "Rostock-Laage"),
    ERF("ERF", "Erfurt-Weimar"),
    KSF("KSF", "Kassel-Calden"),
    HDF("HDF", "Heringsdorf"),
    MHG("MHG", "Mannheim-Neuostheim"),
    ESS("ESS", "Essen"),
    EME("EME", "Emden"),
    QWU("QWU", "Würzburg"),
    LBC("LBC", "Lübeck Blankensee"),
    HOQ("HOQ", "Hof-Plauen"),
    ZQW("ZQW", "Zweibrücken"),
    BRV("BRV", "Bremerhaven"),
    HEI("HEI", "Heide-Büsum"),
    HGL("HGL", "Helgoland"),
    MGL("MGL", "Mönchengladbach"),
    AOC("AOC", "Altenburg Nobitz"),
    UNKNOWN("", "");

    private final String shortName;
    private final String longName;


    Airport(String shortName, String longName) {
        this.shortName = shortName;
        this.longName = longName;
    }

    public String toString() {
        return shortName;
    }

    public String toPlainString() {
        return longName;
    }
    public static Airport fromString(String text) {
        for (Airport b : Airport.values()) {
            if (b.shortName.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return Airport.UNKNOWN;
    }
}
