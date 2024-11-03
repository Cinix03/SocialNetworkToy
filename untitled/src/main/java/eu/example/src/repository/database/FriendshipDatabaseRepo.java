package eu.example.src.repository.database;

import eu.example.src.domain.Friendship;
import eu.example.src.validators.Validator;
import eu.example.src.domain.Tuple;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FriendshipDatabaseRepo extends AbstractDatabaseRepository<Tuple<Long, Long>, Friendship> {
    public FriendshipDatabaseRepo(Validator<Friendship> validator, String url, String username, String password) {
        super(validator, url, username, password);
    }


    @Override
    public Friendship createEntity(ResultSet resultSet) throws SQLException {
        try{
            Long id1 = resultSet.getLong("id1");
            Long id2 = resultSet.getLong("id2");
            Friendship friendship = new Friendship(id1, id2);
            return friendship;

        }catch (SQLException e){
            throw new RuntimeException("Could not create entity from result set", e);
        }
    }

    @Override
    public PreparedStatement prepareStatementForEntity(Friendship entity, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setObject(1, entity.getId().getFirst());
        preparedStatement.setObject(2, entity.getId().getSecond());
        return preparedStatement;
    }

    @Override
    protected String getTableName() {
        return "friendships";
    }

    @Override
    protected String getColumns() {
        return "id1, id2";
    }

    @Override
    protected String getPlaceholder() {
        return "?, ?";
    }

    @Override
    protected String getUpdateSetClause() {
        return "id1 = ?, id2 = ?";
    }

    @Override
    protected int getUpdateParameterCount() {
        return 2;
    }
}
