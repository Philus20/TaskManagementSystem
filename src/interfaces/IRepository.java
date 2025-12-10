package interfaces;

public interface IRepository {
    void add(Object obj);
    Object[] getAll();
    Object getById(String id);
    void removeById(String id);
    void update(String id, Object obj);

}
