package com.project.back_end.DTO;

public class Login {

    // L’identifiant de l’utilisateur : email, username, etc.
    private String identifier;

    // Mot de passe fourni par l’utilisateur
    private String password;

    // Constructeur par défaut
    public Login() {
    }

    // Constructeur avec paramètres
    public Login(String identifier, String password) {
        this.identifier = identifier;
        this.password = password;
    }

    // Getters et Setters
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
