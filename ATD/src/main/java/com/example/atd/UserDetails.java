package com.example.atd;

import java.time.LocalDateTime;
import java.util.List;

public class UserDetails {
    private int id;
    private String name;
    private String forname;
    private String email;
    private LocalDateTime email_verified_at;
    private String phone_country;
    private String phone_number;
    private int gender;
    private LocalDateTime birth_date;
    private String address;
    private String zipcode;
    private String siret_number;
    private String compagny;
    private int status;
    private boolean visited;
    private boolean ban;
    private boolean notification;
    private boolean archive;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private List<Role> roles;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getForname() {
        return forname;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getEmail_verified_at() {
        return email_verified_at;
    }

    public String getPhone_country() {
        return phone_country;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public int getGender() {
        return gender;
    }

    public LocalDateTime getBirth_date() {
        return birth_date;
    }

    public String getAddress() {
        return address;
    }

    public String getZipcode() {
        return zipcode;
    }

    public String getSiret_number() {
        return siret_number;
    }

    public String getCompagny() {
        return compagny;
    }

    public int getStatus() {
        return status;
    }

    public boolean isVisited() {
        return visited;
    }

    public boolean isBan() {
        return ban;
    }

    public boolean isNotification() {
        return notification;
    }

    public boolean isArchive() {
        return archive;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public List<Role> getRoles() {
        return roles;
    }

    // Constructeur
    public UserDetails(int id, String name, String forname, String email, LocalDateTime email_verified_at,
                       String phone_country, String phone_number, int gender, LocalDateTime birth_date,
                       String address, String zipcode, String siret_number, String compagny, int status,
                       boolean visited, boolean ban, boolean notification, boolean archive,
                       LocalDateTime created_at, LocalDateTime updated_at, List<Role> roles) {
        this.id = id;
        this.name = name;
        this.forname = forname;
        this.email = email;
        this.email_verified_at = email_verified_at;
        this.phone_country = phone_country;
        this.phone_number = phone_number;
        this.gender = gender;
        this.birth_date = birth_date;
        this.address = address;
        this.zipcode = zipcode;
        this.siret_number = siret_number;
        this.compagny = compagny;
        this.status = status;
        this.visited = visited;
        this.ban = ban;
        this.notification = notification;
        this.archive = archive;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.roles = roles;
    }
}
