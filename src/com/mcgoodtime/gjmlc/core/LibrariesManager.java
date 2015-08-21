package com.mcgoodtime.gjmlc.core;

import org.apache.commons.lang3.SystemUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by suhao on 2015-6-11-0011.
 *
 * @author suhao
 */
public class LibrariesManager {

    /**
     *
     * @param version Minecraft version.
     * @return Missing Libraries Files List.
     */
    public static List<String> check(String version) {
        return checkLibraries(Launcher.loadVersionInfoFile(version));
    }

    /**
     *
     * @param text, Loaded json info.
     * @return, Missing Libraries Files List.
     */
    public static List<String> checkFormJson(String text) {
        return checkLibraries(text);
    }

    /**
     * @param text Minecraft version info file text.
     * @return Missing Libraries Files List.
     */
    private static List<String> checkLibraries(String text) {
        List<String> missingLib = new ArrayList<String>();
        for (int i = 0; i < Launcher.libArray.length(); i++) {
            JSONObject arrayObject = (JSONObject) Launcher.libArray.get(i);
            String lib = arrayObject.getString("name");
            String a = lib.substring(0, lib.lastIndexOf(":")).replace(".", "/").replace(":", "/");
            String b = lib.substring(lib.lastIndexOf(":") + 1);
            String c = lib.substring(lib.indexOf(":") + 1).replace(":", "-");
            String libs = "./.minecraft/libraries/" + a + "/" + b + "/" + c + ".jar";
            File fileLib = new File(libs);
            if (!fileLib.exists()) {
                if (arrayObject.has("natives")) {;
                    JSONObject nativesObject = (JSONObject) arrayObject.get("natives");
                    String natives = null;
                    if (SystemUtils.IS_OS_WINDOWS) {
                        natives = nativesObject.getString("windows").replace("${arch}", SystemUtils.OS_ARCH);
                    }
                    if (SystemUtils.IS_OS_LINUX) {
                        natives = nativesObject.getString("linux");
                    }
                    if (SystemUtils.IS_OS_MAC_OSX) {
                        natives = nativesObject.getString("osx");
                    }
                    String nLibs = "./.minecraft/libraries/" + a + "/" + b + "/" + c + "-" + natives + ".jar";
                    File nFileLib = new File(nLibs);
                    if (!nFileLib.exists()) {
                        missingLib.add(lib);
                    }
                } else {
                    missingLib.add(lib);
                }
            }
        }
        System.err.println("Missing " + missingLib.size() + " Libraries");

        for (String s : missingLib) {
            System.err.println(s);
        }

        return missingLib;
    }
}
