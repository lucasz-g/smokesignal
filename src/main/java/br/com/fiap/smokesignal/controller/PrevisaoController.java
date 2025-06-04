package br.com.fiap.smokesignal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.smokesignal.entity.IncendioEntity;
import br.com.fiap.smokesignal.entity.PrevisaoEntity;
import br.com.fiap.smokesignal.entity.DTO.PrevisaoRequestDTO;
import br.com.fiap.smokesignal.entity.DTO.PrevisaoResponseDTO;
import br.com.fiap.smokesignal.security.PermissaoValidator;
import br.com.fiap.smokesignal.service.IncendioService;
import br.com.fiap.smokesignal.service.PrevisaoService;


@RestController
@RequestMapping("/api/v1")
public class PrevisaoController {

    @Autowired
    private PrevisaoService previsaoService;
    @Autowired
    private IncendioService incendioService; 
    
    //Validação de permissão de usuários mock
    @Autowired
    private PermissaoValidator permissaoValidator;
    
    // Endpoint: GET /api/v1/incendios
    @GetMapping("/incendios")
    public List<IncendioEntity> getAllFires() {
        return incendioService.buscarTodosIncendios();
    }

    @GetMapping("/previsoes")
    public List<PrevisaoEntity> getAllPrevisoes() {
        return previsaoService.listarPrevisoes();
    }

    @GetMapping("/previsoes/estado/{state}")
    public List<PrevisaoEntity> getMethodName(@PathVariable String state) {
        return previsaoService.listarPorEstado(state);
    }
    
    @PostMapping("/previsoes")
    public PrevisaoResponseDTO criarPrevisao(@RequestBody PrevisaoRequestDTO dto) {
        permissaoValidator.validarAdmin();
        return previsaoService.salvarPrevisao(dto); 
    }

    @PutMapping("/previsoes/{id}")
    public PrevisaoResponseDTO atualizarPrevisao(@PathVariable Long id, @RequestBody PrevisaoRequestDTO dto){
        permissaoValidator.validarAdmin();
        return previsaoService.atualizarPrevisao(id, dto);
    }

    @DeleteMapping("previsoes/{id}")
    public String deletePrevisao(@PathVariable Long id){
        permissaoValidator.validarAdmin();
        return previsaoService.deletarPrevisao(id);
    }
    
}