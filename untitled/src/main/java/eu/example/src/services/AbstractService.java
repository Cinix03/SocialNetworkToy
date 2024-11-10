package eu.example.src.services;

import eu.example.src.domain.Entity;
import eu.example.src.repository.Repository;
import eu.example.src.validators.ValidationException;
import eu.example.src.validators.Validator;

import java.util.Optional;

public abstract class AbstractService<ID, E extends Entity<ID>> implements Service{
    protected Repository<ID, E> repo;
    protected Validator<E> validator;

    AbstractService(Repository<ID, E> repo, Validator<E> validator) {
        this.repo = repo;
        this.validator = validator;
    }

    abstract boolean isType(Object o);
    abstract boolean isTypeOfID(Object o);

    @Override
    public void add(Object entity) {
        if(!isType(entity))
            throw new ValidationException("Invalid type");
        try {
            validator.validate((E) entity);
            repo.save((E) entity);
        } catch (Exception e) {
            throw new ValidationException(e.getMessage());
        }

    }

    @Override
    public void delete(Object o) {
        if(!isType(o))
            throw new ValidationException("Invalid type");
        try {
            validator.validate((E) o);
            repo.delete(((E)o).getId());
        } catch (Exception e) {
            throw new ValidationException(e.getMessage());
        }
    }

    @Override
    public void update(Object entity) {
        if(!isType(entity))
            throw new ValidationException("Invalid type");
        try {
            validator.validate((E) entity);
            repo.update((E) entity);
        } catch (Exception e) {
            throw new ValidationException(e.getMessage());
        }
    }

    @Override
    public Optional<E> findOne(Object o) {
        if(!isTypeOfID(o))
            throw new ValidationException("Invalid type");
        System.out.println(1);
        return repo.findOne((ID) o);
    }

    @Override
    public Iterable findAll() {
        return repo.findAll();
    }

}
