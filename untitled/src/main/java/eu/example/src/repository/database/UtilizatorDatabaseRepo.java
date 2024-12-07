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
            String usernameUser = resultSet.getString("username");
            String passwordUser = resultSet.getString("password");
            String profilePicturePath = resultSet.getString("profileimagepath");
            Utilizator utilizator = new Utilizator(firstName, lastName, usernameUser, passwordUser);
            if(profilePicturePath == null)
                profilePicturePath = "/Users/vasilegeorge/Desktop/SocialNetworkToy/untitled/src/main/resources/eu/example/fxml/default-profile-picture-avatar-user-avatar-icon-person-icon-head-icon-profile-picture-icons-default-anonymous-user-male-and-female-businessman-photo-placeholder-social-network-avatar-portrait-free-vector.jpg";

            utilizator.setProfilePicturePath(profilePicturePath);
            utilizator.setId(id);
            return utilizator;
        } catch (SQLException e) {
            throw new RuntimeException("Could not create entity from result set", e);
        }
    }

    @Override
    public String getClassType() {
        return "Utilizator";
    }

    @Override
    public void prepareStatementForEntity(Utilizator entity, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setObject(1, entity.getFirstName());
        preparedStatement.setObject(2, entity.getLastName());
        preparedStatement.setObject(3, entity.getUsername());
        preparedStatement.setObject(4, entity.getPassword());
        preparedStatement.setObject(5, entity.getProfilePicturePath());
    }

    @Override
    protected String getTableName() {
        return "users";
    }

    @Override
    protected String getColumns() {
        return "first_name, last_name, username, password, profileimagepath";
    }

    @Override
    protected String getPlaceholder() {
        return "?, ?, ?, ?, ?";
    }

    @Override
    protected String getUpdateSetClause() {
        return "first_name = ?, last_name = ?, username = ?, password = ?, profileimagepath = ?";
    }

    @Override
    protected int getUpdateParameterCount() {
        return 5; // first_name, last_name
    }

    public void setProfilePicturePath(String path, Long id){
        try {
            String sql = "UPDATE " + getTableName() + " SET profileimagepath = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setObject(1, path);
            preparedStatement.setObject(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Could not update profile picture path", e);
        }
    }
}
