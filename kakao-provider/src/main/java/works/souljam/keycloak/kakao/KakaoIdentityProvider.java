package works.souljam.keycloak.kakao;

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

public class KakaoIdentityProvider extends AbstractOAuth2IdentityProvider implements SocialIdentityProvider {
 
	// 카카오 Oauth
	public static final String AUTH_URL = "https://kauth.kakao.com/oauth/authorize";
	public static final String TOKEN_URL = "https://kauth.kakao.com/oauth/token";
	public static final String PROFILE_URL = "https://kapi.kakao.com/v2/user/me";

	// 카카오 개발자 도구 > 내 애플리케이션 > 제품 설정 > 카카오 로그인> 동의항목(account_email, profile_nickname)
	public static final String DEFAULT_SCOPE = "account_email profile_nickname openid";

	public KakaoIdentityProvider(KeycloakSession session, OAuth2IdentityProviderConfig config) {
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

		BrokeredIdentityContext user = new BrokeredIdentityContext(profile.get("id").asText());

		String email = profile.get("kakao_account").get("email").asText();

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
			// access_token query string 을 이용한 방 
			//JsonNode profile = SimpleHttp.doGet(PROFILE_URL, session).param("access_token", accessToken).asJson();
			
			// Bearer Token 을 이용하는 방법 
			JsonNode profile = SimpleHttp.doGet(PROFILE_URL, session).header("Authorization", "Bearer " + accessToken).asJson();

			BrokeredIdentityContext user = extractIdentityFromProfile(null, profile);

			return user;

		} catch (Exception e) {

			throw new IdentityBrokerException("Could not obtain user profile from Kakao.", e);
		}
	}

	@Override
	protected String getDefaultScopes() {
		return "";
	}
}
