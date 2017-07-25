package at.qe.crac.utils;

import at.qe.crac.model.Group;
import at.qe.crac.model.Role;
import at.qe.crac.services.GroupService;
import at.qe.crac.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Component
@FacesConverter("groupConverter")
public class GroupConverter implements Converter {

    @Autowired
    private GroupService groupService;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return groupService.getGroup(Integer.parseInt(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if(!(value instanceof Group)) {
            return null;
        }

        return ((Group)value).getId().toString();
    }
}
