package br.com.fiap.smokesignal.service.csvServices;

import br.com.fiap.smokesignal.entity.IncendioEntity;
import br.com.fiap.smokesignal.entity.LocalidadeEntity;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class CsvToIncendioMapper {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/mm/dd");

    public IncendioEntity map(String[] line, String[] header) throws Exception {
        int colId = indexOf(header, "FOD_ID");
        int colFireYear = indexOf(header, "DISCOVERY_DATE");
        int colCause = indexOf(header, "NWCG_GENERAL_CAUSE");
        int colFireSize = indexOf(header, "FIRE_SIZE");
        int colLatitude = indexOf(header, "LATITUDE");
        int colLongitude = indexOf(header, "LONGITUDE");
        int colState = indexOf(header, "STATE");

        Long id = Long.parseLong(line[colId]); 
        Date data = sdf.parse(line[colFireYear]);
        String causa = line[colCause];
        Double area = Double.parseDouble(line[colFireSize]);
        Double lat = Double.parseDouble(line[colLatitude]);
        Double lon = Double.parseDouble(line[colLongitude]);
        String estado = line[colState];

        IncendioEntity incendio = new IncendioEntity();
        incendio.setId(id);
        incendio.setDataIncendio(data);
        incendio.setCausa(causa);
        incendio.setAreaQueimada(area);

        LocalidadeEntity local = new LocalidadeEntity();
        local.setId(id);
        local.setLatitude(lat);
        local.setLongitude(lon);
        local.setEstado(estado);
        incendio.setLocal(local);

        return incendio;
    }

    private int indexOf(String[] header, String colName) {
        for (int i = 0; i < header.length; i++) {
            if (header[i].equalsIgnoreCase(colName)) return i;
        }
        throw new IllegalArgumentException("Coluna não encontrada: " + colName);
    }
}
