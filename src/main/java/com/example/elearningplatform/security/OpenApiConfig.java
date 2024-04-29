package com.example.elearningplatform.security;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(info = @Info(title = "E-Learning Platform", description = """
    eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyMUBleGFtcGxlLmNvbSIsImNyZWF0ZWQiOjE3MTQxMjI4NTA0NDQsImV4cCI6MTcxNzEyMjg1MCwidXNlcklkIjo2NTJ9.lT3YRewEu5X5hIlTSn58NL-XtniMMGW5R0FUebOIf7lBMaNNBgcu7PWBCXEQ6KRTUDY9EDjNUqVL-ZywAcYvNA
        """)

// , security = @SecurityRequirement(name = "bearerAuth")

)
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT", in = SecuritySchemeIn.HEADER)
public class OpenApiConfig {

}
