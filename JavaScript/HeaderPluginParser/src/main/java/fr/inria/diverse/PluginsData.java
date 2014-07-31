package fr.inria.diverse;

import org.apache.commons.lang3.ArrayUtils;

import java.util.*;

/**
 * Created by aelie on 19/06/14.
 */
public class PluginsData {

    static final int VERSIONMIN = 0;
    static final int VERSIONMAX = 1;

    Set<String> pluginsSet;
    public Set<String> jQueryAuthorizedVersionsSet;
    public Map<String, String[]> minMaxVersionsByPlugin;

    public PluginsData() {
        String[] plugins = ArrayUtils.addAll(plugins1, plugins2);
        Arrays.copyOf(plugins, plugins.length + 1);
        plugins[plugins.length - 1] = jQuery;
        pluginsSet = new LinkedHashSet<String>(Arrays.asList(plugins));
        jQueryAuthorizedVersionsSet = new LinkedHashSet<String>(Arrays.asList(jQueryAuthorizedVersions));
        minMaxVersionsByPlugin = new LinkedHashMap<String, String[]>();
        for (String plugin : pluginsSet) {
            String[] versions = new String[2];
            if (plugin.split(":").length == 3) {
                versions[VERSIONMIN] = plugin.split(":")[1];
                versions[VERSIONMAX] = plugin.split(":")[2];
            } else if (plugin.split(":").length == 2) {
                versions[VERSIONMIN] = plugin.split(":")[1];
                versions[VERSIONMAX] = null;
            } else {
                versions[VERSIONMIN] = "0.0";
                versions[VERSIONMAX] = null;
            }
            minMaxVersionsByPlugin.put(plugin.split(":")[0], versions);
        }
        System.out.println("Searching for " + minMaxVersionsByPlugin.keySet().size() + " plugins");
    }

    public static String jQuery = "jQuery:1.0:2.1";

    public static String[] jQueryAuthorizedVersions = {
            "1.0", "1.0.1", "1.0.2", "1.0.3", "1.0.4", "1.1a", "1.1b", "1.1", "1.1.1", "1.1.2", "1.1.3a", "1.1.3",
            "1.1.3.1", "1.2", "1.2.1", "1.2.2", "1.2.3", "1.2.4", "1.2.5", "1.2.6", "1.3", "1.3.1", "1.3.2", "1.4",
            "1.4.1", "1.4.2", "1.4.3", "1.4.4", "1.5", "1.5.1", "1.5.2", "1.6", "1.6.1", "1.6.2", "1.6.3", "1.6.4",
            "1.7", "1.7.1", "1.7.2", "1.8", "1.8.1", "1.8.2", "1.8.3", "1.9", "1.9.1", "1.10", "1.10.0", "1.10.1",
            "1.10.2", "1.11", "1.11.0", "1.11.1", "2.0", "2.0.1", "2.0.2", "2.0.3", "2.1.0", "2.1.1"
    };

    public static String[] plugins1 = {
            "HTML5 Shiv:0.0:3.8",
            "AccDC:0.0:3.2",
            "Dojo Toolkit:0.0:2.0",
            "Glow:0.0:1.8",
            "jQuery Cookie:0.0:1.5",
            "jqPlot",
            "Infinite Scroll",
            "jQuery Migrate:0.0:1.3",
            "jQuery Form",
            "jQuery.nicescroll",
            "jQuery outside events",
            "jQuery Once",
            "jQuery imagesLoaded",
            "jPlayer",
            "jQuery grab",
            "Poshy Tip",
            "jquery.transform.js",
            "X-editable",
            "Lazy Load",
            "jQuery dotdotdot",
            "Ad Refresher",
            "sharrre",
            "sidr",
            "Lazy Javascript Loader",
            "validate.js",
            "respond.js",
            "Parsleyjs:0.0:2.1",
            "dropzone.js",
            "jquery.jkit:0.0:1.3",
            "toolbar.js:0.0:1.1",
            "midori:0.0:2011.0",
            "MooTools:0.0:1.5",
            "Prototype:0.0:1.8",
            "YUI Library:0.0:3.2",
            "Ample SDK:0.0:1.0",
            "DHTMLX:0.0:3.7",
            "Ext JS:0.0:5.1",
            "iX Framework:0.0:1.2",
            "jQuery UI:0.0:1.11",
            "Lively Kernel:0.0:2.2",
            "qooxdoo:0.0:4.1",
            "Script.aculo.us:0.0:2.0",
            "SmartClient:0.0:9.2",
            "D3.js:0.0:3.5",
            "InfoVis:0.0:2.1",
            "Kinetic.js:0.0:5.2",
            "Processing.js:0.0:1.5",
            "Raphaël:0.0:2.2",
            "SWFObject:0.0:2.3",
            "Three.js",
            "Babylon.js:0.0:1.13",
            "EaselJS:0.0:0.8",
            "AngularJS:0.0:1.3",
            "Atoms.js:0.0:1.1",
            "Backbone.js:0.0:1.2",
            "Cappuccino:0.0:1.0",
            "Chaplin.js:0.0:1.1",
            "Echo:0.0:3.1",
            "Ember.js:0.0:1.6",
            "Enyo:0.0:2.5",
            "Google Web Toolkit:0.0:2.7",
            "JavaScriptMVC:0.0:3.4",
            "Knockout:0.0:3.2",
            "Rialto",
            "SproutCore:0.0:2.0",
            "Wakanda:0.0:8.159169",
            "FuncJS",
            "Google Closure",
            "Joose:0.0:2.2",
            "jsPHP",
            "AjaxTransport",
            //"Ajax",
            "MochiKit:0.0:1.5",
            "PDF.js",
            "Rico:0.0:3.1",
            "Socket.IO:0.0:1.1",
            "Spry:0.0:1.7",
            "Underscore.js:0.0:1.7",
            "Node.js:0.0:0.12",
            "Cascade:0.0:1.6",
            "Handlebars:0.0:1.4",
            "jQuery Mobile:0.0:1.5",
            "Mustache:0.0:0.9",
            "Twitter Bootstrap:0.0:3.2",
            "zurb:0.0:5.2",
            "Jasmine:0.0:2.1",
            "Unit.js",
            "QUnit:0.0:1.15",
            "Cucumber",
            "Mocha",
            "Modernizr:0.0:2.8"
    };

    public static String[] plugins2 = {
            "CreateJS",
            "CupQ",
            "Echo3",
            "PhoneJS",
            "Pyjamas",
            "SmartGWT",
            //"ZK",
            "Webix",
            "JSTweener",
            "Facebook Animation",
            "SoundManager",
            "Flowplayer",
            //"Cookies",
            "EasyCookie",
            "MD5",
            "Taffy DB",
            "ActiveRecord",
            //"Date",
            "Firebug Lite",
            "Blackbird",
            "NitobiBug",
            "strokeText",
            "typeface",
            "Cufón",
            "Hyphenator",
            "LiveValidation",
            "wForms",
            "Validanguage",
            "AS3Wrapper",
            "Aflax",
            "GameJS",
            "PlotKit",
            "JS Charts",
            "Flot",
            "SortTable",
            "DragTable",
            "KeyTable",
            "Pixastic",
            "VectorGraphics",
            "Reflection",
            "CVI",
            "ExplorerCanvas",
            "Canvas 3D JS Library",
            "C3DL",
            "jsDraw2D",
            "Shortcuts",
            "QFocuser",
            "Mapstraction",
            "Sylvester",
            "XRegExp",
            "textMonster",
            //"URL",
            "UED",
            "URL Encoded Data",
            "MoreCSS",
            //"IE7",
            "Sizzle",
            "DD_Roundies",
            "DD_BelatedPNG",
            "SocialHistory",
            "SyntaxHighlighter",
            //"FX",
            "Bytefx",
            "Animator",
            "jsAnim",
            "sIFR",
            "Facelift",
            "FontJazz",
            "Yav",
            "qForms",
            "jqplot"
    };
}
