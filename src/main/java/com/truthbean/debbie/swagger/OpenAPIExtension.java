package com.truthbean.debbie.swagger;

import com.fasterxml.jackson.annotation.JsonView;
import com.truthbean.debbie.io.MediaTypeInfo;
import com.truthbean.debbie.mvc.request.HttpMethod;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.Operation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public interface OpenAPIExtension {

    HttpMethod extractOperationMethod(Method method, Iterator<OpenAPIExtension> chain);

    HttpMethod[] extractOperationMethods(Method method, Iterator<OpenAPIExtension> chain);

    ResolvedParameter extractParameters(List<Annotation> annotations, Type type, Set<Type> typesToSkip, Components components,
                                        MediaTypeInfo consumes, boolean includeRequestBody, JsonView jsonViewAnnotation, Iterator<OpenAPIExtension> chain);

    /**
     * Decorates operation with additional vendor based extensions.
     *
     * @param operation the operation, build from swagger definition
     * @param method    the method for additional scan
     * @param chain     the chain with swagger extensions to process
     */
    void decorateOperation(Operation operation, Method method, Iterator<OpenAPIExtension> chain);
}