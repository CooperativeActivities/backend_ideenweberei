package at.qe.crac.services;

import at.qe.crac.model.Group;
import at.qe.crac.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Service for accessing and manipulating user data.
 *
 */
@Component
@Scope("application")
public class GroupService extends AbstractService<Group> {

    @Autowired
    private HelperService helperService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Group getGroup(int id) {
        JsonNode response = helperService.request("/group/"+id, "get");
        ObjectMapper mapper = new ObjectMapper();
        Group group = null;

        try {
            group = mapper.treeToValue(response.path("object"), Group.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return group;
    }

    public List<Group> getGroupList() {
        JsonNode response = helperService.request("/group", "get");
        System.out.println(response);
        ObjectMapper mapper = new ObjectMapper();
        List<Group> groups = new ArrayList<>();

        if(response == null) {
            return groups;
        }

        if (response.path("object").isArray()) {
            for (final JsonNode node : response.path("object")) {
                try {
                    groups.add(mapper.treeToValue(node, Group.class));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

        return groups;
    }

    //PUT /group/{group_id}/add/multiple
    public void add(Group group, List<User> userList) {

        List<User> tmpList = new ArrayList<>();

        for(User user : userList) {
            User tmp = new User();
            tmp.setId(user.getId());
            tmp.setName(user.getName());
            tmpList.add(tmp);
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.convertValue(tmpList, JsonNode.class);

        System.out.println(node);

        JsonNode response = helperService.request("/group/"+group.id+"/add/multiple", "put", node);
        if(response == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "Could not add User."));
            return;
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info:", "User added."));
    }

    public Group createGroup() {
        Group group = new Group();
        group.setName("");
        group.setDescription("");
        group.setMaxEnrols(1024);
        return group;
    }

    public Group saveGroup(Group group) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.convertValue(group, JsonNode.class);

        JsonNode response;
        if(group.isNew()) {
            response = helperService.request("/group", "post", node);
            if(response == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "Could not create Group."));
                return group;
            }
        } else {
            response = helperService.request("/group/"+group.getId(), "put", node);
        }

        if(response == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "Could not save Group."));
            return group;
        }

        try {
            group = mapper.treeToValue(response.path("object"), Group.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error:", "Could not save Group."));
            return group;
        }

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Group saved."));
        return group;
    }

    public void deleteGroup(Group group) {
        helperService.request("/group/"+group.id, "delete");
    }
}
