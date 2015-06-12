package com.mcgoodtime.com.gjmlc.core;

import org.apache.commons.lang3.SystemUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by suhao on 2015-6-11-0011.
 *
 * @author suhao
 */
public class NativesManager {

    private static List<String> checkNatives(String text, String version) {
        JSONObject jsonObject = new JSONObject(text);
        JSONArray array = (JSONArray) jsonObject.get("libraries");
        List<String> missingLib = new ArrayList<String>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject arrayObject = (JSONObject) array.get(i);
            if (arrayObject.has("natives")) {
                String lib = arrayObject.getString("name");
                String a = lib.substring(0, lib.lastIndexOf(":")).replace(".", "/").replace(":", "/");
                String b = lib.substring(lib.lastIndexOf(":") + 1);
                String c = lib.substring(lib.indexOf(":") + 1).replace(":", "-");

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
                String libs = "./.minecraft/libraries/" + a + "/" + b + "/" + c + "-" + natives + ".jar";
                File fileLib = new File(libs);
                if (!fileLib.exists()) {
                    missingLib.add(libs);
                }
            }
        }
        return missingLib;
    }
}
