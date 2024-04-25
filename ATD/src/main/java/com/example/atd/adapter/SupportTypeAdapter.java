package com.example.atd.adapter;

import com.example.atd.model.Support;
import com.example.atd.model.Ticket;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class SupportTypeAdapter extends TypeAdapter<Support> {

    private final Gson gson = new Gson();
    private final Type ticketListType = new TypeToken<List<Ticket>>(){}.getType();
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
                case "tickets":
                    in.beginArray();
                    List<Ticket> tickets = gson.fromJson(in, ticketListType);
                    support.setTicketList(tickets);
                    in.endArray();
                    break;
                default:
                    in.skipValue();
                    break;
            }
        }
        in.endObject();
        return support;
    }
}
