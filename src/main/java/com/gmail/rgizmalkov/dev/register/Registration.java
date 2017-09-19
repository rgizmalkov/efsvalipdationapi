package com.gmail.rgizmalkov.dev.register;

public class Registration {
    private String criticality;
    private String disable;

    public Registration(String criticality, String disable) {
        this.criticality = criticality;
        this.disable = disable;
    }

    public String getCriticality() {
        return criticality;
    }

    public String getDisable() {
        return disable;
    }
}
