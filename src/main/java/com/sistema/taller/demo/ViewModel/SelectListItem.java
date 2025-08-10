package com.sistema.taller.demo.ViewModel;

public class SelectListItem {
    private String value;
    private String text;

    public SelectListItem(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
