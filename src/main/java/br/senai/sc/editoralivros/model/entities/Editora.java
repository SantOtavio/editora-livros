package br.senai.sc.editoralivros.model.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Entity
@Table(name = "tb_editora")
@ToString @EqualsAndHashCode
public class Editora {
    @Id
    @Column(length = 14, nullable = false, unique = true)
    private Long cnpj;

    @Column(length = 50, nullable = false)
    private String nome;
}
