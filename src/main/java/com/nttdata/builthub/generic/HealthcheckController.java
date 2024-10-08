package com.nttdata.builthub.generic;

import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
public class HealthcheckController {
    @RequestMapping(value="/check", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> check() {
        KeycloakSecurityContext keycloakSecurityContext = this.getSecurityContext();
        AccessToken accessToken = keycloakSecurityContext.getToken();

        return ResponseEntity.ok()
                .body("{\"message\" : \"Well done " + accessToken.getPreferredUsername() + " (" + accessToken.getEmail() + ") !!\"}");
    }

    private KeycloakSecurityContext getSecurityContext() {
        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return (KeycloakSecurityContext) request.getAttribute(KeycloakSecurityContext.class.getName());
    }
}
