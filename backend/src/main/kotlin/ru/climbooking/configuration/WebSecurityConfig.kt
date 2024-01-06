package ru.climbooking.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

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
                val configuration = CorsConfiguration()
                configuration.allowedOrigins = listOf("http://127.0.0.1:5173")
                configuration.allowedMethods = listOf("GET", "POST")
                configurationSource = UrlBasedCorsConfigurationSource().apply {
                    registerCorsConfiguration("/**", configuration)
                }
            }
        }
        return http.build()
    }
}
