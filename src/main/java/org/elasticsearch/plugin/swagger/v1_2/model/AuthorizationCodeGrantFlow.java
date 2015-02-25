package org.elasticsearch.plugin.swagger.v1_2.model;

import lombok.*;

/**
 * Provides details regarding the OAuth2's Authorization Code Grant flow model.
 */
@Data
@Builder
@NoArgsConstructor
@RequiredArgsConstructor
public class AuthorizationCodeGrantFlow extends SwaggerModel {
    /**
     * The token request endpoint definition.
     */
    @NonNull
    private TokenRequestEndpoint tokenRequestEndpoint;

    /**
     * The token endpoint definition.
     */
    @NonNull
    private TokenEndpoint tokenEndpoint;
}