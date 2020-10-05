package nl.hu.dp.ovchip.dao.interfaces;

import nl.hu.dp.ovchip.domein.Reiziger;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public interface ReizigerDAO {
    public boolean save(Reiziger reiziger) throws SQLException;
    public boolean update(Reiziger reiziger) throws SQLException;
    public boolean delete(Reiziger reiziger);
    public Reiziger findById(int id) throws Exception;
    public List<Reiziger> findByGbdatum(Date datum);
    public List<Reiziger> findAll();
}
