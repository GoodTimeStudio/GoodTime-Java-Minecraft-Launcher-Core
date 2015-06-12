package com.mcgoodtime.com.gjmlc.core;

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
     * @param text Minecraft version info file text.
     * @return Missing Libraries Files List.
     */
    private static List<String> checkLibraries(String text) {
        JSONObject jsonObject = new JSONObject(text);
        JSONArray array = (JSONArray) jsonObject.get("libraries");
        List<String> missingLib = new ArrayList<String>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject arrayObject = (JSONObject) array.get(i);
            String lib = arrayObject.getString("name");
            String a = lib.substring(0, lib.lastIndexOf(":")).replace(".", "/").replace(":", "/");
            String b = lib.substring(lib.lastIndexOf(":") + 1);
            String c = lib.substring(lib.indexOf(":") + 1).replace(":", "-");
            String libs = "./.minecraft/libraries/" + a + "/" + b + "/" + c + ".jar";
            File fileLib = new File(libs);
            if (!fileLib.exists()) {
                if (arrayObject.has("natives")) {
                    String os = System.getProperties().getProperty("os.name");
                    if (os != null) {
                        if (os.startsWith("Windows")) {
                            System.out.println("Windows System");
                        }
                    }
                } else {
                    missingLib.add(lib);
                }
            }
        }
        System.out.println("Missing " + missingLib.size() + " Libraries");
        System.out.println(missingLib);

        return missingLib;
    }
}
