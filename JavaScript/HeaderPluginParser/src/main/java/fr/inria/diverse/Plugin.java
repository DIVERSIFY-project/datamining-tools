package fr.inria.diverse;

/**
 * Created by aelie on 19/06/14.
 */
public class Plugin {
    String name;
    String version;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = formatVersion(version);
    }

    public Plugin(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public static String formatVersion(String version) {
        String result = "";
        result = (version.startsWith("v") || version.startsWith("V")) ? version.substring(1, version.length()) : version;
        return result.trim();
    }

    public int moreRecentThan(Plugin plugin) {
        if (this.version == null) {
            return 1;
        }
        if (plugin.getVersion() == null) {
            return -1;
        }
        String[] v1Decomp = formatVersion(this.version).split("\\.");
        String[] v2Decomp = formatVersion(plugin.getVersion()).split("\\.");
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
