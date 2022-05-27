package utils;

import burp.BurpExtender;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;


/**
 * @author linchen
 */
public class Config {
    /**
     * 配置文件
     */
    public static File sylasConfig = new File("sylas_setting.json");
    public static HashMap<String, String> initDatabaseSetting = new HashMap<String, String>(6){{
        put("db_server","");
        put("host","");
        put("port","");
        put("username","");
        put("password","");
        put("database","");
    }};
    public static String getDomainIp(String domain) {
        try {
            return InetAddress.getByName(domain).getHostAddress();
        }catch (UnknownHostException ne){
            return "Unknown";
        }
    }

    public static Boolean isBuild(){
        return sylasConfig.exists()&& sylasConfig.isFile();
    }

    public static HashMap<String,String> parseJson() {
        try {
            BurpExtender.getStdout().println(Config.sylasConfig.getAbsolutePath());
            BufferedReader settingReader = new BufferedReader(new FileReader(Config.sylasConfig));
            HashMap<String,String> tmp = new Gson().fromJson(settingReader, new TypeToken<HashMap<String, String>>() {}.getType());
            if(tmp == null){
                return initDatabaseSetting;
            }
            return tmp;
        } catch (FileNotFoundException e) {
            BurpExtender.getStderr().println(e);
            return initDatabaseSetting;
        }
    }

    public static void writeJson(HashMap<String,String> setting){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(Config.sylasConfig));
            writer.write(gson.toJson(setting));
            writer.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
