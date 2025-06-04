package br.com.fiap.smokesignal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.fiap.smokesignal.entity.LogDeRequisicaoEntity;

public interface LogDeRequisicaoRepository extends JpaRepository<LogDeRequisicaoEntity, Long> {
}
