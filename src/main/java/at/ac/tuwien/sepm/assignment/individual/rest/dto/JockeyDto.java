package at.ac.tuwien.sepm.assignment.individual.rest.dto;



import java.time.LocalDateTime;
import java.util.Objects;

public class JockeyDto {

    private Integer id;
    private String name;
    private Double skill;
    private LocalDateTime created;
    private LocalDateTime updated;

    public JockeyDto() {
    }

    public JockeyDto(Integer id, String name, Double skill, LocalDateTime created, LocalDateTime updated) {
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
        if (!(o instanceof JockeyDto)) return false;
        JockeyDto jockeyDto = (JockeyDto) o;
        return Objects.equals(id, jockeyDto.id) &&
               Objects.equals(name, jockeyDto.name) &&
               Objects.equals(skill, jockeyDto.skill) &&
               Objects.equals(created, jockeyDto.created) &&
               Objects.equals(updated, jockeyDto.updated);
    }
        @Override
        public int hashCode() {

        return Objects.hash(id, name, skill, created, updated);
        }

        @Override
        public String toString() {
            return "JockeyDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", skill=" + skill +
                ", created=" + created +
                ", updated=" + updated +
                '}';
        }
    }


