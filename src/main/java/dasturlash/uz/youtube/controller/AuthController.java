    package dasturlash.uz.youtube.controller;

    import dasturlash.uz.youtube.dto.auth.AuthResponseDTO;
    import dasturlash.uz.youtube.dto.auth.LoginDTO;
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
            return ResponseEntity.ok(authService.registration(dto));
        }

        @GetMapping("/verification/{token}")
        public ResponseEntity<String> verify(@PathVariable String token) {
            return ResponseEntity.ok(authService.verifyEmail(token));
        }

        @PostMapping("/login")
        public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginDTO dto) {
            return ResponseEntity.ok(authService.login(dto));
        }
    }