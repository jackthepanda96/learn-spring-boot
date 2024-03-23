package altaspring.springrestfulapi.model;

import altaspring.springrestfulapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegisterQuery extends JpaRepository<User, String> {
}
