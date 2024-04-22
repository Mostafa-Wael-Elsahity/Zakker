package com.example.elearningplatform.login.oAuth2;

import java.util.Map;

public abstract class OAuth2UserDetails {
    Map<String, Object> attributes;

    OAuth2UserDetails(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getEmail();

    public abstract String getFirstName();

    public abstract String getLastName();

    public abstract String getName();

    public abstract String getPicture();

}
