package nl.hu.dp.ovchip.dao.hibernate;

import nl.hu.dp.ovchip.dao.interfaces.ReizigerDAO;
import nl.hu.dp.ovchip.domein.Reiziger;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class ReizigerDAOHibernate implements ReizigerDAO {
    private Session session;

    public ReizigerDAOHibernate(Session session) {
        this.session = session;
    }

    @Override
    public boolean save(Reiziger reiziger) throws SQLException {
        try {
            session.save(reiziger);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean update(Reiziger reiziger) throws SQLException {
        try {
            session.update(reiziger);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(Reiziger reiziger) {
        try {
            session.delete(reiziger);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public Reiziger findById(int id) throws Exception {
        return (Reiziger) session.get(Reiziger.class, id);
    }

    @Override
    public List<Reiziger> findByGbdatum(Date datum) {
        return session.createQuery("from Reiziger where geboortedatum = '"+ datum +"'").list();
    }

    @Override
    public List<Reiziger> findAll() {
        Query query = session.createQuery("from Reiziger");

        List<Reiziger> reizigers = query.list();
        return reizigers;
    }
}
