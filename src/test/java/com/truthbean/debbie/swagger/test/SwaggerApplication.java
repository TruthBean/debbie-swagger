package com.truthbean.debbie.swagger.test;

import com.truthbean.debbie.boot.DebbieApplicationFactory;

public class SwaggerApplication {
    public static void main(String[] args) {
        var application = DebbieApplicationFactory.create(SwaggerApplication.class);
        application.start(args);
    }
}
