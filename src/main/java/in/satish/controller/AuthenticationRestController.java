package in.satish.controller;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.satish.model.Role;
import in.satish.model.User;
import in.satish.payload.JwtResponse;
import in.satish.payload.LoginRequest;
import in.satish.payload.MessageResponse;
import in.satish.payload.SignUpRequest;
import in.satish.repo.UserRepository;
import in.satish.service.UserDetailsImpl;
import in.satish.util.JwtUtils;
import in.satish.util.RoleUtils;

@RestController
@RequestMapping("/auth")
public class AuthenticationRestController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleUtils roleUtils;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){
		// check for authentication 
		Authentication authentication = authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String jwt = jwtUtils.generateToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		
		return ResponseEntity.ok(
				new JwtResponse(
						jwt, 
						userDetails.getId(),
						userDetails.getUsername(),
						userDetails.getEmail(),
						userDetails.getAuthorities()
						.stream()
						.map(auth -> auth.getAuthority())
						.collect(Collectors.toSet())
						)
						
				);
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> createUser(@Valid @RequestBody SignUpRequest signUpRequest){
		boolean existsByUsername = userRepository.existsByUsername(signUpRequest.getUsername());
		if(existsByUsername) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error : Username already Exist"));
		}
		boolean existsByEmail = userRepository.existsByEmail(signUpRequest.getEmail());
		if(existsByEmail) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error : Email already Exist"));
		}
		
		User user = new User(
				signUpRequest.getUsername(),
				signUpRequest.getEmail(),
				passwordEncoder.encode(signUpRequest.getPassword())
				);
		Set<String> userRole = signUpRequest.getRole();
		Set<Role> dbRoles = new HashSet<>();
		roleUtils.mapRoles(userRole, dbRoles);
		user.setRoles(dbRoles);
		userRepository.save(user);
		
		return ResponseEntity.ok(new MessageResponse("User Created Successfully"));
	}
	
	
	
}
