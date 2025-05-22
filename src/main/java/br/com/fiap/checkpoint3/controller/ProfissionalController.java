package br.com.fiap.checkpoint3.controller;

import br.com.fiap.checkpoint3.model.Profissional;
import br.com.fiap.checkpoint3.model.Consulta;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/profissionais")
public class ProfissionalController {
    private static final List<Profissional> profissionais = new ArrayList<>();
    private static final List<Consulta> consultas = new ArrayList<>();
    private static Long idCounter = 1L;

    // POST /profissionais
    @PostMapping
    public ResponseEntity<Profissional> createProfissional(@RequestBody Profissional profissional) {
        profissional.setId(idCounter++);
        profissionais.add(profissional);
        return ResponseEntity.ok(profissional);
    }

    // GET /profissionais?sort={asc, desc}
    @GetMapping
    public ResponseEntity<List<Profissional>> getProfissionais(@RequestParam(defaultValue = "asc") String sort) {
        List<Profissional> sortedProfissionais = new ArrayList<>(profissionais);
        if (sort.equalsIgnoreCase("desc")) {
            sortedProfissionais.sort(Comparator.comparing(Profissional::getNome).reversed());
        } else {
            sortedProfissionais.sort(Comparator.comparing(Profissional::getNome));
        }
        return ResponseEntity.ok(sortedProfissionais);
    }

    // GET /profissionais/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Profissional> getProfissionalById(@PathVariable Long id) {
        return profissionais.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /profissionais/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Profissional> updateProfissional(@PathVariable Long id, @RequestBody Profissional updatedProfissional) {
        for (Profissional profissional : profissionais) {
            if (profissional.getId().equals(id)) {
                profissional.setNome(updatedProfissional.getNome());
                profissional.setEspecialidade(updatedProfissional.getEspecialidade());
                profissional.setValorHora(updatedProfissional.getValorHora());
                profissional.setUpdatedAt(LocalDateTime.now());
                return ResponseEntity.ok(profissional);
            }
        }
        return ResponseEntity.notFound().build();
    }

    // DELETE /profissionais/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfissional(@PathVariable Long id) {
        boolean removed = profissionais.removeIf(p -> p.getId().equals(id));
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // GET /profissionais/{id}/stats
    @GetMapping("/{id}/stats")
    public ResponseEntity<Map<String, Object>> getProfissionalStats(@PathVariable Long id) {
        long totalConsultas = consultas.stream()
                .filter(c -> c.getProfissionalId().equals(id))
                .count();
        long consultasRealizadas = consultas.stream()
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

        List<Consulta> result = consultas.stream()
                .filter(c -> c.getProfissionalId().equals(id))
                .filter(c -> c.getStatusConsulta().equalsIgnoreCase(status))
                .filter(c -> !c.getDataConsulta().isBefore(startDate) && !c.getDataConsulta().isAfter(endDate))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }
}