package at.qe.crac.ui.controllers;

import at.qe.crac.model.Group;
import at.qe.crac.model.User;
import at.qe.crac.services.GroupService;
import at.qe.crac.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 */
@Controller
@Scope("view")
public class GroupDetailController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupListController groupListController;

    private Group group;

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Group create() {
        return groupService.createGroup();
    }

    public void save() {
        //todo: optimize
        if(group.isNew()) {
            for (Group test : groupListController.getGroupList()) {
                if (test.getName().equals(group.getName())) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Groupname already exists."));
                    return;
                }
            }
        }

        group = groupService.saveGroup(group);
        groupListController.setGroupList(null);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "User saved."));
    }

    public void delete() {
        groupService.deleteGroup(group);
        groupListController.setGroupList(null);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "User deleted."));
    }
}
