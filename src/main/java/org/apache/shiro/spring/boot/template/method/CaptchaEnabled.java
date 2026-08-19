package org.apache.shiro.spring.boot.template.method;


import java.util.List;

import org.apache.shiro.spring.boot.captcha.ShiroKaptchaProperties;
import org.springframework.beans.factory.annotation.Autowired;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

/**
 * A FreeMarker template method that checks whether captcha is enabled in the Shiro configuration.
 * Can be used in FreeMarker templates to conditionally render captcha-related UI elements.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class CaptchaEnabled implements TemplateMethodModelEx {

	@Autowired
	private ShiroKaptchaProperties properties;
	
	@SuppressWarnings("rawtypes")
    /**
     * <p>Exec.</p>
     * @param arguments
     * @return the exec
     */
	public Object exec(List arguments) throws TemplateModelException {
		return new SimpleScalar(Boolean.toString(properties.isEnabled()));
	}

}
