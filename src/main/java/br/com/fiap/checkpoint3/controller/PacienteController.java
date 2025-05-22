package br.com.fiap.checkpoint3.controller;

import br.com.fiap.checkpoint3.model.Paciente;
import br.com.fiap.checkpoint3.model.Consulta;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {
    private static final List<Paciente> pacientes = new ArrayList<>();
    private static final List<Consulta> consultas = new ArrayList<>();
    private static Long idCounter = 1L;

    // POST /pacientes
    @PostMapping
    public ResponseEntity<Paciente> createPaciente(@RequestBody Paciente paciente) {
        paciente.setId(idCounter++);
        pacientes.add(paciente);
        return ResponseEntity.ok(paciente);
    }

    // GET /pacientes?sort={asc, desc}
    @GetMapping
    public ResponseEntity<List<Paciente>> getPacientes(@RequestParam(defaultValue = "asc") String sort) {
        List<Paciente> sortedPacientes = new ArrayList<>(pacientes);
        if (sort.equalsIgnoreCase("desc")) {
            sortedPacientes.sort(Comparator.comparing(Paciente::getNome).reversed());
        } else {
            sortedPacientes.sort(Comparator.comparing(Paciente::getNome));
        }
        return ResponseEntity.ok(sortedPacientes);
    }

    // GET /pacientes/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Paciente> getPacienteById(@PathVariable Long id) {
        return pacientes.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /pacientes/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Paciente> updatePaciente(@PathVariable Long id, @RequestBody Paciente updatedPaciente) {
        for (Paciente paciente : pacientes) {
            if (paciente.getId().equals(id)) {
                paciente.setNome(updatedPaciente.getNome());
                paciente.setEndereco(updatedPaciente.getEndereco());
                paciente.setBairro(updatedPaciente.getBairro());
                paciente.setEmail(updatedPaciente.getEmail());
                paciente.setTelefone(updatedPaciente.getTelefone());
                paciente.setDataNascimento(updatedPaciente.getDataNascimento());
                paciente.setUpdatedAt(LocalDateTime.now());
                return ResponseEntity.ok(paciente);
            }
        }
        return ResponseEntity.notFound().build();
    }

    // DELETE /pacientes/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaciente(@PathVariable Long id) {
        boolean removed = pacientes.removeIf(p -> p.getId().equals(id));
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
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

        List<Consulta> result = consultas.stream()
                .filter(c -> c.getPacienteId().equals(id))
                .filter(c -> c.getStatusConsulta().equalsIgnoreCase(status))
                .filter(c -> !c.getDataConsulta().isBefore(startDate) && !c.getDataConsulta().isAfter(endDate))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }
}