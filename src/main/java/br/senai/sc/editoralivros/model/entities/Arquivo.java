package br.senai.sc.editoralivros.model.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tb_arquivos")
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Getter @Setter
@ToString
public class Arquivo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @NonNull
    private String nome;
    @NonNull
    private String tipo;
    //para criar um construtor com parametros diferente do padrão, não é todos mas não é vazio, para isso usamos o nonnull
    @NonNull
    @Lob
    private byte[] arquivo;


}
