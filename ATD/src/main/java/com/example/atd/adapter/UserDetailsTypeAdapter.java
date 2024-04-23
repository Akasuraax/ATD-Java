package com.example.atd;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UserDetailsTypeAdapter extends TypeAdapter<UserDetails> {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");

    @Override
    public void write(JsonWriter out, UserDetails value) throws IOException {
        // Implémentez la sérialisation ici si nécessaire
    }

    @Override
    public UserDetails read(JsonReader in) throws IOException {
        UserDetails userDetails = new UserDetails();
        in.beginObject();
        while (in.hasNext()) {
            String name = in.nextName();
            switch (name) {
                case "id":
                    userDetails.setId(in.nextInt());
                    break;
                case "name":
                    userDetails.setName(in.nextString());
                    break;
                case "forname":
                    userDetails.setForname(in.nextString());
                    break;
                case "email":
                    userDetails.setEmail(in.nextString());
                    break;
                case "status":
                    userDetails.setStatus(in.nextInt());
                    break;
                case "created_at":
                    String dateTimeString = in.nextString();
                    userDetails.setCreatedAt(LocalDateTime.parse(dateTimeString, DATE_TIME_FORMATTER));
                    break;
                case "updated_at":
                    String dateTimeStringU = in.nextString();
                    userDetails.setUpdatedAt(LocalDateTime.parse(dateTimeStringU, DATE_TIME_FORMATTER));                    break;
                case "roles":
                    // Vous devez implémenter la logique pour lire la liste des rôles
                    // Cela peut nécessiter un autre TypeAdapter pour la classe Role
                    break;
                default:
                    in.skipValue(); // Ignorez les champs supplémentaires
                    break;
            }
        }
        in.endObject();
        return userDetails;
    }
}
