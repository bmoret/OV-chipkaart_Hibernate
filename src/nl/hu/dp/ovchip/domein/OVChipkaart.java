package nl.hu.dp.ovchip.domein;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ov_chipkaart")
public class OVChipkaart {
    @Id
    @Column(name = "kaart_nummer")
    private int kaartNummer;
    private int klasse;
    private double saldo;
    @Column(name = "geldig_tot")
    private Date geldigTot;
    @ManyToOne
    @JoinColumn(name = "reiziger_id", referencedColumnName = "reiziger_id")
    private Reiziger reiziger;
    @ManyToMany
    @JoinTable(
            name = "ov_chipkaart_product",
            joinColumns = @JoinColumn(name = "kaart_nummer", referencedColumnName = "kaart_nummer"),
            inverseJoinColumns = @JoinColumn(name = "product_nummer", referencedColumnName = "product_nummer")
    )
    private List<Product> producten = new ArrayList<>();

    public OVChipkaart() {}

    public OVChipkaart (int kaartNummer, double saldo, int klasse, Date geldigTot, Reiziger reiziger) throws Exception {
        this.kaartNummer = kaartNummer;
        this.saldo = saldo;
        if (klasse ==1 || klasse == 2) {
            this.klasse = klasse;
        } else {
            throw new Exception("Klasse moet gelijk zijn aan 1 of 2");
        }
        this.geldigTot = geldigTot;
        this.reiziger = reiziger;
    }

    public int getKaartNummer() {
        return kaartNummer;
    }

    public void setKaartNummer(int kaartNummer) {
        this.kaartNummer = kaartNummer;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public int getKlasse() {
        return klasse;
    }

    public void setKlasse(int klasse) {
        this.klasse = klasse;
    }

    public Date getGeldigTot() {
        return geldigTot;
    }

    public void setGeldigTot(Date geldigTot) {
        this.geldigTot = geldigTot;
    }

    public Reiziger getReiziger() {
        return reiziger;
    }

    public void setReiziger(Reiziger reiziger) {
        this.reiziger = reiziger;
    }

    public List<Product> getProducten() {
        return producten;
    }

    public void setProducten(List<Product> producten) {
        this.producten = producten;
    }

    public void addProduct(Product product){
        if (!producten.contains(product)) {
            producten.add(product);
        }
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder("{#" + kaartNummer + ", geldig tot: " + geldigTot + ", saldo: " + saldo + ", klasse: " + klasse + ", producten: ");
        for (Product product : producten) {
            string.append(product);
        }
        string.append("}");
        return string.toString();
    }
}
