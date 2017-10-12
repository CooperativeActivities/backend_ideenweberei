package at.qe.crac.repositories;

import at.qe.crac.model.Group;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface GroupRepository extends AbstractRepository<Group, Integer> {}
