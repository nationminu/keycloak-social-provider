package works.souljam.keycloak.kakao;

import org.keycloak.broker.oidc.mappers.AbstractJsonUserAttributeMapper;

/**
 * User attribute mapper.
 */
public class KakaoUserAttributeMapper extends AbstractJsonUserAttributeMapper {

	private static final String[] cp = new String[] { KakaoIdentityProviderFactory.PROVIDER_ID };

	@Override
	public String[] getCompatibleProviders() {
		return cp;
	}

	@Override
	public String getId() {
		return "kakao-user-attribute-mapper";
	}
}
