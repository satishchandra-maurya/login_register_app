package in.satish.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
public class HomeRestController {

	@GetMapping("/all")
	public String allowAll() {
		return "Perit All Data";
	}
	
	@GetMapping("/user")
	public String userData() {
		return "User Data";
	}
	
	@GetMapping("/mod")
	public String moderatorData() {
		return "Modertor Data";
	}
	
	@GetMapping("/admin")
	public String adminData() {
		return "Admin Data";
	}
}
