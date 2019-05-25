package ru.homecatering;

import android.graphics.drawable.Drawable;

class LeftMenuModel {
    private String text, id;
    private Drawable icon;
    boolean hasChildren;

    LeftMenuModel(String text, String id, Drawable icon, boolean hasChildren){
        this.hasChildren = hasChildren;
        this.icon = icon;
        this.id = id;
        this.text = text;
    }


    String getText() {
        return text;
    }

    String getId() {
        return id;
    }

    Drawable getIcon() {
        return icon;
    }
}

