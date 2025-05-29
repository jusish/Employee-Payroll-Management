package rw.erp.manage.payroll.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

        @Bean
        public OpenAPI customOpenAPI() {
                return new OpenAPI()
                                .info(new Info()
                                                .title("Payroll management API")
                                                .version("1.0.0")
                                                .description(
                                                                "This API provides endpoints for managing payroll, including employee management, salary calculations, and reporting."))
                                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                                .components(new io.swagger.v3.oas.models.Components()
                                                .addSecuritySchemes("bearerAuth",
                                                                new SecurityScheme()
                                                                                .type(SecurityScheme.Type.HTTP)
                                                                                .scheme("bearer")
                                                                                .bearerFormat("JWT")));
        }

        @Bean
        public GroupedOpenApi authApi() {
                return GroupedOpenApi.builder()
                                .group("Authentication Endpoints")
                                .pathsToMatch("/api/v1/auth/**")
                                .build();
        }

        @Bean
        public GroupedOpenApi employeeApi() {
                return GroupedOpenApi.builder()
                                .group("Employee Endpoints")
                                .pathsToMatch("/api/v1/employee/**")
                                .build();
        }

        @Bean
        public GroupedOpenApi deductionApi() {
                return GroupedOpenApi.builder()
                                .group("Deductin Endpoints")
                                .pathsToMatch("/api/v1/deductions/**")
                                .build();
        }

        @Bean
        public GroupedOpenApi payrollApi() {
                return GroupedOpenApi.builder()
                                .group("Payroll Endpoints")
                                .pathsToMatch("/api/v1/payroll/**")
                                .build();
        }

        @Bean
        public GroupedOpenApi payslipApi() {
                return GroupedOpenApi.builder()
                                .group("Payslip Endpoints")
                                .pathsToMatch("/api/v1/payslip/**")
                                .build();
        }

        @Bean
        public GroupedOpenApi employmentApi() {
                return GroupedOpenApi.builder()
                                .group("Employment Endpoints")
                                .pathsToMatch("/api/v1/employment/**")
                                .build();
        }

}