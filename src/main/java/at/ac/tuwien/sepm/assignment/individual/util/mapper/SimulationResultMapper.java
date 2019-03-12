package at.ac.tuwien.sepm.assignment.individual.util.mapper;


import at.ac.tuwien.sepm.assignment.individual.entity.SimulationResult;
import at.ac.tuwien.sepm.assignment.individual.rest.dto.SimulationResultDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SimulationResultMapper {


        /**
         * Changes a simulationResult entity to dto.
         *
         * @param simulationResult
         * @return value is a DTO.
         */
        public SimulationResultDto entityToDto(SimulationResult simulationResult) {
            return new SimulationResultDto(simulationResult.getId(), simulationResult.getName(), simulationResult.getCreated(),
                simulationResult.getHorseJockeyCombinations());
        }

        /**
         * Changes a simulation result dto to entity.
         *
         * @param simulation
         * @return is an entity.
         */
        public SimulationResult dtoToEntity (SimulationResultDto simulation) {
            return new SimulationResult(simulation.getId(), simulation.getName(), simulation.getCreated(),
                simulation.getHorseJockeyCombinations());
        }

        /**
         * Changes a simulation result entity list to dto list.
         *
         * @param simulationResultList
         * @return value is a list of simulation result DTOs.
         */
        public List<SimulationResultDto> listEntityToDTO(List<SimulationResult> simulationResultList){
            List<SimulationResultDto> simulationResultDtos = new ArrayList<>();
            for (SimulationResult simulationResult : simulationResultList) {
                simulationResultDtos.add(entityToDto(simulationResult));
            }
            return simulationResultDtos;
        }


}
