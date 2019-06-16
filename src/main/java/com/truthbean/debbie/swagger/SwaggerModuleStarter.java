package com.truthbean.debbie.swagger;

import com.truthbean.debbie.bean.BeanFactoryHandler;
import com.truthbean.debbie.boot.DebbieModuleStarter;
import com.truthbean.debbie.properties.DebbieConfigurationFactory;

/**
 * @author truthbean
 * @since 0.0.2
 */
public class SwaggerModuleStarter implements DebbieModuleStarter {
    @Override
    public void starter(DebbieConfigurationFactory configurationFactory, BeanFactoryHandler beanFactoryHandler) {
        DebbieSwaggerConfiguration configuration = new DebbieSwaggerConfiguration();
        configuration.configure(beanFactoryHandler);
    }
}