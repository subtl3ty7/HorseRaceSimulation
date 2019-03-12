package at.ac.tuwien.sepm.assignment.individual.rest.dto;

import at.ac.tuwien.sepm.assignment.individual.entity.SimulationParticipant;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class SimulationDto {

        private String name;
        private List<SimulationParticipant> simulationParticipants;
        private LocalDateTime created;



        public SimulationDto(){}



        public SimulationDto(String name,List<SimulationParticipant> simulationParticipants,LocalDateTime created){

            this.name = name;
            this.created = created;
            this.simulationParticipants = simulationParticipants;

        }


        public List<SimulationParticipant> getSimulationParticipants() {
            return simulationParticipants;
        }

        public LocalDateTime getCreated() {
            return created;
        }

        public String getName() {
            return name;
        }


        public void setCreated(LocalDateTime created) {
            this.created = created;
        }

        public void setSimulationParticipants(List<SimulationParticipant> simulationParticipants) {
            this.simulationParticipants = simulationParticipants;
        }

        public void setName(String name) {
            this.name = name;
        }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimulationDto)) return false;
        SimulationDto simulationDto = (SimulationDto) o;
        return Objects.equals(name, simulationDto.name) &&
            Objects.equals(simulationParticipants, simulationDto.simulationParticipants) &&
            Objects.equals(created, simulationDto.created);
    }

    @Override
    public int hashCode() {

        return Objects.hash( name, simulationParticipants, created);
    }

    @Override
    public String toString() {
        return "SimulationDto{" +
            ", name='" + name + '\'' +
            ", Simulation Participants=" + simulationParticipants +
            ", created=" + created +
            '}';
    }

}
