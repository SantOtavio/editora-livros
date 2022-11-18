package br.senai.sc.editoralivros.model.entities;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;


@Entity
@Table(name = "tb_livros")
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Livro {

    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(length = 13, nullable = false, unique = true)
    private Long isbn;

    @Column(length = 50, nullable = false)
    private String titulo;

    @ManyToOne
    @JoinColumn(name = "cpf_autor", nullable = false)
    private Autor autor;

    @Column(nullable = false)
    private Integer qtdPag;

    @ManyToOne
    @JoinColumn(name = "cpf_revisor")
    private Revisor revisor;

    @Column(nullable = false)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "nome_editora")
    private Editora editora;

    @ManyToOne(cascade = CascadeType.ALL)
    private Arquivo arquivo;

    public void setArquivo(MultipartFile file) {
        try {
            this.arquivo = new Arquivo(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
