package at.ac.tuwien.sepm.assignment.individual.persistence;

import at.ac.tuwien.sepm.assignment.individual.entity.SimulationResult;
import at.ac.tuwien.sepm.assignment.individual.persistence.exceptions.PersistenceException;

public interface ISimulationDao {

    SimulationResult insertSimulationResultToDB(SimulationResult simulationResult) throws PersistenceException;
}
