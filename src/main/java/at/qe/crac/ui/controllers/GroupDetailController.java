package at.qe.crac.ui.controllers;

import at.qe.crac.model.Group;
import at.qe.crac.model.User;
import at.qe.crac.services.GroupService;
import at.qe.crac.services.UserService;
import at.qe.crac.ui.beans.SessionInfoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.List;

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
    private List<User> userList;

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public void add(List<User> userList) {
        if(userList == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No User selected or User already in Group."));
            return;
        }
        if(group == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No Group selected."));
            return;
        }

        groupService.add(group, userList);
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
    }

    public void delete() {
        if(group.getEnroledUsers() != null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Group ist not empty."));
            return;
        }
        groupService.deleteGroup(group);
        groupListController.setGroupList(null);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Group deleted."));
    }

    public void remove(List<User> userList) {
        groupService.remove(group, userList);
    }
}
