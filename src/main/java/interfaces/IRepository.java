package interfaces;

import java.util.List;

/**
 * Repository interface following Interface Segregation Principle (ISP)
 * and Dependency Inversion Principle (DIP)
 */
public interface IRepository<T> {
    void add(T item, String index);
    List<T> getAll();
    T getById(String id);
    void removeById(String id);
    void update(String id, T item);
}
