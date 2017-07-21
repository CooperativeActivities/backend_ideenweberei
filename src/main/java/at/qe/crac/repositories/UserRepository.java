package at.qe.crac.repositories;

import at.qe.crac.model.User;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface UserRepository extends AbstractRepository<User, Integer> {}
