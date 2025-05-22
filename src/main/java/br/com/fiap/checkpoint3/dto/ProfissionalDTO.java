package br.com.fiap.checkpoint3.dto;

public class ProfissionalDTO {
    private Long id;
    private String nome;
    private String especialidade;

    // Construtores
    public ProfissionalDTO() {}

    public ProfissionalDTO(Long id, String nome, String especialidade) {
        this.id = id;
        this.nome = nome;
        this.especialidade = especialidade;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }
}