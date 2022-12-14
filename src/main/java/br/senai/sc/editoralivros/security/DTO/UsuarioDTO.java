package br.senai.sc.editoralivros.security.DTO;

import lombok.Data;

@Data
public class UsuarioDTO {
    private String email;
    private String senha;
}
