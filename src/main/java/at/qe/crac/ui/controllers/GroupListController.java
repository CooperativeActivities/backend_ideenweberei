package at.qe.crac.ui.controllers;

import at.qe.crac.model.Group;
import at.qe.crac.model.User;
import at.qe.crac.services.GroupService;
import at.qe.crac.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 *
 */
@Controller
@Scope("view")
public class GroupListController {

    @Autowired
    private GroupService groupService;

    private List<Group> groupList;
    private List<User> selectedUser;

    public List<Group> getGroupList() {
        if(groupList == null) {
            groupList = groupService.getGroupList();
        }
        return groupList;
    }

    public void setGroupList(List<Group> groupList) {
        this.groupList = groupList;
    }

    public List<User> getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(List<User> selectedUser) {
        this.selectedUser = selectedUser;
    }
}
