package at.ac.tuwien.sepm.assignment.individual.persistence.impl;

import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import at.ac.tuwien.sepm.assignment.individual.exceptions.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.persistence.IHorseDao;
import at.ac.tuwien.sepm.assignment.individual.persistence.exceptions.PersistenceException;
import at.ac.tuwien.sepm.assignment.individual.persistence.util.DBConnectionManager;
import at.ac.tuwien.sepm.assignment.individual.rest.dto.HorseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;


@Repository
public class HorseDao implements IHorseDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(HorseDao.class);
    private final DBConnectionManager dbConnectionManager;

    @Autowired
    public HorseDao(DBConnectionManager dbConnectionManager) {
        this.dbConnectionManager = dbConnectionManager;
    }

    private static Horse dbResultToHorseDto(ResultSet result) throws SQLException {
        return new Horse(
            result.getInt("id"),
            result.getString("name"),
            result.getString("breed"),
            result.getDouble("min_speed"),
            result.getDouble("max_speed"),
            result.getTimestamp("created").toLocalDateTime(),
            result.getTimestamp("updated").toLocalDateTime());
    }


    @Override
    public Horse findOneById(Integer id) throws PersistenceException, NotFoundException {
        LOGGER.info("Get horse with id " + id);
        String sql = "SELECT * FROM Horse WHERE id=?";
        Horse horse = null;
        try {
            PreparedStatement statement = dbConnectionManager.getConnection().prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                horse = dbResultToHorseDto(result);
            }
        } catch (SQLException e) {
            LOGGER.error("Problem while executing SQL select statement for reading horse with id " + id, e);
            throw new PersistenceException("Could not read horses with id " + id, e);
        }
        if (horse != null) {
            return horse;
        } else {
            throw new NotFoundException("Could not find horse with id " + id);
        }
    }





    @Override
    public Horse insertHorseToDB(Horse horse) throws PersistenceException {
        LOGGER.info("Insert horse: " + horse.toString());
        String sql = "INSERT INTO Horse (name,breed,min_speed,max_speed,created,updated) VALUES (?,?,?,?,?,?)";
        String responseSQL = "SELECT TOP 1 * FROM Horse \n" +
                             "ORDER BY Id DESC";
        try {
            PreparedStatement statement = dbConnectionManager.getConnection().prepareStatement(sql);
            statement.setString(1,horse.getName());
            statement.setString(2,horse.getBreed());
            statement.setDouble(3,horse.getMinSpeed());
            statement.setDouble(4,horse.getMaxSpeed());
            statement.setTimestamp(5, Timestamp.valueOf(horse.getCreated()));
            statement.setTimestamp(6, Timestamp.valueOf(horse.getUpdated()));

            statement.executeUpdate();
            statement.close();


            PreparedStatement statement1 = dbConnectionManager.getConnection().prepareStatement(responseSQL);
            Horse horseResponse = null;
            ResultSet result = statement1.executeQuery();
            while (result.next()) {
                horseResponse = dbResultToHorseDto(result);
            }
            statement1.close();
            LOGGER.info("Horse with id " + horseResponse.getId() + " is successfully inserted.");

            return horseResponse;
        } catch (SQLException e) {
            LOGGER.error("Problem while executing SQL select statement for inserting horse: " + horse.toString(), e);
            throw new PersistenceException("Could not insert horse: " + horse.toString(), e);
        }



    }


}
