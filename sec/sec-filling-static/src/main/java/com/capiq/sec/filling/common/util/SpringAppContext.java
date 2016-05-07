package com.capiq.sec.filling.common.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;

public final class SpringAppContext {

    // Should have private constructor.
    // As it is utility class and we do not want it's object in application.
    private SpringAppContext() { throw new UnsupportedOperationException(); }
    private static ClassPathXmlApplicationContext CONTEXT;

    public static void loadApplicationContext() {
        CONTEXT = new ClassPathXmlApplicationContext("spring-app-context.xml");;
    }
    
    public static void closeApplicationContext() {
        CONTEXT.close();
    }

    /**
     * Gets the bean.
     *
     * @param beanName the bean name
     * @return the bean
     */
    @SuppressWarnings("unchecked")
	public static <T> T getBean(String beanName) {
        ApplicationContext ac = CONTEXT;
        return (T) ac.getBean(beanName);
    }
    
    @SuppressWarnings("unchecked")
 	public static <T> T getBean(String beanName, Object... args) {
         ApplicationContext ac = CONTEXT;
         return (T) ac.getBean(beanName, args);
     }
    
    public static Resource getResource(String resourceFilePath){
    	ApplicationContext ac = CONTEXT;
    	return ac.getResource(resourceFilePath);
    }
}
