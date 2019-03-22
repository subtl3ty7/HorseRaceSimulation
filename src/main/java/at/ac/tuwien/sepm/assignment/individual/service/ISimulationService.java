package at.ac.tuwien.sepm.assignment.individual.service;

import at.ac.tuwien.sepm.assignment.individual.entity.SimulationInput;
import at.ac.tuwien.sepm.assignment.individual.entity.SimulationResult;
import at.ac.tuwien.sepm.assignment.individual.exceptions.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.service.exceptions.ServiceException;

import java.util.List;

public interface ISimulationService {

    /**
     * Returns a simulation from its id.
     *
     * @param id is the id of the searched simulation
     * @return value is the SimulationResult
     * @throws ServiceException will be thrown if something goes wrong during the data processing.
     * @throws NotFoundException will be thrown if there is no simulations in the system.
     */

    SimulationResult getOneById( Integer id) throws ServiceException,NotFoundException;

    /**
     *Inserts a simulation into database and calculates the simulation result in this layer.
     * SimulationInput to SimulationResult conversion happens here.
     *
     * @param simulation is the simulation to be inserted.
     * @return is the result of the inserted simulation.
     * @throws ServiceException will be thrown if something goes wrong during the data processing.
     * @throws NotFoundException will be thrown if there is no simulations in the system.
     */
    SimulationResult insertSimulationToDB(SimulationInput simulation) throws ServiceException, NotFoundException;

    /**
     * Returns all the simulations in database.
     *
     * @return value is a list of simulations in database.
     * @throws ServiceException will be thrown if something goes wrong during the data processing.
     * @throws NotFoundException will be thrown if there is no simulations in the system.
     */
    List<SimulationResult> getAllSimulations() throws ServiceException, NotFoundException;

    /**
     * Returns all the simulations passing a desired filter in database.
     * @param name is the name pattern to filter out the simulations.
     * @return value is a list of filtered simulations.
     * @throws ServiceException will be thrown if something goes wrong during the data processing.
     * @throws NotFoundException will be thrown if there is no simulations in the system.
     */
    List<SimulationResult> getAllSimulationsFiltered(String name) throws ServiceException, NotFoundException;

    /**
     *
     * The main method to run simulation and calculate the results.
     * @param simulation
     * @return
     * @throws ServiceException
     * @throws NotFoundException
     */
    SimulationResult simulate(SimulationInput simulation) throws ServiceException,NotFoundException;

    /**
     * For calculating horse's average speed.
     * @param horseSpeed is the speed of the horse
     * @param jockeySkill is the skill of the jockey
     * @param luckFactor is the luck factor.
     * @return value is the calculated average speed.
     */
    Double calculateAvgSpeed(Double horseSpeed, Double jockeySkill, Double luckFactor);

    /**
     * For calculating jockey's skill.
     *
     * @param skill is the jockey's default skill value.
     * @return value is the calculated skill value.
     */
    Double calculateJockeySkill(Double skill);

    /**
     * For calculating horse's speed.
     * @param horseMin is the minimum speed of the horse.
     * @param horseMax is the maximum speed of the horse.
     * @param luckFactor is the luck factor.
     * @return value is the speed of the horse.
     */
    Double calculateSpeed (Double horseMin, Double horseMax, Double luckFactor);

    /**
     * Truncates a value and depending on condition, rounds it up or down
     * @param input is the value to be truncated and rounded.
     * @return value is the truncated and rounded value.
     */
    Double truncateAndRound(Double input);
}
