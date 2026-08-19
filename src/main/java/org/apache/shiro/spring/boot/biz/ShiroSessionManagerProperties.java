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

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the Shiro session manager, controlling session ID cookie usage,
 * URL rewriting, and automatic deletion of invalid sessions.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@ConfigurationProperties(ShiroSessionManagerProperties.PREFIX)
public class ShiroSessionManagerProperties {

	/**
	 * Returns the session id cookie enabled.
	 *
	 * @return the session id cookie enabled
	 */
	public static final String PREFIX = "shiro.sessionManager";
	
    /** Whether the session ID cookie is enabled, default is true. */
    private boolean sessionIdCookieEnabled = true;
    /** Whether session ID URL rewriting is enabled, default is true. */
    private boolean sessionIdUrlRewritingEnabled = true;
    /** Whether to automatically delete invalid sessions, default is true. */
    protected boolean deleteInvalidSessions = true;
    
    /**
     * <p>Checks if session id cookie enabled.</p>
     * @return the is session id cookie enabled
     */
	public boolean isSessionIdCookieEnabled() {
		return sessionIdCookieEnabled;
	}

    /**
     * <p>Sets the session id cookie enabled.</p>
     * @param sessionIdCookieEnabled
     */
	public void setSessionIdCookieEnabled(boolean sessionIdCookieEnabled) {
		this.sessionIdCookieEnabled = sessionIdCookieEnabled;
	}

    /**
     * <p>Checks if session id url rewriting enabled.</p>
     * @return the is session id url rewriting enabled
     */
	public boolean isSessionIdUrlRewritingEnabled() {
		return sessionIdUrlRewritingEnabled;
	}

    /**
     * <p>Sets the session id url rewriting enabled.</p>
     * @param sessionIdUrlRewritingEnabled
     */
	public void setSessionIdUrlRewritingEnabled(boolean sessionIdUrlRewritingEnabled) {
		this.sessionIdUrlRewritingEnabled = sessionIdUrlRewritingEnabled;
	}

    /**
     * <p>Checks if delete invalid sessions.</p>
     * @return the is delete invalid sessions
     */
	public boolean isDeleteInvalidSessions() {
		return deleteInvalidSessions;
	}

    /**
     * <p>Sets the delete invalid sessions.</p>
     * @param deleteInvalidSessions
     */
	public void setDeleteInvalidSessions(boolean deleteInvalidSessions) {
		this.deleteInvalidSessions = deleteInvalidSessions;
	}

}
