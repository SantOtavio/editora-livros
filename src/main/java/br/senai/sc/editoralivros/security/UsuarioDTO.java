package br.senai.sc.editoralivros.security;

import lombok.Data;

@Data
public class UsuarioDTO {
    private String email;
    private String senha;
}
