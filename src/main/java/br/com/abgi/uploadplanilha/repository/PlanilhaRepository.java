package br.com.abgi.uploadplanilha.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.abgi.uploadplanilha.model.Planilha;

@Repository
public interface PlanilhaRepository extends JpaRepository<Planilha, Integer> {

}
