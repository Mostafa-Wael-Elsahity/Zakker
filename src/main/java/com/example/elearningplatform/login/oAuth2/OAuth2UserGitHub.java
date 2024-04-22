package com.example.elearningplatform.login.oAuth2;

import java.util.Map;

public class OAuth2UserGitHub extends OAuth2UserDetails {
    public OAuth2UserGitHub(Map<String, Object> attributes) {
        super(attributes);

    }

    @Override
    public String getEmail() {
        return (String) attributes.get("login");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getPicture() {
        return (String) attributes.get("avatar_url");
    }

    @Override
    public String getFirstName() {
        String name = (String) attributes.get("name");
        String[] parts = name.split(" ");
        return parts[0];
    }

    @Override
    public String getLastName() {
        String name = (String) attributes.get("name");
        String[] parts = name.split(" ", 2);
        return parts.length > 1 ? parts[1] : "";
    }

}
