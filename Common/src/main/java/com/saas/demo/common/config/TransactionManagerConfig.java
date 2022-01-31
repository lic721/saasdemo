package com.saas.demo.common.config;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

@Aspect
@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
public class TransactionManagerConfig {

    private String aopPointCutExpression = "execution(* com.saas.demo..service..*Service.*(..))";

    @Bean
    public Advisor txAdvisor(PlatformTransactionManager transactionManager) {
        NameMatchTransactionAttributeSource transactionAttributeSource = new NameMatchTransactionAttributeSource();
        DefaultTransactionAttribute transactionAttribute = new AllThrowableTransactionAttribute();
        transactionAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        transactionAttributeSource.addTransactionalMethod("**", transactionAttribute);
        DefaultTransactionAttribute txRequiredReadonly = new AllThrowableTransactionAttribute();
        txRequiredReadonly.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        txRequiredReadonly.setReadOnly(true);
        transactionAttributeSource.addTransactionalMethod("get*", txRequiredReadonly);
        transactionAttributeSource.addTransactionalMethod("query*", txRequiredReadonly);
        transactionAttributeSource.addTransactionalMethod("find*", txRequiredReadonly);
        transactionAttributeSource.addTransactionalMethod("list*", txRequiredReadonly);
        transactionAttributeSource.addTransactionalMethod("count*", txRequiredReadonly);
        transactionAttributeSource.addTransactionalMethod("is*", txRequiredReadonly);
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(aopPointCutExpression);
        return new DefaultPointcutAdvisor(pointcut,
                new TransactionInterceptor(transactionManager, transactionAttributeSource));
    }

    class AllThrowableTransactionAttribute extends DefaultTransactionAttribute {
        private static final long serialVersionUID = -6234507725070346836L;

        @Override
        public boolean rollbackOn(Throwable ex) {
            return null != ex;
        }
    }

}
