package works.souljam.keycloak.naver;

import org.keycloak.broker.oidc.OAuth2IdentityProviderConfig;
import org.keycloak.broker.provider.AbstractIdentityProviderFactory;
import org.keycloak.broker.social.SocialIdentityProviderFactory;
import org.keycloak.models.IdentityProviderModel;
import org.keycloak.models.KeycloakSession;

public class NaverworksIdentityProviderFactory extends AbstractIdentityProviderFactory<NaverworksIdentityProvider> implements SocialIdentityProviderFactory<NaverworksIdentityProvider> {

    public static final String PROVIDER_ID = "naverworks";

    @Override
    public String getName() {
        return "NaverWorks";
    }

    @Override
    public NaverworksIdentityProvider create(KeycloakSession session, IdentityProviderModel model) {
        return new NaverworksIdentityProvider(session, new OAuth2IdentityProviderConfig(model));
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public OAuth2IdentityProviderConfig createConfig() {
        return new OAuth2IdentityProviderConfig();
    }
}

