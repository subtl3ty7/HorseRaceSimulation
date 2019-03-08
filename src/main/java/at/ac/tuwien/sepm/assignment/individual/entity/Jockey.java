package at.ac.tuwien.sepm.assignment.individual.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class Jockey {
    private Integer id;
    private String name;
    private Double skill;
    private LocalDateTime created;
    private LocalDateTime updated;

    public Jockey(){
    }
    public Jockey(Integer id,String name, Double skill, LocalDateTime created, LocalDateTime updated) {
        this.id = id;
        this.name = name;
        this.skill = skill;
        this.created = created;
        this.updated = updated;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getSkill() {
        return skill;
    }

    public void setSkill(Double skill) {
        this.skill = skill;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Jockey)) return false;
        Jockey jockey = (Jockey) o;
        return Objects.equals(id, jockey.id) &&
            Objects.equals(name, jockey.name) &&
            Objects.equals(skill, jockey.skill) &&
            Objects.equals(created, jockey.created) &&
            Objects.equals(updated, jockey.updated);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, skill, created, updated);
    }

    @Override
    public String toString() {
        return "Jockey{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", skill=" + skill +
            ", created=" + created +
            ", updated=" + updated +
            '}';
    }
}
