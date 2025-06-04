package br.com.fiap.smokesignal.service.test;

import br.com.fiap.smokesignal.service.machineLearningServices.DataFrameService;
import smile.data.DataFrame;

public class DataFrameServiceTest {
    public static void main(String[] args) {
        DataFrameService dataFrameService = new DataFrameService();
        DataFrame df = dataFrameService.getDf("amostra_500k.csv");
        if (df != null) {
            System.out.println("Dataframe processado!");
            System.out.println(df.ncol());
            System.out.println(df.nrow());    
        }
        
    }
}
