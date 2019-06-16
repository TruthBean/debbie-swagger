package com.truthbean.debbie.swagger;

import io.swagger.v3.oas.models.parameters.Parameter;

import java.util.ArrayList;
import java.util.List;

class ResolvedParameter {
    private List<Parameter> parameters;
    private Parameter requestBody;
    private List<Parameter> formParameters;

    ResolvedParameter() {
        parameters = new ArrayList<>();
        formParameters = new ArrayList<>();
    }

    public void setRequestBody(Parameter requestBody) {
        this.requestBody = requestBody;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void addParameters(List<Parameter> parameters) {
        this.parameters.addAll(parameters);
    }

    public void addParameter(Parameter parameter) {
        this.parameters.add(parameter);
    }

    public Parameter getRequestBody() {
        return requestBody;
    }

    public List<Parameter> getFormParameters() {
        return formParameters;
    }

    public void addFormParameters(List<Parameter> formParameters) {
        this.formParameters.addAll(formParameters);
    }

    public void addFormParameter(Parameter formParameter) {
        this.formParameters.add(formParameter);
    }
}