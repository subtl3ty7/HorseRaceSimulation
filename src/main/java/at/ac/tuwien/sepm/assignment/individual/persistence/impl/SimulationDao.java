package at.ac.tuwien.sepm.assignment.individual.persistence.impl;


import at.ac.tuwien.sepm.assignment.individual.entity.HorseJockeyCombination;
import at.ac.tuwien.sepm.assignment.individual.entity.SimulationResult;
import at.ac.tuwien.sepm.assignment.individual.persistence.ISimulationDao;
import at.ac.tuwien.sepm.assignment.individual.persistence.exceptions.PersistenceException;
import at.ac.tuwien.sepm.assignment.individual.persistence.util.DBConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Repository
public class SimulationDao implements ISimulationDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimulationDao.class);
    private final DBConnectionManager dbConnectionManager;

    @Autowired
    public SimulationDao(DBConnectionManager dbConnectionManager) {
        this.dbConnectionManager = dbConnectionManager;
    }


    @Override
    public SimulationResult insertSimulationResultToDB (SimulationResult simulationResult) throws PersistenceException {

        String sql = "INSERT INTO Simulation (id,name,created,horseName,jockeyName,avgSpeed,horseSpeed,skill,luckFactor,rank) " +
            "VALUES (?,?,?,?,?,?,?,?,?,?)";

        String sql2 = "SELECT COUNT(*) AS RowCount FROM Simulation";
        String sql3 = "SELECT TOP 1 * FROM Simulation \n" +
            "ORDER BY Id DESC";
        int id = 1;
        boolean databaseEmpty = true;
        try {

            PreparedStatement statementCheck = dbConnectionManager.getConnection().prepareStatement(sql2);
            ResultSet result;
            result = statementCheck.executeQuery();
            while(result.next()){
                if(result.getInt(1) != 0){
                    databaseEmpty = false;
                }
            }
            statementCheck.close();

            if(databaseEmpty == false){
                PreparedStatement statementIdCheck = dbConnectionManager.getConnection().prepareStatement(sql3);
                result = statementIdCheck.executeQuery();
                while(result.next()){
                    id = result.getInt(1);
                }
            }

            for (HorseJockeyCombination hj : simulationResult.getHorseJockeyCombinations()) {
                PreparedStatement statement = dbConnectionManager.getConnection().prepareStatement(sql);
                statement.setInt(1,id+1);
                statement.setString(2,simulationResult.getName());
                statement.setTimestamp(3,Timestamp.valueOf(simulationResult.getCreated()));
                statement.setString(4,hj.getHorseName());
                statement.setString(5,hj.getJockeyName());
                statement.setDouble(6, hj.getAvgSpeed());
                statement.setDouble(7, hj.getHorseSpeed());
                statement.setDouble(8,hj.getSkill());
                statement.setDouble(9,hj.getLuckFactor());
                statement.setInt(10,hj.getRank());

                statement.executeUpdate();
                statement.close();
            }





        } catch(SQLException e){

        }

        simulationResult.setId(id);
        return simulationResult;
    }

}
