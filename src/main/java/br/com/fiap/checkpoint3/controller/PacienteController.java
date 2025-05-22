package br.com.fiap.checkpoint3.controller;

import br.com.fiap.checkpoint3.dto.PacienteDTO;
import br.com.fiap.checkpoint3.model.Paciente;
import br.com.fiap.checkpoint3.repository.PacienteRepository;
import br.com.fiap.checkpoint3.repository.ConsultaRepository;
import br.com.fiap.checkpoint3.model.Consulta;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {
    private final PacienteRepository pacienteRepository = new PacienteRepository();
    private final ConsultaRepository consultaRepository = new ConsultaRepository();

    // POST /pacientes
    @PostMapping
    public ResponseEntity<PacienteDTO> createPaciente(@RequestBody Paciente paciente) {
        Paciente savedPaciente = pacienteRepository.save(paciente);
        PacienteDTO pacienteDTO = new PacienteDTO(savedPaciente.getId(), savedPaciente.getNome(), savedPaciente.getEmail());
        return ResponseEntity.ok(pacienteDTO);
    }

    // GET /pacientes?sort={asc, desc}
    @GetMapping
    public ResponseEntity<List<PacienteDTO>> getPacientes(@RequestParam(defaultValue = "asc") String sort) {
        List<Paciente> pacientes = pacienteRepository.findAll();
        List<PacienteDTO> pacienteDTOs = pacientes.stream()
                .map(p -> new PacienteDTO(p.getId(), p.getNome(), p.getEmail()))
                .sorted(sort.equalsIgnoreCase("desc") ? Comparator.comparing(PacienteDTO::getNome).reversed() : Comparator.comparing(PacienteDTO::getNome))
                .collect(Collectors.toList());
        return ResponseEntity.ok(pacienteDTOs);
    }

    // GET /pacientes/{id}
    @GetMapping("/{id}")
    public ResponseEntity<PacienteDTO> getPacienteById(@PathVariable Long id) {
        return pacienteRepository.findById(id)
                .map(p -> new PacienteDTO(p.getId(), p.getNome(), p.getEmail()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /pacientes/{id}
    @PutMapping("/{id}")
    public ResponseEntity<PacienteDTO> updatePaciente(@PathVariable Long id, @RequestBody Paciente updatedPaciente) {
        return pacienteRepository.findById(id)
                .map(p -> {
                    p.setNome(updatedPaciente.getNome());
                    p.setEndereco(updatedPaciente.getEndereco());
                    p.setBairro(updatedPaciente.getBairro());
                    p.setEmail(updatedPaciente.getEmail());
                    p.setTelefone(updatedPaciente.getTelefone());
                    p.setDataNascimento(updatedPaciente.getDataNascimento());
                    p.setUpdatedAt(LocalDateTime.now());
                    Paciente savedPaciente = pacienteRepository.save(p);
                    return new PacienteDTO(savedPaciente.getId(), savedPaciente.getNome(), savedPaciente.getEmail());
                })
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /pacientes/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaciente(@PathVariable Long id) {
        if (pacienteRepository.findById(id).isPresent()) {
            pacienteRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // GET /pacientes/{id}/consultas?status={AGENDADA, REALIZADA, CANCELADA}&data_de=24-04-2025&data_ate=25-04-2025
    @GetMapping("/{id}/consultas")
    public ResponseEntity<List<Consulta>> getConsultasByPaciente(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam("data_de") String dataDe,
            @RequestParam("data_ate") String dataAte) {
        LocalDateTime startDate = LocalDateTime.parse(dataDe + "T00:00:00");
        LocalDateTime endDate = LocalDateTime.parse(dataAte + "T23:59:59");

        List<Consulta> consultas = consultaRepository.findAll().stream()
                .filter(c -> c.getPacienteId().equals(id))
                .filter(c -> c.getStatusConsulta().equalsIgnoreCase(status))
                .filter(c -> !c.getDataConsulta().isBefore(startDate) && !c.getDataConsulta().isAfter(endDate))
                .collect(Collectors.toList());
        return ResponseEntity.ok(consultas);
    }
}