package eu.example.src.repository.database;

import eu.example.src.domain.Entity;
import eu.example.src.domain.Friendship;
import eu.example.src.domain.Messages;
import eu.example.src.validators.Validator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class MessagesDatabaseRepo extends AbstractDatabaseRepository<Long, Messages> {

    public MessagesDatabaseRepo(Validator validator, String url, String username, String password) {
        super(validator, url, username, password);
    }

    @Override
    public String getClassType() {
        return "Messages";
    }

    @Override
    public Messages createEntity(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        Long from = resultSet.getLong("sender");
        Long to = resultSet.getLong("receiver");
        String message = resultSet.getString("content");
        Timestamp timestamp = resultSet.getTimestamp("date");
        Messages messages = new Messages(from, to, message, timestamp);
        messages.setId(id);

        return messages;
    }

    @Override
    public void prepareStatementForEntity(Messages entity, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setLong(1, entity.getFrom());
        preparedStatement.setLong(4, entity.getTo());
        preparedStatement.setString(2, entity.getMessage());
        preparedStatement.setTimestamp(3, entity.getDate());
    }


    @Override
    protected String getTableName() {
        return "messages";
    }

    @Override
    protected String getColumns() {
        return "sender, content, date, receiver";
    }

    @Override
    protected String getPlaceholder() {
        return "?, ?, ?, ?";
    }

    @Override
    protected String getUpdateSetClause() {
        return "";
    }

    @Override
    protected int getUpdateParameterCount() {
        return 4;
    }
}
