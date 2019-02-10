package org.zavod.util;

import org.springframework.security.access.AccessDeniedException;
import org.zavod.AuthorizedUser;
import org.zavod.model.MailEntity;
import org.zavod.model.Role;

public class ValidationUtil {

    private ValidationUtil(){}

    public static void checkAccessPdf(MailEntity mail, AuthorizedUser authUser) {
        if (isUser(authUser) && !mail.getAuthor().getId().equals(authUser.getId())) {
            throw new AccessDeniedException("Access level not lower than MANAGER is required");
        }
    }

    public static void checkAccessUpdate(MailEntity existing, AuthorizedUser authUser) {
        if (isUser(authUser) && (authUser.getId() != existing.getAuthor().getId() || existing.isAccept())) {
            throw new AccessDeniedException("Access Denied for USER");
        }
    }

    private static boolean isUser(AuthorizedUser authUser) {
        return authUser.getAuthorities().contains(Role.ROLE_USER);
    }
}
