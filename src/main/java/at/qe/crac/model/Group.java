package at.qe.crac.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Group extends AbstractEntity<Integer> {

    @Column(nullable = false)
    protected String name;

    @Column(nullable = false)
    protected String description;

    @Column(nullable = false)
    protected Integer maxEnrols;

    @CollectionTable(name = "group_user")
    @ElementCollection(targetClass = User.class, fetch = FetchType.EAGER)
    protected List<User> enroledUsers;

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

    public List<User> getEnroledUsers() {
        return enroledUsers;
    }

    public void setEnroledUsers(List<User> enroledUsers) {
        this.enroledUsers = enroledUsers;
    }

    @Override
    public String toString(){
    return name;
    }
}
