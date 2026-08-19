/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.shiro.spring.boot;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletResponse;

import org.apache.shiro.biz.authc.credential.CredentialsRetryLimitCredentialsMatcher;
import org.apache.shiro.biz.web.filter.HttpServletSessionDequeFilter;
import org.apache.shiro.biz.web.filter.authc.AbstractTrustableAuthenticatingFilter;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(ShiroBizProperties.PREFIX)
/**
 * <p>Configuration properties for ShiroBiz.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class ShiroBizProperties {

	public static final String PREFIX = "shiro";
	public static final long DEFAULT_CAPTCHA_TIMEOUT = 60 * 1000;

	protected static final long MILLIS_PER_SECOND = 1000;
	protected static final long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;
	protected static final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;

	/**
	 * Default main session timeout value, equal to {@code 30} minutes.
	 */
	public static final long DEFAULT_GLOBAL_SESSION_TIMEOUT = 30 * MILLIS_PER_MINUTE;
	/**
	 * Default session validation interval value, equal to {@code 30} seconds.
	 */
	private static final long DEFAULT_SESSION_VALIDATION_INTERVAL = 30 * MILLIS_PER_SECOND;

	public static final List<String> DEFAULT_IGNORED = Arrays.asList("/**/favicon.ico", "/assets/**", "/webjars/**");

	/*
	 * ============================== Shiro Basic =================================
	 */
	/**
	 * The name of the session cache, defaults to {@link CachingSessionDAO#ACTIVE_SESSION_CACHE_NAME}.
	 */
	private String activeSessionsCacheName = CachingSessionDAO.ACTIVE_SESSION_CACHE_NAME;
	/**
	 * The cache used by this realm to store AuthorizationInfo instances associated
	 * with individual Subject principals.
	 */
	private boolean authorizationCachingEnabled = false;
	/**
	 * the name of a authorization {@link Cache} to lookup from any available
	 */
	private String authorizationCacheName = "shiro-authorizationCache";
	/**
	 * Whether authentication caching should be utilized
	 */
	private boolean authenticationCachingEnabled = false;
	/**
	 * the name of a authentication {@link Cache} to lookup from any available
	 */
	private String authenticationCacheName = "shiro-authenticationCache";
	/** 
	 * Whether to enable the authentication authorization cache 
	 */
	private boolean cachingEnabled = false;
	/**
	 * Whether to enable captcha
	 */
	private boolean captchaEnabled = false;
	/**
	 * The request parameter name of the captcha
	 */
	private String captchaParamName = AbstractTrustableAuthenticatingFilter.DEFAULT_CAPTCHA_PARAM;
	
	/**
     * The credentials retry limit, defaults to {@link CredentialsRetryLimitCredentialsMatcher#CREDENTIALS_RETRY_TIMES_LIMIT}.
     */
	protected int credentialsRetryTimesLimit = CredentialsRetryLimitCredentialsMatcher.CREDENTIALS_RETRY_TIMES_LIMIT;
	 /**
     * The name of the retry times, defaults to {@link CredentialsRetryLimitCredentialsMatcher#CREDENTIALS_RETRY_CACHE_NAME}.
     */
	protected String credentialsRetryCacheName = CredentialsRetryLimitCredentialsMatcher.CREDENTIALS_RETRY_CACHE_NAME;
	
	/**
	 * The default permissions for authenticated role
	 */
	private Map<String /* role */, String /* permissions */> defaultRolePermissions = new LinkedHashMap<String, String>();;
	/**
	 * Enable Shiro Biz.
	 */
	private boolean enabled = false;
	/**
	 * Failure Url: Jump path when authentication fails
	 */
	private String failureUrl;
	/**
	 * filter chain
	 */
	private Map<String /* pattern */, String /* Chain names */> filterChainDefinitionMap = new LinkedHashMap<String, String>();
	/**
     * The login url to used to authenticate a user, used when redirecting users if authentication is required.
     */
	private String loginUrl = AccessControlFilter.DEFAULT_LOGIN_URL;
	/**
     * Due to browser pre-fetching, using a GET requests for logout my cause a user to be logged accidentally, for example:
     * out while typing in an address bar.  If <code>postOnlyLogout</code> is <code>true</code>. Only POST requests will cause
     * a logout to occur.
     */
    private boolean postOnlyLogout = false;
    /**
     * The URL to where the user will be redirected after logout.
     */
	private String redirectUrl = LogoutFilter.DEFAULT_REDIRECT_URL;
	/**
	 * The attribute name of Retry Times 
	 */
	private String retryTimesKeyAttribute = AbstractTrustableAuthenticatingFilter.DEFAULT_RETRY_TIMES_KEY_ATTRIBUTE_NAME;
    /** 
     * Maximum number of retry to login . 
     */
	private int retryTimesWhenAccessDenied = 3;
	/**
	 * Whether Session caching should be utilized
	 */
	private boolean sessionCachingEnabled = false;
	/**
	 * Whether or not the constructed {@code Subject} instance should be allowed to create a session,
     * {@code false} otherwise.
	 */
	private boolean sessionCreationEnabled = true;
	/** 
	 * The data object cache name of session control filter 
	 */
	private String sessionDequeCacheName = HttpServletSessionDequeFilter.DEFAULT_SESSION_DEQUE_CACHE_NAME;

    /**
     * Whether or not to bind the constructed SecurityManager instance to static memory (via
     * SecurityUtils.setSecurityManager).  This was added to support https://issues.apache.org/jira/browse/SHIRO-287
     * @since 1.2
     */
    private boolean staticSecurityManagerEnabled;
    
	/** 
	 * Whether to kickout the first login session. 
	 */
    private boolean kickoutFirst = false;
    /** 
     * Maximum number of sessions for the same account . 
     */
	private int sessionMaximumKickout = 1;
	/**
     * Global policy determining if Subject sessions may be used to persist Subject state if the Subject's Session
     * does not yet exist.
     */
    private boolean sessionStorageEnabled = true;
    /** 
     * Whether stateless session
     */
	private boolean sessionStateless = false;
	/** 
	 * Default main session timeout value, equal to {@code 30} minutes. 
	 */
	private long sessionTimeout = DEFAULT_GLOBAL_SESSION_TIMEOUT;
	/** 
	 * Default session validation interval value, equal to {@code 30} seconds. 
	 */
	private long sessionValidationInterval = DEFAULT_SESSION_VALIDATION_INTERVAL;
	/** 
	 * Whether to open the session timer cleaner
	 */
	private boolean sessionValidationSchedulerEnabled = true;
	/** 
	 * Redirect address after successful login
	 */
	private String successUrl = AuthenticationFilter.DEFAULT_SUCCESS_URL;
	/**
     * The URL to which users should be redirected if they are denied access to an underlying path or resource,
     * {@code null} by default which will issue a raw {@link HttpServletResponse#SC_UNAUTHORIZED} response
     * (401 Unauthorized).
     */
    private String unauthorizedUrl;
	/** 
	 * Whether to enable user unique login, if true, the last login will kick out the previous Session 
	 */
	private boolean uniqueSessin = false;
	/** 
	 * Whether use native session manager
	 */
	private boolean userNativeSessionManager = false;
	
	/**
	 * Constructs a new shiro biz properties instance.
	 *
	 */
	public ShiroBizProperties() {

		for (String ingored : DEFAULT_IGNORED) {
			filterChainDefinitionMap.put(ingored, "anon");
		}
		
	}

    /**
     * <p>Returns the active sessions cache name.</p>
     * @return the get active sessions cache name
     */
	public String getActiveSessionsCacheName() {
		return activeSessionsCacheName;
	}

    /**
     * <p>Sets the active sessions cache name.</p>
     * @param activeSessionsCacheName
     */
	public void setActiveSessionsCacheName(String activeSessionsCacheName) {
		this.activeSessionsCacheName = activeSessionsCacheName;
	}

    /**
     * <p>Returns the authorization cache name.</p>
     * @return the get authorization cache name
     */
	public String getAuthorizationCacheName() {
		return authorizationCacheName;
	}

    /**
     * <p>Sets the authorization cache name.</p>
     * @param authorizationCacheName
     */
	public void setAuthorizationCacheName(String authorizationCacheName) {
		this.authorizationCacheName = authorizationCacheName;
	}
	
	/**
	 * <p>Returns {@code true} if authorization caching should be utilized if a
	 * {@link CacheManager} has been configured, {@code false} otherwise.
	 * </p>
	 * The default value is {@code true}.
	 *
	 * @return {@code true} if authorization caching should be utilized,
	 *         {@code false} otherwise.
	 */
	public boolean isAuthorizationCachingEnabled() {
		return isCachingEnabled() && authorizationCachingEnabled;
	}

	/**
	 * <p>Sets whether or not authorization caching should be utilized if a
	 * {@link CacheManager} has been configured, {@code false} otherwise.
	 * </p>
	 * The default value is {@code true}.
	 *
	 * @param authenticationCachingEnabled
	 *            the value to set
	 */
	public void setAuthorizationCachingEnabled(boolean authenticationCachingEnabled) {
		this.authorizationCachingEnabled = authenticationCachingEnabled;
		if (authenticationCachingEnabled) {
			setCachingEnabled(true);
		}
	}

	/**
	 * <p>Returns {@code true} if authentication caching should be utilized if a
	 * {@link CacheManager} has been configured, {@code false} otherwise.
	 * </p>
	 * The default value is {@code true}.
	 *
	 * @return {@code true} if authentication caching should be utilized,
	 *         {@code false} otherwise.
	 */
	public boolean isAuthenticationCachingEnabled() {
		return this.authenticationCachingEnabled && isCachingEnabled();
	}

	/**
	 * <p>Sets whether or not authentication caching should be utilized if a
	 * {@link CacheManager} has been configured, {@code false} otherwise.
	 * </p>
	 * <p>The default value is {@code false} to retain backwards compatibility with
	 * Shiro 1.1 and earlier.
	 * </p>
	 * <b>WARNING:</b> Only set this property to {@code true} if safe caching
	 * conditions apply, as documented at the top of this page in the class-level
	 * JavaDoc.
	 *
	 * @param authenticationCachingEnabled
	 *            the value to set
	 */
	public void setAuthenticationCachingEnabled(boolean authenticationCachingEnabled) {
		this.authenticationCachingEnabled = authenticationCachingEnabled;
		if (authenticationCachingEnabled) {
			setCachingEnabled(true);
		}
	}

	/**
	 * <p>Returns the name of a {@link Cache} to lookup from any available
	 * cacheManager if a cache is not explicitly
	 * configured via org.apache.shiro.cache.Cache.
	 * </p>
	 * <p>This name will only be used to look up a cache if authentication caching is
	 * {@link #isAuthenticationCachingEnabled() enabled}.
	 * </p>
	 * <b>WARNING:</b> Only set this property if safe caching conditions apply, as
	 * documented at the top of this page in the class-level JavaDoc.
	 *
	 * @return the name of a {@link Cache} to lookup from any available
	 *         cacheManager if a cache is not explicitly
	 *         configured via
	 *         org.apache.shiro.cache.Cache.
	 * @see #isAuthenticationCachingEnabled()
	 */
	public String getAuthenticationCacheName() {
		return this.authenticationCacheName;
	}

	/**
	 * <p>Sets the name of a {@link Cache} to lookup from any available
	 * cacheManager if a cache is not explicitly
	 * configured via org.apache.shiro.cache.Cache.
	 * </p>
	 * This name will only be used to look up a cache if authentication caching is
	 * {@link #isAuthenticationCachingEnabled() enabled}.
	 *
	 * @param authenticationCacheName
	 *            the name of a {@link Cache} to lookup from any available
	 *            cacheManager if a cache is not
	 *            explicitly configured via
	 *            org.apache.shiro.cache.Cache.
	 * @see #isAuthenticationCachingEnabled()
	 */
	public void setAuthenticationCacheName(String authenticationCacheName) {
		this.authenticationCacheName = authenticationCacheName;
	}

    /**
     * <p>Checks if caching enabled.</p>
     * @return the is caching enabled
     */
	public boolean isCachingEnabled() {
		return cachingEnabled;
	}

	/**
	 * Sets whether or not caching should be used if a {@link CacheManager} has been configured.
	 * @param cachingEnabled  whether or not to globally enable caching for this realm.
	 */
	public void setCachingEnabled(boolean cachingEnabled) {
		this.cachingEnabled = cachingEnabled;
	}
	
    /**
     * <p>Checks if captcha enabled.</p>
     * @return the is captcha enabled
     */
	public boolean isCaptchaEnabled() {
		return captchaEnabled;
	}

    /**
     * <p>Sets the captcha enabled.</p>
     * @param captchaEnabled
     */
	public void setCaptchaEnabled(boolean captchaEnabled) {
		this.captchaEnabled = captchaEnabled;
	}

    /**
     * <p>Returns the captcha param name.</p>
     * @return the get captcha param name
     */
	public String getCaptchaParamName() {
		return captchaParamName;
	}

    /**
     * <p>Sets the captcha param name.</p>
     * @param captchaParamName
     */
	public void setCaptchaParamName(String captchaParamName) {
		this.captchaParamName = captchaParamName;
	}
	
    /**
     * <p>Returns the credentials retry times limit.</p>
     * @return the get credentials retry times limit
     */
	public int getCredentialsRetryTimesLimit() {
		return credentialsRetryTimesLimit;
	}

    /**
     * <p>Sets the credentials retry times limit.</p>
     * @param credentialsRetryTimesLimit
     */
	public void setCredentialsRetryTimesLimit(int credentialsRetryTimesLimit) {
		this.credentialsRetryTimesLimit = credentialsRetryTimesLimit;
	}

    /**
     * <p>Returns the credentials retry cache name.</p>
     * @return the get credentials retry cache name
     */
	public String getCredentialsRetryCacheName() {
		return credentialsRetryCacheName;
	}

    /**
     * <p>Sets the credentials retry cache name.</p>
     * @param credentialsRetryCacheName
     */
	public void setCredentialsRetryCacheName(String credentialsRetryCacheName) {
		this.credentialsRetryCacheName = credentialsRetryCacheName;
	}

    /**
     * <p>Returns the default role permissions.</p>
     * @return the get default role permissions
     */
	public Map<String, String> getDefaultRolePermissions() {
		return defaultRolePermissions;
	}

    /**
     * <p>Sets the default role permissions.</p>
     * @param defaultRolePermissions
     */
	public void setDefaultRolePermissions(Map<String, String> defaultRolePermissions) {
		this.defaultRolePermissions = defaultRolePermissions;
	}
	
    /**
     * <p>Checks if enabled.</p>
     * @return the is enabled
     */
	public boolean isEnabled() {
		return enabled;
	}

    /**
     * <p>Sets the enabled.</p>
     * @param enabled
     */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
    /**
     * <p>Returns the failure url.</p>
     * @return the get failure url
     */
	public String getFailureUrl() {
		return failureUrl;
	}

    /**
     * <p>Sets the failure url.</p>
     * @param failureUrl
     */
	public void setFailureUrl(String failureUrl) {
		this.failureUrl = failureUrl;
	}
	
    /**
     * <p>Returns the filter chain definition map.</p>
     * @return the get filter chain definition map
     */
	public Map<String, String> getFilterChainDefinitionMap() {
		return filterChainDefinitionMap;
	}

    /**
     * <p>Sets the filter chain definition map.</p>
     * @param filterChainDefinitionMap
     */
	public void setFilterChainDefinitionMap(Map<String, String> filterChainDefinitionMap) {
		this.filterChainDefinitionMap = filterChainDefinitionMap;
	}
	
	/**
     * <p>Returns the login URL used to authenticate a user.</p>
     * <p>Most Shiro filters use this url
     * as the location to redirect a user when the filter requires authentication.  Unless overridden, the
     * {@link AccessControlFilter#DEFAULT_LOGIN_URL DEFAULT_LOGIN_URL} is assumed, which can be overridden via
     * {@link #setLoginUrl(String) setLoginUrl}.
     * </p>
     * @return the login URL used to authenticate a user, used when redirecting users if authentication is required.
     */
    public String getLoginUrl() {
        return loginUrl;
    }

    /**
     * <p>Sets the login URL used to authenticate a user.</p>
     * <p>Most Shiro filters use this url as the location to redirect a user when the filter requires
     * authentication.  Unless overridden, the {@link AccessControlFilter#DEFAULT_LOGIN_URL DEFAULT_LOGIN_URL} is assumed.
     * </p>
     * @param loginUrl the login URL used to authenticate a user, used when redirecting users if authentication is required.
     */
    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }
	
    /**
     * <p>Checks if post only logout.</p>
     * @return the is post only logout
     */
	public boolean isPostOnlyLogout() {
		return postOnlyLogout;
	}

    /**
     * <p>Sets the post only logout.</p>
     * @param postOnlyLogout
     */
	public void setPostOnlyLogout(boolean postOnlyLogout) {
		this.postOnlyLogout = postOnlyLogout;
	}

    /**
     * <p>Returns the redirect url.</p>
     * @return the get redirect url
     */
	public String getRedirectUrl() {
		return redirectUrl;
	}

    /**
     * <p>Sets the redirect url.</p>
     * @param redirectUrl
     */
	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

    /**
     * <p>Returns the retry times key attribute.</p>
     * @return the get retry times key attribute
     */
	public String getRetryTimesKeyAttribute() {
		return retryTimesKeyAttribute;
	}

    /**
     * <p>Sets the retry times key attribute.</p>
     * @param retryTimesKeyAttribute
     */
	public void setRetryTimesKeyAttribute(String retryTimesKeyAttribute) {
		this.retryTimesKeyAttribute = retryTimesKeyAttribute;
	}

    /**
     * <p>Returns the retry times when access denied.</p>
     * @return the get retry times when access denied
     */
	public int getRetryTimesWhenAccessDenied() {
		return retryTimesWhenAccessDenied;
	}

    /**
     * <p>Sets the retry times when access denied.</p>
     * @param retryTimesWhenAccessDenied
     */
	public void setRetryTimesWhenAccessDenied(int retryTimesWhenAccessDenied) {
		this.retryTimesWhenAccessDenied = retryTimesWhenAccessDenied;
	}

    /**
     * <p>Checks if session caching enabled.</p>
     * @return the is session caching enabled
     */
	public boolean isSessionCachingEnabled() {
		return isCachingEnabled() && sessionCachingEnabled;
	}

    /**
     * <p>Sets the session caching enabled.</p>
     * @param sessionCachingEnabled
     */
	public void setSessionCachingEnabled(boolean sessionCachingEnabled) {
		this.sessionCachingEnabled = sessionCachingEnabled;
		if (sessionCachingEnabled) {
			setCachingEnabled(true);
		}
	}

	/**
     * Returns {@code true} if the constructed {@code Subject} should be allowed to create a session, {@code false}
     * otherwise.  Shiro's configuration defaults to {@code true} as most applications find value in Sessions.
     *
     * @return {@code true} if the constructed {@code Subject} should be allowed to create sessions, {@code false}
     * otherwise.
     */
	public boolean isSessionCreationEnabled(){
		return sessionCreationEnabled;
	}

    /**
     * Sets whether or not the constructed {@code Subject} instance should be allowed to create a session,
     * {@code false} otherwise.
     *
     * @param sessionCreationEnabled whether or not the constructed {@code Subject} instance should be allowed to create a session,
     * {@code false} otherwise.
     */
	public void setSessionCreationEnabled(boolean sessionCreationEnabled) {
		this.sessionCreationEnabled = sessionCreationEnabled;
	}
	
    /**
     * <p>Returns the session deque cache name.</p>
     * @return the get session deque cache name
     */
	public String getSessionDequeCacheName() {
		return sessionDequeCacheName;
	}

    /**
     * <p>Sets the session deque cache name.</p>
     * @param sessionDequeCacheName
     */
	public void setSessionDequeCacheName(String sessionDequeCacheName) {
		this.sessionDequeCacheName = sessionDequeCacheName;
	}
	
    /**
     * <p>Checks if static security manager enabled.</p>
     * @return the is static security manager enabled
     */
	public boolean isStaticSecurityManagerEnabled() {
		return staticSecurityManagerEnabled;
	}

    /**
     * <p>Sets the static security manager enabled.</p>
     * @param staticSecurityManagerEnabled
     */
	public void setStaticSecurityManagerEnabled(boolean staticSecurityManagerEnabled) {
		this.staticSecurityManagerEnabled = staticSecurityManagerEnabled;
	}

    /**
     * <p>Checks if kickout first.</p>
     * @return the is kickout first
     */
	public boolean isKickoutFirst() {
		return kickoutFirst;
	}

    /**
     * <p>Sets the kickout first.</p>
     * @param kickoutFirst
     */
	public void setKickoutFirst(boolean kickoutFirst) {
		this.kickoutFirst = kickoutFirst;
	}

    /**
     * <p>Returns the session maximum kickout.</p>
     * @return the get session maximum kickout
     */
	public int getSessionMaximumKickout() {
		return sessionMaximumKickout;
	}

    /**
     * <p>Sets the session maximum kickout.</p>
     * @param sessionMaximumKickout
     */
	public void setSessionMaximumKickout(int sessionMaximumKickout) {
		this.sessionMaximumKickout = sessionMaximumKickout;
	}

    /**
     * <p>Checks if session storage enabled.</p>
     * @return the is session storage enabled
     */
	public boolean isSessionStorageEnabled() {
		return sessionStorageEnabled;
	}

    /**
     * <p>Sets the session storage enabled.</p>
     * @param sessionStorageEnabled
     */
	public void setSessionStorageEnabled(boolean sessionStorageEnabled) {
		this.sessionStorageEnabled = sessionStorageEnabled;
	}
	
    /**
     * <p>Checks if session stateless.</p>
     * @return the is session stateless
     */
	public boolean isSessionStateless() {
		return sessionStateless;
	}

    /**
     * <p>Sets the session stateless.</p>
     * @param sessionStateless
     */
	public void setSessionStateless(boolean sessionStateless) {
		this.sessionStateless = sessionStateless;
	}

    /**
     * <p>Returns the session timeout.</p>
     * @return the get session timeout
     */
	public long getSessionTimeout() {
		return sessionTimeout;
	}

    /**
     * <p>Sets the session timeout.</p>
     * @param sessionTimeout
     */
	public void setSessionTimeout(long sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

    /**
     * <p>Returns the session validation interval.</p>
     * @return the get session validation interval
     */
	public long getSessionValidationInterval() {
		return sessionValidationInterval;
	}

    /**
     * <p>Sets the session validation interval.</p>
     * @param sessionValidationInterval
     */
	public void setSessionValidationInterval(long sessionValidationInterval) {
		this.sessionValidationInterval = sessionValidationInterval;
	}

    /**
     * <p>Checks if session validation scheduler enabled.</p>
     * @return the is session validation scheduler enabled
     */
	public boolean isSessionValidationSchedulerEnabled() {
		return sessionValidationSchedulerEnabled;
	}

    /**
     * <p>Sets the session validation scheduler enabled.</p>
     * @param sessionValidationSchedulerEnabled
     */
	public void setSessionValidationSchedulerEnabled(boolean sessionValidationSchedulerEnabled) {
		this.sessionValidationSchedulerEnabled = sessionValidationSchedulerEnabled;
	}
	
	/**
	 * <p>
     * Returns the success url to use as the default location a user is sent after logging in.  
     * Typically a redirect after login will redirect to the originally request URL; this property is provided mainly as a fallback in case
     * the original request URL is not available or not specified.
     * </p>
     * The default value is {@link AuthenticationFilter#DEFAULT_SUCCESS_URL}.
     *
     * @return the success url to use as the default location a user is sent after logging in.
     */
    public String getSuccessUrl() {
        return successUrl;
    }

    /**
     * <p>
     * Sets the default/fallback success url to use as the default location a user is sent after logging in.  
     * Typically a redirect after login will redirect to the originally request URL; this property is provided mainly as a
     * fallback in case the original request URL is not available or not specified.
     * </p>
     * The default value is {@link AuthenticationFilter#DEFAULT_SUCCESS_URL}.
     *
     * @param successUrl the success URL to redirect the user to after a successful login.
     */
    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    /**
     * <p>
     * Returns the URL to which users should be redirected if they are denied access to an underlying path or resource,
     * or {@code null} if a raw {@link HttpServletResponse#SC_UNAUTHORIZED} response should be issued (401 Unauthorized).
     * </p>
     * <p>
     * The default is {@code null}, ensuring default web server behavior.  Override this default by calling the
     * {@link #setUnauthorizedUrl(String) setUnauthorizedUrl} method with a meaningful path within your application
     * if you would like to show the user a 'nice' page in the event of unauthorized access.
     * </p>
     * @return the URL to which users should be redirected if they are denied access to an underlying path or resource,
     *         or {@code null} if a raw {@link HttpServletResponse#SC_UNAUTHORIZED} response should be issued (401 Unauthorized).
     */
    public String getUnauthorizedUrl() {
        return unauthorizedUrl;
    }

    /**
     * <p>Sets the URL to which users should be redirected if they are denied access to an underlying path or resource.</p>
     * <p>
     * If the value is {@code null} a raw {@link HttpServletResponse#SC_UNAUTHORIZED} response will
     * be issued (401 Unauthorized), retaining default web server behavior.
     * </p>
     * Unless overridden by calling this method, the default value is {@code null}.  If desired, you can specify a
     * meaningful path within your application if you would like to show the user a 'nice' page in the event of
     * unauthorized access.
     *
     * @param unauthorizedUrl the URL to which users should be redirected if they are denied access to an underlying
     *                        path or resource, or {@code null} to a ensure raw {@link HttpServletResponse#SC_UNAUTHORIZED} response is
     *                        issued (401 Unauthorized).
     */
    public void setUnauthorizedUrl(String unauthorizedUrl) {
        this.unauthorizedUrl = unauthorizedUrl;
    }

    /**
     * <p>Checks if unique sessin.</p>
     * @return the is unique sessin
     */
	public boolean isUniqueSessin() {
		return uniqueSessin;
	}

    /**
     * <p>Sets the unique sessin.</p>
     * @param uniqueSessin
     */
	public void setUniqueSessin(boolean uniqueSessin) {
		this.uniqueSessin = uniqueSessin;
	}

    /**
     * <p>Checks if user native session manager.</p>
     * @return the is user native session manager
     */
	public boolean isUserNativeSessionManager() {
		return userNativeSessionManager;
	}

    /**
     * <p>Sets the user native session manager.</p>
     * @param userNativeSessionManager
     */
	public void setUserNativeSessionManager(boolean userNativeSessionManager) {
		this.userNativeSessionManager = userNativeSessionManager;
	}
	
}
