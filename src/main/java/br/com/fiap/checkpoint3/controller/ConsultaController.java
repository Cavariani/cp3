package br.com.fiap.checkpoint3.controller;

import br.com.fiap.checkpoint3.model.Consulta;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {
    private static final List<Consulta> consultas = new ArrayList<>();
    private static Long idCounter = 1L;

    // POST /consultas
    @PostMapping
    public ResponseEntity<Consulta> createConsulta(@RequestBody Consulta consulta) {
        consulta.setId(idCounter++);
        consultas.add(consulta);
        return ResponseEntity.ok(consulta);
    }

    // GET /consultas
    @GetMapping
    public ResponseEntity<List<Consulta>> getConsultas() {
        return ResponseEntity.ok(consultas);
    }

    // GET /consultas/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Consulta> getConsultaById(@PathVariable Long id) {
        return consultas.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /consultas/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Consulta> updateConsulta(@PathVariable Long id, @RequestBody Consulta updatedConsulta) {
        for (Consulta consulta : consultas) {
            if (consulta.getId().equals(id)) {
                consulta.setProfissionalId(updatedConsulta.getProfissionalId());
                consulta.setPacienteId(updatedConsulta.getPacienteId());
                consulta.setDataConsulta(updatedConsulta.getDataConsulta());
                consulta.setStatusConsulta(updatedConsulta.getStatusConsulta());
                consulta.setQuantidadeHoras(updatedConsulta.getQuantidadeHoras());
                consulta.setValorConsulta(updatedConsulta.getValorConsulta());
                consulta.setUpdatedAt(LocalDateTime.now());
                return ResponseEntity.ok(consulta);
            }
        }
        return ResponseEntity.notFound().build();
    }

    // DELETE /consultas/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsulta(@PathVariable Long id) {
        boolean removed = consultas.removeIf(c -> c.getId().equals(id));
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // GET /consultas?status={AGENDADA, REALIZADA, CANCELADA}&data_de=24-04-2025&data_ate=25-04-2025
    @GetMapping(params = {"status", "data_de", "data_ate"})
    public ResponseEntity<List<Consulta>> getConsultasByStatusAndDate(
            @RequestParam String status,
            @RequestParam("data_de") String dataDe,
            @RequestParam("data_ate") String dataAte) {
        LocalDateTime startDate = LocalDateTime.parse(dataDe + "T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse(dataAte + "T23:59:59");

        List<Consulta> result = consultas.stream()
                .filter(c -> c.getStatusConsulta().equalsIgnoreCase(status))
                .filter(c -> !c.getDataConsulta().isBefore(startDate) && !c.getDataConsulta().isAfter(endDate))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }
}