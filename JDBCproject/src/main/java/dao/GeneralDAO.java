package dao;

import java.util.List;

public interface GeneralDAO<T> {

    List<T> getAll();

    T getById(int id);

    void add(T object);

    void update(T object);

    void delete(T object);

}
