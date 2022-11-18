package br.senai.sc.editoralivros.DTO;

import br.senai.sc.editoralivros.model.entities.Autor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LivroDTO {
    private Long isbn;
    private String titulo;
    private Autor autor;
    private Integer qtdPag;
}
