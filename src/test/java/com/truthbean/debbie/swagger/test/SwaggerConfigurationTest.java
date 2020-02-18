package com.truthbean.debbie.swagger.test;

import com.truthbean.debbie.bean.BeanInject;
import com.truthbean.debbie.swagger.SwaggerReader;
import com.truthbean.debbie.test.DebbieApplicationExtension;
import com.truthbean.debbie.util.JacksonUtils;
import io.swagger.v3.oas.integration.GenericOpenApiContextBuilder;
import io.swagger.v3.oas.integration.OpenApiConfigurationException;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

/**
 * @author truthbean/Rogar·Q
 * @since 0.0.2
 */
@ExtendWith({DebbieApplicationExtension.class})
public class SwaggerConfigurationTest {

    @Test
    public void content(@BeanInject Info info) {
        OpenAPI oas = new OpenAPI();

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
