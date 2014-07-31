package fr.inria.diverse;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

/**
 * Created by aelie on 06/06/14.
 */
public class Main {

    static final String mavenSeparator = ":";
    static final String cellSeparator = ",";
    static final String elementSeparator = ",";

    public static void main(String[] args) {
        if ((args.length <= 0)) throw new AssertionError("Provide mining result folder");
        List<String> emptyWebsites = new ArrayList<String>();
        int emptyWebsiteCounter = 0;
        int websiteCounter = 0;
        File resultFolder = new File(args[0]);
        if ((!resultFolder.isDirectory())) throw new AssertionError("Not a folder");
        PluginsData pluginsData = new PluginsData();
        Map<String, Set<String>> pluginsByWebsite = new HashMap<String, Set<String>>();
        Map<String, Set<String>> websitesByPlugin = new HashMap<String, Set<String>>();
        Set<String> pluginsInFile;
        for (File resultFolderContent : resultFolder.listFiles()) {
            if (resultFolderContent.isDirectory()) {
                //System.out.println("Website " + resultFolderContent.getName());
                websiteCounter++;
                boolean jsIsEmpty = false;
                boolean jsHasNoPlugin = false;
                for (File websiteFolderContent : resultFolderContent.listFiles()) {
                    if (websiteFolderContent.getName().equals("js")) {
                        int jsPluginsSize = 0;
                        jsIsEmpty = websiteFolderContent.listFiles().length == 0;
                        for (File jsFile : websiteFolderContent.listFiles()) {
                            pluginsInFile = HeaderParser.parse(jsFile, pluginsData);
                            if (!pluginsByWebsite.containsKey(resultFolderContent.getName())) {
                                pluginsByWebsite.put(resultFolderContent.getName(), pluginsInFile);
                            } else {
                                pluginsByWebsite.get(resultFolderContent.getName()).addAll(pluginsInFile);
                            }
                            for (String plugin : pluginsInFile) {
                                if (!websitesByPlugin.containsKey(plugin)) {
                                    websitesByPlugin.put(plugin, new LinkedHashSet<String>());
                                }
                                websitesByPlugin.get(plugin).add(resultFolderContent.getName());
                            }
                            jsPluginsSize += pluginsInFile.size();
                        }
                        jsHasNoPlugin = jsPluginsSize == 0;
                    }
                }
                if (jsIsEmpty) {
                    System.out.print("_");
                    emptyWebsites.add(resultFolderContent.getName());
                    emptyWebsiteCounter++;
                } else if (jsHasNoPlugin) {
                    System.out.print("-");
                } else {
                    System.out.print("+");
                }
                if (websiteCounter % 100 == 0) {
                    System.out.println();
                    System.out.print(websiteCounter);
                }
            }
        }
        // remove version number
        Map<String, Set<String>> websitesByPluginNoVersion = new HashMap<String, Set<String>>();
        for (String plugin : websitesByPlugin.keySet()) {
            if (!websitesByPluginNoVersion.containsKey(plugin.split(mavenSeparator)[0])) {
                websitesByPluginNoVersion.put(plugin.split(mavenSeparator)[0], new LinkedHashSet<String>());
            }
            websitesByPluginNoVersion.get(plugin.split(mavenSeparator)[0]).addAll(websitesByPlugin.get(plugin));
        }

        System.out.println(System.getProperty("line.separator"));
        System.out.println("Empty: " + emptyWebsiteCounter + "/" + websiteCounter);

        //writing files
        try {
            //plugins by website
            BufferedWriter bw = Files.newBufferedWriter(new File("websites.csv").toPath());
            for (String website : pluginsByWebsite.keySet()) {
                bw.write("\"" + website + "\"" + cellSeparator + pluginsByWebsite.get(website).size() + cellSeparator + "\"");
                for (String plugin : pluginsByWebsite.get(website)) {
                    bw.write(plugin + elementSeparator);
                }
                bw.write("\"" + System.getProperty("line.separator"));
            }
            bw.close();
            //plugins by website, keeping the most recent version only
            bw = Files.newBufferedWriter(new File("websitespessimist.csv").toPath());
            for (String website : pluginsByWebsite.keySet()) {
                Map<String, String> singlePlugins = new HashMap<String, String>();
                for (String plugin : pluginsByWebsite.get(website)) {
                    String pluginName = plugin.split(mavenSeparator)[0];
                    String pluginVersion = plugin.split(mavenSeparator)[1];
                    if (singlePlugins.keySet().contains(pluginName)) {
                        if (HeaderParser.versionLaterThan(singlePlugins.get(pluginName), pluginVersion) > 0) {
                            singlePlugins.put(pluginName, pluginVersion);
                        }
                    } else {
                        singlePlugins.put(pluginName, pluginVersion);
                    }
                }
                bw.write("\"" + website + "\"" + cellSeparator + singlePlugins.keySet().size() + cellSeparator + "\"");
                for (String plugin : singlePlugins.keySet()) {
                    bw.write(plugin + mavenSeparator + singlePlugins.get(plugin) + elementSeparator);
                }
                bw.write("\"" + System.getProperty("line.separator"));
            }
            bw.close();
            //websites by plugin
            bw = Files.newBufferedWriter(new File("plugins.csv").toPath());
            for (String plugin : websitesByPlugin.keySet()) {
                bw.write("\"" + plugin + "\"" + cellSeparator + websitesByPlugin.get(plugin).size() + cellSeparator + "\"");
                for (String website : websitesByPlugin.get(plugin)) {
                    bw.write(website + elementSeparator);
                }
                bw.write("\"" + System.getProperty("line.separator"));
            }
            bw.close();
            //websites by plugin, compacting all versions
            bw = Files.newBufferedWriter(new File("pluginsnoversion.csv").toPath());
            for (String plugin : websitesByPluginNoVersion.keySet()) {
                bw.write("\"" + plugin + "\"" + cellSeparator + websitesByPluginNoVersion.get(plugin).size() + cellSeparator + "\"");
                for (String website : websitesByPluginNoVersion.get(plugin)) {
                    bw.write(website + elementSeparator);
                }
                bw.write("\"" + System.getProperty("line.separator"));
            }
            bw.close();
            //websites without any JS file
            bw = Files.newBufferedWriter(new File("emptywebsites.csv").toPath());
            for (String website : emptyWebsites) {
                bw.write("\"" + website + "\"" + cellSeparator);
                bw.write(System.getProperty("line.separator"));
            }
            bw.close();
            //detecting vulnerable websites: jQuery 1.6.3 and below
            bw = Files.newBufferedWriter(new File("dangerous.csv").toPath());
            for (String plugin : websitesByPlugin.keySet()) {
                if (plugin.split(mavenSeparator)[0].equalsIgnoreCase("jQuery")
                        && HeaderParser.versionLaterThan("1.6.3", plugin.split(mavenSeparator)[1]) >= 0) {
                    bw.write("\"" + plugin + "\"" + cellSeparator + websitesByPlugin.get(plugin).size() + cellSeparator + "\"");
                    for (String website : websitesByPlugin.get(plugin)) {
                        bw.write(website + elementSeparator);
                    }
                    bw.write("\"" + System.getProperty("line.separator"));
                }
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
