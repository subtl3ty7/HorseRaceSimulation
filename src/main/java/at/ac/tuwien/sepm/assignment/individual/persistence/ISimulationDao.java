package at.ac.tuwien.sepm.assignment.individual.persistence;

import at.ac.tuwien.sepm.assignment.individual.entity.SimulationResult;
import at.ac.tuwien.sepm.assignment.individual.exceptions.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.persistence.exceptions.PersistenceException;

import java.util.List;

public interface ISimulationDao {

    /**
     * Returns a simulation by its id.
     * @param id is the id of the searched simulation.
     * @return is the SimulationResult Object.
     * @throws PersistenceException will be thrown if something goes wrong during the database access.
     */
    SimulationResult getOneById (Integer id) throws PersistenceException;

    /**
     * Inserts a finished simulation with its results to database.
     * @param simulationResult  is the finished simulation to insert.
     * @return is the inserted simulation.
     * @throws PersistenceException will be thrown if something goes wrong during the database access.
     */
    SimulationResult insertSimulationResultToDB(SimulationResult simulationResult) throws PersistenceException;

    /**
     * Returns all the simulations in database.
     * @return is the all simulations.
     * @throws PersistenceException will be thrown if something goes wrong during the database access.
     * @throws NotFoundException will be thrown if database is empty.
     */
    List<SimulationResult> getAllSimulations() throws PersistenceException, NotFoundException;

    /**
     * Returns all the simulations corresponding to the name filter.
     * @param name is the string to filter the name
     * @return is the filtered  simulations.
     * @throws PersistenceException will be thrown if something goes wrong during the database access.
     * @throws NotFoundException will be thrown if database is empty or a Simulation could not be found with matching filter.
     */
    List<SimulationResult> getAllSimulationsFiltered(String name) throws PersistenceException, NotFoundException;
}
