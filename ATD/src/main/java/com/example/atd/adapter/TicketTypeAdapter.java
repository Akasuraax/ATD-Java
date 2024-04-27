package com.example.atd.adapter;

import com.example.atd.model.Support;
import com.example.atd.model.Ticket;
import com.example.atd.model.User; // Assurez-vous d'importer la classe User appropriée
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TicketTypeAdapter extends TypeAdapter<Ticket> {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");

    @Override
    public void write(JsonWriter out, Ticket value) throws IOException {
        out.beginObject();
        out.name("id").value(value.getId());
        out.name("title").value(value.getTitle());
        out.name("description").value(value.getDescription());
        out.name("status").value(value.getStatus());
        out.name("severity").value(value.getSeverity());
        out.name("archive").value(value.isArchive());
        out.name("created_at").value(value.getCreatedAt().format(DATE_TIME_FORMATTER));
        out.name("updated_at").value(value.getUpdatedAt().format(DATE_TIME_FORMATTER));
        out.name("problem").value(value.getProblem());
        // Ajouter le support
        out.name("support");
        // Supposons que vous ayez un TypeAdapter pour User
        SupportTypeAdapter userTypeAdapter = new SupportTypeAdapter();
        userTypeAdapter.write(out, value.getSupport());
        out.endObject();
    }

    @Override
    public Ticket read(JsonReader in) throws IOException {
        Ticket ticket = new Ticket();
        in.beginObject();
        while (in.hasNext()) {
            String name = in.nextName();
            switch (name) {
                case "id":
                    ticket.setId(in.nextInt());
                    break;
                case "title":
                    ticket.setTitle(in.nextString());
                    break;
                case "description":
                    ticket.setDescription(in.nextString());
                    break;
                case "status":
                    ticket.setStatus(in.nextInt());
                    break;
                case "severity":
                    ticket.setSeverity(in.nextInt());
                    break;
                case "archive":
                    ticket.setArchive(in.nextBoolean());
                    break;
                case "created_at":
                    String dateTimeString = in.nextString();
                    ticket.setCreatedAt(LocalDateTime.parse(dateTimeString, DATE_TIME_FORMATTER));
                    break;
                case "updated_at":
                    String dateTimeStringU = in.nextString();
                    ticket.setUpdatedAt(LocalDateTime.parse(dateTimeStringU, DATE_TIME_FORMATTER));
                    break;
                case "problem":
                    ticket.setProblem(in.nextString());
                    break;
                case "support":
                    if (in.peek() == JsonToken.NULL) {
                        in.nextNull(); // Consommez le token null
                        ticket.setSupport(null); // Définissez le support à null
                    } else {
                        SupportTypeAdapter userTypeAdapter = new SupportTypeAdapter();
                        Support support = userTypeAdapter.read(in);
                        ticket.setSupport(support); // Définissez le support avec la valeur lue
                    }
                    break;
                default:
                    in.skipValue(); // Ignorez les champs supplémentaires
                    break;
            }
        }
        in.endObject();
        return ticket;
    }
}
