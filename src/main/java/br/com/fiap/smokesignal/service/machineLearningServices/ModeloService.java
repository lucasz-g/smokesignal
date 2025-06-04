package br.com.fiap.smokesignal.service.machineLearningServices;

import org.springframework.stereotype.Service;
import br.com.fiap.smokesignal.service.csvServices.ReaderCsvService;
import smile.classification.LogisticRegression;
import smile.data.DataFrame;

@Service
public class ModeloService {

    private final ReaderCsvService readerCsvService;
    private LogisticRegression modelo;
    private DataFrame dados;

    public ModeloService(ReaderCsvService readerCsvService) {
        this.readerCsvService = readerCsvService;
    }

    private void treinarModelo() {
        if (modelo == null || dados == null) {
            DataFrame df = readerCsvService.lerCsvComoDataFrame("df_modelo_dummies.csv");

            System.out.println(df.schema());
            System.out.println(df.names());
            
            int[] y = df.column("FIRE_OCCURED").toIntArray();
            df = df.drop("FIRE_OCCURED").drop("FIRE_COUNT");
            double[][] X = df.toArray();

            modelo = LogisticRegression.fit(X, y);
            this.dados = df;
        }
    }

    public double calcularProb(String estado, int mes) {
        treinarModelo();

        double[] entrada = new double[dados.ncol()];
        String estadoCol = "STATE_" + estado.toUpperCase();
        String mesCol = "MES_" + mes;

        for (int i = 0; i < dados.ncol(); i++) {
            String nomeColuna = dados.names()[i];
            entrada[i] = (nomeColuna.equals(estadoCol) || nomeColuna.equals(mesCol)) ? 1.0 : 0.0;
        }

        double[] probs = new double[2];
        modelo.predict(entrada, probs);  // preenche as probabilidades
        return probs[1];  // probabilidade de ocorrer incêndio
    }
}
