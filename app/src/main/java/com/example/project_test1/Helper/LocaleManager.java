package com.example.project_test1.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import java.util.Locale;

public class LocaleManager {

    private static final String SELECTED_LANGUAGE_KEY = "selected_language";

    public static void setLocale(Context context, String language) {
        // Save the selected language to SharedPreferences for persistence
        saveSelectedLanguage(context, language);

        // Set the locale for the entire application
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(new Locale(language));
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    public static String getSelectedLanguage(Context context) {
        // Retrieve the selected language from SharedPreferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(SELECTED_LANGUAGE_KEY, Locale.getDefault().getLanguage());
    }

    private static void saveSelectedLanguage(Context context, String language) {
        // Save the selected language to SharedPreferences
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(SELECTED_LANGUAGE_KEY, language);
        editor.apply();
    }
}

