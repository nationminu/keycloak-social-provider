# Custom Keycloak Provider

## 소셜 로그인
> keycloak 에서는 다양한 소셜 로그인을 지원한다. 대부분 해외 소셜 서비스의 로그인을 지원한다.
기본적으로 네이버와 카카오 같은 국내 서비스의 Provider는 지원하지 않는다. 한국에서는 네이버, 카카오 사용자 수를 무시할 수 없다.

## keycloak dependency
> pom.xml

```
		<dependency>
			<groupId>org.keycloak</groupId>
			<artifactId>keycloak-services</artifactId>
			<version>18.0.2</version> 
		</dependency>
		<dependency>
			<groupId>org.keycloak</groupId>
			<artifactId>keycloak-server-spi</artifactId>
			<version>18.0.2</version> 
		</dependency>
		<dependency>
			<groupId>org.keycloak</groupId>
			<artifactId>keycloak-server-spi-private</artifactId>
			<version>18.0.2</version> 
		</dependency>
```

## Custom Provider 구조

```
├── META-INF
│   └── services
│       └── org.keycloak.broker.social.SocialIdentityProviderFactory
└── theme-resources
    └── base
        └── admin
            ├── messages
            │   ├── admin-messages_en.properties
            │   └── messages_en.properties
            └── resources
                └── partials
                    ├── realm-identity-provider-customauth-ext.html
                    └── realm-identity-provider-customauth.html
```

## Provider SPI 설정
> 서비스 로더는 주로 애플리케이션 내부에서 플러그인을 제공할 때 사용하는데, 전체적인 원리는 특정 기능을 제공하기 위한 인터페이스가 있고, 다양한 벤더 회사들이 이 인터페이스를 기반으로 자신만의 구체 서비스를 구현하게 된다. 사용자 입장에서는 어떤 벤더 회사가 어떻게 구현을 했든 공통 인터페이스만 가지고 있으면 각 벤더 회사 서비스의 장단점을 고려해 원하는 구현체만 골라서 사용하면 된다. <BR>
> 서비스 로더가 META-INF/service 디렉터리에서 SPI 이름으로 된 파일에 적힌 클래스들을 로드한다. META-INF/services 아래 파일과 Custom Provider 구현체를 넣어준다.

```
> org.keycloak.broker.provider.IdentityProviderMapper
works.souljam.keycloak.naver.NaverUserAttributeMapper
works.souljam.keycloak.naver.NaverworksUserAttributeMapper

> org.keycloak.broker.social.SocialIdentityProviderFactory
works.souljam.keycloak.naver.NaverIdentityProviderFactory
works.souljam.keycloak.naver.NaverworksIdentityProviderFactory
```

## partials 설정
> Provider 에 대한 partials 을 구성한다.

```
> resoruces/themes/base/admin/resources/partials/
realm-identity-provider-naver.html
realm-identity-provider-naver-ext.html
realm-identity-provider-naverworks.html
realm-identity-provider-naverworks-ext.html
```

## 배포 
> Copy target/VERSION.jar to KEYCLOAK_HOME/standalone/deployments.<BR>
> Copy files from resources/keycloak/theme/base/admin/resources/partials to ${keycloak.home.dir}/themes/base/admin/resources/partials

## Referer
> https://github.com/danny8376/keycloak-social-baidu
> https://github.com/keycloak/keycloak/tree/18.0.2/services/src/main/java/org/keycloak/social

