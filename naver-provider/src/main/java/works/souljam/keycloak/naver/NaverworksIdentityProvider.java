package works.souljam.keycloak.naver;

import com.fasterxml.jackson.databind.JsonNode; 
import org.keycloak.broker.oidc.AbstractOAuth2IdentityProvider;
import org.keycloak.broker.oidc.OAuth2IdentityProviderConfig;
import org.keycloak.broker.oidc.mappers.AbstractJsonUserAttributeMapper;
import org.keycloak.broker.provider.BrokeredIdentityContext;
import org.keycloak.broker.provider.IdentityBrokerException;
import org.keycloak.broker.provider.util.SimpleHttp;
import org.keycloak.broker.social.SocialIdentityProvider;
import org.keycloak.events.EventBuilder;
import org.keycloak.models.KeycloakSession;

public class NaverworksIdentityProvider extends AbstractOAuth2IdentityProvider implements SocialIdentityProvider {

	// 네이버 웍스
	public static final String AUTH_URL = "https://auth.worksmobile.com/oauth2/v2.0/authorize";
	public static final String TOKEN_URL = "https://auth.worksmobile.com/oauth2/v2.0/token";
	public static final String PROFILE_URL = "https://www.worksapis.com/v1.0/users/me";
	
	public static final String DEFAULT_SCOPE = "user";

	public NaverworksIdentityProvider(KeycloakSession session, OAuth2IdentityProviderConfig config) {
		super(session, config);
		
		config.setAuthorizationUrl(AUTH_URL);
		config.setTokenUrl(TOKEN_URL);
		config.setUserInfoUrl(PROFILE_URL);
	}

	@Override
	protected boolean supportsExternalExchange() {
		return true;
	}

	@Override
	protected String getProfileEndpointForValidation(EventBuilder event) {
		return PROFILE_URL;
	}

	@Override
	protected BrokeredIdentityContext extractIdentityFromProfile(EventBuilder event, JsonNode profile) {

		BrokeredIdentityContext user = new BrokeredIdentityContext(profile.get("userId").asText());

		String email = profile.get("email").asText();

		user.setIdpConfig(getConfig());
		user.setUsername(email);
		user.setEmail(email);
		user.setIdp(this);

		AbstractJsonUserAttributeMapper.storeUserProfileForMapper(user, profile, getConfig().getAlias());

		return user;
	}

	@Override
	protected BrokeredIdentityContext doGetFederatedIdentity(String accessToken) {
		try {

			// access_token query string 을 이용한 방법
			//JsonNode profile = SimpleHttp.doGet(PROFILE_URL, session).param("access_token", accessToken).asJson();

			// Bearer Token 을 이용하는 방법 
			JsonNode profile = SimpleHttp.doGet(PROFILE_URL, session).header("Authorization", "Bearer " + accessToken).asJson();
			BrokeredIdentityContext user = extractIdentityFromProfile(null, profile);

			return user;
		} catch (Exception e) {

			throw new IdentityBrokerException("Could not obtain user profile from Naver Works.", e);
		}
	}

	@Override
	protected String getDefaultScopes() {
		return "";
	}
}
