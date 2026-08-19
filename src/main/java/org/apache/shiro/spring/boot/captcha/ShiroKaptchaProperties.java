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
package org.apache.shiro.spring.boot.captcha;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Shiro captcha integration, controlling captcha enablement,
 * retry limits, cache keys, and timeout settings.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@ConfigurationProperties(ShiroKaptchaProperties.PREFIX)
public class ShiroKaptchaProperties {

	public static final String PREFIX = "shiro.kaptcha";

	protected static final long DEFAULT_CAPTCHA_TIMEOUT = 60 * 1000;
	
	/*
	 * ============================= Shiro Basic Captcha  ============================
	 */

	/**
	 * Enable Shiro Captcha.
	 */
	private boolean enabled = false;
	/** The number of failed retries before captcha is required, default is 3. */
	private int retryTimesWhenAccessDenied = 3;
	/** The cache key for storing the captcha text. */
	private String storeKey;
	/** The cache key for storing the captcha creation time. */
	private String dateStoreKey;
	/** The captcha validity period in milliseconds, default is 60000 (60 seconds). */
	private long timeout = DEFAULT_CAPTCHA_TIMEOUT;
	/** The cache name for captcha storage. */
	private String cacheKey;

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
     * <p>Returns the store key.</p>
     * @return the get store key
     */
	public String getStoreKey() {
		return storeKey;
	}

    /**
     * <p>Sets the store key.</p>
     * @param storeKey
     */
	public void setStoreKey(String storeKey) {
		this.storeKey = storeKey;
	}

    /**
     * <p>Returns the date store key.</p>
     * @return the get date store key
     */
	public String getDateStoreKey() {
		return dateStoreKey;
	}

    /**
     * <p>Sets the date store key.</p>
     * @param dateStoreKey
     */
	public void setDateStoreKey(String dateStoreKey) {
		this.dateStoreKey = dateStoreKey;
	}

    /**
     * <p>Returns the timeout.</p>
     * @return the get timeout
     */
	public long getTimeout() {
		return timeout;
	}

    /**
     * <p>Sets the timeout.</p>
     * @param timeout
     */
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

    /**
     * <p>Returns the cache key.</p>
     * @return the get cache key
     */
	public String getCacheKey() {
		return cacheKey;
	}

    /**
     * <p>Sets the cache key.</p>
     * @param cacheKey
     */
	public void setCacheKey(String cacheKey) {
		this.cacheKey = cacheKey;
	}
	
}
