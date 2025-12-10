package ccn.elkadiri.jwtimplementation.model;


/**
 * DTO (Data Transfer Object) pour la requête de login
 * Utilise un Record Java (Java 14+)
 * Équivalent à une classe immutable avec getters automatiques
 */
public record AuthRequest(String username, String password) {
    // Le record génère automatiquement:
    // - Constructeur
    // - Getters (username(), password())
    // - equals(), hashCode(), toString()
}