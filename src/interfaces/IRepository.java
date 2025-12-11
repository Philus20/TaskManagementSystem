package interfaces;

/**
 * Repository interface following Interface Segregation Principle (ISP)
 * and Dependency Inversion Principle (DIP)
 */
public interface IRepository<T> {
    void add(T item, int index);
    T[] getAll();
    T getById(int id);
    void removeById(int id);
    void update(int id, T item);
}
