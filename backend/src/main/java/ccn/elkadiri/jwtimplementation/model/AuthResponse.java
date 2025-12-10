package ccn.elkadiri.jwtimplementation.model;

/**
 * DTO pour la réponse de login
 * Contient le token JWT généré
 */
public record AuthResponse(String token) {
    // Génération automatique des méthodes par le record
}