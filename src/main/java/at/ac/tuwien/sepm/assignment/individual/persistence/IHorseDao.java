package at.ac.tuwien.sepm.assignment.individual.persistence;

import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import at.ac.tuwien.sepm.assignment.individual.exceptions.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.persistence.exceptions.PersistenceException;
import at.ac.tuwien.sepm.assignment.individual.rest.dto.HorseDto;

public interface IHorseDao {

    /**
     * Gets an id, searches for the horse with the corresponding id and returns it if exists.
     *
     * @param id of the horse to find.
     * @return the horse with the specified id.
     * @throws PersistenceException will be thrown if something goes wrong during the database access.
     * @throws NotFoundException    will be thrown if the horse could not be found in the database.
     */
    Horse findOneById(Integer id) throws PersistenceException, NotFoundException;


    /**
     * Gets a Horse Entity and inserts it in Database.
     *
     * @param horse to be inserted in Database.
     * @return the inserted horse with its new id and created/updated dates.
     * @throws PersistenceException will be thrown if something goes wrong during the database access.
     */
    Horse insertHorseToDB(Horse horse) throws PersistenceException;

    /**
     * Gets an id and a Horse Entity to change the update the Horse with id in Database with the new Horse Entity.
     *
     * @param id of the horse to be changed.
     * @param horse with the new values.
     * @return the horse with the updated values.
     * @throws PersistenceException will be thrown if something goes wrong during the database access.
     * @throws NotFoundException will be thrown if the horse could not be found in the database.
     */
    Horse changeHorseData(Integer id,Horse horse) throws PersistenceException,NotFoundException;

    /**
     *
     * @param id
     * @throws PersistenceException
     * @throws NotFoundException
     */
    void deleteHorse(Integer id) throws PersistenceException,NotFoundException;
}
