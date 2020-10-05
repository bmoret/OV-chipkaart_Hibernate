package nl.hu.dp.ovchip.dao.hibernate;

import nl.hu.dp.ovchip.dao.interfaces.ProductDAO;
import nl.hu.dp.ovchip.domein.Product;
import org.hibernate.Session;

import java.util.List;

public class ProductDAOHibernate implements ProductDAO {
    private Session session;

    public ProductDAOHibernate(Session session) {
        this.session = session;
    }

    @Override
    public boolean save(Product product) {
        try {
            session.save(product);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean update(Product product) {
        try {
            session.update(product);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean delete(Product product) {
        try {
            session.delete(product);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    @Override
    public Product findByNummer(int productNummer) throws Exception {
        return (Product) session.get(Product.class, productNummer);
    }

    @Override
    public List<Product> findAll() {
        return (List<Product>) session.createQuery("FROM Product").list();
    }
}
