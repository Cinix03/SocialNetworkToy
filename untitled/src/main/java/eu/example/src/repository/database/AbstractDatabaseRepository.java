package eu.example.src.repository.database;

import eu.example.src.domain.*;
import eu.example.src.repository.memory.InMemoryRepository;
import eu.example.src.validators.ValidationException;
import eu.example.src.validators.Validator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDatabaseRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID, E> {
    private final String url;
    private final String username;
    private final String password;
    protected Connection connection;

    public AbstractDatabaseRepository(Validator<E> validator, String url, String username, String password) {
        super(validator);
        this.url = url;
        this.username = username;
        this.password = password;
        initialize();
    }

    private void initialize() {
        try {
            connection = DriverManager.getConnection(url, username, password);
            loadData();
        } catch (SQLException e) {
            throw new RuntimeException("Could not connect to the database", e);
        }
    }



    private void loadData() {
        try {
            String sql = "SELECT * FROM " + getTableName();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                E entity = createEntity(resultSet);
                super.save(entity);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not load data from the database", e);
        }
    }

    public abstract String getClassType();

    public abstract E createEntity(ResultSet resultSet) throws SQLException;

    public abstract void prepareStatementForEntity(E entity, PreparedStatement preparedStatement) throws SQLException;

    @Override
    public Optional<E> save(E entity) {
        try {
            String sql = "INSERT INTO " + getTableName() + " (" + getColumns() + ") VALUES (" + getPlaceholder() + ")";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            System.out.println(sql);
            if(getClassType().equals("Utilizator"))
            {
                Utilizator u1 = (Utilizator) entity;
                System.out.println(u1.getProfilePicturePath());
            }

            //System.out.println(entity.get);
            prepareStatementForEntity(entity, preparedStatement);
            System.out.println(sql);
            preparedStatement.executeUpdate();
            return super.save(entity);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Could not save entity", e);
        }
    }

    @Override
    public Optional<E> findOne(ID id) {
        try {
            System.out.println(2);
            if(getClassType() == "Friendship"){
                System.out.println(3);
                String sql = "SELECT * FROM " + getTableName() + " WHERE id1 = ? AND id2 = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                Tuple<Long, Long> friendship = (Tuple) id;
                preparedStatement.setObject(1, friendship.getFirst());
                preparedStatement.setObject(2, friendship.getSecond());
                ResultSet resultSet = preparedStatement.executeQuery();
                //returneaza true daca exista un rand in result set
                if (resultSet.next()) {
                    return Optional.of(createEntity(resultSet));
                }
            }
            else {
                String sql = "SELECT * FROM " + getTableName() + " WHERE id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setObject(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                //returneaza true daca exista un rand in result set
                if (resultSet.next()) {
                    return Optional.of(createEntity(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not find entity", e);
        }
        //ca sa returneze un Optional gol daca nu a gasit
        return super.findOne(id);
    }

    @Override
    public Iterable<E> findAll() {
        // Implementation for finding all entities in the database
        try {
            String sql = "SELECT * FROM " + getTableName();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<E> entities = new ArrayList<>();
            while (resultSet.next()) {
                entities.add(createEntity(resultSet));
            }
            return entities;
        } catch (SQLException e) {
            throw new RuntimeException("Could not find all entities", e);
        }
    }

    @Override
    public Optional<E> delete(ID id) {
        Optional<E> entity = super.delete(id);
        if (entity.isPresent()) {
            try {
                if(getClassType() == "Friendship"){
                    String sql = "DELETE FROM " + getTableName() + " WHERE id1 = ? AND id2 = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    Friendship friendship = (Friendship) entity.get();
                    preparedStatement.setObject(1, friendship.getId().getFirst());
                    preparedStatement.setObject(2, friendship.getId().getSecond());
                    preparedStatement.executeUpdate();
                    return entity;
                }
                else {
                    String sql = "DELETE FROM " + getTableName() + " WHERE id = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setObject(1, id);
                    preparedStatement.executeUpdate();
                    return entity;
                }
            } catch (SQLException e) {
                throw new RuntimeException("Could not delete entity", e);
            }
        } else {
            throw new ValidationException("Entity does not exist");
        }
    }

    @Override
    public Optional<E> update(E entity) {
        Optional<E> existingEntity = super.update(entity);
        if (existingEntity.isPresent()) {
            try {
                if(getClassType().equals("Utilizator")) {
                    String sql = "UPDATE " + getTableName() + " SET " + getUpdateSetClause() + " WHERE id = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    prepareStatementForEntity(entity, preparedStatement);
                    preparedStatement.setObject(getUpdateParameterCount() + 1, entity.getId());
                    preparedStatement.executeUpdate();
                }
                return existingEntity;
            } catch (SQLException e) {
                throw new RuntimeException("Could not update entity", e);
            }
        } else {
            throw new ValidationException("Entity does not exist");
        }
    }


    protected abstract String getTableName();

    protected abstract String getColumns();

    protected abstract String getPlaceholder();

    protected abstract String getUpdateSetClause();

    protected abstract int getUpdateParameterCount();

    protected void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not close connection", e);
        }
    }

    public Iterable<E> findAllPaginated(int pageNumber, int pageSize, int idCautat) {
        return null;
    }
}
