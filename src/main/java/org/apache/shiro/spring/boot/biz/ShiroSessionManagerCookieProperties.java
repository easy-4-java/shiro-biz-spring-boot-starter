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

import org.apache.shiro.web.servlet.ShiroHttpSession;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the Shiro session manager cookie, controlling the session ID cookie
 * name, max age, domain, path, and security flag.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@ConfigurationProperties(ShiroSessionManagerCookieProperties.PREFIX)
public class ShiroSessionManagerCookieProperties {

	public static final String PREFIX = "shiro.sessionManager.cookie";
	
    /** The session cookie name, default is {@link ShiroHttpSession#DEFAULT_SESSION_ID_NAME}. */
    protected String name = ShiroHttpSession.DEFAULT_SESSION_ID_NAME;
    /** The session cookie max age in seconds, default is {@link SimpleCookie#DEFAULT_MAX_AGE}. */
    protected int maxAge = SimpleCookie.DEFAULT_MAX_AGE;
    /** The session cookie domain. */
    protected String domain;
    /** The session cookie path. */
    protected String path;
    /** Whether the session cookie requires HTTPS, default is false. */
    protected boolean secure = false;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(int maxAge) {
		this.maxAge = maxAge;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isSecure() {
		return secure;
	}

	public void setSecure(boolean secure) {
		this.secure = secure;
	}

}
