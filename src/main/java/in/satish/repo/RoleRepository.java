package in.satish.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import in.satish.model.ERole;
import in.satish.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{

	Optional<Role> findByName(ERole name);
}
