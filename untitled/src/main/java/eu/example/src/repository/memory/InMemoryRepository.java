package eu.example.src.repository.memory;

import eu.example.src.domain.Entity;
import eu.example.src.repository.Repository;
import eu.example.src.validators.ValidationException;
import eu.example.src.validators.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID,E> {

    private Validator<E> validator;
    protected Map<ID,E> entities;
    protected Long currentId;

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities=new HashMap<ID,E>();
    }

    @Override
    public Optional<E> findOne(ID id) {
        Optional.ofNullable(id).orElseThrow(IllegalArgumentException::new);
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    /**
     *
     * @param entity
     *         entity must be not null
     * @return null- if the given entity is saved
     *         otherwise returns the entity (id already exists)
     * @throws ValidationException
     *            if the entity is not valid
     * @throws IllegalArgumentException
     *             if the given entity is null.     *
     */
    @Override
    public Optional<E> save(E entity) throws ValidationException {
        Optional.ofNullable(entity).orElseThrow(() -> new ValidationException("ENTITY CANNOT BE NULL"));
        validator.validate(entity);
        return Optional.ofNullable(entities.putIfAbsent(entity.getId(), entity));
    }

    @Override
    public Optional<E> delete(ID id) {
        Optional.ofNullable(id).orElseThrow(IllegalArgumentException::new);
        return Optional.ofNullable(entities.remove(id));
    }

    /**
     *
     * @param entity
     *          entity must not be null
     * @return null - if the entity is updated,
     *                otherwise  returns the entity  - (e.g id does not exist).
     * @throws IllegalArgumentException
     *             if the given entity is null.
     * @throws ValidationException
     *             if the entity is not valid.
     */

    @Override
    public Optional<E> update(E entity) {
        Optional.ofNullable(entity).orElseThrow(() -> new IllegalArgumentException("ENTITY CANNOT BE NULL"));
        validator.validate(entity);
        return Optional.ofNullable(entities.computeIfPresent(entity.getId(), (id, e) -> entity));
    }

    @Override
    public Optional<E> findByUsernameDB(String username) {
        return Optional.empty();
    }

    @Override
    public Iterable<E> findAllPaginated(int page, int size, int idCautat) {
        return null;
    }
}

