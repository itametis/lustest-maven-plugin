/*
 * COPYRIGHT © ITAMETIS - TOUS DROITS RÉSERVÉS
 * Pour plus d'information veuillez contacter : copyright@itametis.com
 */
package com.itametis.maven.plugins.lustest;

import com.itametis.maven.plugins.lustest.watcher.FileSystemWatcher;
import java.io.IOException;
import org.apache.maven.plugin.logging.Log;


/**
 *
 * @author ITAMETIS ©
 */
public class Main {

    public static void main(String[] args) throws IOException {
        FileSystemWatcher watcher = new FileSystemWatcher(new Log() {
            @Override
            public void debug(CharSequence cs) {
                System.out.println(cs);
            }


            @Override
            public void debug(CharSequence cs, Throwable thrwbl) {
                System.out.println(cs + thrwbl.getMessage());
            }


            @Override
            public void debug(Throwable thrwbl) {
                System.out.println(thrwbl.getMessage());
            }


            @Override
            public void error(CharSequence cs) {
                System.out.println(cs);
            }


            @Override
            public void error(CharSequence cs, Throwable thrwbl) {
                System.out.println(cs + thrwbl.getMessage());
            }


            @Override
            public void error(Throwable thrwbl) {
                System.out.println(thrwbl.getMessage());
            }


            @Override
            public void info(CharSequence cs) {
                System.out.println(cs);
            }


            @Override
            public void info(CharSequence cs, Throwable thrwbl) {
                System.out.println(cs + thrwbl.getMessage());
            }


            @Override
            public void info(Throwable thrwbl) {
                System.out.println(thrwbl.getMessage());
            }


            @Override
            public boolean isDebugEnabled() {
                return true;
            }


            @Override
            public boolean isErrorEnabled() {
                return true;
            }


            @Override
            public boolean isInfoEnabled() {
                return true;
            }


            @Override
            public boolean isWarnEnabled() {
                return true;
            }


            @Override
            public void warn(CharSequence cs) {
                System.out.println(cs);
            }


            @Override
            public void warn(CharSequence cs, Throwable thrwbl) {
                System.out.println(cs + thrwbl.getMessage());
            }


            @Override
            public void warn(Throwable thrwbl) {
                System.out.println(thrwbl.getMessage());
            }
        });
        watcher.watch("./src");
        watcher.startWatching();
    }
}
