package br.com.fiap.checkpoint3.repository;

import br.com.fiap.checkpoint3.model.Consulta;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConsultaRepository {
    private static final List<Consulta> consultas = new ArrayList<>();
    private static Long idCounter = 1L;

    public Consulta save(Consulta consulta) {
        if (consulta.getId() == null) {
            consulta.setId(idCounter++);
            consultas.add(consulta);
        } else {
            consultas.removeIf(c -> c.getId().equals(consulta.getId()));
            consultas.add(consulta);
        }
        return consulta;
    }

    public List<Consulta> findAll() {
        return new ArrayList<>(consultas);
    }

    public Optional<Consulta> findById(Long id) {
        return consultas.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    public void deleteById(Long id) {
        consultas.removeIf(c -> c.getId().equals(id));
    }
}