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
package org.apache.shiro.spring.boot.biz;

import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the Shiro RememberMe manager cookie, controlling the cookie name,
 * max age, domain, path, security flag, and encryption secret key.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@ConfigurationProperties(ShiroRememberMeManagerCookieProperties.PREFIX)
public class ShiroRememberMeManagerCookieProperties {

	public static final String PREFIX = "shiro.rememberMeManager.cookie";
	
	/**
     * The number of seconds in one week (= 60 * 60 * 24 * 7).
     */
	private static final Integer ONE_WEEK = 60 * 60 * 24 * 7;
	/** Default encryption secret key for the RememberMe cookie. */
	private static final String DEFAULT_REMEMBERME_SECRETKEY = "1a2b5c8e6c9e5g2s";
	
    /** The RememberMe cookie name. */
    private String name = CookieRememberMeManager.DEFAULT_REMEMBER_ME_COOKIE_NAME;
    /** The RememberMe cookie max age in seconds, default is one week. */
    private int maxAge = ONE_WEEK;
    /** The RememberMe cookie domain. */
    private String domain;
    /** The RememberMe cookie path. */
    private String path;
    /** Whether the RememberMe cookie requires HTTPS, default is false. */
    private boolean secure = false;
    /** The encryption secret key for the RememberMe cookie. */
    private String secretKey = DEFAULT_REMEMBERME_SECRETKEY;

    /**
     * <p>Returns the name.</p>
     * @return the get name
     */
	public String getName() {
		return name;
	}

    /**
     * <p>Sets the name.</p>
     * @param name
     */
	public void setName(String name) {
		this.name = name;
	}

    /**
     * <p>Returns the max age.</p>
     * @return the get max age
     */
	public int getMaxAge() {
		return maxAge;
	}

    /**
     * <p>Sets the max age.</p>
     * @param maxAge
     */
	public void setMaxAge(int maxAge) {
		this.maxAge = maxAge;
	}

    /**
     * <p>Returns the domain.</p>
     * @return the get domain
     */
	public String getDomain() {
		return domain;
	}

    /**
     * <p>Sets the domain.</p>
     * @param domain
     */
	public void setDomain(String domain) {
		this.domain = domain;
	}

    /**
     * <p>Returns the path.</p>
     * @return the get path
     */
	public String getPath() {
		return path;
	}

    /**
     * <p>Sets the path.</p>
     * @param path
     */
	public void setPath(String path) {
		this.path = path;
	}

    /**
     * <p>Checks if secure.</p>
     * @return the is secure
     */
	public boolean isSecure() {
		return secure;
	}

    /**
     * <p>Sets the secure.</p>
     * @param secure
     */
	public void setSecure(boolean secure) {
		this.secure = secure;
	}

    /**
     * <p>Returns the secret key.</p>
     * @return the get secret key
     */
	public String getSecretKey() {
		return secretKey;
	}

    /**
     * <p>Sets the secret key.</p>
     * @param secretKey
     */
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

}
