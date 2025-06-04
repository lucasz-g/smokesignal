package br.com.fiap.smokesignal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import br.com.fiap.smokesignal.entity.LogDeRequisicaoEntity;
import br.com.fiap.smokesignal.security.PermissaoValidator;
import br.com.fiap.smokesignal.service.LogDeRequisicaoService;

@RestController
@RequestMapping("/api/v1/logs")
public class LogDeRequisicaoController {

    @Autowired
    private LogDeRequisicaoService logService;
    @Autowired
    private PermissaoValidator adminValidator;

    @GetMapping
    public List<LogDeRequisicaoEntity> listarTodos() {
        adminValidator.validarAdmin();
        return logService.listarTodos();
    }
}
