package br.com.alura.forum.controller;

import br.com.alura.forum.controller.dto.AtualizacaoTopicoForm;
import br.com.alura.forum.controller.dto.DetalhesDoTopicoDTO;
import br.com.alura.forum.controller.dto.TopicoDTO;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.model.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping(value = "/topicos")
public class TopicosController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping
    @Cacheable(value = "listaDeTopicos")
    public Page<TopicoDTO> listar(@RequestParam(required = false) String nomeCurso,
                                  @PageableDefault(sort = "id", direction = Sort.Direction.ASC, page = 0, size = 10) Pageable paginacao){
        if (nomeCurso == null){
            Page<Topico> topicos = topicoRepository.findAll(paginacao);
            return TopicoDTO.converter(topicos);
        } else {
            Page<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso, paginacao);
            return TopicoDTO.converter(topicos);
        }
    }

    @PostMapping
    @Transactional
    //parâmetro para limpar todos os registros, que é o allEntries = true. No nosso caso,
    // quero limpar todos os registros para ele atualizar tudo e deixar o cache zerado de novo.
    @CacheEvict(value = "listaDeTopicos", allEntries = true )
    public ResponseEntity<TopicoDTO> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriComponentsBuilder){
        Topico topico = form.converter(cursoRepository);
        topicoRepository.save(topico);
        URI uri = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new TopicoDTO(topico));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<DetalhesDoTopicoDTO> detalhar(@PathVariable Long id){
        Optional<Topico> topico = Optional.ofNullable(topicoRepository.carregarComRespostas(id));
        if (topico.isPresent()){
            return ResponseEntity.ok(new DetalhesDoTopicoDTO(topico.get()));
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Transactional
    //parâmetro para limpar todos os registros, que é o allEntries = true. No nosso caso,
    // quero limpar todos os registros para ele atualizar tudo e deixar o cache zerado de novo.
    @CacheEvict(value = "listaDeTopicos", allEntries = true )
    public ResponseEntity<TopicoDTO> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form){
         Topico topico = form.atualizar(id, topicoRepository);
         return ResponseEntity.ok(new TopicoDTO(topico));
    }

    @DeleteMapping("/{id}")
    @Transactional
    //parâmetro para limpar todos os registros, que é o allEntries = true. No nosso caso,
    // quero limpar todos os registros para ele atualizar tudo e deixar o cache zerado de novo.
    @CacheEvict(value = "listaDeTopicos", allEntries = true )
    public ResponseEntity<?> remover(@PathVariable Long id){
        Optional<Topico> optional = Optional.ofNullable(topicoRepository.carregarComRespostas(id));
        if (optional.isPresent()){
            topicoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}

