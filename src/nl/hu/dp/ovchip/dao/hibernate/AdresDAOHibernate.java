package nl.hu.dp.ovchip.dao.hibernate;

import nl.hu.dp.ovchip.dao.interfaces.AdresDAO;
import nl.hu.dp.ovchip.domein.Adres;
import nl.hu.dp.ovchip.domein.Reiziger;
import org.hibernate.Session;

import java.util.List;

public class AdresDAOHibernate implements AdresDAO {
    private Session session;

    public AdresDAOHibernate(Session session) {
        this.session = session;
    }

    @Override
    public boolean save(Adres adres) {
        try {
            session.save(adres);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean update(Adres adres) {
        try {
            session.update(adres);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(Adres adres) {
        try {
            session.delete(adres);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) throws Exception {
        return ((List<Adres>) session.createQuery("FROM Adres WHERE reiziger_id = "+reiziger.getId()).list()).get(0);
    }

    @Override
    public List<Adres> findAll() {
        return (List<Adres>) session.createQuery("FROM Adres").list();
    }
}
