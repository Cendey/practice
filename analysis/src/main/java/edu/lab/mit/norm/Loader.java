package edu.lab.mit.norm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * <p>Project: KEWILL FORWARD ENTERPRISE</p>
 * <p>File: edu.lab.mit.norm.Loader</p>
 * <p>Copyright: Copyright @ 2015 Kewill Co., Ltd. All Rights Reserved.</p>
 * <p>Company: Kewill Co., Ltd</p>
 *
 * @author <chao.deng@kewill.com>
 * @version 1.0
 * @since 8/5/2015
 */
public class Loader {

    public final static String IGNORE_ERROR_ID_CONFIGURE = "config/ignores.xml";
    public final static String FILTER_CRITERION_CONFIGURE = "config/filter.xml";
    private final static Properties ignores = new Properties();
    private final static Properties filters = new Properties();
    private final static URL url = Thread.currentThread().getContextClassLoader().getResource("");

    public static Properties getIgnores() {
        return ignores;
    }

    public static Properties getFilters() {
        return filters;
    }

    public static void init(Properties config, String filePath) {
        String currentPath = null;
        if (url != null) {
            currentPath = getCurrentPath();
        }
        File configDir =
            new File(currentPath + filePath.substring(0, filePath.lastIndexOf("/")));

        if (!configDir.exists()) {
            if (!configDir.mkdirs()) {
                System.err.println("Cannot create ignore error directory!");
            }
        } else {
            if (new File(currentPath + filePath).exists()) {
                load(config, filePath);
            }
        }
    }

    private static String getCurrentPath() {
        return url != null ? url.getPath() : "";
    }

    public static void load(Properties config, String filePath) {
        try {
            InputStream inputStream =
                Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
            if (inputStream != null && inputStream.available() != 0) {
                config.loadFromXML(inputStream);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void writeInfo(Map<String, Object> backupInfo, String filePath) {
        File configFile = new File(getCurrentPath() + filePath);
        Boolean isIgnoreInfo = IGNORE_ERROR_ID_CONFIGURE.equals(filePath);

        if (backupInfo != null && backupInfo.size() > 0) {
            if (!isIgnoreInfo) {
                backupInfo.entrySet().stream()
                    .filter(item -> String.valueOf(item.getValue()).contains("\\\\"))
                    .forEach(item -> item.setValue(String.valueOf(item.getValue()).replaceAll("\\\\", "\\\\\\\\")));
            }
            writeToXML(configFile, backupInfo, isIgnoreInfo);
        }
    }

    public static void pushFilter(Criterion criterion) {
        if (filters.size() > 0) {
            criterion.setErrorStartID((String) filters.get("error.start.id"));
            criterion.setErrorEndID((String) filters.get("error.end.id"));
            criterion.setUserID((String) filters.get("user.id"));
            criterion.setSourceFilePath((String) filters.get("source.file.path"));
            criterion.setTargetFilePath((String) filters.get("target.file.path"));
        }
    }

    private static void writeToXML(File configFile, Map<String, Object> backupInfo, Boolean isIgnoreInfo) {
        if (backupInfo != null && backupInfo.size() > 0) {
            List<String> keys = new ArrayList<>();
            backupInfo.keySet().forEach(keys::add);
            keys.sort(String::compareTo);
            try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(configFile, false), "UTF-8")) {
                StringBuilder content = new StringBuilder();
                content.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n")
                    .append("<!DOCTYPE properties SYSTEM \"http://java.sun.com/dtd/properties.dtd\">\n")
                    .append("<properties>\n")
                    .append("    <comment>").append(isIgnoreInfo ? "Ignore List!" : "Filter Conditions!")
                    .append("</comment>\n");
                keys.forEach(key -> content.append("    <entry key=\"").append(key).append("\">")
                    .append(String.valueOf(backupInfo.get(key)).replaceAll("<", "&lt;").replaceAll(">", "&gt;"))
                    .append("</entry>\n"));
                content.append("</properties>");
                writer.write(content.toString());
                writer.flush();
            } catch (IOException e) {
                System.err.println(e.getCause().getMessage());
            }
        }
    }
}
