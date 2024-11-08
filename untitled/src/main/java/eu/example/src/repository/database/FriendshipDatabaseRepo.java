package eu.example.src.repository.database;

import eu.example.src.domain.Friendship;
import eu.example.src.validators.Validator;
import eu.example.src.domain.Tuple;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.sql.Timestamp;

public class FriendshipDatabaseRepo extends AbstractDatabaseRepository<Tuple<Long, Long>, Friendship> {
    public FriendshipDatabaseRepo(Validator<Friendship> validator, String url, String username, String password) {
        super(validator, url, username, password);
    }


    @Override
    public Friendship createEntity(ResultSet resultSet) throws SQLException {
        try {
            Long id1 = resultSet.getLong("id1");
            Long id2 = resultSet.getLong("id2");

            String status = resultSet.getString("status");

            // Preluarea datei ca String din baza de date
            String dateString = resultSet.getString("data");

            Long sender = resultSet.getLong("sender");
            Long receiver = resultSet.getLong("receiver");

            // Verifică dacă data este validă
            if (dateString == null || dateString.isEmpty()) {
                throw new SQLException("Data prieteniei este null sau goală în tabelul friendships");
            }

            // Încearcă să parsezi string-ul într-un LocalDateTime
            LocalDateTime dateTime = null;
            try {
                // Folosește formatul cu milisecunde (până la 6 caractere pentru microsecunde)
                dateTime = LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
            } catch (Exception e) {
                throw new SQLException("Formatul datei nu este valid: " + dateString, e);
            }

            Friendship friendship = new Friendship(id1, id2);
            friendship.setStatus(status);
            friendship.setSender(sender);
            friendship.setReceiver(receiver);
            friendship.setDate(dateTime);
            return friendship;

        } catch (SQLException e) {
            // Mesaj mai detaliat pentru depanare
            throw new SQLException("Eroare la crearea entității Friendship din ResultSet", e);
        }
    }



    @Override
    public void prepareStatementForEntity(Friendship entity, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setObject(1, entity.getId().getFirst());
        preparedStatement.setObject(2, entity.getId().getSecond());
        preparedStatement.setObject(3, entity.getStatus());

        // Convertim LocalDateTime în Timestamp
        LocalDateTime date = entity.getDate();
        Timestamp timestamp = Timestamp.valueOf(date);
        preparedStatement.setTimestamp(4, timestamp);
        preparedStatement.setObject(5, entity.getSender());
        preparedStatement.setObject(6, entity.getReceiver());
    }

    @Override
    public String getClassType() {
        return "Friendship";
    }

    @Override
    protected String getTableName() {
        return "friendships";
    }

    @Override
    protected String getColumns() {
        return "id1, id2, status, data, sender, receiver";
    }

    @Override
    protected String getPlaceholder() {
        return "?, ?, ?, ?, ?, ?";
    }

    @Override
    protected String getUpdateSetClause() {
        return "id1 = ?, id2 = ?, status = ?, data = ?, sender = ?, receiver = ?";
    }

    @Override
    protected int getUpdateParameterCount() {
        return 6;
    }

}
