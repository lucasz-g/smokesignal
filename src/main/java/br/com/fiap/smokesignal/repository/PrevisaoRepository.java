package br.com.fiap.smokesignal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fiap.smokesignal.entity.PrevisaoEntity;

@Repository
public interface PrevisaoRepository extends JpaRepository<PrevisaoEntity, Long> {
  List<PrevisaoEntity> findByEstadoIgnoreCase(String estado);  
}