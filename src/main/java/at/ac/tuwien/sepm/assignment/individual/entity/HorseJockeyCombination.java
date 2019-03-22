package at.ac.tuwien.sepm.assignment.individual.entity;

import java.util.Objects;

public class HorseJockeyCombination {
    private Integer rank;
    private String horseName;
    private String jockeyName;
    private Double avgSpeed;
    private Double horseSpeed;
    private Double skill;
    private Double luckFactor;
    private Integer horseId;
    private Integer jockeyId;
    public HorseJockeyCombination() {}


    public HorseJockeyCombination(Integer rank, String horseName, String jockeyName, Double avgSpeed, Double horseSpeed, Double skill, Double luckFactor){
        this.rank = rank;
        this.horseName = horseName;
        this.jockeyName = jockeyName;
        this.avgSpeed = avgSpeed;
        this.horseSpeed = horseSpeed;
        this.skill = skill;
        this.luckFactor = luckFactor;
    }


    public Double getAvgSpeed() {
        return avgSpeed;
    }

    public Double getHorseSpeed() {
        return horseSpeed;
    }

    public Double getLuckFactor() {
        return luckFactor;
    }

    public Double getSkill() {
        return skill;
    }

    public Integer getRank() {
        return rank;
    }

    public Integer getHorseId() {
        return horseId;
    }

    public Integer getJockeyId() {
        return jockeyId;
    }

    public String getHorseName() {
        return horseName;
    }

    public String getJockeyName() {
        return jockeyName;
    }

    public void setHorseId(Integer horseId) {
        this.horseId = horseId;
    }

    public void setJockeyId(Integer jockeyId) {
        this.jockeyId = jockeyId;
    }

    public void setAvgSpeed(Double avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public void setHorseName(String horseName) {
        this.horseName = horseName;
    }

    public void setHorseSpeed(Double horseSpeed) {
        this.horseSpeed = horseSpeed;
    }

    public void setJockeyName(String jockeyName) {
        this.jockeyName = jockeyName;
    }

    public void setLuckFactor(Double luckFactor) {
        this.luckFactor = luckFactor;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public void setSkill(Double skill) {
        this.skill = skill;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HorseJockeyCombination)) return false;
        HorseJockeyCombination horseJockeyCombination = (HorseJockeyCombination) o;
        return Objects.equals(rank, horseJockeyCombination.rank) &&
            Objects.equals(horseName, horseJockeyCombination.horseName) &&
            Objects.equals(jockeyName, horseJockeyCombination.jockeyName) &&
            Objects.equals(avgSpeed,horseJockeyCombination.avgSpeed) &&
            Objects.equals(horseSpeed, horseJockeyCombination.horseSpeed) &&
            Objects.equals(skill, horseJockeyCombination.skill) &&
            Objects.equals(luckFactor, horseJockeyCombination.luckFactor);


    }


    @Override
    public int hashCode() {
        return Objects.hash(rank,horseName,jockeyName,avgSpeed,horseSpeed,skill,luckFactor);
    }


    @Override
    public String toString() {
        return "Horse-Jockey Combination{" +
            ", rank='" + rank + '\'' +
            ", horseName=" + horseName +
            ", jockeyName=" + jockeyName +
            ", avgSpeed=" + avgSpeed +
            ", horseSpeed=" + horseSpeed +
            ", skill=" + skill +
            ", luckFactor=" + luckFactor +
            '}';
    }
}
