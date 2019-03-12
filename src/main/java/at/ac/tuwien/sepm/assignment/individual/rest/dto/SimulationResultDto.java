package at.ac.tuwien.sepm.assignment.individual.rest.dto;

import at.ac.tuwien.sepm.assignment.individual.entity.HorseJockeyCombination;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class SimulationResultDto {
    private Integer id;
    private String name;
    private LocalDateTime created;
    private List<HorseJockeyCombination> horseJockeyCombinations;


    public SimulationResultDto(){}


    public SimulationResultDto(Integer id, String name, LocalDateTime created, List<HorseJockeyCombination> horseJockeyCombinations){

        this.id = id;
        this.name = name;
        this.created = created;
        this.horseJockeyCombinations = horseJockeyCombinations;
    }

    public Integer getId() {
        return id;
    }

    public List<HorseJockeyCombination> getHorseJockeyCombinations() {
        return horseJockeyCombinations;
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

    public void setHorseJockeyCombinations(List<HorseJockeyCombination> horseJockeyCombinations) {
        this.horseJockeyCombinations = horseJockeyCombinations;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimulationResultDto)) return false;
        SimulationResultDto simulationResultDto = (SimulationResultDto) o;
        return Objects.equals(id, simulationResultDto.id) &&
            Objects.equals(name, simulationResultDto.name) &&
            Objects.equals(created, simulationResultDto.created) &&
            Objects.equals(horseJockeyCombinations, simulationResultDto.horseJockeyCombinations);
    }

    @Override
    public int hashCode() {

        return Objects.hash( id, name, created, horseJockeyCombinations);
    }

    @Override
    public String toString() {
        return "SimulationResultDto{" +
            "id='" + id + '\'' +
            ", name=" + name +
            ", created=" + created +
            ", Horse Jockey Combinations=" + horseJockeyCombinations +

            '}';
    }



}
