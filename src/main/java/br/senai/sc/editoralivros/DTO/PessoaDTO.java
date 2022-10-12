package br.senai.sc.editoralivros.DTO;

import br.senai.sc.editoralivros.model.entities.Genero;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
public class PessoaDTO {
    @NotBlank(message = "CPF é obrigatório")
    private Long cpf;
    @NotBlank(message = "Nome é obrigatório")
    @Min(value = 3, message = "Nome deve ter no mínimo 3 caracteres")
    @Max(value = 50, message = "Nome deve ter no máximo 50 caracteres")
    private String nome;
    @NotBlank(message = "Sobrenome é obrigatório")
    @Max(value = 100, message = "Sobrenome deve ter no máximo 100 caracteres")
    @Min(value = 3, message = "Sobrenome deve ter no mínimo 3 caracteres")
    private String sobrenome;
    @NotBlank(message = "Email é obrigatório")
    @Max(value = 150, message = "Email deve ter no máximo 150 caracteres")
    @Min(value = 3, message = "Email deve ter no mínimo 3 caracteres")
    private String email;
    @NotBlank(message = "Senha é obrigatória")
    @Max(value = 25, message = "Senha deve ter no máximo 25 caracteres")
    @Min(value = 3, message = "Senha deve ter no mínimo 3 caracteres")
    private String senha;
    @Min(value = 0, message = "Gênero é obrigatório")
    private Genero genero;
}
