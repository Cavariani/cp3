package br.com.fiap.checkpoint3.controller;

import br.com.fiap.checkpoint3.dto.ConsultaDTO;
import br.com.fiap.checkpoint3.model.Consulta;
import br.com.fiap.checkpoint3.repository.ConsultaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {
    private final ConsultaRepository consultaRepository = new ConsultaRepository();

    // POST /consultas
    @PostMapping
    public ResponseEntity<ConsultaDTO> createConsulta(@RequestBody Consulta consulta) {
        Consulta savedConsulta = consultaRepository.save(consulta);
        ConsultaDTO consultaDTO = new ConsultaDTO(savedConsulta.getId(), savedConsulta.getProfissionalId(), savedConsulta.getPacienteId(), savedConsulta.getDataConsulta(), savedConsulta.getStatusConsulta());
        return ResponseEntity.ok(consultaDTO);
    }

    // GET /consultas
    @GetMapping
    public ResponseEntity<List<ConsultaDTO>> getConsultas() {
        List<ConsultaDTO> consultaDTOs = consultaRepository.findAll().stream()
                .map(c -> new ConsultaDTO(c.getId(), c.getProfissionalId(), c.getPacienteId(), c.getDataConsulta(), c.getStatusConsulta()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(consultaDTOs);
    }

    // GET /consultas/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ConsultaDTO> getConsultaById(@PathVariable Long id) {
        return consultaRepository.findById(id)
                .map(c -> new ConsultaDTO(c.getId(), c.getProfissionalId(), c.getPacienteId(), c.getDataConsulta(), c.getStatusConsulta()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /consultas/{id}
    @PutMapping("/{id}")
    public ResponseEntity<ConsultaDTO> updateConsulta(@PathVariable Long id, @RequestBody Consulta updatedConsulta) {
        return consultaRepository.findById(id)
                .map(c -> {
                    c.setProfissionalId(updatedConsulta.getProfissionalId());
                    c.setPacienteId(updatedConsulta.getPacienteId());
                    c.setDataConsulta(updatedConsulta.getDataConsulta());
                    c.setStatusConsulta(updatedConsulta.getStatusConsulta());
                    c.setQuantidadeHoras(updatedConsulta.getQuantidadeHoras());
                    c.setValorConsulta(updatedConsulta.getValorConsulta());
                    c.setUpdatedAt(LocalDateTime.now());
                    Consulta savedConsulta = consultaRepository.save(c);
                    return new ConsultaDTO(savedConsulta.getId(), savedConsulta.getProfissionalId(), savedConsulta.getPacienteId(), savedConsulta.getDataConsulta(), savedConsulta.getStatusConsulta());
                })
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /consultas/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsulta(@PathVariable Long id) {
        if (consultaRepository.findById(id).isPresent()) {
            consultaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // GET /consultas?status={AGENDADA, REALIZADA, CANCELADA}&data_de=24-04-2025&data_ate=25-04-2025
    @GetMapping(params = {"status", "data_de", "data_ate"})
    public ResponseEntity<List<ConsultaDTO>> getConsultasByStatusAndDate(
            @RequestParam String status,
            @RequestParam("data_de") String dataDe,
            @RequestParam("data_ate") String dataAte) {
        LocalDateTime startDate = LocalDateTime.parse(dataDe + "T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse(dataAte + "T23:59:59");

        List<ConsultaDTO> consultaDTOs = consultaRepository.findAll().stream()
                .filter(c -> c.getStatusConsulta().equalsIgnoreCase(status))
                .filter(c -> !c.getDataConsulta().isBefore(startDate) && !c.getDataConsulta().isAfter(endDate))
                .map(c -> new ConsultaDTO(c.getId(), c.getProfissionalId(), c.getPacienteId(), c.getDataConsulta(), c.getStatusConsulta()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(consultaDTOs);
    }
}