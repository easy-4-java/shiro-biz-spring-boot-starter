package org.apache.shiro.spring.boot;

import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.permission.PermissionResolver;
import org.apache.shiro.authz.permission.RolePermissionResolver;
import org.apache.shiro.biz.authc.DefaultAuthenticationFailureHandler;
import org.apache.shiro.biz.authz.permission.BitAndWildPermissionResolver;
import org.apache.shiro.biz.authz.permission.DefaultRolePermissionResolver;
import org.apache.shiro.biz.realm.AuthorizingRealmListener;
import org.apache.shiro.spring.boot.autoconfigure.ShiroAutoConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Auto-configuration for Shiro business-layer beans including realm listeners, permission resolvers,
 * credentials matcher, and authentication failure handler.
 * <p>This configuration is applied before the web and core Shiro auto-configurations to ensure
 * that foundational beans are available for injection.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Configuration
@AutoConfigureBefore({ShiroBizWebAutoConfiguration.class, ShiroAutoConfiguration.class})
@ConditionalOnProperty(name = ShiroBizProperties.PREFIX, matchIfMissing = true)
@EnableConfigurationProperties({ ShiroBizProperties.class })
public class ShiroBizAutoConfiguration implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	/**
	 * Collects all {@link AuthorizingRealmListener} beans from the application context to listen for
	 * authentication success and failure events, enabling business-specific actions such as logging.
	 *
	 * @return a list of realm listeners
	 */
	@Bean("realmListeners")
	@ConditionalOnMissingBean(name = "realmListeners")
	public List<AuthorizingRealmListener> realmListeners() {

		List<AuthorizingRealmListener> realmListeners = new ArrayList<AuthorizingRealmListener>();

		Map<String, AuthorizingRealmListener> beansOfType = getApplicationContext()
				.getBeansOfType(AuthorizingRealmListener.class);
		if (!ObjectUtils.isEmpty(beansOfType)) {
			Iterator<Entry<String, AuthorizingRealmListener>> ite = beansOfType.entrySet().iterator();
			while (ite.hasNext()) {
				realmListeners.add(ite.next().getValue());
			}
		}

		return realmListeners;
	}


	/**
	 * Registers a {@link PermissionResolver} that supports both bit-based and wildcard permission resolution.
	 *
	 * @return the permission resolver
	 */
	@Bean
	@ConditionalOnMissingBean
	public PermissionResolver permissionResolver() {
		return new BitAndWildPermissionResolver();
	}

	/**
	 * Registers a {@link RolePermissionResolver} configured with default role-permission mappings
	 * from the {@link ShiroBizProperties}.
	 *
	 * @param bizProperties the Shiro business properties
	 * @return the role permission resolver
	 */
	@Bean
	@ConditionalOnMissingBean
	public RolePermissionResolver rolePermissionResolver(ShiroBizProperties bizProperties) {
		DefaultRolePermissionResolver permissionResolver = new DefaultRolePermissionResolver();
		permissionResolver.setDefaultRolePermissions(bizProperties.getDefaultRolePermissions());
		return permissionResolver;
	}

	/**
	 * Registers a default {@link CredentialsMatcher} that allows all credentials (no validation).
	 *
	 * @return the credentials matcher
	 */
	@Bean
	@ConditionalOnMissingBean
	public CredentialsMatcher credentialsMatcher() {
		return new AllowAllCredentialsMatcher();
	}

	/**
	 * Registers the default authentication failure handler.
	 *
	 * @return the default authentication failure handler
	 */
	@Bean
	protected DefaultAuthenticationFailureHandler defaultAuthenticationFailureHandler() {
		return new DefaultAuthenticationFailureHandler();
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}
