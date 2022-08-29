package in.satish.model;
import java.util.Set;
import javax.persistence.*;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name="users_tab", 
	uniqueConstraints = {
			@UniqueConstraint(columnNames = "username"),
			@UniqueConstraint(columnNames = "email")
	}
		)
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Size(max=20)
	@NonNull
	private String username;
	
	@NotBlank
	@Size(max=50)
	@Email
	@NonNull
	private String email;
	
	@NotBlank
	@Size(max=100)
	@NonNull
	private String password;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name="users_roles_tab",
			joinColumns = @JoinColumn(name="user_id"),
			inverseJoinColumns = @JoinColumn(name="role_id")
			)
	private Set<Role> roles;
}





