package br.com.fiap.checkpoint3.controller;

import br.com.fiap.checkpoint3.dto.ProfissionalDTO;
import br.com.fiap.checkpoint3.model.Profissional;
import br.com.fiap.checkpoint3.repository.ProfissionalRepository;
import br.com.fiap.checkpoint3.repository.ConsultaRepository;
import br.com.fiap.checkpoint3.model.Consulta;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/profissionais")
public class ProfissionalController {
    private final ProfissionalRepository profissionalRepository = new ProfissionalRepository();
    private final ConsultaRepository consultaRepository = new ConsultaRepository();

    // POST /profissionais
    @PostMapping
    public ResponseEntity<ProfissionalDTO> createProfissional(@RequestBody Profissional profissional) {
        Profissional savedProfissional = profissionalRepository.save(profissional);
        ProfissionalDTO profissionalDTO = new ProfissionalDTO(savedProfissional.getId(), savedProfissional.getNome(), savedProfissional.getEspecialidade());
        return ResponseEntity.ok(profissionalDTO);
    }

    // GET /profissionais?sort={asc, desc}
    @GetMapping
    public ResponseEntity<List<ProfissionalDTO>> getProfissionais(@RequestParam(defaultValue = "asc") String sort) {
        List<Profissional> profissionais = profissionalRepository.findAll();
        List<ProfissionalDTO> profissionalDTOs = profissionais.stream()
                .map(p -> new ProfissionalDTO(p.getId(), p.getNome(), p.getEspecialidade()))
                .sorted(sort.equalsIgnoreCase("desc") ? Comparator.comparing(ProfissionalDTO::getNome).reversed() : Comparator.comparing(ProfissionalDTO::getNome))
                .collect(Collectors.toList());
        return ResponseEntity.ok(profissionalDTOs);
    }

    // GET /profissionais/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ProfissionalDTO> getProfissionalById(@PathVariable Long id) {
        return profissionalRepository.findById(id)
                .map(p -> new ProfissionalDTO(p.getId(), p.getNome(), p.getEspecialidade()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /profissionais/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ProfissionalDTO> updateProfissional(@PathVariable Long id, @RequestBody Profissional updatedProfissional) {
        return profissionalRepository.findById(id)
                .map(p -> {
                    p.setNome(updatedProfissional.getNome());
                    p.setEspecialidade(updatedProfissional.getEspecialidade());
                    p.setValorHora(updatedProfissional.getValorHora());
                    p.setUpdatedAt(LocalDateTime.now());
                    Profissional savedProfissional = profissionalRepository.save(p);
                    return new ProfissionalDTO(savedProfissional.getId(), savedProfissional.getNome(), savedProfissional.getEspecialidade());
                })
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /profissionais/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfissional(@PathVariable Long id) {
        if (profissionalRepository.findById(id).isPresent()) {
            profissionalRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // GET /profissionais/{id}/stats
    @GetMapping("/{id}/stats")
    public ResponseEntity<Map<String, Object>> getProfissionalStats(@PathVariable Long id) {
        long totalConsultas = consultaRepository.findAll().stream()
                .filter(c -> c.getProfissionalId().equals(id))
                .count();
        long consultasRealizadas = consultaRepository.findAll().stream()
                .filter(c -> c.getProfissionalId().equals(id) && c.getStatusConsulta().equalsIgnoreCase("REALIZADA"))
                .count();
        return ResponseEntity.ok(Map.of("totalConsultas", totalConsultas, "consultasRealizadas", consultasRealizadas));
    }

    // GET /profissionais/{id}/consultas?status={AGENDADA, REALIZADA, CANCELADA}&data_de=24-04-2025&data_ate=25-04-2025
    @GetMapping("/{id}/consultas")
    public ResponseEntity<List<Consulta>> getConsultasByProfissional(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam("data_de") String dataDe,
            @RequestParam("data_ate") String dataAte) {
        LocalDateTime startDate = LocalDateTime.parse(dataDe + "T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse(dataAte + "T23:59:59");

        List<Consulta> consultas = consultaRepository.findAll().stream()
                .filter(c -> c.getProfissionalId().equals(id))
                .filter(c -> c.getStatusConsulta().equalsIgnoreCase(status))
                .filter(c -> !c.getDataConsulta().isBefore(startDate) && !c.getDataConsulta().isAfter(endDate))
                .collect(Collectors.toList());
        return ResponseEntity.ok(consultas);
    }
}