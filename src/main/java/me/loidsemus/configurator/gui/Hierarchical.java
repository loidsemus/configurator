package me.loidsemus.configurator.gui;

public interface Hierarchical {
    void setCurrentPath(String path);
    void loadCurrentPath();
    void reload();
    void setBackButton();
}
