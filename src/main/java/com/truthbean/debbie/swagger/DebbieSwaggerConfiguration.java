package com.truthbean.debbie.swagger;

import com.truthbean.debbie.bean.BeanFactoryHandler;
import com.truthbean.debbie.bean.BeanInitialization;
import com.truthbean.debbie.bean.DebbieBeanInfo;
import io.swagger.v3.oas.integration.GenericOpenApiContextBuilder;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * @author truthbean
 * @since 0.0.2
 */
public class DebbieSwaggerConfiguration {

    public void configure(BeanFactoryHandler beanFactoryHandler) {
        OpenAPI oas = new OpenAPI();

        BeanInitialization beanInitialization = beanFactoryHandler.getBeanInitialization();
        beanInitialization.init(DebbieSwaggerRouter.class);

        Info info = beanInitialization.getRegisterBean(Info.class);
        DebbieSwaggerProperties properties = new DebbieSwaggerProperties();
        if (info == null) {
            info = properties.getInfo();
        }
        oas.info(info);

        Server server = beanInitialization.getRegisterBean(Server.class);
        if (server == null) {
            server = properties.getServer();
        }
        var servers = new ArrayList<Server>();
        servers.add(server);
        oas.servers(servers);

        SwaggerConfiguration oasConfig = new SwaggerConfiguration()
                .openAPI(oas)
                .prettyPrint(true);

        OpenAPI result;
        try {
            result = new GenericOpenApiContextBuilder<>()
                    .openApiConfiguration(oasConfig)
                    .buildContext(true)
                    .read();

        } catch (OpenApiConfigurationException e) {
            LOGGER.error("", e);

            result = oas;
        }

        DebbieBeanInfo<OpenAPI> beanInfo = new DebbieBeanInfo<>(OpenAPI.class);
        beanInfo.setBeanName("openApi");
        beanInfo.setBean(result);
        beanInitialization.initSingletonBean(beanInfo);
        beanFactoryHandler.refreshBeans();
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(DebbieSwaggerConfiguration.class);
}
