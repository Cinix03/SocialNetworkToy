package eu.example.src.repository.database;

import eu.example.src.domain.Utilizator;
import eu.example.src.validators.Validator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtilizatorDatabaseRepo extends AbstractDatabaseRepository<Long, Utilizator> {

    public UtilizatorDatabaseRepo(Validator<Utilizator> validator, String url, String username, String password) {
        super(validator, url, username, password);
    }


    @Override
    public Utilizator createEntity(ResultSet resultSet) throws SQLException {
        try {
            Long id = resultSet.getLong("id");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            Utilizator utilizator = new Utilizator(firstName, lastName);
            utilizator.setId(id);
            return utilizator;
        } catch (SQLException e) {
            throw new RuntimeException("Could not create entity from result set", e);
        }
    }

    @Override
    public void prepareStatementForEntity(Utilizator entity, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setObject(1, entity.getFirstName());
        preparedStatement.setObject(2, entity.getLastName());
    }

    @Override
    protected String getTableName() {
        return "users";
    }

    @Override
    protected String getColumns() {
        return "first_name, last_name";
    }

    @Override
    protected String getPlaceholder() {
        return "?, ?";
    }

    @Override
    protected String getUpdateSetClause() {
        return "first_name = ?, last_name = ?";
    }

    @Override
    protected int getUpdateParameterCount() {
        return 2; // first_name, last_name
    }
}
