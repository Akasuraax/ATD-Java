package com.example.atd.adapter;

import com.example.atd.model.Message;
import com.example.atd.model.User;
import com.example.atd.model.UserDetails;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MessageTypeAdapter extends TypeAdapter<Message> {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");

    @Override
    public void write(JsonWriter out, Message value) throws IOException {
        // Implémentation de la méthode write si nécessaire
    }

    @Override
    public Message read(JsonReader in) throws IOException {
        Message message = new Message();
        in.beginObject();
        while (in.hasNext()) {
            String name = in.nextName();
            switch (name) {
                case "id":
                    message.setId(in.nextInt());
                    break;
                case "description":
                    message.setDescription(in.nextString());
                    break;
                case "archive":
                    message.setArchive(in.nextBoolean());
                    break;
                case "created_at":
                    String dateTimeString = in.nextString();
                    message.setCreatedAt(LocalDateTime.parse(dateTimeString, DATE_TIME_FORMATTER));
                    break;
                case "updated_at":
                    String dateTimeStringU = in.nextString();
                    message.setUpdatedAt(LocalDateTime.parse(dateTimeStringU, DATE_TIME_FORMATTER));
                    break;
                case "id_user":
                    message.setIdUser(in.nextInt());
                    break;
                case "id_ticket":
                    message.setIdTicket(in.nextInt());
                    break;
                case "user_who_send_the_message":
                    if (in.peek() == JsonToken.NULL) {
                        in.nextNull(); // Consommez le token null
                        message.setUserWhoSendTheMessage(null); // Définissez l'utilisateur à null
                    } else {
                        UserDetailsTypeAdapter userTypeAdapter = new UserDetailsTypeAdapter();
                        UserDetails user = userTypeAdapter.read(in);
                        message.setUserWhoSendTheMessage(user); // Définissez l'utilisateur avec la valeur lue
                    }
                    break;
                default:
                    in.skipValue(); // Ignorez les champs supplémentaires
                    break;
            }
        }
        in.endObject();
        return message;
    }
}
