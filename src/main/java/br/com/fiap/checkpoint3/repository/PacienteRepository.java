package br.com.fiap.checkpoint3.repository;

import br.com.fiap.checkpoint3.model.Paciente;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PacienteRepository {
    private static final List<Paciente> pacientes = new ArrayList<>();
    private static Long idCounter = 1L;

    public Paciente save(Paciente paciente) {
        if (paciente.getId() == null) {
            paciente.setId(idCounter++);
            pacientes.add(paciente);
        } else {
            pacientes.removeIf(p -> p.getId().equals(paciente.getId()));
            pacientes.add(paciente);
        }
        return paciente;
    }

    public List<Paciente> findAll() {
        return new ArrayList<>(pacientes);
    }

    public Optional<Paciente> findById(Long id) {
        return pacientes.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    public void deleteById(Long id) {
        pacientes.removeIf(p -> p.getId().equals(id));
    }
}