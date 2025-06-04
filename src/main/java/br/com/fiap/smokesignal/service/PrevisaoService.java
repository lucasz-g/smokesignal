package br.com.fiap.smokesignal.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fiap.smokesignal.entity.PrevisaoEntity;
import br.com.fiap.smokesignal.entity.DTO.PrevisaoRequestDTO;
import br.com.fiap.smokesignal.entity.DTO.PrevisaoResponseDTO;
import br.com.fiap.smokesignal.repository.PrevisaoRepository;
import br.com.fiap.smokesignal.service.machineLearningServices.ModeloService;
import jakarta.persistence.EntityNotFoundException;

@Service
public class PrevisaoService {

    @Autowired
    private ModeloService modeloService;

    @Autowired
    private PrevisaoRepository previsaoRepository;

    
    public List<PrevisaoEntity> listarPrevisoes() {
        return previsaoRepository.findAll();
    }
    
    public List<PrevisaoEntity> listarPorEstado(String state) {
        return previsaoRepository.findByEstadoIgnoreCase(state);
    }

    public PrevisaoResponseDTO salvarPrevisao(PrevisaoRequestDTO dto) {
        double prob = modeloService.calcularProb(dto.getEstado(), dto.getMes());

        PrevisaoEntity entity = new PrevisaoEntity();
        entity.setEstado(dto.getEstado());
        entity.setMes(dto.getMes());
        entity.setProbabilidadeIncendio(BigDecimal.valueOf(prob * 100)); // armazenado em %
        entity.setDataHora(LocalDateTime.now());

        previsaoRepository.save(entity);

        return new PrevisaoResponseDTO(dto.getEstado(), dto.getMes(), prob);
    }

    // Método recalcula a probabilidade conforme novas informações
    public PrevisaoResponseDTO atualizarPrevisao(Long id, PrevisaoRequestDTO dto) {
        Optional<PrevisaoEntity> optional = previsaoRepository.findById(id);

        if (optional.isPresent()) {
            PrevisaoEntity entity = optional.get();
            double prob = modeloService.calcularProb(dto.getEstado(), dto.getMes());

            entity.setEstado(dto.getEstado());
            entity.setMes(dto.getMes());
            entity.setProbabilidadeIncendio(BigDecimal.valueOf(prob * 100));
            entity.setDataHora(LocalDateTime.now());

            previsaoRepository.save(entity);

            return new PrevisaoResponseDTO(entity.getEstado(), entity.getMes(), prob);
        } else {
            throw new EntityNotFoundException("Previsão não encontrada para o ID: " + id);
        }
    }

    public String deletarPrevisao(Long id) {
        if (previsaoRepository.existsById(id)) {
            previsaoRepository.deleteById(id);
            return "Previsão deletada com sucesso!";
        } else {
            return "Previsão não encontrada para o ID: " + id;
        }
    }

}
