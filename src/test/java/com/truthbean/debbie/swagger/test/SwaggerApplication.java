package com.truthbean.debbie.swagger.test;

import com.truthbean.debbie.boot.DebbieApplicationFactory;

public class SwaggerApplication {
    public static void main(String[] args) {
        var application = DebbieApplicationFactory.factory();
        application.start(args);
    }
}
