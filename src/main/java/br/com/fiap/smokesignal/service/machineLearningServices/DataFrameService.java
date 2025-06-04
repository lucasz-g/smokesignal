package br.com.fiap.smokesignal.service.machineLearningServices;

import java.nio.file.Paths;

import org.springframework.stereotype.Service;

import smile.data.DataFrame;
import smile.io.Read;

@Service
public class DataFrameService {
    
    private DataFrame df;
    
    public DataFrame getDf(String enderecoDataframe) {
        if (df == null) {
            try {
                String path = Paths.get(getClass().getClassLoader().getResource(enderecoDataframe).toURI()).toString();
                df = Read.csv(path);
                System.out.println(path);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Erro ao carregar o CSV", e);
            }
        }
        return df;
    }
}