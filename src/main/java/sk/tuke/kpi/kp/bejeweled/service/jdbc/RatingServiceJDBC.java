package sk.tuke.kpi.kp.bejeweled.service.jdbc;

import sk.tuke.kpi.kp.bejeweled.entity.Rating;
import sk.tuke.kpi.kp.bejeweled.service.RatingException;
import sk.tuke.kpi.kp.bejeweled.service.RatingService;

import java.sql.*;

public class RatingServiceJDBC implements RatingService {
    public static final String URL = "jdbc:postgresql://localhost:5432/gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "postgres";
    public static final String AVERAGE = "SELECT ROUND(AVG(rating)) FROM rating WHERE game = ?";
    public static final String FIND = "SELECT rating, ratedOn FROM rating WHERE game = ? AND player = ?";
    public static final String UPDATE = "UPDATE rating SET rating = ?, ratedOn = ? WHERE game = ? AND player = ?";
    public static final String DELETE = "DELETE FROM rating";
    public static final String INSERT = "INSERT INTO rating (game, player, rating, ratedOn) VALUES (?, ?, ?, ?)";


    @Override
    public void setRating(Rating rating) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement findStatement = connection.prepareStatement(FIND);
             PreparedStatement updateStatement = connection.prepareStatement(UPDATE);
             PreparedStatement insertStatement = connection.prepareStatement(INSERT)
        ) {
            findStatement.setString(1, rating.getGame());
            findStatement.setString(2, rating.getPlayer());

            try (ResultSet rs = findStatement.executeQuery()) {
                if(rs.next()) {
                    updateStatement.setInt(1, rating.getRating());
                    updateStatement.setTimestamp(2, new Timestamp(rating.getRatedOn().getTime()));
                    updateStatement.setString(3, rating.getGame());
                    updateStatement.setString(4, rating.getPlayer());
                    updateStatement.executeUpdate();
                }
                else {
                    insertStatement.setString(1, rating.getGame());
                    insertStatement.setString(2, rating.getPlayer());
                    insertStatement.setInt(3, rating.getRating());
                    insertStatement.setTimestamp(4, new Timestamp(rating.getRatedOn().getTime()));
                    insertStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RatingException("Problem setting rating", e);
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(AVERAGE)
        ) {
            statement.setString(1, game);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        } catch (SQLException e) {
            throw new RatingException("Problem getting average rating", e);
        }
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(FIND)
        ) {
            statement.setString(1, game);
            statement.setString(2, player);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        } catch (SQLException e) {
            throw new RatingException("Problem getting rating", e);
        }
    }

    @Override
    public void reset() throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate(DELETE);
        } catch (SQLException e) {
            throw new RatingException("Problem deleting rating", e);
        }
    }
}
