package eu.example.src.repository.file;

import eu.example.src.domain.Entity;
import eu.example.src.repository.memory.InMemoryRepository;
import eu.example.src.validators.ValidationException;
import eu.example.src.validators.Validator;

import java.io.*;
import java.util.Optional;

public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {
    private String filename;

    public AbstractFileRepository(Validator<E> validator, String fileName) {
        super(validator);
        filename = fileName;
        // loadData();
    }

    public void initialize() {
        loadData();
    }

    public abstract E createEntity(String line);

    public abstract String saveEntity(E entity);

    @Override
    public Optional<E> save(E entity) {
        if (entity.getId() == null)
            entity.setId((ID) currentId++);
        Optional<E> e = super.save(entity);
        Optional.ofNullable(e).ifPresentOrElse(
                ent -> {
                    writeToFile();
                    System.out.println("Saved");
                },
                () -> {
                    throw new ValidationException("Entity already exists");
                }
        );
        return e;
    }

    private void writeToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            entities.values().forEach(entity -> {
                String ent = saveEntity(entity);
                try {
                    writer.write(ent);
                    writer.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadData() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals(""))
                    continue;
                E entity = createEntity(line);
                super.save(entity);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<E> findOne(ID id) {
        return super.findOne(id);
    }

    @Override
    public Iterable<E> findAll() {
        return super.findAll();
    }

    @Override
    public Optional<E> delete(ID id) {
        Optional<E> entity = super.delete(id);
        entity.ifPresentOrElse(
                e -> {
                    writeToFile();
                    System.out.println("Deleted");
                },
                () -> {
                    throw new ValidationException("Entity does not exist");
                }
        );
        return entity;
    }

    @Override
    public Optional<E> update(E entity) {
        Optional<E> entity2 = super.update(entity);
        entity2.ifPresentOrElse(
                e -> {
                    writeToFile();
                    System.out.println("Updated");
                },
                () -> {
                    throw new ValidationException("Entity does not exist");
                }
        );
        return entity2;
    }
}