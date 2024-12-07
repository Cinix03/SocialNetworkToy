package eu.example.src.repository.database;

import eu.example.src.domain.Friendship;
import eu.example.src.validators.Validator;
import eu.example.src.domain.Tuple;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Optional;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatterBuilder;

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

            // Preluăm data ca `Timestamp` direct din ResultSet
            Timestamp timestamp = resultSet.getTimestamp("data");
            Long sender = resultSet.getLong("sender");
            Long receiver = resultSet.getLong("receiver");

            // Verifică dacă timestamp-ul este null
            if (timestamp == null) {
                throw new SQLException("Data prieteniei este null în tabelul friendships");
            }

            // Convertim `Timestamp` în `LocalDateTime`
            LocalDateTime dateTime = timestamp.toLocalDateTime();

            Friendship friendship = new Friendship(id1, id2);
            friendship.setStatus(status);
            friendship.setSender(sender);
            friendship.setReceiver(receiver);
            friendship.setDate(dateTime);
            return friendship;

        } catch (SQLException e) {
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
    public ArrayList<Friendship> findAllPaginated(int pageNumber, int pageSize, int idCautat) {
        if (pageNumber < 1 || pageSize < 1) {
            throw new IllegalArgumentException("Page number and page size must be greater than zero.");
        }

        try {
            int offset = (pageNumber - 1) * pageSize;
            // Adăugăm condiția WHERE pentru a filtra după id1 sau id2
            String sql = "SELECT * FROM " + getTableName() +
                    " WHERE status = 'accepted' AND (id1 = ? OR id2 = ?) LIMIT ? OFFSET ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, idCautat);
            preparedStatement.setInt(2, idCautat);
            preparedStatement.setInt(3, pageSize);
            preparedStatement.setInt(4, offset);

            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Friendship> entities = new ArrayList<>();
            int cnt = 0;
            while (resultSet.next()) {
                entities.add(createEntity(resultSet));
                System.out.println(cnt);
                cnt++;
            }
            System.out.println(cnt);
            return entities;
        } catch (SQLException e) {
            throw new RuntimeException("Could not find all entities paginated", e);
        }
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
