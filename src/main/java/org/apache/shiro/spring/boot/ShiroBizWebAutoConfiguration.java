package org.apache.shiro.spring.boot;

import com.google.common.collect.Lists;
import org.apache.commons.collections.MapUtils;
import org.apache.shiro.authc.AuthenticationListener;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.biz.authc.DefaultAuthenticationFailureHandler;
import org.apache.shiro.biz.authc.pam.DefaultModularRealmAuthenticator;
import org.apache.shiro.biz.session.DefaultSessionListener;
import org.apache.shiro.biz.session.mgt.SimpleOnlineSessionFactory;
import org.apache.shiro.biz.web.mgt.SessionCreationEnabledSubjectFactory;
import org.apache.shiro.mgt.*;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.*;
import org.apache.shiro.session.mgt.eis.*;
import org.apache.shiro.spring.config.ShiroAnnotationProcessorConfiguration;
import org.apache.shiro.spring.config.web.autoconfigure.ShiroWebAutoConfiguration;
import org.apache.shiro.spring.config.web.autoconfigure.ShiroWebMvcAutoConfiguration;
import org.apache.shiro.spring.web.config.AbstractShiroWebConfiguration;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.micrometer.metrics.autoconfigure.CompositeMeterRegistryAutoConfiguration;
import org.springframework.boot.micrometer.metrics.autoconfigure.MetricsAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Auto-configuration for Shiro web-layer beans in a servlet environment, including the security manager,
 * session manager, authenticator, subject factory, and filter chain definition.
 * <p>This configuration extends {@link AbstractShiroWebConfiguration} and is applied after the web MVC
 * and metrics auto-configurations to ensure all required dependencies are available.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Configuration
@AutoConfigureBefore({ ShiroWebAutoConfiguration.class, ShiroAnnotationProcessorConfiguration.class })
@AutoConfigureAfter({ MetricsAutoConfiguration.class, CompositeMeterRegistryAutoConfiguration.class, ShiroWebMvcAutoConfiguration.class })
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnProperty(name = "shiro.web.enabled", matchIfMissing = true)
@EnableConfigurationProperties({ ShiroBizProperties.class })
public class ShiroBizWebAutoConfiguration extends AbstractShiroWebConfiguration implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Autowired
	private ShiroBizProperties bizProperties;
	@Autowired(required = false)
	private SessionValidationScheduler sessionValidationScheduler;

	/**
	 * Collects all {@link AuthenticationListener} beans from the application context.
	 *
	 * @return a list of authentication listeners
	 */
	protected List<AuthenticationListener> authenticationListeners() {
		List<AuthenticationListener> authenticationListeners = Lists.newLinkedList();
		Map<String, AuthenticationListener> beansOfType = getApplicationContext().getBeansOfType(AuthenticationListener.class);
		if (!ObjectUtils.isEmpty(beansOfType)) {
			Iterator<Entry<String, AuthenticationListener>> ite = beansOfType.entrySet().iterator();
			while (ite.hasNext()) {
				authenticationListeners.add(ite.next().getValue());
			}
		}
		return authenticationListeners;
	}

	@Bean
	@ConditionalOnMissingBean
	@Override
    /**
     * <p>Authenticator.</p>
     * @return the authenticator
     */
	protected Authenticator authenticator() {
		ModularRealmAuthenticator authenticator = new DefaultModularRealmAuthenticator();
		authenticator.setAuthenticationStrategy(authenticationStrategy());
		authenticator.setAuthenticationListeners(authenticationListeners());
		return authenticator;
	}

	@Bean
	@ConditionalOnMissingBean
	@Override
    /**
     * <p>Session factory.</p>
     * @return the session factory
     */
	protected SessionFactory sessionFactory() {
		return new SimpleOnlineSessionFactory();
	}

	@Bean
	@ConditionalOnMissingBean
    /**
     * <p>Session id generator.</p>
     * @return the session id generator
     */
	protected SessionIdGenerator sessionIdGenerator() {
		return new JavaUuidSessionIdGenerator();
	}

	@Bean
	@ConditionalOnMissingBean
	@Override
    /**
     * <p>Session d a o.</p>
     * @return the session d a o
     */
	protected SessionDAO sessionDAO() {
		// 缓存存在的时候使用外部Session管理器
		if (useNativeSessionManager && bizProperties.isSessionCachingEnabled() && cacheManager != null) {
			CachingSessionDAO sessionDAO = new EnterpriseCacheSessionDAO();
			sessionDAO.setCacheManager(cacheManager);
			sessionDAO.setActiveSessionsCacheName(bizProperties.getActiveSessionsCacheName());
			sessionDAO.setSessionIdGenerator(sessionIdGenerator());
			return sessionDAO;
		}
		return new MemorySessionDAO();
	}

	@Bean
	@ConditionalOnMissingBean
	@Override
    /**
     * <p>Subject d a o.</p>
     * @return the subject d a o
     */
	protected SubjectDAO subjectDAO() {
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        subjectDAO.setSessionStorageEvaluator(sessionStorageEvaluator());
        return subjectDAO;
    }

	@Bean
	@ConditionalOnMissingBean
	@Override
    /**
     * <p>Session storage evaluator.</p>
     * @return the session storage evaluator
     */
	protected SessionStorageEvaluator sessionStorageEvaluator() {
		DefaultSessionStorageEvaluator sessionStorageEvaluator = new DefaultSessionStorageEvaluator();
		// 无状态逻辑情况下不持久化session
		sessionStorageEvaluator.setSessionStorageEnabled(bizProperties.isSessionStorageEnabled());
		return sessionStorageEvaluator;
	}

	/**
	 * Collects all {@link SessionListener} beans from the application context and adds a default session listener.
	 *
	 * @return a list of session listeners
	 */
	protected List<SessionListener> sessionListeners() {
		List<SessionListener> sessionListeners = Lists.newLinkedList();
		Map<String, SessionListener> beansOfType = getApplicationContext().getBeansOfType(SessionListener.class);
		if (!ObjectUtils.isEmpty(beansOfType)) {
			Iterator<Entry<String, SessionListener>> ite = beansOfType.entrySet().iterator();
			while (ite.hasNext()) {
				sessionListeners.add(ite.next().getValue());
			}
		}
		DefaultSessionListener defSessionListener = new DefaultSessionListener();
		sessionListeners.add(defSessionListener);
		return sessionListeners;
	}

	@Bean
	@ConditionalOnMissingBean
	@Override
    /**
     * <p>Session manager.</p>
     * @return the session manager
     */
	protected SessionManager sessionManager() {
		SessionManager sessionManager = super.sessionManager();
		if (sessionManager instanceof AbstractSessionManager) {
			AbstractSessionManager absSessionManager = (AbstractSessionManager) sessionManager;
			absSessionManager.setGlobalSessionTimeout(bizProperties.getSessionTimeout());
		}
		if (sessionManager instanceof AbstractNativeSessionManager) {
			AbstractNativeSessionManager sManager = (AbstractNativeSessionManager) sessionManager;
			sManager.setSessionListeners(sessionListeners());
		}
		if (sessionManager instanceof AbstractValidatingSessionManager) {
			AbstractValidatingSessionManager sManager = (AbstractValidatingSessionManager) sessionManager;
			sManager.setSessionValidationInterval(bizProperties.getSessionValidationInterval());
			sManager.setSessionValidationSchedulerEnabled(bizProperties.isSessionValidationSchedulerEnabled());
			sManager.setSessionValidationScheduler(sessionValidationScheduler);
		}

		if (sessionManager instanceof DefaultSessionManager) {
			DefaultSessionManager defSessionManager = (DefaultSessionManager) sessionManager;
			if (cacheManager != null) {
				defSessionManager.setCacheManager(cacheManager);
			}
			defSessionManager.setSessionDAO(sessionDAO());
			return defSessionManager;
		}
		return sessionManager;
	}

	@Bean
	@ConditionalOnMissingBean
	@Override
    /**
     * <p>Security manager.</p>
     * @param realms
     * @return the security manager
     */
	protected SessionsSecurityManager securityManager(List<Realm> realms) {

		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

		securityManager.setAuthenticator(authenticator());
		securityManager.setAuthorizer(authorizer());
		if (cacheManager != null) {
			securityManager.setCacheManager(cacheManager);
		}
		securityManager.setEventBus(eventBus);
		securityManager.setRealms(realms);
		securityManager.setRememberMeManager(rememberMeManager());
		securityManager.setSessionManager(sessionManager());
		securityManager.setSubjectDAO(subjectDAO());
		securityManager.setSubjectFactory(subjectFactory());
		return securityManager;

	}

	@Bean
	@ConditionalOnMissingBean
	@Override
    /**
     * <p>Subject factory.</p>
     * @return the subject factory
     */
	protected SubjectFactory subjectFactory() {
		return new SessionCreationEnabledSubjectFactory(bizProperties.isSessionCreationEnabled());
	}

	/**
	 * Defines the Shiro filter chain definition, applying path definitions from properties and
	 * any registered {@link FilterChainDefinitionConfigurer} beans.
	 *
	 * @return the Shiro filter chain definition
	 */
	@Bean
	@ConditionalOnMissingBean
	@Override
    /**
     * <p>Shiro filter chain definition.</p>
     * @return the shiro filter chain definition
     */
	protected ShiroFilterChainDefinition shiroFilterChainDefinition() {
		DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
		if (MapUtils.isNotEmpty(bizProperties.getFilterChainDefinitionMap())) {
			chainDefinition.addPathDefinitions(bizProperties.getFilterChainDefinitionMap());
		}
		try {

			Map<String, FilterChainDefinitionConfigurer> configurers = getApplicationContext().getBeansOfType(FilterChainDefinitionConfigurer.class);
			if(MapUtils.isNotEmpty(configurers)) {
				Iterator<Entry<String, FilterChainDefinitionConfigurer>> ite = configurers.entrySet().iterator();
				while (ite.hasNext()) {
					Entry<String, FilterChainDefinitionConfigurer> entry = ite.next();
					entry.getValue().configurePathDefinition(chainDefinition);
				}
			}

		} catch (BeansException e) {
		}
		return chainDefinition;
	}

	@Bean
    /**
     * <p>Default authentication failure handler.</p>
     * @return the default authentication failure handler
     */
	protected DefaultAuthenticationFailureHandler defaultAuthenticationFailureHandler() {
		return new DefaultAuthenticationFailureHandler();
	}

	/*
	 * @Bean
	 *
	 * @ConditionalOnMissingBean(name = "sessionCookieTemplate")
	 *
	 * @Override protected Cookie sessionCookieTemplate() { if
	 * (sessionManagerCookieProperties != null &&
	 * StringUtils.hasText(sessionManagerCookieProperties.getName()) &&
	 * sessionManagerCookieProperties.getMaxAge() > 0) { return
	 * buildCookie(sessionManagerCookieProperties.getName(),
	 * sessionManagerCookieProperties.getMaxAge(),
	 * sessionManagerCookieProperties.getPath(),
	 * sessionManagerCookieProperties.getDomain(),
	 * sessionManagerCookieProperties.isSecure()); } return
	 * super.sessionCookieTemplate(); }
	 *
	 * @Bean
	 *
	 * @ConditionalOnMissingBean(name = "rememberMeCookieTemplate")
	 *
	 * @Override protected Cookie rememberMeCookieTemplate() { if
	 * (rememberMeProperties != null &&
	 * StringUtils.hasText(rememberMeProperties.getName()) &&
	 * rememberMeProperties.getMaxAge() > 0) { return
	 * buildCookie(rememberMeProperties.getName(), rememberMeProperties.getMaxAge(),
	 * rememberMeProperties.getPath(), rememberMeProperties.getDomain(),
	 * rememberMeProperties.isSecure()); } return super.rememberMeCookieTemplate();
	 *
	 * }
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

    /**
     * <p>Returns the application context.</p>
     * @return the get application context
     */
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

    /**
     * <p>Returns the biz properties.</p>
     * @return the get biz properties
     */
	public ShiroBizProperties getBizProperties() {
		return bizProperties;
	}

    /**
     * <p>Sets the biz properties.</p>
     * @param bizProperties
     */
	public void setBizProperties(ShiroBizProperties bizProperties) {
		this.bizProperties = bizProperties;
	}

}
