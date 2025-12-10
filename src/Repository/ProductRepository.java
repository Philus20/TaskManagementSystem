package Repository;

import interfaces.IRepository;
import models.Project;

public class ProductRepository implements IRepository {

    public ProductRepository(int capacity) {
        if (capacity <= 0) capacity = 10;
    }
    @Override
    public void add(Object obj) {

    }

    @Override
    public Object[] getAll() {
        return new Object[0];
    }

    @Override
    public Object getById(String id) {
        return null;
    }

    @Override
    public void removeById(String id) {

    }

    @Override
    public void update(String id, Object obj) {

    }
}
