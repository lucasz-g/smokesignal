package br.com.fiap.smokesignal.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import br.com.fiap.smokesignal.interceptor.LogDeRequisicaoInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LogDeRequisicaoInterceptor logInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logInterceptor);
    }
}
