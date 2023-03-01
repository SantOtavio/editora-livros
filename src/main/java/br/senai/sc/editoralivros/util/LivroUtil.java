package br.senai.sc.editoralivros.util;

import br.senai.sc.editoralivros.DTO.LivroDTO;
import br.senai.sc.editoralivros.model.entities.Livro;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.fit.pdfdom.PDFDomTree;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

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

    private static void generateHTMLFromPDF(String filename) throws IOException {
        PDDocument pdf = PDDocument.load(new File(filename));
        Writer output = new PrintWriter("src/output/pdf.html", "utf-8");
        new PDFDomTree().writeText(pdf, output);

        output.close();
    }

    public static void main(String[] args) throws IOException {

        generateHTMLFromPDF("src/output/pdf.pdf");

    }

}
