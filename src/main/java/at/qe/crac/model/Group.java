package at.qe.crac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Group extends AbstractEntity<Integer> {

    @Column(nullable = false)
    protected String name;

    @Column(nullable = false)
    protected String description;

    @Column(nullable = false)
    protected Integer maxEnrols;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMaxEnrols() {
        return maxEnrols;
    }

    public void setMaxEnrols(Integer maxEnrols) {
        this.maxEnrols = maxEnrols;
    }

    @Override
    public String toString(){
    return name;
    }
}
