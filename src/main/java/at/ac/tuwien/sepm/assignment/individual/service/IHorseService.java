package at.ac.tuwien.sepm.assignment.individual.service;

import at.ac.tuwien.sepm.assignment.individual.entity.Horse;
import at.ac.tuwien.sepm.assignment.individual.exceptions.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.service.exceptions.ServiceException;

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
     *
     * @param id
     * @throws ServiceException
     * @throws NotFoundException
     */
    void deleteHorse(Integer id) throws ServiceException,NotFoundException;
}
