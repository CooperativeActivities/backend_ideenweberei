package at.qe.crac.repositories;

import at.qe.crac.model.Role;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface RoleRepository extends AbstractRepository<Role, Integer> {}
