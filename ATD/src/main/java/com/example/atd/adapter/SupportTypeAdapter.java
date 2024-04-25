package com.example.atd.adapter;

import com.example.atd.model.Support;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class SupportTypeAdapter extends TypeAdapter<Support> {

    @Override
    public void write(JsonWriter out, Support value) throws IOException {
        // Implémentez la sérialisation ici si nécessaire
        out.beginObject();
        out.name("id").value(value.getId());
        out.name("name").value(value.getName());
        out.name("forname").value(value.getForname());
        out.endObject();
    }

    @Override
    public Support read(JsonReader in) throws IOException {
        Support support = new Support();
        in.beginObject();
        while (in.hasNext()) {
            String name = in.nextName();
            switch (name) {
                case "id":
                    support.setId(in.nextInt());
                    break;
                case "name":
                    support.setName(in.nextString());
                    break;
                case "forname":
                    support.setForname(in.nextString());
                    break;
                default:
                    in.skipValue(); // Ignorez les champs supplémentaires
                    break;
            }
        }
        in.endObject();
        return support;
    }
}
