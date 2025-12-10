package ccn.elkadiri.jwtimplementation.controller;

import ccn.elkadiri.jwtimplementation.model.AuthRequest;
import ccn.elkadiri.jwtimplementation.model.AuthResponse;
import ccn.elkadiri.jwtimplementation.service.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Contrôleur gérant les endpoints d'authentification
 */
@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtService jwtService,
                          UserDetailsService uds) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userDetailsService = uds;
    }

    /**
     * Endpoint de connexion
     * POST /api/auth/login
     * Body: {"username": "user", "password": "password"}
     * Retourne un token JWT si les credentials sont valides
     */
    @PostMapping("/auth/login")
    public AuthResponse login(@RequestBody AuthRequest req) {
        // 1. Créer un token d'authentification avec username et password
        var auth = new UsernamePasswordAuthenticationToken(req.username(), req.password());

        // 2. Authentifier l'utilisateur (lève une exception si invalide)
        authenticationManager.authenticate(auth);

        // 3. Charger les détails de l'utilisateur authentifié
        UserDetails user = userDetailsService.loadUserByUsername(req.username());

        // 4. Générer le token JWT avec les rôles de l'utilisateur
        String token = jwtService.generateToken(
                user.getUsername(),
                Map.of("roles", user.getAuthorities())
        );

        // 5. Retourner le token dans la réponse
        return new AuthResponse(token);
    }

    /**
     * Endpoint protégé de test
     * GET /api/hello
     * Nécessite un token JWT valide dans le header Authorization
     */
    @GetMapping("/hello")
    public Map<String, String> hello() {
        return Map.of("message", "Bonjour, endpoint protégé OK ✅");
    }

    /**
     * Endpoint protégé supplémentaire pour tester
     * GET /api/profile
     */
    @GetMapping("/profile")
    public Map<String, Object> profile() {
        return Map.of(
                "message", "Profil utilisateur",
                "status", "authenticated",
                "timestamp", System.currentTimeMillis()
        );
    }

    /**
     * Endpoint public pour vérifier que l'API fonctionne
     * GET /api/auth/status
     */
    @GetMapping("/auth/status")
    public Map<String, String> status() {
        return Map.of(
                "status", "API JWT is running",
                "version", "1.0.0"
        );
    }
}