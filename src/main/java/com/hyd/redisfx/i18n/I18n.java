package com.hyd.redisfx.i18n;

import java.util.ResourceBundle;

/**
 * (description)
 * created at 2017/3/14
 *
 * @author yidin
 */
public class I18n {

    private static final ResourceBundle UI_MAIN_BUNDLE = ResourceBundle.getBundle(
            "i18n.uiMain", new XMLResourceBundleControl());

    public static String getString(String key) {
        return UI_MAIN_BUNDLE.getString(key);
    }

    public static String[] getStringArray(String key) {
        return UI_MAIN_BUNDLE.getStringArray(key);
    }
}
