package works.souljam.keycloak.naver;

import org.keycloak.broker.oidc.mappers.AbstractJsonUserAttributeMapper;

/**
 * User attribute mapper.
 */
public class NaverworksUserAttributeMapper extends AbstractJsonUserAttributeMapper {

	private static final String[] cp = new String[] { NaverworksIdentityProviderFactory.PROVIDER_ID };

	@Override
	public String[] getCompatibleProviders() {
		return cp;
	}

	@Override
	public String getId() {
		return "naverworks-user-attribute-mapper";
	}

}
