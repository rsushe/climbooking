package ru.climbooking.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource

@Configuration
@EnableWebSecurity
class WebSecurityConfig {
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeRequests {
                authorize("/v1/*", permitAll)
            }
            cors {
                configurationSource = CorsConfigurationSource {
                    val configuration = CorsConfiguration().applyPermitDefaultValues()
                    configuration.allowedOrigins = listOf("http://localhost:5173", "http://127.0.0.1:5173")
                    configuration.addAllowedMethod(HttpMethod.PATCH)
                    configuration
                }
            }
            csrf { disable() }
        }
        return http.build()
    }
}
