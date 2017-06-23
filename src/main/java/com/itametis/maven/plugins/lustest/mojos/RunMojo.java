/*
 * COPYRIGHT © ITAMETIS - TOUS DROITS RÉSERVÉS
 * Pour plus d'information veuillez contacter : copyright@itametis.com
 */
package com.itametis.maven.plugins.lustest.mojos;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;


/**
 * Start the watching service.
 *
 * @goal run
 * @phase generate-sources
 * @aggregator true
 *
 * @author © ITAMETIS
 */
public class RunMojo extends AbstractMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
//        FileSystemWatcher watcher = new FileSystemWatcher(super.getLog());
//        watcher.watch("./src");
//        watcher.startWatching();
    }
}
