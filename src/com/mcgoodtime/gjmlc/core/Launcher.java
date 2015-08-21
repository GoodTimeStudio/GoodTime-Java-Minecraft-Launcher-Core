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

    public static JSONObject verInfoObject;
    public static JSONArray libArray;

    private static String text;

    protected static String versionPath = "./.minecraft/versions/";
    private static String versionInfoJson;

    private String version;
    private String parentVer;

    /*
     * Demo.
     */
    public static void main(String[] args) {
        Launcher launcher = new Launcher("1.7.10-Forge10.13.4.1448-1.7.10");
        launcher.launch("BestOwl", 2048, null);
    }

    public Launcher(String version) {
        text = loadVersionInfoFile(version);
        verInfoObject = new JSONObject(text);
        libArray = (JSONArray) verInfoObject.get("libraries");
        this.version = version;
    }

    /**
     * Before you launch, you must run the libraries checker.
     *
     * @param username Minecraft username.
     * @param maxMemory Java VM max use memory.
     */
    public void launch(String username, int maxMemory, String jvmArgs) {
        String id = getVersionInfo("id");
        String time = getVersionInfo("time");
        String releaseTime = getVersionInfo("releaseTime");
        String minecraftArguments = getVersionInfo("minecraftArguments");
        int minimumLauncherVersion = getVersionInfoAsInt(text, "minimumLauncherVersion");
        String mainClass = getVersionInfo("mainClass");
        String assets = getVersionInfo("assets");

        String libraries;
        Boolean isInherits;
        if (verInfoObject.has("inheritsFrom")) {
            libraries = getLibraries(text);

            parentVer = getVersionInfo("inheritsFrom");
            String parentText = loadVersionInfoFile(parentVer);
            JSONObject parentVerInfoObj = new JSONObject(parentText);
            JSONArray parentLibArray = (JSONArray) parentVerInfoObj.get("libraries");

            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < parentLibArray.length(); i++) {
                JSONObject arrayObject = (JSONObject) parentLibArray.get(i);
                String lib = arrayObject.get("name").toString();
                String a = lib.substring(0, lib.lastIndexOf(":")).replace(".", "/").replace(":", "/");
                String b = lib.substring(lib.lastIndexOf(":") + 1);
                String c = lib.substring(lib.indexOf(":") + 1).replace(":", "-");
                String libs = "\"" + "./.minecraft/libraries/" + a + "/" + b + "/" + c + ".jar" + "\"" + ";";
                stringBuffer.append(libs);
            }
            isInherits = true;
            libraries = libraries + stringBuffer.toString();
        } else {
            isInherits = false;
            libraries = getLibraries(text);
        }

        if (jvmArgs == null) {
            jvmArgs = "";
        }

        tryToLaunch(libraries, minecraftArguments, mainClass, assets, username, maxMemory, jvmArgs, isInherits);
    }

    private void tryToLaunch(String libraries, String minecraftArguments,
                                    String mainClass, String assets, String username, int maxMemory, String jvmArgs, boolean isInherits) {
        String nativesPath = versionPath + version + "/" + version + "-" + "Natives";

        String classPath;
        if (isInherits) {
            classPath = versionPath + parentVer + "/" + parentVer + ".jar";
        } else {
            classPath = versionPath + version + "/" + version + ".jar";
        }

        String arg = minecraftArguments.replace("${auth_player_name}", username)
                .replace("${version_name}", version)
                .replace("${game_directory}", "./.minecraft")
                .replace("${assets_root}", "./.minecraft/assets")
                .replace("${assets_index_name}", assets)
                .replace("${auth_uuid}", "auth_uuid")
                .replace("${auth_access_token}", "auth_access_token")
                .replace("${user_properties}", "{}")
                .replace("${user_type}", "legacy");

        String cmd = "java -Xmx" + maxMemory + "M" + " " + "-XX:+UseConcMarkSweepGC -XX:+CMSIncrementalMode -XX:-UseAdaptiveSizePolicy" + " "
                + jvmArgs + "-Djava.library.path=" + nativesPath + " " + "-classpath" + " " + libraries + "\"" + classPath + "\"" + " " + mainClass + " " + arg;
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
    private String getVersionInfo(String key) {
        String value = null;
        try {
            value = verInfoObject.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
    * Get Version Info From Loaded Json
    */
    private int getVersionInfoAsInt(String text, String key) {
        int value = 0;
        try {
            value = verInfoObject.getInt(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * Load version info from json file
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

    protected String  getLibraries(String text) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < libArray.length(); i++) {
            JSONObject arrayObject = (JSONObject) libArray.get(i);
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
