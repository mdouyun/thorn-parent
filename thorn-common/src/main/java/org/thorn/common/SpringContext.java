/*
 * Copyright (c) 2014 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.thorn.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.Assert;

import java.util.Arrays;

/**
 * Spring容器上下文, 可以通过该类直接获取spring的bean, 也可以通过该类加载加载spring配置文件.
 *
 * @author chenyun313@gmail.com, 2014-03-27.
 * @version 1.0
 * @since 1.0
 */
public class SpringContext implements ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringContext.class);

    private static ApplicationContext applicationContext = null;

    @Override
    public synchronized void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        if(applicationContext == null) {
            LOGGER.warn("ApplicationContext为空.");
            return ;
        }

        LOGGER.debug("注入ApplicationContext到SpringContext: " + applicationContext.getApplicationName());

        if(SpringContext.applicationContext != null) {
            LOGGER.warn("SpringContext中的ApplicationContext被覆盖.");
        }

        SpringContext.applicationContext = applicationContext;
    }

    public static synchronized void loadApplicationContext(String... configLocations) {

        if (applicationContext != null) {
            LOGGER.warn("SpringContext中的ApplicationContext被覆盖");
        }

        LOGGER.debug("加载配置文件到SpringContext: " + Arrays.toString(configLocations));

        applicationContext = new ClassPathXmlApplicationContext(configLocations);
    }

    public static Object getBean(String name) {
        Assert.notNull(applicationContext);
        return applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> cls) {
        Assert.notNull(applicationContext);
        return applicationContext.getBean(cls);
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
