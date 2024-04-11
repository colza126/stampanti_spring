package com.example.stampanti;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class stampa extends JsonSerializer<stampa> {
    public String fronte;
    public String retro;
    public int id;
    public boolean colorata;

    public stampa(String fronte, String retro, int id, boolean colorata) {
        this.fronte = fronte;
        this.retro = retro;
        this.id = id;
        this.colorata = colorata;
    }
    public String getFronte() {
        return fronte;
    }

    public String getRetro() {
        return retro;
    }

    public int getID() {
        return id;
    }

    public boolean isColorata() {
        return colorata;
    }
    
    // Getter e setter

    @Override
    public void serialize(stampa stampa, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("fronte", stampa.getFronte());
        jsonGenerator.writeStringField("retro", stampa.getRetro());
        jsonGenerator.writePOJOField("ID", stampa.getID());
        jsonGenerator.writeBooleanField("colorata", stampa.isColorata());
        jsonGenerator.writeEndObject();
    }
}
