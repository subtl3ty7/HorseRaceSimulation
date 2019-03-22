package at.ac.tuwien.sepm.assignment.individual.service;

import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import at.ac.tuwien.sepm.assignment.individual.exceptions.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.service.exceptions.ServiceException;

import java.util.List;

public interface IHorseService {

    /**
     * Gets the id of the horse, applies required logic and passes it to Persistence Layer.
     *
     * @param id of the horse to find.
     * @return the horse with the specified id.
     * @throws ServiceException  will be thrown if something goes wrong during data processing.
     * @throws NotFoundException will be thrown if the horse could not be found in the system.
     */
    Horse findOneById(Integer id) throws ServiceException, NotFoundException;


    /**
     * Validates the Horse' values if they correspond the correct criteria and in that case passes it to
     * Persistence Layer.
     *
     * @param horse horse to be validated and passed to Persistence Layer.
     * @return the inserted horse with its new id and created/updated dates.
     * @throws ServiceException will be thrown if something goes wrong during the data processing.
     */
    Horse insertHorseToDB(Horse horse) throws ServiceException;


    /**
     * Gets id of the horse and a Horse entity with values to be updated in DB. Checks if it applies the correct criteria
     * and then passes it to Persistence Layer depending on the validation.
     *
     * @param id of the horse to be changed.
     * @param horse is the HorseDto which contains the information that we want to change.
     * @return DTO of the changed horse.
     * @throws ServiceException will be thrown if something goes wrong during the data processing.
     * @throws NotFoundException will be thrown if the Horse which user wants to change is not found in the system.
     */
    Horse changeHorseData(Integer id,Horse horse) throws ServiceException,NotFoundException;

    /**
     * Gets the id of a horse and passes it to Persistence Layer on purpose of deleting.
     *
     * @param id of the horse to be deleted.
     * @throws ServiceException will be thrown if something goes wrong during the data processing.
     * @throws NotFoundException will be thrown if the Horse which user wants to delete is not found in the system.
     */
    void deleteHorse(Integer id) throws ServiceException,NotFoundException;


    /**
     * Returns all the horses from database.
     *
     * @return value is the list of all Horses.
     * @throws ServiceException will be thrown if something goes wrong during the data processing.
     * @throws NotFoundException will be thrown if there is no horse in the system.
     */
    List<Horse> getAllHorses() throws ServiceException, NotFoundException;

    /**
     * Returns all th filtered horses from database.
     *
     * @return value is the list of all filtered Horses.
     * @throws ServiceException will be thrown if something goes wrong during the data processing.
     * @throws NotFoundException will be thrown if there is no horse in the system.
     */
    List<Horse> getAllHorsesFiltered(Horse horse) throws ServiceException, NotFoundException;
}
