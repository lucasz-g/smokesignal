package br.com.fiap.smokesignal.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import br.com.fiap.smokesignal.service.LogDeRequisicaoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LogDeRequisicaoInterceptor implements HandlerInterceptor {

    @Autowired
    private LogDeRequisicaoService logService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        logService.registrar(request.getRequestURI());
        return true;
    }
}
