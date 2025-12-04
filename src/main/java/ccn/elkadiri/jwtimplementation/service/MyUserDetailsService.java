package ccn.elkadiri.jwtimplementation.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

/**
 * Service personnalisé pour charger les utilisateurs
 * Dans cet exemple, les utilisateurs sont stockés en mémoire
 * En production, remplacer par une base de données (JPA + UserRepository)
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

    private final InMemoryUserDetailsManager delegate;

    /**
     * Constructeur: initialise les utilisateurs de test
     * {noop} = No Operation password encoder (pas de hash)
     * En production, utiliser BCryptPasswordEncoder
     */
    public MyUserDetailsService() {
        // Utilisateur basique avec rôle USER
        UserDetails user = User.withUsername("user")
                .password("{noop}password")
                .roles("USER")
                .build();

        // Administrateur avec rôle ADMIN
        UserDetails admin = User.withUsername("admin")
                .password("{noop}admin123")
                .roles("ADMIN")
                .build();

        // Utilisateur avec plusieurs rôles
        UserDetails manager = User.withUsername("manager")
                .password("{noop}manager123")
                .roles("USER", "MANAGER")
                .build();

        this.delegate = new InMemoryUserDetailsManager(user, admin, manager);
    }

    /**
     * Charge un utilisateur par son username
     * @param username Username de l'utilisateur
     * @return UserDetails contenant les informations de l'utilisateur
     * @throws UsernameNotFoundException si l'utilisateur n'existe pas
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return delegate.loadUserByUsername(username);
    }
}
