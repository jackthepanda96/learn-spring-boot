package altaspring.springrestfulapi.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisterQuery extends JpaRepository<User, String> {
}
