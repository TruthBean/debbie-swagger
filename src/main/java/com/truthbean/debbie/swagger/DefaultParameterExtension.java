package com.truthbean.debbie.swagger;

import com.fasterxml.jackson.annotation.JsonView;

import com.truthbean.debbie.io.MediaTypeInfo;

import com.truthbean.debbie.mvc.request.RequestParameterInfo;
import io.swagger.v3.core.util.ParameterProcessor;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.apache.commons.lang3.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class DefaultParameterExtension extends AbstractOpenAPIExtension {
    private static final String QUERY_PARAM = "query";
    private static final String HEADER_PARAM = "header";
    private static final String COOKIE_PARAM = "cookie";
    private static final String PATH_PARAM = "path";
    private static final String FORM_PARAM = "form";

    @Override
    public ResolvedParameter extractParameters(List<Annotation> annotations,
                                               Type type,
                                               Set<Type> typesToSkip,
                                               Components components,
                                               MediaTypeInfo consumes,
                                               boolean includeRequestBody,
                                               JsonView jsonViewAnnotation,
                                               Iterator<OpenAPIExtension> chain) {
        if (shouldIgnoreType(type, typesToSkip)) {
            return new ResolvedParameter();
        }


        Parameter parameter = null;
        for (Annotation annotation : annotations) {
            RequestParameterInfo param = RequestParameterInfo.fromAnnotation(annotation);
            if (param != null) {
                String name;
                switch (param.paramType()) {
                    case QUERY:
                        parameter = new Parameter();
                        parameter.setIn(QUERY_PARAM);
                        name = param.value();
                        if (name.isBlank()) {
                            name = param.name();
                        }
                        parameter.setName(name);
                        break;
                    case PATH:
                        parameter = new Parameter();
                        parameter.setIn(PATH_PARAM);
                        name = param.value();
                        if (name.isBlank()) {
                            name = param.name();
                        }
                        parameter.setName(name);
                        break;
                    case MATRIX:
                        parameter = new Parameter();
                        parameter.setIn(PATH_PARAM);
                        parameter.setStyle(Parameter.StyleEnum.MATRIX);
                        name = param.value();
                        if (name.isBlank()) {
                            name = param.name();
                        }
                        parameter.setName(name);
                        break;
                    case HEAD:
                        parameter = new Parameter();
                        parameter.setIn(HEADER_PARAM);
                        name = param.value();
                        if (name.isBlank()) {
                            name = param.name();
                        }
                        parameter.setName(name);
                        break;
                    case COOKIE:
                        parameter = new Parameter();
                        parameter.setIn(COOKIE_PARAM);
                        name = param.value();
                        if (name.isBlank()) {
                            name = param.name();
                        }
                        parameter.setName(name);
                        break;
                    case PARAM:
                        parameter = new Parameter();
                        parameter.setIn(FORM_PARAM);
                        name = param.value();
                        if (name.isBlank()) {
                            name = param.name();
                        }
                        parameter.setName(name);
                        break;
                    default:
                        break;
                }
            } else if (annotation instanceof io.swagger.v3.oas.annotations.Parameter) {
                if (((io.swagger.v3.oas.annotations.Parameter) annotation).hidden()) {
                    return new ResolvedParameter();
                }
                if (parameter == null) {
                    parameter = new Parameter();
                }
                if (StringUtils.isNotBlank(((io.swagger.v3.oas.annotations.Parameter) annotation).ref())) {
                    parameter.$ref(((io.swagger.v3.oas.annotations.Parameter) annotation).ref());
                }
            } else {
                List<Parameter> formParameters = new ArrayList<>();
                List<Parameter> parameters = new ArrayList<>();
                if (handleAdditionalAnnotation(parameters, formParameters, annotation, type, typesToSkip, consumes, components, includeRequestBody, jsonViewAnnotation)) {
                    ResolvedParameter extractParametersResult = new ResolvedParameter();
                    extractParametersResult.addParameters(parameters);
                    extractParametersResult.addFormParameters(formParameters);
                    return extractParametersResult;
                }
            }
        }
        List<Parameter> parameters = new ArrayList<>();
        ResolvedParameter extractParametersResult = new ResolvedParameter();

        if (parameter != null && (StringUtils.isNotBlank(parameter.getIn()) || StringUtils.isNotBlank(parameter.get$ref()))) {
            parameters.add(parameter);
        } else if (includeRequestBody) {
            Parameter unknownParameter = ParameterProcessor.applyAnnotations(
                    null,
                    type,
                    annotations,
                    components,
                    new String[0],
                    consumes == null ? new String[0] : new String[]{consumes.toString()}, jsonViewAnnotation);
            if (unknownParameter != null) {
                if (StringUtils.isNotBlank(unknownParameter.getIn()) && !"form".equals(unknownParameter.getIn())) {
                    extractParametersResult.addParameter(unknownParameter);
                } else if ("form".equals(unknownParameter.getIn())) {
                    unknownParameter.setIn(null);
                    extractParametersResult.addFormParameter(unknownParameter);
                } else {
                    // return as request body
                    extractParametersResult.setRequestBody(unknownParameter);
                }
            }
        }
        for (Parameter p : parameters) {
            Parameter processedParameter = ParameterProcessor.applyAnnotations(
                    p,
                    type,
                    annotations,
                    components,
                    new String[0],
                    consumes == null ? new String[0] : new String[]{consumes.toString()},
                    jsonViewAnnotation);
            if (processedParameter != null) {
                extractParametersResult.addParameter(processedParameter);
            }
        }
        return extractParametersResult;
    }

    /**
     * Adds additional annotation processing support
     *
     * @param parameters
     * @param annotation
     * @param type
     * @param typesToSkip
     */

    private boolean handleAdditionalAnnotation(List<Parameter> parameters, List<Parameter> formParameters, Annotation annotation,
                                               final Type type, Set<Type> typesToSkip, MediaTypeInfo consumes,
                                               Components components, boolean includeRequestBody, JsonView jsonViewAnnotation) {
        boolean processed = false;
        /*if (BeanParam.class.isAssignableFrom(annotation.getClass())) {
            // Use Jackson's logic for processing Beans
            final BeanDescription beanDesc = mapper.getSerializationConfig().introspect(constructType(type));
            final List<BeanPropertyDefinition> properties = beanDesc.findProperties();

            for (final BeanPropertyDefinition propDef : properties) {
                final AnnotatedField field = propDef.getField();
                final AnnotatedMethod setter = propDef.getSetter();
                final AnnotatedMethod getter = propDef.getGetter();
                final List<Annotation> paramAnnotations = new ArrayList<Annotation>();
                final Iterator<OpenAPIExtension> extensions = OpenAPIExtensions.chain();
                Type paramType = null;

                // Gather the field's details
                if (field != null) {
                    paramType = field.getType();

                    for (final Annotation fieldAnnotation : field.annotations()) {
                        if (!paramAnnotations.contains(fieldAnnotation)) {
                            paramAnnotations.add(fieldAnnotation);
                        }
                    }
                }

                // Gather the setter's details but only the ones we need
                if (setter != null) {
                    // Do not set the param class/type from the setter if the values are already identified
                    if (paramType == null) {
                        // paramType will stay null if there is no parameter
                        paramType = setter.getParameterType(0);
                    }

                    for (final Annotation fieldAnnotation : setter.annotations()) {
                        if (!paramAnnotations.contains(fieldAnnotation)) {
                            paramAnnotations.add(fieldAnnotation);
                        }
                    }
                }

                // Gather the getter's details but only the ones we need
                if (getter != null) {
                    // Do not set the param class/type from the getter if the values are already identified
                    if (paramType == null) {
                        paramType = getter.getType();
                    }

                    for (final Annotation fieldAnnotation : getter.annotations()) {
                        if (!paramAnnotations.contains(fieldAnnotation)) {
                            paramAnnotations.add(fieldAnnotation);
                        }
                    }
                }

                if (paramType == null) {
                    continue;
                }

                // Re-process all Bean fields and let the default swagger-jaxrs/swagger-jersey-jaxrs processors do their thing
                ResolvedParameter resolvedParameter = extensions.next().extractParameters(
                        paramAnnotations,
                        paramType,
                        typesToSkip,
                        components,
                        consumes,
                        includeRequestBody,
                        jsonViewAnnotation,
                        extensions);

                List<Parameter> extractedParameters =
                        resolvedParameter.parameters;

                for (Parameter p : extractedParameters) {
                    Parameter processedParam = ParameterProcessor.applyAnnotations(
                            p,
                            paramType,
                            paramAnnotations,
                            components,
                            new String[0],
                            consumes == null ? new String[0] : new String[]{consumes.toString()},
                            jsonViewAnnotation);
                    if (processedParam != null) {
                        parameters.add(processedParam);
                    }
                }

                List<Parameter> extractedFormParameters =
                        resolvedParameter.formParameters;

                for (Parameter p : extractedFormParameters) {
                    Parameter processedParam = ParameterProcessor.applyAnnotations(
                            p,
                            paramType,
                            paramAnnotations,
                            components,
                            new String[0],
                            consumes == null ? new String[0] : new String[]{consumes.toString()},
                            jsonViewAnnotation);
                    if (processedParam != null) {
                        formParameters.add(processedParam);
                    }
                }

                processed = true;
            }
        }*/
        return processed;
    }

    @Override
    protected boolean shouldIgnoreClass(Class<?> cls) {
        return cls.getName().startsWith("com.truthbean.debbie.mvc");
    }

}