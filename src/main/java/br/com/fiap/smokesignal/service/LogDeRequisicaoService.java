package br.com.fiap.smokesignal.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fiap.smokesignal.entity.LogDeRequisicaoEntity;
import br.com.fiap.smokesignal.repository.LogDeRequisicaoRepository;

@Service
public class LogDeRequisicaoService {

    @Autowired
    private LogDeRequisicaoRepository logRepo;

    public void registrar(String endpoint) {
        LogDeRequisicaoEntity log = new LogDeRequisicaoEntity();
        log.setDataHora(LocalDateTime.now());
        log.setEndpoint(endpoint);
        logRepo.save(log);
    }

    public List<LogDeRequisicaoEntity> listarTodos(){
        List<LogDeRequisicaoEntity> logs = logRepo.findAll();
        return logs;  
    }
}
