package com.example.fb_application;

public class MySingletonClass {

    private static MySingletonClass instance;

    /**
     * cette classe permet d'avoir des objets globaux à toute les activités
     */
    public static MySingletonClass getInstance() {
        if (instance == null)
            instance = new MySingletonClass();
        return instance;
    }

    private MySingletonClass() {
    }

    private String ipAddress = "192.168.0.44";
    private Boolean darkMode = false;
    private int ln = 0;

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String value) {
        this.ipAddress = value;
    }

    public int getLanguage() {
        return ln;
    }

    public void setLanguage(int value) {
        this.ln = value;
    }

    public Boolean isDarkMode() {
        return darkMode;
    }

    public void setDarkMode(Boolean value) {
        this.darkMode = value;
    }

}