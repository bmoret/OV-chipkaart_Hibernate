package nl.hu.dp.ovchip.dao.interfaces;

import nl.hu.dp.ovchip.domein.Product;
import java.util.List;

public interface ProductDAO {
    public boolean save (Product product);
    public boolean update (Product product);
    public boolean delete (Product product);
    public Product findByNummer (int productNummer) throws Exception;
    public List<Product> findAll ();
}
