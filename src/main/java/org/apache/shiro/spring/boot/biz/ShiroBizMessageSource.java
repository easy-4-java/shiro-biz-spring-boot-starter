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

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * A {@link ResourceBundleMessageSource} that loads Shiro business-layer messages from the
 * {@code org.apache.shiro.spring.boot.biz.messages} resource bundle.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class ShiroBizMessageSource extends ResourceBundleMessageSource {
	
	// ~ Constructors
	// ===================================================================================================

	/**
	 * Constructs a new shiro biz message source instance.
	 *
	 */
	public ShiroBizMessageSource() {
		setBasename("org.apache.shiro.spring.boot.biz.messages");
	}

	// ~ Methods
	// ========================================================================================================

	/**
	 * Creates a new {@link MessageSourceAccessor} backed by a fresh {@link ShiroBizMessageSource} instance.
	 *
	 * @return a message source accessor for Shiro business messages
	 */
	public static MessageSourceAccessor getAccessor() {
		return new MessageSourceAccessor(new ShiroBizMessageSource());
	}
}
