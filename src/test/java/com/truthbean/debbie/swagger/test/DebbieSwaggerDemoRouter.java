package com.truthbean.debbie.swagger.test;

import com.truthbean.debbie.io.MediaType;
import com.truthbean.debbie.mvc.request.HttpMethod;
import com.truthbean.debbie.mvc.request.RequestParameter;
import com.truthbean.debbie.mvc.request.RequestParameterType;
import com.truthbean.debbie.mvc.router.Router;
import com.truthbean.debbie.watcher.Watcher;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@OpenAPIDefinition(
        tags = {
                @Tag(name = "example", description = "debbie example router")
        }
)
@Watcher
@Router("example")
public class DebbieSwaggerDemoRouter {

    @Operation(tags = {"example"})
    @Router(urlPatterns = "/swagger", method = HttpMethod.GET, responseType = MediaType.APPLICATION_JSON_UTF8)
    public String swagger(@RequestParameter(name = "test", paramType = RequestParameterType.QUERY) String test) {
        return "{\"swagger test\": " + test + "}";
    }

}
