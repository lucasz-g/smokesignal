package br.com.fiap.smokesignal.service.csvServices;

import com.opencsv.CSVReader;

import smile.data.DataFrame;
import smile.io.Read;

import org.apache.commons.csv.CSVFormat;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.net.URL;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReaderCsvService {

    public List<String[]> lerCsv(String caminhoCsv) {
        List<String[]> linhas = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(caminhoCsv))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                linhas.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace(); 
        }

        return linhas;
    }

    public DataFrame lerCsvComoDataFrame(String caminho) {
        try {
            URL resource = getClass().getClassLoader().getResource(caminho);
            if (resource == null) {
                throw new RuntimeException("Arquivo CSV não encontrado no classpath: " + caminho);
            }
            Path path = Paths.get(resource.toURI());
            CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader()  // <- faz ele usar a primeira linha como header
                .setSkipHeaderRecord(true) // <- ignora a linha de header ao iterar
                .get();
            return Read.csv(path.toString(), format);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao carregar DataFrame do CSV: " + caminho, e);
        }
    }
}
