package br.senai.sc.editoralivros.DTO;

import br.senai.sc.editoralivros.model.entities.Genero;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ToString
@Getter
@Setter
@Data
public class PessoaDTO {
    private Long cpf;
    private String nome;
    private String sobrenome;
    private String email;
    private String senha;
    private Genero genero;
    private String tipo;
}
