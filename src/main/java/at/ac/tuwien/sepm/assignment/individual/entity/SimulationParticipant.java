package at.ac.tuwien.sepm.assignment.individual.entity;

import java.util.Objects;

public class SimulationParticipant {

    private Integer horseId;
    private Integer jockeyId;
    private Double luckFactor;



    public SimulationParticipant(){}

    public SimulationParticipant(Integer horseId, Integer jockeyId, Double luckFactor){
        this.horseId = horseId;
        this.jockeyId = jockeyId;
        this.luckFactor = luckFactor;
    }


    public Double getLuckFactor() {
        return luckFactor;
    }

    public Integer getHorseId() {
        return horseId;
    }

    public Integer getJockeyId() {
        return jockeyId;
    }

    public void setHorseId(Integer horseId) {
        this.horseId = horseId;
    }

    public void setJockeyId(Integer jockeyId) {
        this.jockeyId = jockeyId;
    }

    public void setLuckFactor(Double luckFactor) {
        this.luckFactor = luckFactor;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimulationParticipant)) return false;
        SimulationParticipant simulationParticipant = (SimulationParticipant) o;
        return Objects.equals(horseId, simulationParticipant.horseId) &&
            Objects.equals(jockeyId, simulationParticipant.jockeyId) &&
            Objects.equals(luckFactor, simulationParticipant.luckFactor);

    }

    @Override
    public int hashCode() {

        return Objects.hash(horseId, jockeyId, luckFactor);

    }

    @Override
    public String toString() {
        return "SimulationInput Participant{" +
            "horseId=" + horseId +
            ", jockeyId='" + jockeyId + '\'' +
            ", luck factor=" + luckFactor +
            '}';
    }
}
