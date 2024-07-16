package com.hyunec.pickuprequest.app.pickuprequestapi.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(
    basePackages = [
        "com.hyunec.pickuprequest.app.pickuprequestapi",
        "com.hyunec.pickuprequest.domain.pickup",
        "com.hyunec.pickuprequest.infrastructure.mysql",
    ]
)
class ProjectConfig
