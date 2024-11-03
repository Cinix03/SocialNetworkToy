package eu.example.src.services;

public interface Service<ID, E> {
    void add(E entity);
    void delete(ID id);
    void update(E entity);
    E findOne(ID id);
    Iterable<E> findAll();
}
