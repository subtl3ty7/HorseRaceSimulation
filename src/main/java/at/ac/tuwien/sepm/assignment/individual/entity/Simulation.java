package at.ac.tuwien.sepm.assignment.individual.entity;

import java.time.LocalDateTime;
import java.util.List;

public class Simulation {

    private String name;
    private List<SimulationParticipant> simulationParticipants;
    private LocalDateTime created;



    public Simulation(){}



    public Simulation(String name,List<SimulationParticipant> simulationParticipants,LocalDateTime created){

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
}
