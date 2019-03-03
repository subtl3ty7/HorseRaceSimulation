package at.ac.tuwien.sepm.assignment.individual.rest;

import at.ac.tuwien.sepm.assignment.individual.rest.dto.HorseDto;
import at.ac.tuwien.sepm.assignment.individual.exceptions.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.service.IHorseService;
import at.ac.tuwien.sepm.assignment.individual.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.assignment.individual.util.mapper.HorseMapper;
import org.apache.tomcat.jni.Local;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/horses")
public class HorseEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(HorseEndpoint.class);
    private static final String BASE_URL = "/api/v1/horses";
    private final IHorseService horseService;
    private final HorseMapper horseMapper;

    @Autowired
    public HorseEndpoint(IHorseService horseService, HorseMapper horseMapper) {
        this.horseService = horseService;
        this.horseMapper = horseMapper;
    }

    /**
     * Finds a horse by its id by passing the id to Service Layer.
     * If successful, returns the DTO transformed seeked Horse.
     * Mapped to HTTP GET requests from client.
     * @param id which belongs to the horse user wants to find.
     * @return DTO of the found horse.
     */

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public HorseDto getOneById(@PathVariable("id") Integer id) {
        LOGGER.info("GET " + BASE_URL + "/" + id);
        try {
            return horseMapper.entityToDto(horseService.findOneById(id));
        } catch (ServiceException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error during read horse with id " + id, e);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error during reading horse: " + e.getMessage(), e);
        }
    }

    /**
     * Gets a Horse DTO and passes it to Service Layer for Validation and Business Logic.
     * Mapped to HTTP POST requests from client.
     * @param horse is the DTO which we want to insert the database.
     * @return
     */

    @RequestMapping( method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public HorseDto insertHorseToDatabase(@RequestBody HorseDto horse) {
        LOGGER.info("POST"+ BASE_URL);
        horse.setCreated(LocalDateTime.now());
        horse.setUpdated(LocalDateTime.now());
        try {
          HorseDto horseResponse = horseMapper.entityToDto(horseService.insertHorseToDB(horseMapper.dtoToEntity(horse)));
          LOGGER.info("Horse insertion has been finished.");
          return horseResponse;
        } catch (ServiceException e) {
            LOGGER.error("Error during insertion of horse data");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error during insertion of horse data",e);
        } catch (IllegalArgumentException e){
            LOGGER.error("Mandatory field is missing or entered wrong type of value");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Mandatory field is missing or entered a wrong type of value",e);
        }


    }

}
