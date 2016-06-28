package org.n52.iceland.config.spring;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * TODO JavaDoc
 * @author Christian Autermann
 */
public class ProviderAwareXmlWebApplicationContext extends XmlWebApplicationContext{

    @Override
    protected DefaultListableBeanFactory createBeanFactory() {
        return new ProviderAwareListableBeanFactory(getInternalParentBeanFactory());
    }



}
