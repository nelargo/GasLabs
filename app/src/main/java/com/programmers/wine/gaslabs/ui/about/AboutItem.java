package com.programmers.wine.gaslabs.ui.about;


/**
 * Inner class for about items.
 */
public class AboutItem {
    private static final String EMPTY = "";
    private String title = EMPTY;
    private String subtitle = EMPTY;
    private AboutItemType type;

    /**
     * Constructor for about items.
     *
     * @param title    Title of the item.
     * @param subtitle Subtitle of the item
     * @param type     Enum that indicate the type of the item.
     */
    public AboutItem(String title, String subtitle, AboutItemType type) {
        this.title = title;
        this.subtitle = subtitle;
        this.type = type;
    }

    /**
     * Constructor for about items.
     *
     * @param title Title of the item.
     * @param type  Enum that indicate the type of the item.
     */
    public AboutItem(String title, AboutItemType type) {
        this.title = title;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public AboutItemType getType() {
        return type;
    }
}