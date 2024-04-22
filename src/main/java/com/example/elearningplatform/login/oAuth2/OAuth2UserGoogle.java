package com.example.elearningplatform.login.oAuth2;

import java.util.Map;

public class OAuth2UserGoogle extends OAuth2UserDetails {

    public OAuth2UserGoogle(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getPicture() {
        return (String) attributes.get("picture");
    }

    @Override
    public String getFirstName() {
        return (String) attributes.get("given_name");
    }

    @Override
    public String getLastName() {
        return attributes.get("family_name").toString();
    }

}
