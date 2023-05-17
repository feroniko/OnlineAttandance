package uz.attendance_system.attendance_system.web_controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uz.attendance_system.attendance_system.domain.User;
import uz.attendance_system.attendance_system.repository.UserRepository;
import uz.attendance_system.attendance_system.security.JwtProvider;
import uz.attendance_system.attendance_system.web_controller.vm.LoginVM;

@RestController
@RequestMapping("/auth")
public class UserJwtCont {
    
    private final JwtProvider jwtProvider;

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    public UserJwtCont(JwtProvider jwtProvider, AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginVM loginVM){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginVM.getLogin(), loginVM.getPassword()));
        User user = userRepository.findByLogin(loginVM.getLogin());
        if(user == null){
            throw new UsernameNotFoundException("This user not found!");
        }
        String token = jwtProvider.createToken(user.getLogin(), user.getUserRoles());
        Map<Object, Object> map = new HashMap<>();
        map.put("login", user.getLogin());
        map.put("token", token);
        return ResponseEntity.ok(map);
    }
}
