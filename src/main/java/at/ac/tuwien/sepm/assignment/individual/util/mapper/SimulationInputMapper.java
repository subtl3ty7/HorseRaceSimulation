package at.ac.tuwien.sepm.assignment.individual.util.mapper;

import at.ac.tuwien.sepm.assignment.individual.entity.SimulationInput;
import at.ac.tuwien.sepm.assignment.individual.rest.dto.SimulationInputDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SimulationInputMapper {

    /**
     * Changes a simulationInput entity to dto.
     *
     * @param simulationInput
     * @return value is a DTO.
     */
    public SimulationInputDto entityToDto(SimulationInput simulationInput) {
        return new SimulationInputDto(simulationInput.getName(), simulationInput.getSimulationParticipants(), simulationInput.getCreated());
    }

    /**
     * Changes a simulation dto to entity.
     *
     * @param simulation
     * @return is an entity.
     */
    public SimulationInput dtoToEntity (SimulationInputDto simulation) {
        return new SimulationInput(simulation.getName(), simulation.getSimulationParticipants(), simulation.getCreated());
    }

    /**
     * Changes a simulation entity list to dto list.
     *
     * @param simulationInputList
     * @return value is a list of simulation DTOs.
     */
    public List<SimulationInputDto> listEntityToDTO(List<SimulationInput> simulationInputList){
        List<SimulationInputDto> simulationInputDtos = new ArrayList<>();
        for (SimulationInput simulationInput : simulationInputList) {
            simulationInputDtos.add(entityToDto(simulationInput));
        }
        return simulationInputDtos;
    }


}
