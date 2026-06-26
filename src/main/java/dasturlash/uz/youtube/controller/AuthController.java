package dasturlash.uz.youtube.controller;

import dasturlash.uz.youtube.dto.auth.RegistrationDTO;
import dasturlash.uz.youtube.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/registration")
    public ResponseEntity<String> registration(@Valid @RequestBody RegistrationDTO dto) {
        authService.registration(dto);
        return ResponseEntity.ok("Registration successfully completed.");
    }

}