package com.mcgoodtime.gjmlc.core;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.List;

/**
 * Created by suhao on 2015-6-8-0008.
 *
 * @author suhao
 */
public class Launcher {

    protected static String versionPath = "./.minecraft/versions/";
    private static String versionInfoJson;

    /*
     * Just test.
     */
    public static void main(String[] args) {
        launch("1.8", "_JAVA7", 2048);
    }

    /**
     *
     * @param version Launch minecraft version.
     * @param username Minecraft username.
     * @param maxMemory Java VM max use memory.
     */
    public static List<String> launch(String version, String username, int maxMemory) {
        String text = loadVersionInfoFile(version);
        LibrariesManager.checkFormJson(text);

        String id = getVersionInfo(text, "id");
        String time = getVersionInfo(text, "time");
        String releaseTime = getVersionInfo(text, "releaseTime");
        String minecraftArguments = getVersionInfo(text, "minecraftArguments");
        int minimumLauncherVersion = getVersionInfoAsInt(text, "minimumLauncherVersion");
        String mainClass = getVersionInfo(text, "mainClass");
        String assets = getVersionInfo(text, "assets");

        String libraries = getLibraries(text);

        tryToLaunch(version, libraries, minecraftArguments, mainClass, assets, username, maxMemory);
        return null;
    }

    private static void tryToLaunch(String version, String libraries, String minecraftArguments,
                                    String mainClass, String assets, String username, int maxMemory) {
        String nativesPath = versionPath + version + "/" + version + "-" + "Natives";
        String chassPath = versionPath + version + "/" + version + ".jar";
        String arg = minecraftArguments.replace("${auth_player_name}", username)
                .replace("${version_name}", version)
                .replace("${game_directory}", "./.minecraft")
                .replace("${assets_root}", "./.minecraft/assets")
                .replace("${assets_index_name}", assets)
                .replace("${auth_uuid}", "auth_uuid")
                .replace("${auth_access_token}", "auth_access_token")
                .replace("${user_properties}", "{}")
                .replace("${user_type}", "legacy");

        String cmd = "java -Xmx" + maxMemory + "M" + " " + "-Djava.library.path=" + nativesPath + " "
                + "-classpath" + " " + libraries + "\"" + chassPath + "\"" + " " + mainClass + " " + arg;
        System.out.println(cmd);
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    * Get Version Info From Loaded Json
    */
    private static String getVersionInfo(String text, String key) {
        JSONObject jsonObject = new JSONObject(text);
        String value = null;
        try {
            value = jsonObject.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
    * Get Version Info From Loaded Json
    */
    private static int getVersionInfoAsInt(String text, String key) {
        JSONObject jsonObject = new JSONObject(text);
        int value = 0;
        try {
            value = jsonObject.getInt(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * Load From Json File
     * @param version Launch Minecraft version.
     * @return Minecraft version info file text.
     */
    protected static String loadVersionInfoFile(String version) {
        versionInfoJson = versionPath + version + "/" + version + ".json";
        System.out.println("Version Info Json Path:" + versionInfoJson);

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
        return stringBuffer.toString();
    }

    protected static String  getLibraries(String text) {
        JSONObject jsonObject = new JSONObject(text);
        JSONArray array = (JSONArray) jsonObject.get("libraries");
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < array.length(); i++) {
            JSONObject arrayObject = (JSONObject) array.get(i);
            String lib = arrayObject.get("name").toString();
            String a = lib.substring(0, lib.lastIndexOf(":")).replace(".", "/").replace(":", "/");
            String b = lib.substring(lib.lastIndexOf(":") + 1);
            String c = lib.substring(lib.indexOf(":") + 1).replace(":", "-");
            String libs = "\"" + "./.minecraft/libraries/" + a + "/" + b + "/" + c + ".jar" + "\"" + ";";
            stringBuffer.append(libs);
        }
        return stringBuffer.toString();
    }
}
