package com.gbf.auth.filter.integration;

import java.util.List;

public class AuthorisationData {
    private String login;
    private List<String> authorities;

    public AuthorisationData() {
    }

    public AuthorisationData(String login, List<String> authorities) {
        this.login = login;
        this.authorities = authorities;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }
}
