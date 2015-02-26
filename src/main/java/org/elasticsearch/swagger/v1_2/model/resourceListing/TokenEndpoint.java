package org.elasticsearch.swagger.v1_2.model.resourceListing;

import lombok.*;
import org.elasticsearch.swagger.v1_2.model.SwaggerModel;

/**
 * Provides details regarding the OAuth2's Token Endpoint.
 */
@Data
@Builder
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class TokenEndpoint extends SwaggerModel {
    /**
     * The URL of the token endpoint for the authentication code grant flow.
     */
    @NonNull
    private String url;

    /**
     * An optional alternative name to standard "access_token" OAuth2 parameter.
     */
    private String tokenName;
}