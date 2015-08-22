package com.mcgoodtime.gjmlc.core;

import org.apache.commons.lang3.SystemUtils;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by suhao on 2015-6-11-0011.
 *
 * @author suhao
 */
public class LibrariesManager {

    private static List<String> nativesLibList = new ArrayList<>();
    private static List<String> missingLib = new ArrayList<>();
    private static List<String> libList = new ArrayList<>();

    public LibrariesManager() {
        for (int i = 0; i < Launcher.libArray.length(); i++) {
            JSONObject arrayObject = (JSONObject) Launcher.libArray.get(i);
            String lib = arrayObject.getString("name");

            if (arrayObject.has("natives")) {
                nativesLibList.add(lib);
            } else {
                libList.add(lib);
            }
        }


        deleteFile(Launcher.nativesPath);
        unzipNatives();
    }

    /**
     * @return Missing Libraries Files List.
     */
    protected static List<String> checkLibraries() {
        for (int i = 0; i < libList.size(); i++) {
            String lib = libList.get(i);
            String a = lib.substring(0, lib.lastIndexOf(":")).replace(".", "/").replace(":", "/");
            String b = lib.substring(lib.lastIndexOf(":") + 1);
            String c = lib.substring(lib.indexOf(":") + 1).replace(":", "-");
            String libs = "./.minecraft/libraries/" + a + "/" + b + "/" + c + ".jar";

            if (!new File(libs).exists()) {
                missingLib.add(lib);
            }

            //check natives lib
            JSONObject arrayObject = (JSONObject) Launcher.libArray.get(i);

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
            if (!new File(nLibs).exists()) {
                missingLib.add(lib);
            }
        }

        System.err.println("Missing " + missingLib.size() + " Libraries");
        for (String s : missingLib) {
            System.err.println(s);
        }

        return missingLib;
    }

    private static void unzipNatives() {
        for (File aNativesList : nativesLibList) {
            try {
                unZipFiles(aNativesList, Launcher.nativesPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void unZipFiles(File zipFile, String descDir)throws IOException{
        File pathFile = new File(descDir);
        if(!pathFile.exists()){
            pathFile.mkdirs();
        }
        ZipFile zip = new ZipFile(zipFile);
        for(Enumeration entries = zip.entries(); entries.hasMoreElements();){
            ZipEntry entry = (ZipEntry)entries.nextElement();
            String zipEntryName = entry.getName();
            InputStream in = zip.getInputStream(entry);
            String outPath = (descDir+zipEntryName).replaceAll("\\*", "/");;
            //判断路径是否存在,不存在则创建文件路径
            File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
            if(!file.exists()){
                file.mkdirs();
            }
            //判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
            if(new File(outPath).isDirectory()){
                continue;
            }

            System.out.println(outPath);

            OutputStream out = null;
            try {
                out = new FileOutputStream(outPath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            byte[] buf1 = new byte[1024];
            int len;
            while((len=in.read(buf1))>0){
                if (out != null) {
                    out.write(buf1,0,len);
                }
            }
            in.close();
            if (out != null) {
                out.close();
            }
        }
    }

    public static void deleteFile(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            File[] ff = file.listFiles();
            for (File aFf : ff) {
                deleteFile(aFf.getPath());
            }
        }
        file.delete();
    }
}
