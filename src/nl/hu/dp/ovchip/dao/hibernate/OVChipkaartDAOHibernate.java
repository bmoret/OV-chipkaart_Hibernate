package nl.hu.dp.ovchip.dao.hibernate;

import nl.hu.dp.ovchip.dao.interfaces.OVChipkaartDAO;
import nl.hu.dp.ovchip.domein.Adres;
import nl.hu.dp.ovchip.domein.OVChipkaart;
import nl.hu.dp.ovchip.domein.Reiziger;
import org.hibernate.Session;

import java.util.List;

public class OVChipkaartDAOHibernate implements OVChipkaartDAO {
    private Session session;

    public OVChipkaartDAOHibernate(Session session) {
        this.session = session;
    }

    @Override
    public boolean save(OVChipkaart kaart) {
        try {
            session.save(kaart);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean update(OVChipkaart kaart) {
        try {
            session.update(kaart);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(OVChipkaart kaart) {
        try {
            session.delete(kaart);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public OVChipkaart findByKaartNummer(int kaartNummer) {
        return (OVChipkaart) session.get(OVChipkaart.class, kaartNummer);
    }

    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) {
        return (List<OVChipkaart>) session.createQuery("FROM OVChipkaart WHERE reiziger_id = "+reiziger.getId()).list();

    }

    @Override
    public List<OVChipkaart> findAll() {
        return (List<OVChipkaart>) session.createQuery("FROM OVChipkaart").list();
    }
}
