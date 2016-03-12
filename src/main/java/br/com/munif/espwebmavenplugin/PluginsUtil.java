/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.munif.espwebmavenplugin;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.project.MavenProject;

/**
 *
 * @author munif
 */
public class PluginsUtil {

    public static void assina() {
        System.out.println(""
                + " _____                _    _      _     \n"
                + "|  ___|              | |  | |    | |    \n"
                + "| |__ ___ _ __   ___ | |  | | ___| |__  \n"
                + "|  __/ __| '_ \\ / _ \\| |/\\| |/ _ \\ '_ \\ \n"
                + "| |__\\__ \\ |_) |  __/\\  /\\  /  __/ |_) |\n"
                + "\\____/___/ .__/ \\___| \\/  \\/ \\___|_.__/ \n"
                + "         | |                            \n"
                + "         |_|                            "
                + "");
    }

    public static String windowsSafe(String s) {
        return s.replaceAll("\\\\", "/");
    }

    public static ClassLoader getClassLoader(MavenProject project) {
        ClassLoader aRetornar = null;
        try {
            List elementos = new ArrayList();
            elementos.addAll(project.getRuntimeClasspathElements());
            elementos.addAll(project.getTestClasspathElements());

            URL[] runtimeUrls = new URL[elementos.size()];
            for (int i = 0; i < elementos.size(); i++) {
                String element = (String) elementos.get(i);
                runtimeUrls[i] = new File(element).toURI().toURL();
            }
            aRetornar = new URLClassLoader(runtimeUrls,
                    Thread.currentThread().getContextClassLoader());

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return aRetornar;
    }

}
