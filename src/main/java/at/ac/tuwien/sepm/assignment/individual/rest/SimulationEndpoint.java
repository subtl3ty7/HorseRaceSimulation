package at.ac.tuwien.sepm.assignment.individual.rest;
import at.ac.tuwien.sepm.assignment.individual.exceptions.NotFoundException;
import at.ac.tuwien.sepm.assignment.individual.rest.dto.SimulationInputDto;
import at.ac.tuwien.sepm.assignment.individual.rest.dto.SimulationResultDto;
import at.ac.tuwien.sepm.assignment.individual.service.ISimulationService;
import at.ac.tuwien.sepm.assignment.individual.service.exceptions.ServiceException;
import at.ac.tuwien.sepm.assignment.individual.service.impl.SimulationService;
import at.ac.tuwien.sepm.assignment.individual.util.mapper.SimulationInputMapper;
import at.ac.tuwien.sepm.assignment.individual.util.mapper.SimulationResultMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;


@RestController
@RequestMapping("/api/v1/simulations")
public class SimulationEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(JockeyEndpoint.class);
    private static final String BASE_URL = "/api/v1/simulations";
    private final ISimulationService simulationService;
    private final SimulationInputMapper simulationInputMapper;
    private final SimulationResultMapper simulationResultMapper;

    @Autowired
    public SimulationEndpoint(ISimulationService simulationService, SimulationInputMapper simulationInputMapper, SimulationResultMapper simulationResultMapper) {
        this.simulationService = simulationService;
        this.simulationInputMapper = simulationInputMapper;
        this.simulationResultMapper = simulationResultMapper;
    }

















    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public SimulationResultDto insertSimulationToDatabase(@RequestBody SimulationInputDto simulation) {
        LOGGER.info("POST"+ BASE_URL);
        simulation.setCreated(LocalDateTime.now());
        try {
            SimulationResultDto simResponse = simulationResultMapper.entityToDto(simulationService.insertSimulationToDB(simulationInputMapper.dtoToEntity(simulation)));
            LOGGER.info("Simulation insertion has been finished.");
            return simResponse;
        } catch (ServiceException e) {
            LOGGER.error("Error during insertion of simulation data");
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error during insertion of simulation data",e);
        } catch (IllegalArgumentException e){
            LOGGER.error("Mandatory field is missing or entered wrong type of value");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Mandatory field is missing or entered a wrong type of value",e);
        }

        catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error during inserting: " + e.getMessage(), e);
        }


    }

}
