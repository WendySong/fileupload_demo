package com.pisx.fileupload.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by pisx on 2014/12/24.
 */
public class SpringConfigTool implements ApplicationContextAware {

    private static ApplicationContext context = null;

    private static SpringConfigTool stools = null;

    public synchronized static SpringConfigTool init() {
        if (stools == null) {
            stools = new SpringConfigTool();
        }
        return stools;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return context;
    }

    public synchronized static Object getBean(String beanName) {
        return context.getBean(beanName);
    }
}
