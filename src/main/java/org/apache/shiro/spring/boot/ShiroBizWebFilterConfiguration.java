package org.apache.shiro.spring.boot;

import java.util.stream.Collectors;

import org.apache.shiro.biz.authz.principal.ShiroPrincipal;
import org.apache.shiro.biz.spring.ShiroFilterProxyFactoryBean;
import org.apache.shiro.biz.web.filter.HttpServletRequestEscapeHtml4Filter;
import org.apache.shiro.biz.web.filter.HttpServletRequestHeaderFilter;
import org.apache.shiro.biz.web.filter.HttpServletRequestMethodFilter;
import org.apache.shiro.biz.web.filter.HttpServletRequestReferrerFilter;
import org.apache.shiro.biz.web.filter.HttpServletSessionDequeFilter;
import org.apache.shiro.biz.web.filter.HttpServletSessionExpiredFilter;
import org.apache.shiro.biz.web.filter.HttpServletSessionStatusFilter;
import org.apache.shiro.biz.web.filter.authc.AuthenticatingFailureCounter;
import org.apache.shiro.biz.web.filter.authc.AuthenticatingFailureRequestCounter;
import org.apache.shiro.biz.web.filter.authc.AuthenticatingFailureSessionCounter;
import org.apache.shiro.biz.web.filter.authc.listener.LogoutListener;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.boot.biz.ShiroBizFilterFactoryBean;
import org.apache.shiro.spring.boot.biz.ShiroHttpServletHeaderProperties;
import org.apache.shiro.spring.boot.biz.ShiroHttpServletReferrerProperties;
import org.apache.shiro.spring.boot.biz.authc.BizLogoutFilter;
import org.apache.shiro.spring.boot.utils.JakartaFilterAdapter;
import org.apache.shiro.spring.boot.utils.StringUtils;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.AbstractShiroWebFilterConfiguration;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * Auto-configuration for Shiro web filters in a servlet environment, including logout, session management,
 * header/referrer validation, and authentication failure counting filters.
 * <p>Shiro ships many built-in filters for authentication and authorization (see
 * {@code org.apache.shiro.web.filter.mgt.DefaultFilter}). Custom filters registered via {@code @Bean} are
 * automatically added to the Spring Boot servlet filter chain, which means they apply to all URLs rather than
 * only the paths configured in Shiro. This configuration addresses that by registering filters as
 * {@link FilterRegistrationBean} instances with {@code enabled=false}, so they participate only in the
 * Shiro filter chain.</p>
 * @see <a href="https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-disable-registration-of-a-servlet-or-filter">
 *      Disable Registration of a Servlet or Filter</a>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Configuration
@AutoConfigureBefore( name = {
	"org.apache.shiro.spring.config.web.autoconfigure.ShiroWebFilterConfiguration" // shiro-spring-boot-web-starter
})
@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = ShiroBizProperties.PREFIX, value = "enabled", havingValue = "true")
@EnableConfigurationProperties({ ShiroBizProperties.class, ShiroHttpServletHeaderProperties.class, ShiroHttpServletReferrerProperties.class })
public class ShiroBizWebFilterConfiguration extends AbstractShiroWebFilterConfiguration {

	@Autowired
	private ShiroBizProperties bizProperties;

	/**
	 * Registers the system logout filter. Delegates to {@link BizLogoutFilter} and configures
	 * logout listeners, POST-only logout enforcement, and the post-logout redirect URL.
	 *
	 * @param logoutListenerProvider provider for optional logout listeners
	 * @return the logout filter registration bean
	 */
	@Bean("logout")
	@ConditionalOnMissingBean(name = "logout")
    /**
     * <p>Logout filter.</p>
     * @param logoutListenerProvider
     * @return the logout filter
     */
	public FilterRegistrationBean logoutFilter(ObjectProvider<LogoutListener> logoutListenerProvider){

		FilterRegistrationBean registration = new FilterRegistrationBean<>();
		BizLogoutFilter logoutFilter = new BizLogoutFilter();
		// 监听器
		logoutFilter.setLogoutListeners(logoutListenerProvider.stream().collect(Collectors.toList()));
		logoutFilter.setPostOnlyLogout(bizProperties.isPostOnlyLogout());
		//登录注销后的重定向地址：直接进入登录页面
		logoutFilter.setRedirectUrl(bizProperties.getRedirectUrl());

		registration.setFilter(new JakartaFilterAdapter(logoutFilter));
	    registration.setEnabled(false);
	    return registration;
	}

	/**
	 * Registers the HTML entity escaping filter that sanitizes request parameters.
	 *
	 * @return the escape HTML filter registration bean
	 */
	@Bean("escapeHtml4")
	@ConditionalOnMissingBean(name = "escapeHtml4")
    /**
     * <p>Escape html4 filter.</p>
     * @return the escape html4 filter
     */
	public FilterRegistrationBean escapeHtml4Filter(){
		FilterRegistrationBean registration = new FilterRegistrationBean<>();
		registration.setFilter(new JakartaFilterAdapter(new HttpServletRequestEscapeHtml4Filter()));
	    registration.setEnabled(false);
	    return registration;
	}

	/**
	 * Registers the HTTP header filter for enforcing access-control headers.
	 *
	 * @param properties the header configuration properties
	 * @return the header filter registration bean
	@Bean("headers")
	@ConditionalOnMissingBean(name = "headers")
	public FilterRegistrationBean headerFilter(ShiroHttpServletHeaderProperties properties){

		FilterRegistrationBean registration = new FilterRegistrationBean<>();
		HttpServletRequestHeaderFilter headFilter = new HttpServletRequestHeaderFilter(properties);
		registration.setFilter(new JakartaFilterAdapter(headFilter));
	    registration.setEnabled(false);
	    return registration;
	}

	/**
	 * Registers the HTTP method filter that restricts allowed request methods.
	 *
	 * @param properties the header configuration properties containing allowed methods
	 * @return the method filter registration bean
	@Bean("methods")
	@ConditionalOnMissingBean(name = "methods")
	public FilterRegistrationBean methodFilter(ShiroHttpServletHeaderProperties properties){

		FilterRegistrationBean registration = new FilterRegistrationBean<>();

		HttpServletRequestMethodFilter methodFilter = new HttpServletRequestMethodFilter();
		String methods = properties.getAccessControlAllowMethods();
		if (methods != null && !methods.isEmpty()) {
			methodFilter.setAllowedHTTPMethods(StringUtils.tokenizeToStringArray(methods));
		}

		registration.setFilter(new JakartaFilterAdapter(methodFilter));
	    registration.setEnabled(false);
	    return registration;
	}

	/**
	 * Registers the HTTP referrer filter for validating request referrer headers.
	 *
	 * @param properties the referrer configuration properties
	 * @return the referrer filter registration bean
	@Bean("referrers")
	@ConditionalOnMissingBean(name = "referrers")
	public FilterRegistrationBean referrerFilter(ShiroHttpServletReferrerProperties properties){

		FilterRegistrationBean registration = new FilterRegistrationBean<>();
		HttpServletRequestReferrerFilter referrerFilter = new HttpServletRequestReferrerFilter(properties);
		registration.setFilter(new JakartaFilterAdapter(referrerFilter));
	    registration.setEnabled(false);
	    return registration;
	}

	/**
	 * Registers the session online status filter to handle forced session logout scenarios.
	 *
	 * @param sessionManager the Shiro session manager
	 * @return the session status filter registration bean
	 */
	@Bean("sessionStatus")
	@ConditionalOnBean({CacheManager.class, SessionManager.class})
	//@ConditionalOnMissingBean(name = "sessionStatus")
    /**
     * <p>Session online filter.</p>
     * @param sessionManager
     * @return the session online filter
     */
	public FilterRegistrationBean sessionOnlineFilter(SessionManager sessionManager){

		FilterRegistrationBean registration = new FilterRegistrationBean<>();

		HttpServletSessionStatusFilter sessionOnlineFilter = new HttpServletSessionStatusFilter();
		sessionOnlineFilter.setLoginUrl(bizProperties.getLoginUrl());
		sessionOnlineFilter.setSessionManager(sessionManager);

		registration.setFilter(new JakartaFilterAdapter(sessionOnlineFilter));
	    registration.setEnabled(false);
	    return registration;
	}

	/**
	 * Registers the session deque filter to enforce unique session login, kicking out earlier sessions
	 * when the maximum concurrent session limit is reached.
	 *
	 * @param cacheManager the cache manager for session tracking
	 * @param sessionManager the Shiro session manager
	 * @return the session deque filter registration bean
	 */
	@Bean("sessionDeque")
	@ConditionalOnBean({CacheManager.class, SessionManager.class})
	//@ConditionalOnMissingBean(name = "sessionDeque")
    /**
     * <p>Session deque filter.</p>
     * @param cacheManager
     * @param sessionManager
     * @return the session deque filter
     */
	public FilterRegistrationBean sessionDequeFilter(CacheManager cacheManager, SessionManager sessionManager){

		FilterRegistrationBean registration = new FilterRegistrationBean<>();

		HttpServletSessionDequeFilter sessionDequeFilter = new HttpServletSessionDequeFilter() {

			@Override
    /**
     * <p>Returns the session deque cache key.</p>
     * @param principal
     * @return the get session deque cache key
     */
			protected String getSessionDequeCacheKey(Object principal) {
				ShiroPrincipal sp = (ShiroPrincipal) principal;
				return sp.getUserid();
			}

		};

		sessionDequeFilter.setCacheManager(cacheManager);
		sessionDequeFilter.setKickoutFirst(bizProperties.isKickoutFirst());
		sessionDequeFilter.setSessionDequeCacheName(bizProperties.getSessionDequeCacheName());
		sessionDequeFilter.setSessionManager(sessionManager);
		sessionDequeFilter.setSessionMaximumKickout(bizProperties.getSessionMaximumKickout());
		sessionDequeFilter.setRedirectUrl(bizProperties.getRedirectUrl());

		registration.setFilter(new JakartaFilterAdapter(sessionDequeFilter));

	    registration.setEnabled(false);
	    return registration;
	}

	/**
	 * Registers the session expired filter to gracefully handle session expiration during AJAX requests.
	 *
	 * @return the session expired filter registration bean
	 */
	@Bean("sessionExpired")
	@ConditionalOnMissingBean(name = "sessionExpired")
    /**
     * <p>Session expired filter.</p>
     * @return the session expired filter
     */
	public FilterRegistrationBean sessionExpiredFilter(){

		FilterRegistrationBean registration = new FilterRegistrationBean<>();
		registration.setFilter(new JakartaFilterAdapter(new HttpServletSessionExpiredFilter()));

	    registration.setEnabled(false);
	    return registration;
	}

	/**
	 * Registers the authentication failure counter. Uses request-based counting for stateless sessions
	 * and session-based counting otherwise.
	 *
	 * @return the authentication failure counter
	 */
	@Bean
	@ConditionalOnMissingBean
    /**
     * <p>Authc failure counter.</p>
     * @return the authc failure counter
     */
	public AuthenticatingFailureCounter authcFailureCounter() {
		if (bizProperties.isSessionStateless()) {
			return new AuthenticatingFailureRequestCounter();
		}
		return new AuthenticatingFailureSessionCounter();
	}

	/**
	 * Creates and configures the {@link ShiroFilterFactoryBean} with the security manager,
	 * login/success/unauthorized URLs, and the filter chain definition map.
	 *
	 * @return the configured Shiro filter factory bean
	 */
	@Bean
    @ConditionalOnMissingBean
    @Override
    /**
     * <p>Shiro filter factory bean.</p>
     * @return the shiro filter factory bean
     */
    protected ShiroFilterFactoryBean shiroFilterFactoryBean() {

		ShiroFilterProxyFactoryBean filterFactoryBean = new ShiroBizFilterFactoryBean();
		filterFactoryBean.setStaticSecurityManagerEnabled(bizProperties.isStaticSecurityManagerEnabled());

		//登录地址：会话不存在时访问的地址
        filterFactoryBean.setLoginUrl(bizProperties.getLoginUrl());
  		//系统主页：登录成功后跳转路径
  		filterFactoryBean.setSuccessUrl(bizProperties.getSuccessUrl());
  		//异常页面：无权限时的跳转路径
  		filterFactoryBean.setUnauthorizedUrl(bizProperties.getUnauthorizedUrl());

  		//必须设置 SecurityManager
 		filterFactoryBean.setSecurityManager(securityManager);
 		//拦截规则
 		filterFactoryBean.setFilterChainDefinitionMap(shiroFilterChainDefinition.getFilterChainMap());

 		return filterFactoryBean;

    }

    /**
     * Registers the main Shiro filter with the servlet container at the lowest precedence.
     *
     * @return the Shiro filter registration bean
     * @throws Exception if the filter factory bean cannot be created
     */
    @Bean(name = "filterShiroFilterRegistrationBean")
    @ConditionalOnMissingBean
    /**
     * <p>Filter shiro filter registration bean.</p>
     * @return the filter shiro filter registration bean
     */
    protected FilterRegistrationBean filterShiroFilterRegistrationBean() throws Exception {

        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new JakartaFilterAdapter(shiroFilterFactoryBean().getObject()));
        filterRegistrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);

        return filterRegistrationBean;
    }

}
