package at.qe.crac.ui.controllers;

import at.qe.crac.model.Group;
import at.qe.crac.model.Role;
import at.qe.crac.model.User;
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
public class UserListController {

    @Autowired
    private UserService userService;

    private List<User> userList;
    private List<User> selectedUser;
    private List<Role> roleList;
    private List<Group> groupList;

    public List<User> getUserList() {
        if(userList == null) {
            userList = userService.getUserList();
        }
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public List<Role> getRoleList() {
        if(roleList == null) {
            roleList = userService.getRoleList();
        }
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public List<User> getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(List<User> selectedUser) {
        this.selectedUser = selectedUser;
    }

    public List<Group> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<Group> groupList) {
        this.groupList = groupList;
    }
}
