package br.com.screenmatchweb.service;

import br.com.screenmatchweb.model.TranslatedData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URLEncoder;

public class QueryMyMemory {
    public static String getTranslation(String text) {
        ObjectMapper mapper = new ObjectMapper();

        ApiConsumption consumption = new ApiConsumption();

        String texto = URLEncoder.encode(text);
        String langpair = URLEncoder.encode("en|pt-br");

        String url = "https://api.mymemory.translated.net/get?q=" + texto + "&langpair=" + langpair;

        String json = consumption.getData(url);

        TranslatedData translation;
        try {
            translation = mapper.readValue(json, TranslatedData.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return translation.responseData().translatedText();
    }
}