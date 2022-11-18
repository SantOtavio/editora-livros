package br.senai.sc.editoralivros.util;

import br.senai.sc.editoralivros.DTO.LivroDTO;
import br.senai.sc.editoralivros.model.entities.Livro;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class LivroUtil {
    private ObjectMapper objectMapper = new ObjectMapper();

    public Livro convertJsonToLivro(String livroISBN) throws JsonProcessingException {
        LivroDTO livroDTO = convertJsonToDto(livroISBN);
        return convertDtoToModel(livroDTO);
    }

    private LivroDTO convertJsonToDto(String livroISBN) {
        try {
            return this.objectMapper.readValue(livroISBN, LivroDTO.class);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private Livro convertDtoToModel(@Valid LivroDTO livroDTO){
        return this.objectMapper.convertValue(livroDTO, Livro.class);
    }
}
