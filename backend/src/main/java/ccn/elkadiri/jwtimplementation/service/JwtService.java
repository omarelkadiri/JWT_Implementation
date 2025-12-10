package ccn.elkadiri.jwtimplementation.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;

/**
 * Service pour générer, signer et valider les tokens JWT
 */
@Service
public class JwtService {

    private final Key key;
    private final long expirationMs;

    /**
     * Constructeur: initialise la clé de signature et la durée d'expiration
     * @param secret Clé secrète pour signer les tokens (depuis application.properties)
     * @param expirationMs Durée de validité du token en millisecondes
     */
    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration-ms}") long expirationMs
    ) {
        // Créer une clé HMAC-SHA à partir du secret
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationMs = expirationMs;
    }

    /**
     * Génère un token JWT signé
     *
     * Structure du token:
     * HEADER.PAYLOAD.SIGNATURE
     *
     * @param username Identifiant de l'utilisateur
     * @param claims Informations supplémentaires (rôles, permissions, etc.)
     * @return Token JWT encodé en Base64
     */
    public String generateToken(String username, Map<String, Object> claims) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setClaims(claims)              // Claims personnalisés
                .setSubject(username)           // Sujet du token (username)
                .setIssuedAt(now)               // Date de création
                .setExpiration(exp)             // Date d'expiration
                .signWith(key, SignatureAlgorithm.HS256) // Signature HMAC-SHA256
                .compact();                     // Génère le token final
    }

    /**
     * Extrait le username (subject) du token
     * @param token Token JWT
     * @return Username contenu dans le token
     */
    public String extractUsername(String token) {
        return parse(token).getBody().getSubject();
    }

    /**
     * Vérifie si un token est valide
     * Un token est valide si:
     * - La signature est correcte
     * - Le username correspond
     * - Le token n'est pas expiré
     *
     * @param token Token JWT à valider
     * @param username Username à vérifier
     * @return true si valide, false sinon
     */
    public boolean isTokenValid(String token, String username) {
        try {
            return extractUsername(token).equals(username) && !isExpired(token);
        } catch (JwtException e) {
            // Token malformé, signature invalide, etc.
            return false;
        }
    }

    /**
     * Vérifie si le token est expiré
     * @param token Token JWT
     * @return true si expiré, false sinon
     */
    private boolean isExpired(String token) {
        return parse(token).getBody().getExpiration().before(new Date());
    }

    /**
     * Parse et valide le token JWT
     * Vérifie la signature automatiquement
     *
     * @param token Token JWT
     * @return Claims du token si valide
     * @throws JwtException si token invalide
     */
    private Jws<Claims> parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    /**
     * Extrait tous les claims du token (pour usage avancé)
     * @param token Token JWT
     * @return Map des claims
     */
    public Map<String, Object> extractAllClaims(String token) {
        return parse(token).getBody();
    }
}