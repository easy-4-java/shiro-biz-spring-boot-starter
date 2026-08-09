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
 * @author [@Loong Wan](https://github.com/loong10k)
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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public int getRetryTimesWhenAccessDenied() {
		return retryTimesWhenAccessDenied;
	}

	public void setRetryTimesWhenAccessDenied(int retryTimesWhenAccessDenied) {
		this.retryTimesWhenAccessDenied = retryTimesWhenAccessDenied;
	}

	public String getStoreKey() {
		return storeKey;
	}

	public void setStoreKey(String storeKey) {
		this.storeKey = storeKey;
	}

	public String getDateStoreKey() {
		return dateStoreKey;
	}

	public void setDateStoreKey(String dateStoreKey) {
		this.dateStoreKey = dateStoreKey;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public String getCacheKey() {
		return cacheKey;
	}

	public void setCacheKey(String cacheKey) {
		this.cacheKey = cacheKey;
	}
	
}
