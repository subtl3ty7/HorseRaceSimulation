package at.ac.tuwien.sepm.assignment.individual.persistence;

import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import at.ac.tuwien.sepm.assignment.individual.exceptions.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.persistence.exceptions.PersistenceException;
import at.ac.tuwien.sepm.assignment.individual.rest.dto.HorseDto;

import java.util.List;

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
     * Gets an id and deletes the horse from database with the corresponding id.
     *
     * @param id of the horse to be deleted.
     * @throws PersistenceException will be thrown if something goes wrong during the database access.
     * @throws NotFoundException will be thrown if the horse could not be found in the database.
     */
    void deleteHorse(Integer id) throws PersistenceException,NotFoundException;


    /**
     * Returns a list of all the horses in database.
     *
     * @return values is the list of horses in database.
     * @throws PersistenceException will be thrown if something goes wrong during the database access.
     * @throws NotFoundException will be thrown if database is empty.
     */
    List<Horse> getAllHorses() throws PersistenceException, NotFoundException;

    /**
     * Returns a filtered list of all the horses in database.
     * @param horse is the filtered Horse
     * @return is the List of Horses matching the filter criteria
     * @throws PersistenceException will be thrown if something goes wrong during the database access.
     * @throws NotFoundException will be thrown if database is empty or a Horse could not be found with matching filters.
     */
    List<Horse> getAllHorsesFiltered(Horse horse) throws PersistenceException, NotFoundException;
}
