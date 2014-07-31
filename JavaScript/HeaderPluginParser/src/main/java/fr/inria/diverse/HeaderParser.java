package fr.inria.diverse;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by aelie on 06/06/14.
 */
public class HeaderParser {
    static String commentRegex = "/\\*(?:.|[\\n\\r])*?\\*/";
    static String versionRegex = "(v|V|v\\s|V\\s|\\s)((\\d{1,4})\\.)+(\\d{1,4})";

    public static Set<String> parse(File inputFile, PluginsData pluginsData) {
        Set<String> result = new LinkedHashSet<String>();
        //putting jQuery at the end
        //Arrays.copyOf(PluginsData.plugins, PluginsData.plugins.length + 1);
        //PluginsData.plugins[PluginsData.plugins.length - 1] = PluginsData.jQuery;
        //Set<String> pluginsSet = new LinkedHashSet<String>(Arrays.asList(PluginsData.plugins));
        //Set<String> jQueryAuthorizedVersionsSet = new LinkedHashSet<String>(Arrays.asList(PluginsData.jQueryAuthorizedVersions));

        String text = "";
        try {
            text = readFile(inputFile.getPath());
        } catch (IOException e) {
            System.err.print("1");
        }
        Pattern commentPattern = Pattern.compile(commentRegex);
        Matcher commentMatcher;
        Pattern versionPattern = Pattern.compile(versionRegex);
        /*try {
            // file name check
            for (String plugin : minMaxVersionsByPlugin.keySet()) {
                if (StringUtils.containsIgnoreCase(inputFile.getName(), plugin)) {
                    commentMatcher = commentPattern.matcher(text);
                    if (commentMatcher.find()) {
                        Matcher versionMatcher = versionPattern.matcher(commentMatcher.group());
                        if (versionMatcher.find()) {
                            String version = versionMatcher.group();
                            if (plugin.equalsIgnoreCase(jQuery.split(":")[0])) {
                                if (jQueryAuthorizedVersionsSet.contains(formatVersion(version))) {
                                    System.out.println("BINGO NAME " + inputFile.getName() + "=>" + plugin + "/" + formatVersion(version));
                                    result.add(plugin + ":" + formatVersion(version));
                                    break;
                                }
                            } else if (versionLaterThan(minMaxVersionsByPlugin.get(plugin)[VERSIONMIN], version) <= 0
                                    && versionLaterThan(version, minMaxVersionsByPlugin.get(plugin)[VERSIONMAX]) <= 0) {
                                System.out.println("BINGO NAME " + inputFile.getName() + "=>" + plugin + "/" + formatVersion(version));
                                result.add(plugin + ":" + formatVersion(version));
                                break;
                            } //else System.out.println("VERSION");
                        }
                    }
                }
            }
        } catch (StackOverflowError soe) {
            //System.out.println(inputFile);
            System.err.print("2");
        }*/
        try {
            // file content check
            commentMatcher = commentPattern.matcher(text);
            while (commentMatcher.find()) {
                String group = commentMatcher.group();
                Matcher versionMatcher = versionPattern.matcher(group);
                if (versionMatcher.find()) {
                    for (String plugin : pluginsData.minMaxVersionsByPlugin.keySet()) {
                        if (StringUtils.containsIgnoreCase(group, plugin)) {
                            String version = versionMatcher.group();
                            if (plugin.equalsIgnoreCase(PluginsData.jQuery.split(":")[0])) {
                                if (pluginsData.jQueryAuthorizedVersionsSet.contains(formatVersion(version))) {
                                    //System.out.println("BINGO PARSE " + inputFile.getName() + "=>" + plugin + "/" + formatVersion(version));
                                    result.add(plugin + ":" + formatVersion(version));
                                    break;
                                }
                            } else if (versionLaterThan(pluginsData.minMaxVersionsByPlugin.get(plugin)[pluginsData.VERSIONMIN], version) <= 0
                                    && versionLaterThan(version, pluginsData.minMaxVersionsByPlugin.get(plugin)[pluginsData.VERSIONMAX]) <= 0) {
                                //System.out.println("BINGO PARSE " + inputFile.getName() + "=>" + plugin + "/" + formatVersion(version));
                                result.add(plugin + ":" + formatVersion(version));
                                break;
                            } //else System.out.println("VERSION");
                        }
                    }
                }
            }
        } catch (StackOverflowError soe) {
            //System.out.println(inputFile);
            System.err.print("3");
        }

        return result;
    }

    static String readFile(String path) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded);
    }

    static String formatVersion(String version) {
        String result = "";
        result = (version.startsWith("v") || version.startsWith("V")) ? version.substring(1, version.length()) : version;
        return result.trim();
    }

    public static int versionLaterThan(String v1, String v2) {
        if (v1 == null) {
            return 1;
        }
        if (v2 == null) {
            return -1;
        }
        String[] v1Decomp = formatVersion(v1).split("\\.");
        String[] v2Decomp = formatVersion(v2).split("\\.");
        int shortestLength = Math.min(v1Decomp.length, v2Decomp.length);
        for (int i = 0; i < shortestLength; i++) {
            if (Integer.parseInt(v1Decomp[i]) < Integer.parseInt(v2Decomp[i])) {
                return -1;
            }
            if (Integer.parseInt(v1Decomp[i]) > Integer.parseInt(v2Decomp[i])) {
                return 1;
            }
        }
        if (v1Decomp.length < v2Decomp.length) {
            return -1;
        }
        if (v1Decomp.length > v2Decomp.length) {
            return 1;
        }
        return 0;
    }

}
