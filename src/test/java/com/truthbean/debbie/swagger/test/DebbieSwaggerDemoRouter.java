/**
 * Copyright (c) 2020 TruthBean(Rogar·Q)
 *    Debbie is licensed under Mulan PSL v2.
 *    You can use this software according to the terms and conditions of the Mulan PSL v2.
 *    You may obtain a copy of Mulan PSL v2 at:
 *                http://license.coscl.org.cn/MulanPSL2
 *    THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 *    See the Mulan PSL v2 for more details.
 */
package com.truthbean.debbie.swagger.test;

import com.truthbean.debbie.io.MediaType;
import com.truthbean.debbie.mvc.request.HttpMethod;
import com.truthbean.debbie.mvc.request.QueryParameter;
import com.truthbean.debbie.mvc.request.RequestParameter;
import com.truthbean.debbie.mvc.request.RequestParameterType;
import com.truthbean.debbie.mvc.router.Router;
import com.truthbean.debbie.watcher.Watcher;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashMap;
import java.util.Map;

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
    public Map<String, String> swagger(@QueryParameter(name = "test") String test) {
        Map<String, String> map = new HashMap<>();
        map.put("swagger test", test);
        return map;
    }

    @Operation(tags = {"example"})
    @Router(urlPatterns = "/swagger2", method = HttpMethod.GET, responseType = MediaType.APPLICATION_JSON_UTF8, requestType = MediaType.APPLICATION_FORM_URLENCODED)
    public RequestCondition swagger(@QueryParameter RequestCondition condition) {
        return condition;
    }

}
