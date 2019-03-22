package at.ac.tuwien.sepm.assignment.individual.service;

import at.ac.tuwien.sepm.assignment.individual.entity.Jockey;
import at.ac.tuwien.sepm.assignment.individual.exceptions.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.service.exceptions.ServiceException;

import java.util.List;

public interface IJockeyService {

    /**
     * Gets the id of the jockey, applies required logic and passes it to Persistence Layer.
     *
     * @param id of the jockey to find.
     * @return the jockey with the specified id.
     * @throws ServiceException  will be thrown if something goes wrong during data processing.
     * @throws NotFoundException will be thrown if the jockey could not be found in the system.
     */
    Jockey findOneById(Integer id) throws ServiceException, NotFoundException;


    /**
     * Validates the Jockey's values if they correspond the correct criteria and in that case passes it to
     * Persistence Layer.
     *
     * @param jockey jockey to be validated and passed to Persistence Layer.
     * @return the inserted jockey with its new id and created/updated dates.
     * @throws ServiceException will be thrown if something goes wrong during the data processing.
     */
    Jockey insertJockeyToDB(Jockey jockey) throws ServiceException;


    /**
     * Gets id of the jockey and a Jockey entity with values to be updated in DB. Checks if it applies the correct criteria
     * and then passes it to Persistence Layer depending on the validation.
     *
     * @param id of the jockey to be changed.
     * @param jockey is the JockeyDto which contains the information that we want to change.
     * @return DTO of the changed jockey.
     * @throws ServiceException will be thrown if something goes wrong during the data processing.
     * @throws NotFoundException will be thrown if the Jockey which user wants to change is not found in the system.
     */
    Jockey changeJockeyData(Integer id,Jockey jockey) throws ServiceException,NotFoundException;

    /**
     * Gets the id of a jockey and passes it to Persistence Layer on purpose of deleting.
     *
     * @param id of the jockey to be deleted.
     * @throws ServiceException will be thrown if something goes wrong during the data processing.
     * @throws NotFoundException will be thrown if the Jockey which user wants to delete is not found in the system.
     */
    void deleteJockey(Integer id) throws ServiceException,NotFoundException;


    /**
     * Returns all the jockeys from database.
     *
     * @return value is the list of all Jockeys.
     * @throws ServiceException will be thrown if something goes wrong during the data processing.
     * @throws NotFoundException will be thrown if there is no jockeys in the system.
     */
    List<Jockey> getAllJockeys() throws ServiceException, NotFoundException;

    /**
     * Returns all the filtered jockeys from database.
     *
     * @return value is the list of all filtered Jockeys.
     * @throws ServiceException will be thrown if something goes wrong during the data processing.
     * @throws NotFoundException will be thrown if there is no jockey in the system.
     */
    List<Jockey> getAllJockeysFiltered(Jockey jockey) throws ServiceException, NotFoundException;
}
