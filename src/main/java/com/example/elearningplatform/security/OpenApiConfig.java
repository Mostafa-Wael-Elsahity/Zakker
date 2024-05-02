package com.example.elearningplatform.security;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(info = @Info(title = "E-Learning Platform", description = """
    eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyMUBleGFtcGxlLmNvbSIsImNyZWF0ZWQiOjE3MTQ0OTU4MDQ5NzYsImV4cCI6MTcxNzQ5NTgwNCwidXNlcklkIjoxMTUyfQ.IL8m_A71Wbdx9MTYCbOUqDc9C8OebMa1CBoqT5IuH3HnflYCMQh45PBDsOdcl7xbKqzTK7LZvekgeUBmdK-mDA
        """)

// , security = @SecurityRequirement(name = "bearerAuth")

)
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT", in = SecuritySchemeIn.HEADER)
public class OpenApiConfig {

}
