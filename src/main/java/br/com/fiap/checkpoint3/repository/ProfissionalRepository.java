package br.com.fiap.checkpoint3.repository;

import br.com.fiap.checkpoint3.model.Profissional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProfissionalRepository {
    private static final List<Profissional> profissionais = new ArrayList<>();
    private static Long idCounter = 1L;

    public Profissional save(Profissional profissional) {
        if (profissional.getId() == null) {
            profissional.setId(idCounter++);
            profissionais.add(profissional);
        } else {
            profissionais.removeIf(p -> p.getId().equals(profissional.getId()));
            profissionais.add(profissional);
        }
        return profissional;
    }

    public List<Profissional> findAll() {
        return new ArrayList<>(profissionais);
    }

    public Optional<Profissional> findById(Long id) {
        return profissionais.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    public void deleteById(Long id) {
        profissionais.removeIf(p -> p.getId().equals(id));
    }
}