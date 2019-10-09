package utils;

import aquality.selenium.utils.JsonFile;

public class ProjectSettingsReader {
    private static final String SETTINGS_FILE_NAME = "project_settings.json";
    private static ProjectSettingsReader instance = null;
    private String siteUrl;
    private String victoryNotificationText;

    private ProjectSettingsReader() {
        JsonFile settingsFile = new JsonFile(SETTINGS_FILE_NAME);
        this.siteUrl = settingsFile.getValue("/siteUrl").toString();
        this.victoryNotificationText = settingsFile.getValue("/victoryNotificationText").toString();
    }

    public static ProjectSettingsReader getInstance() {
        if (instance == null)
            instance = new ProjectSettingsReader();
        return instance;
    }

    public String getSiteUrl() {
        return siteUrl;
    }

    public String getVictoryNotificationText() {
        return victoryNotificationText;
    }
}
