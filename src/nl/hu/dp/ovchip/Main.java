package nl.hu.dp.ovchip;

import nl.hu.dp.ovchip.dao.hibernate.AdresDAOHibernate;
import nl.hu.dp.ovchip.dao.hibernate.OVChipkaartDAOHibernate;
import nl.hu.dp.ovchip.dao.hibernate.ProductDAOHibernate;
import nl.hu.dp.ovchip.dao.hibernate.ReizigerDAOHibernate;
import nl.hu.dp.ovchip.dao.interfaces.AdresDAO;
import nl.hu.dp.ovchip.dao.interfaces.OVChipkaartDAO;
import nl.hu.dp.ovchip.dao.interfaces.ProductDAO;
import nl.hu.dp.ovchip.dao.interfaces.ReizigerDAO;
import nl.hu.dp.ovchip.domein.Adres;
import nl.hu.dp.ovchip.domein.OVChipkaart;
import nl.hu.dp.ovchip.domein.Product;
import nl.hu.dp.ovchip.domein.Reiziger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

/**
 * Testklasse - deze klasse test alle andere klassen in deze package.
 *
 * System.out.println() is alleen in deze klasse toegestaan (behalve voor exceptions).
 *
 * @author tijmen.muller@hu.nl
 */
public class Main {
    // Creëer een factory voor Hibernate sessions.
    private static final SessionFactory factory;

    static {
        try {
            // Create a Hibernate session factory
            factory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Retouneer een Hibernate session.
     *
     * @return Hibernate session
     * @throws HibernateException
     */
    private static Session getSession() throws HibernateException {
        return factory.openSession();
    }

    public static void main(String[] args) {
        Session session = getSession();

        testDAOHibernate(session);
    }

    /**
     * P6. Haal alle (geannoteerde) entiteiten uit de database.
     */
    private static void testFetchAll() {
        Session session = getSession();
        try {
            Metamodel metamodel = session.getSessionFactory().getMetamodel();
            for (EntityType<?> entityType : metamodel.getEntities()) {
                Query query = session.createQuery("from " + entityType.getName());

                System.out.println("[Test] Alle objecten van type " + entityType.getName() + " uit database:");
                for (Object o : query.list()) {
                    System.out.println("  " + o);
                }
                System.out.println();
            }
        } finally {
            session.close();
        }
    }

    private static void testDAOHibernate(Session session) {
        try {
            ReizigerDAOHibernate rdao = new ReizigerDAOHibernate(session);
            AdresDAOHibernate adao = new AdresDAOHibernate(session);
            OVChipkaartDAOHibernate odao = new OVChipkaartDAOHibernate(session);
            ProductDAOHibernate pdao = new ProductDAOHibernate(session);

            testReizigerDAO(rdao, session);
            testAdresDAO(adao, rdao, session);
            testOVChipkaartDAO(odao, rdao, session);
            testProductenDAO(pdao, session);
        } catch (Exception e) {
            System.out.println("test mislukt: "+e.getMessage());
        }

    }

    private static void testReizigerDAO(ReizigerDAO rdao, Session session) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");

        Transaction t = session.beginTransaction();
        rdao.save(sietske);
        t.commit();

        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        // Update een bestaande reiziger + vindt reiziger met Id
        try {
            System.out.print("[Test] Achtenaam is eerst " + rdao.findById(77).getAchternaam() + ". Achternaam is na ReizigerDAO.update ");
            sietske.setAchternaam("Jansen");

            t = session.beginTransaction();
            rdao.update(sietske);
            t.commit();
            System.out.println(rdao.findById(77).getAchternaam() + "\n");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // Delete reiziger
        reizigers = rdao.findAll();
        for (Reiziger reiziger : reizigers) {
            if (reiziger.getId() == 77) {
                System.out.print("[Test] Voor het verwijderen zit "+ reiziger.getAchternaam() + " in de database, na het verwijderen ");
            }
        }

        t = session.beginTransaction();
        rdao.delete(sietske);
        t.commit();

        reizigers = rdao.findAll();
        Boolean x = true;
        for (Reiziger reiziger : reizigers) {
            if (reiziger.getId() == 77) {
                System.out.println("nog steeds.\n");
                x = false;
            }
        }
        if (x) {
            System.out.println("niet meer.\n");
        }

        // Vindt reiziger met geboortedatum
        System.out.println("[Test] ReizigerDAO.findByGbdatum geeft de volgende reizigers met geboortendatum 03-12-2002:");
        for (Reiziger r : rdao.findByGbdatum(java.sql.Date.valueOf("2002-12-03"))) {
            System.out.println(r);
        }
    }

    private static void testAdresDAO(AdresDAO adao, ReizigerDAO rdao, Session session) throws SQLException {
        System.out.println("\n---------- Test AdresDAO -------------");

        // Aanmaken huiseigenaar
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));
        rdao.save(sietske);


        // Haal alle adressen op uit de database
        List<Adres> adressen = adao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende adressen:");
        for (Adres a : adressen) {
            System.out.println(a);
        }
        System.out.println();

        // Maak een nieuw adres aan en persisteer deze in de database
        Adres adres1 = new Adres(77, "1111 AB", "11", "Straat","Woonplaats",sietske);
        System.out.print("[Test] Eerst " + adressen.size() + " adressen, na ReizigerDAO.save() ");

        Transaction t = session.beginTransaction();
        adao.save(adres1);
        t.commit();

        adressen = adao.findAll();
        System.out.println(adressen.size() + " adressen\n");

        // Update een bestaand adres + vindt dit adres met reiziger
        try {
            System.out.print("[Test] Adres is eerst " + adao.findByReiziger(sietske) + ". Adres is na AdresDAO.update ");
            adres1.setHuisnummer("15");
            adres1.setPostcode("1111 AE");

            t = session.beginTransaction();
            adao.update(adres1);
            t.commit();

            System.out.println(adao.findByReiziger(sietske) + "\n");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // Delete adres
        adressen = adao.findAll();
        for (Adres adres : adressen) {
            if (adres.getAdres_id() == 77) {
                System.out.print("[Test] Voor het verwijderen zit "+ adres + " in de database, na het verwijderen ");
            }
        }

        t = session.beginTransaction();
        adao.delete(adres1);
        t.commit();

        adressen = adao.findAll();
        Boolean x = true;
        for (Adres adres : adressen) {
            if (adres.getAdres_id() == 77) {
                System.out.println("nog steeds.\n");
                x = false;
            }
        }
        if (x) {
            System.out.println("niet meer.\n");
        }
        rdao.delete(sietske);
    }

    private static void testOVChipkaartDAO(OVChipkaartDAO odao, ReizigerDAO rdao, Session session) throws Exception {
        System.out.println("\n---------- Test OVChipkaartDAO -------------");

        // Ophalen alle kaarten
        List<OVChipkaart> allKaarten = odao.findAll();
        System.out.println("[Test] OVChipkaartDAO.findAll() geeft de volgende kaarten:");
        for (OVChipkaart kaart : allKaarten) {
            System.out.println(kaart);
        }
        System.out.println();

        // Ophalen kaarten eigenaar
        Reiziger r1 = rdao.findById(2);

        // Haal alle kaarten van deze reiziger uit de database
        List<OVChipkaart> kaarten = odao.findByReiziger(r1);
        System.out.println("[Test] OVChipkaartDAO.findByReiziger() geeft de volgende kaarten:");
        for (OVChipkaart kaart : kaarten) {
            System.out.println(kaart);
        }
        System.out.println();

        // Maak een nieuwe kaart aan en persisteer deze in de database
        OVChipkaart k1 = new OVChipkaart(99999,20,2,java.sql.Date.valueOf("2022-09-10"),r1);
        System.out.print("[Test] Eerst " + kaarten.size() + " adressen, na ReizigerDAO.save() ");

        Transaction t = session.beginTransaction();
        odao.save(k1);
        t.commit();

        kaarten = odao.findByReiziger(r1);
        System.out.println(kaarten.size() + " adressen\n");

        // Update een bestaande kaart + vindt deze kaart met kaart nummer
        try {
            System.out.print("[Test] Saldo is eerst " + odao.findByKaartNummer(k1.getKaartNummer()).getSaldo() + ". Adres is na AdresDAO.update ");
            k1.setSaldo(5);

            t = session.beginTransaction();
            odao.update(k1);
            t.commit();

            System.out.println(odao.findByKaartNummer(k1.getKaartNummer()).getSaldo() + "\n");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // Delete kaart
        kaarten = odao.findByReiziger(r1);
        for (OVChipkaart kaart : kaarten) {
            if (kaart.getKaartNummer() == 99999) {
                System.out.print("[Test] Voor het verwijderen zit kaart "+ kaart.getKaartNummer() + " in de database, na het verwijderen ");
            }
        }

        t = session.beginTransaction();
        odao.delete(k1);
        t.commit();

        kaarten = odao.findByReiziger(r1);
        Boolean x = true;
        for (OVChipkaart kaart : kaarten) {
            if (kaart.getKaartNummer() == 99999) {
                System.out.println("nog steeds.\n");
                x = false;
            }
        }
        if (x) {
            System.out.println("niet meer.\n");
        }
    }

    private static void testProductenDAO(ProductDAO pdao, Session session) {
        System.out.println("\n---------- Test ProductDAO -------------");

        // Haal alle producten op uit de database
        List<Product> producten = pdao.findAll();
        System.out.println("[Test] ProductDAO.findAll() geeft de volgende producten:");
        for (Product p : producten) {
            System.out.println(p);
        }
        System.out.println();

        // Maak een nieuw product aan en persisteer deze in de database
        Product p1 = new Product(77, "testProduct", "beschrijving", 19.00);
        System.out.print("[Test] Eerst " + producten.size() + " reizigers, na ReizigerDAO.save() ");

        Transaction t = session.beginTransaction();
        pdao.save(p1);
        t.commit();

        producten = pdao.findAll();
        System.out.println(producten.size() + " reizigers\n");

        // Update een bestaand product + vindt product met nummer
        try {
            System.out.print("[Test] Prijs is eerst " + pdao.findByNummer(77).getPrijs() + ". Prijs is na ProductDAO.update ");

            p1.setPrijs(15.00);

            t = session.beginTransaction();
            pdao.update(p1);
            t.commit();

            System.out.println(pdao.findByNummer(77).getPrijs() + "\n");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // Delete product
        producten = pdao.findAll();
        for (Product product : producten) {
            if (product.getNummer() == 77) {
                System.out.print("[Test] Voor het verwijderen zit "+ product.getNaam() + " in de database, na het verwijderen ");
            }
        }

        t = session.beginTransaction();
        pdao.delete(p1);
        t.commit();

        producten = pdao.findAll();
        Boolean x = true;
        for (Product product : producten) {
            if (product.getNummer() == 77) {
                System.out.println("nog steeds.\n");
                x = false;
            }
        }
        if (x) {
            System.out.println("niet meer.\n");
        }
    }
}