package at.qe.crac.repositories;

import at.qe.crac.model.GroupUsers;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface GroupUsersRepository extends AbstractRepository<GroupUsers, Integer> {}
