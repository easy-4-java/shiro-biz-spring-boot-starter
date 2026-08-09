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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.Filter;

import org.apache.shiro.biz.spring.ShiroFilterProxyFactoryBean;
import org.apache.shiro.spring.boot.utils.JakartaFilterAdapter;
import org.apache.shiro.web.servlet.AdviceFilter;
import org.springframework.beans.BeansException;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ObjectUtils;
/**
 * A {@link ShiroFilterProxyFactoryBean} that discovers all {@link FilterRegistrationBean} instances
 * from the application context and adds their {@link javax.servlet.Filter} instances to the Shiro
 * filter chain. This ensures that filters registered as Spring beans but intended for Shiro are
 * properly included in the Shiro filter chain rather than the servlet container filter chain.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@SuppressWarnings("rawtypes")
public class ShiroBizFilterFactoryBean extends ShiroFilterProxyFactoryBean implements ApplicationContextAware  {

	private ApplicationContext applicationContext;

	/**
	 * Collects all {@link FilterRegistrationBean} instances from the application context whose filters
	 * are {@link AdviceFilter} subclasses, and merges them with the filters from the parent factory bean.
	 *
	 * @return the combined map of filter name to filter instance
	 */
	@Override
	public Map<String, Filter> getFilters() {

		Map<String, Filter> filters = new LinkedHashMap<String, Filter>();

		Map<String, FilterRegistrationBean> beansOfType = getApplicationContext().getBeansOfType(FilterRegistrationBean.class);
		if (!ObjectUtils.isEmpty(beansOfType)) {
			Iterator<Entry<String, FilterRegistrationBean>> ite = beansOfType.entrySet().iterator();
			while (ite.hasNext()) {
				Entry<String, FilterRegistrationBean> entry = ite.next();
				Object filterObj = entry.getValue().getFilter();
				// Unwrap JakartaFilterAdapter to get the original javax.servlet.Filter
				if (filterObj instanceof JakartaFilterAdapter) {
					filterObj = ((JakartaFilterAdapter) filterObj).getDelegate();
				}
				if (filterObj instanceof AdviceFilter) {
					filters.put(entry.getKey(), (Filter) filterObj);
				}
			}
		}

		filters.putAll(super.getFilters());

		return filters;

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}
