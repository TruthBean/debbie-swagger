package com.truthbean.debbie.swagger.test;

import com.truthbean.debbie.bean.BeanInitialization;
import com.truthbean.debbie.boot.DebbieApplication;
import com.truthbean.debbie.boot.DebbieApplicationFactory;
import com.truthbean.debbie.boot.DebbieBootApplication;
import com.truthbean.debbie.mvc.MvcConfiguration;
import com.truthbean.debbie.mvc.router.MvcRouterRegister;
import com.truthbean.debbie.properties.DebbieConfigurationFactory;
import com.truthbean.debbie.swagger.SwaggerReader;
import com.truthbean.debbie.util.JacksonUtils;
import io.swagger.v3.oas.integration.GenericOpenApiContextBuilder;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

import java.util.List;

@DebbieBootApplication
public class DebbieSwaggerTest {
    public static void main(String[] args) {
        DebbieApplicationFactory factory = DebbieApplicationFactory.configure(DebbieSwaggerTest.class);

        DebbieConfigurationFactory configurationFactory = factory.getConfigurationFactory();
        MvcConfiguration configuration = configurationFactory.factory(MvcConfiguration.class, factory);

        BeanInitialization beanInitialization = factory.getBeanInitialization();

        MvcRouterRegister.registerRouter(configuration, factory);

        OpenAPI oas = new OpenAPI();
        Info info = beanInitialization.getRegisterBean(Info.class);

        oas.info(info);
        oas.servers(List.of(new Server().url("http://localhost:8090").description("debbie swagger example")));

        SwaggerConfiguration oasConfig = new SwaggerConfiguration()
                .openAPI(oas)
                .prettyPrint(true);

        try {
            OpenAPI openAPI = new GenericOpenApiContextBuilder()
                    .openApiConfiguration(oasConfig)
                    .buildContext(true)
                    .read();
            var reader = new SwaggerReader(openAPI);
            OpenAPI newOpenApi = reader.read();
            System.out.println(newOpenApi);
            System.out.println(JacksonUtils.toYaml(newOpenApi));

        } catch (OpenApiConfigurationException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }
}
