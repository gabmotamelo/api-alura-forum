package br.com.alura.forum.repository;

import br.com.alura.forum.model.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TopicoRepository extends JpaRepository<Topico, Long> {

    @Query("SELECT t FROM Topico t LEFT JOIN FETCH t.respostas WHERE t.id = :id")
    public Topico carregarComRespostas(Long id);

    Page<Topico> findByCursoNome(String nomeCurso, Pageable paginacao);
}

