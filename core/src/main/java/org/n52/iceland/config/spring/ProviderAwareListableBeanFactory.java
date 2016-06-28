package org.n52.iceland.config.spring;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.inject.Provider;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * TODO JavaDoc
 *
 * @author Christian Autermann
 */
public class ProviderAwareListableBeanFactory extends DefaultListableBeanFactory {
    private static final long serialVersionUID = -6826027137321052707L;

    public ProviderAwareListableBeanFactory() {
    }

    public ProviderAwareListableBeanFactory(BeanFactory parentBeanFactory) {
        super(parentBeanFactory);
    }

    @Override
    protected Map<String, Object> findAutowireCandidates(String beanName, Class<?> requiredType, DependencyDescriptor descriptor) {
        if (requiredType.equals(Provider.class)) {
            DependencyDescriptor providedDescriptor = new DependencyDescriptor(descriptor);
            providedDescriptor.increaseNestingLevel();
            Class<?> providedType = providedDescriptor.getDependencyType();
            return findAutowireCandidates(beanName, providedType, providedDescriptor).keySet().stream()
                    .collect(Collectors.toMap(Function.identity(), name -> new DependencyProvider(descriptor, name)));
        } else {
            return super.findAutowireCandidates(beanName, requiredType, descriptor);
        }

	}

    private static class OptionalDependencyDescriptor extends DependencyDescriptor {
        private static final long serialVersionUID = -6623409266686702545L;

        OptionalDependencyDescriptor(DependencyDescriptor original) {
            super(original);
            increaseNestingLevel();
        }

        @Override
        public boolean isRequired() {
            return false;
        }
    }

    private class DependencyProvider implements ObjectFactory<Object>, Provider<Object>, Serializable {
        private static final long serialVersionUID = 2498323681896163119L;
        private final DependencyDescriptor descriptor;
        private final boolean optional;
        private final String beanName;

        DependencyProvider(DependencyDescriptor descriptor, String beanName) {
            descriptor = new DependencyDescriptor(descriptor);
            descriptor.increaseNestingLevel();
            this.optional = descriptor.getDependencyType().equals(Optional.class);
            this.descriptor = this.optional ? new OptionalDependencyDescriptor(descriptor) : descriptor;
            this.beanName = beanName;
        }

        @Override
        public Object getObject() throws BeansException {
            Object resolved = doResolveDependency(this.descriptor, beanName, null, null);
            return (this.optional) ? Optional.ofNullable(resolved) : resolved;
        }

        @Override
        public Object get() throws BeansException {
            return getObject();
        }
    }


}
