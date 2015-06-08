package com.mcgoodtime.com.gjmlc.core;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

/**
 * Created by suhao on 2015-6-8-0008.
 */
public class Launcher {

    private static String versionPath = "./.minecraft/versions/";
    private static String versionInfoJson;

    public static void launch(String version, String username, int Max_Memory) {
        String text = loadVersionInfoFile(version);
        String id = getVersionInfo(text, "id");
        String time = getVersionInfo(text, "time");
        String releaseTime = getVersionInfo(text, "releaseTime");
        String minecraftArguments = getVersionInfo(text, "minecraftArguments");
        String minimumLauncherVersion = getVersionInfo(text, "minimumLauncherVersion");
        String mainClass = getVersionInfo(text, "mainClass");
    }

    private static String getVersionInfo(String text, String key) {
        /*
        Get Version Info From Loaded Json
         */
        JSONObject jsonObject = new JSONObject(text);
        String value = null;
        try {
            value = jsonObject.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }

    private static String loadVersionInfoFile(String version) {
        versionInfoJson = versionPath + version + "/" + version + ".json";
        System.out.println("Version Info Json Path:" + versionInfoJson);

        /*
        Load From Json File
         */
        StringBuffer stringBuffer = new StringBuffer();
        String line = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(versionInfoJson)));
            while( (line = br.readLine())!= null ){
                stringBuffer.append(line);
            }
            br.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }

    private static String  getLibraries(String text) {
        JSONObject jsonObject = new JSONObject(text);
        JSONArray array = (JSONArray) jsonObject.get("libraries");
        String libs = null;
        for (int i = 0; i < array.length(); i++) {
            JSONObject arrayObject = (JSONObject) array.get(i);
            String lib = arrayObject.get("name").toString();
            String a = lib.substring(0, lib.lastIndexOf(":")).replace(".", "/").replace(":", "/");
            String b = lib.substring(lib.lastIndexOf(":") + 1);
            String c = lib.substring(lib.indexOf(":") + 1).replace(":", "-");
            libs = "\"" + "./.minecraft/libraries/" + a + "/" + b + "/" + c + ".jar";
        }
        return libs;
    }
}
