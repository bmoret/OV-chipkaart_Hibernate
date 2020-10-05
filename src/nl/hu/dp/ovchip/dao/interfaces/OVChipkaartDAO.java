package nl.hu.dp.ovchip.dao.interfaces;

import nl.hu.dp.ovchip.domein.OVChipkaart;
import nl.hu.dp.ovchip.domein.Reiziger;

import java.util.List;

public interface OVChipkaartDAO {
    public boolean save (OVChipkaart kaart);
    public boolean update (OVChipkaart kaart);
    public boolean delete (OVChipkaart kaart);
    public OVChipkaart findByKaartNummer (int kaartNummer);
    public List<OVChipkaart> findByReiziger(Reiziger reiziger);
    public List<OVChipkaart> findAll();

}
