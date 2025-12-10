package ccn.elkadiri.jwtimplementation.filter;

import ccn.elkadiri.jwtimplementation.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtre personnalisé pour valider le token JWT à chaque requête
 * S'exécute une seule fois par requête (OncePerRequestFilter)
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Méthode principale du filtre, appelée pour chaque requête HTTP
     *
     * Flux:
     * 1. Extraire le header Authorization
     * 2. Vérifier le format "Bearer <token>"
     * 3. Extraire et valider le token
     * 4. Charger l'utilisateur correspondant
     * 5. Placer l'utilisateur dans le SecurityContext
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        // 1. Récupérer le header Authorization
        final String authHeader = request.getHeader("Authorization");

        // 2. Si pas de header ou format incorrect, continuer sans authentification
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        // 3. Extraire le token (enlever "Bearer ")
        final String token = authHeader.substring(7);
        String username = null;

        // 4. Essayer d'extraire le username du token
        try {
            username = jwtService.extractUsername(token);
        } catch (Exception e) {
            // Token invalide ou expiré, continuer sans authentification
            chain.doFilter(request, response);
            return;
        }

        // 5. Si username extrait et pas déjà authentifié
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 6. Charger les détails de l'utilisateur
            UserDetails user = userDetailsService.loadUserByUsername(username);

            // 7. Valider le token
            if (jwtService.isTokenValid(token, user.getUsername())) {

                // 8. Créer un objet d'authentification
                var authToken = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user.getAuthorities()
                );

                // 9. Ajouter les détails de la requête
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 10. Placer l'authentification dans le contexte de sécurité
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 11. Continuer la chaîne de filtres
        chain.doFilter(request, response);
    }
}
