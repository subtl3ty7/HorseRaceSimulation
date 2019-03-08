package at.ac.tuwien.sepm.assignment.individual.persistence;

import at.ac.tuwien.sepm.assignment.individual.entity.Jockey;
import at.ac.tuwien.sepm.assignment.individual.exceptions.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.persistence.exceptions.PersistenceException;
import at.ac.tuwien.sepm.assignment.individual.rest.dto.JockeyDto;
import java.util.List;

public interface IJockeyDao {

    /**
     * Gets an id, searches for the jockey with the corresponding id and returns it if exists.
     *
     * @param id of the jockey to find.
     * @return the jockey with the specified id.
     * @throws PersistenceException will be thrown if something goes wrong during the database access.
     * @throws NotFoundException    will be thrown if the jockey could not be found in the database.
     */
    Jockey findOneById(Integer id) throws PersistenceException, NotFoundException;


    /**
     * Gets a Jockey Entity and inserts it in Database.
     *
     * @param jockey to be inserted in Database.
     * @return the inserted jockey with its new id and created/updated dates.
     * @throws PersistenceException will be thrown if something goes wrong during the database access.
     */
    Jockey insertJockeyToDB(Jockey jockey) throws PersistenceException;

    /**
     * Gets an id and a Jockey Entity to change the update the Jockey with id in Database with the new Jockey Entity.
     *
     * @param id of the jockey to be changed.
     * @param jockey with the new values.
     * @return the jockey with the updated values.
     * @throws PersistenceException will be thrown if something goes wrong during the database access.
     * @throws NotFoundException will be thrown if the jockey could not be found in the database.
     */
    Jockey changeJockeyData(Integer id,Jockey jockey) throws PersistenceException,NotFoundException;

    /**
     * Gets an id and deletes the jockey from database with the corresponding id.
     *
     * @param id of the jockey to be deleted.
     * @throws PersistenceException will be thrown if something goes wrong during the database access.
     * @throws NotFoundException will be thrown if the jockey could not be found in the database.
     */
    void deleteJockey(Integer id) throws PersistenceException,NotFoundException;


    /**
     * Returns a list of all the jockeys in database.
     *
     * @return values is the list of jockeys in database.
     * @throws PersistenceException will be thrown if something goes wrong during the database access.
     * @throws NotFoundException will be thrown if database is empty.
     */
    List<Jockey> getAllJockeys() throws PersistenceException, NotFoundException;


    List<Jockey> getAllJockeysFiltered(Jockey jockey) throws PersistenceException, NotFoundException;
}
