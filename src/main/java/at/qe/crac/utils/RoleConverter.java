package at.qe.crac.utils;

import at.qe.crac.model.Role;
import at.qe.crac.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Component
@FacesConverter("roleConverter")
public class RoleConverter implements Converter {

    @Autowired
    private RoleService roleService;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return roleService.getRole(Integer.parseInt(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if(!(value instanceof Role)) {
            return null;
        }

        return ((Role)value).getId().toString();
    }
}
