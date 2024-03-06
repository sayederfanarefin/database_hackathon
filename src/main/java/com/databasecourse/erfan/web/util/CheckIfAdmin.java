package com.databasecourse.erfan.web.util;

import com.databasecourse.erfan.Constants;
import org.springframework.security.core.context.SecurityContextHolder;

public class CheckIfAdmin {
    public static boolean isAdmin(){
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals(Constants.ADMIN_ROLE));
    }
}
