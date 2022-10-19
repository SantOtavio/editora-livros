package br.senai.sc.editoralivros.factory;

import br.senai.sc.editoralivros.model.entities.Autor;
import br.senai.sc.editoralivros.model.entities.Diretor;
import br.senai.sc.editoralivros.model.entities.Pessoa;
import br.senai.sc.editoralivros.model.entities.Revisor;

public class PessoaFactory {

    public static PessoaFactory getInstance() {
        return new PessoaFactory();
    }

    public Pessoa getPessoa(String tipo) {
        if (tipo.equals("autor")) {
            return new Autor();
        } else if (tipo.equals("diretor")) {
            return new Diretor();
        } else if (tipo.equals("revisor")) {
            return new Revisor();
        }
        return null;
    }
}

