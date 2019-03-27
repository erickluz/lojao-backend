package com.cia.lojao.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private Environment env;
	

//	PUBLIC_MATCHERS: Acesso por qualquer metodo HTTP
	private static final String[] PUBLIC_MATCHERS = {
			"/h2-console/**"
	};	
//	PUBLIC_MATCHERS_GET: Comunicacao somente pelo metodo HTTP GET
	private static final String[] PUBLIC_MATCHERS_GET = {
			"/produtos/**",
			"/categorias/**",
			"/clientes/**"
	};
	
	
	/*
	 * Sobrescrevendo a configuracao padrao do framework
	 * Configuracao para permitir somente as URLs contidas em PUBLIC_MATCHERS
	 */
	
	@Override
	protected void configure (HttpSecurity http) throws Exception {

		// Libera alguma URI se estiver no perfil de testes
		if(Arrays.asList(env.getActiveProfiles()).contains("test")){
			http.headers().frameOptions().disable(); 
		}
		
		// Desabilita a protecao automatica contra ataque CSRF(Porque o sistema e Stateless e nao de Sessao)
		http.cors().and().csrf().disable(); 
		
		http.authorizeRequests()
		.antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll()
		.antMatchers(PUBLIC_MATCHERS).permitAll()
		.anyRequest()
		.authenticated();
		
		// Garantir que a aplicacao e Stateless. Desabilita o sistema padrao de Sessao.
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	// Permissao de acesso por multiplas fontes CORS
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
	    return source;
	}

	// Criptografia (senha)
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
