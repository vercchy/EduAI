package finku.ukim.mk.eduai.security;

import org.springframework.security.core.Authentication;

public class SecurityUtils {
    public static String getEmail(Authentication authentication) {
        if (authentication == null) {
            return null;
        }
        return authentication.getName();
    }

    public static String getRole(Authentication authentication) {
        if (authentication == null || authentication.getAuthorities().isEmpty()) {
            return null;
        }

        return authentication.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
    }
}
