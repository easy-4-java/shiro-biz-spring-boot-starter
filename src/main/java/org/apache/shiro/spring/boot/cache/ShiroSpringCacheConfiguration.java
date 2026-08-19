package org.apache.shiro.spring.boot.cache;

import org.apache.shiro.biz.cache.spring.SpringCacheManager;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.spring.config.web.autoconfigure.ShiroWebAutoConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.AbstractCachingConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuration for Shiro Spring cache manager. Activates when the {@code shiro.cache.type}
 * property is set to "spring". Delegates to the Spring {@link org.springframework.cache.CacheManager}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Configuration
@AutoConfigureAfter(AbstractCachingConfiguration.class)
@AutoConfigureBefore(ShiroWebAutoConfiguration.class)
@ConditionalOnProperty(prefix = ShiroCacheProperties.PREFIX, value = "type", havingValue = "spring")
public class ShiroSpringCacheConfiguration implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	/**
	 * Creates a Shiro {@link CacheManager} that delegates to the Spring cache manager.
	 *
	 * @return the configured Shiro cache manager
	 */
	@Bean
    /**
     * <p>Shiro cache manager.</p>
     * @return the shiro cache manager
     */
	public CacheManager shiroCacheManager() {
		org.springframework.cache.CacheManager springCacheManager = getApplicationContext().getBean(org.springframework.cache.CacheManager.class);
		return new SpringCacheManager(springCacheManager);
	}

	@Override
    /**
     * <p>Sets the application context.</p>
     * @param applicationContext
     */
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

}
