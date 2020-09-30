package nl.hu.dp.ovchip.domein;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Reiziger {
    @Id
    @Column(name = "reiziger_id")
    private int id;
    private String voorletters;
    private String tussenvoegsel;
    private String achternaam;
    private Date geboortedatum;
    @OneToOne(mappedBy = "reiziger")
    private Adres adres;
    @OneToMany(mappedBy = "reiziger")
    private List<OVChipkaart> kaarten = new ArrayList<>();

    public Reiziger() {}

    public Reiziger(int id, String voorletters, String tussenvoegsel, String achternaam, Date geboortedatum) {
        this.id = id;
        this.voorletters = voorletters;
        this.tussenvoegsel = tussenvoegsel;
        this.achternaam = achternaam;
        this.geboortedatum = geboortedatum;
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public String getVoorletters() {
        return voorletters;
    }

    public void setVoorletters(String voorletters) {
        this.voorletters = voorletters;
    }

    public String getTussenvoegsel() {
        return tussenvoegsel;
    }

    public void setTussenvoegsel(String tussenvoegsel) {
        this.tussenvoegsel = tussenvoegsel;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public void setAchternaam(String achternaam) {
        this.achternaam = achternaam;
    }

    public Date getGeboortedatum() {
        return geboortedatum;
    }

    public void setGeboortedatum(Date geboortedatum) {
        this.geboortedatum = geboortedatum;
    }

    public Adres getAdres() {
        return adres;
    }

    public void setAdres(Adres adres) {
        this.adres = adres;
    }

    public List<OVChipkaart> getKaarten() {
        return kaarten;
    }

    public void setKaarten(List<OVChipkaart> kaarten) {
        if (kaarten != null) {
            this.kaarten = kaarten;
        }
    }

    public void addKaart(OVChipkaart kaart) {
        if (!kaarten.contains(kaart)) {
            kaarten.add(kaart);
        }
    }

    public String getNaam() {
        if (tussenvoegsel == null || tussenvoegsel.isBlank() || tussenvoegsel.isEmpty()) {
            return voorletters+". "+achternaam;
        }
        return voorletters+". "+tussenvoegsel+" "+achternaam;
    }

    @Override
    public String toString() {
        StringBuilder fullString = new StringBuilder("#" + id +
                ": " + getNaam() +
                " (" + geboortedatum + "), Adres " +
                adres+", OV chipkaarten: ");
        for (OVChipkaart kaart : kaarten) {
            fullString.append(kaart);
        }
        return fullString.toString();
    }
}
