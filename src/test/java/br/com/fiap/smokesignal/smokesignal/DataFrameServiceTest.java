package br.com.fiap.smokesignal.smokesignal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.fiap.smokesignal.service.machineLearningServices.DataFrameService;
import smile.data.DataFrame;

import static org.junit.jupiter.api.Assertions.*;
import java.util.logging.Logger;


@SpringBootTest
public class DataFrameServiceTest {

    @Autowired
    private DataFrameService dataFrameService;

    @Test
    public void testGetDf() {
        DataFrame df = dataFrameService.getDf("amostra_500k.csv");
        assertNotNull(df, "DataFrame não deve ser nulo");
        assertTrue(df.nrow() > 0, "DataFrame deve ter pelo menos uma linha");
        assertTrue(df.ncol() > 0, "DataFrame deve ter pelo menos uma coluna");
        
        final Logger logger = Logger.getLogger(DataFrameServiceTest.class.getName());

        logger.info("Número de linhas: " + df.nrow());
    }
}
