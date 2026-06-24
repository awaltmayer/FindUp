package br.com.findUp.controllers;

import br.com.findUp.components.JwtUtil;
import br.com.findUp.dtos.SigninDTO;
import br.com.findUp.dtos.SignupResponseDTO;    
import br.com.findUp.dtos.SignupDTO;
import br.com.findUp.entities.User;
import br.com.findUp.entities.UserType;
import br.com.findUp.services.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationConfiguration auth;

    public AuthController(UserService userService, AuthenticationConfiguration auth) {
        this.userService = userService;
        this.auth = auth;
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDTO> postSignup(@RequestBody SignupDTO dto) throws Exception {
        User newUser = new User();
        BeanUtils.copyProperties(dto, newUser);
        newUser.setType(UserType.Common);
        userService.save(newUser);
        return ResponseEntity.status(201)
                .body(new SignupResponseDTO(newUser.getId(), newUser.getName(), newUser.getEmail(), newUser.getType()));
    }

    @PostMapping("/signin")
    public ResponseEntity<String> postSigning(@RequestBody SigninDTO dto) throws Exception {

        System.out.println("ENTROU NO SIGNIN");

        auth.getAuthenticationManager()
                .authenticate(new UsernamePasswordAuthenticationToken(dto.email(), dto.password()));

        User user = (User) this.userService.loadUserByUsername(dto.email());

        String jwt = JwtUtil.generateToken(user.getEmail(), user.getId(),user.getType());

        System.out.println("JWT GERADO: " + jwt);
        return ResponseEntity.ok(jwt);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exceptionHandler(Exception ex){
        String message = ex.getMessage().replace("\r\n", "");
        return ResponseEntity.badRequest().body(message);
    }
}
