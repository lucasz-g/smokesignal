package br.com.fiap.smokesignal.service;

import br.com.fiap.smokesignal.entity.IncendioEntity;
import br.com.fiap.smokesignal.service.csvServices.CsvToIncendioMapper;
import br.com.fiap.smokesignal.service.csvServices.ReaderCsvService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IncendioService {

    @Autowired
    private ReaderCsvService readerCsvService;

    @Autowired
    private CsvToIncendioMapper mapper;

    public List<IncendioEntity> buscarTodosIncendios() {
        String csvFile = "src/main/resources/amostra_500k.csv";
        List<String[]> linhas = readerCsvService.lerCsv(csvFile);

        if (linhas.isEmpty()) return List.of();

        String[] header = linhas.get(0);
        List<IncendioEntity> lista = new ArrayList<>();

        // 100 primeiros registros de incêndio
        for (int i = 1; i < linhas.size() && lista.size() < 100; i++) {
            try {
                IncendioEntity incendio = mapper.map(linhas.get(i), header);
                lista.add(incendio);
            } catch (Exception e) {
                e.printStackTrace(); 
            }
        }

        return lista;
    }
}
