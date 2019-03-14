package at.ac.tuwien.sepm.assignment.individual.persistence;

import at.ac.tuwien.sepm.assignment.individual.entity.SimulationResult;
import at.ac.tuwien.sepm.assignment.individual.exceptions.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.persistence.exceptions.PersistenceException;

import java.util.List;

public interface ISimulationDao {
    SimulationResult getOneById (Integer id) throws PersistenceException;
    SimulationResult insertSimulationResultToDB(SimulationResult simulationResult) throws PersistenceException;
    List<SimulationResult> getAllSimulations() throws PersistenceException, NotFoundException;
    List<SimulationResult> getAllSimulationsFiltered(String name) throws PersistenceException, NotFoundException;
}
