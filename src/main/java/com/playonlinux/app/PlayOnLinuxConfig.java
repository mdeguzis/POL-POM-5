/*
 * Copyright (C) 2015 PÂRIS Quentin
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.playonlinux.app;

import com.playonlinux.services.BackgroundServiceManager;
import com.playonlinux.services.EventHandler;
import com.playonlinux.ui.Controller;
import com.playonlinux.installer.InstallerSource;
import com.playonlinux.services.EventHandlerPlayOnLinuxImplementation;
import com.playonlinux.services.PlayOnLinuxBackgroundServicesManager;
import com.playonlinux.domain.PlayOnLinuxException;
import com.playonlinux.domain.lang.LanguageBundle;
import com.playonlinux.domain.lang.LanguageBundleSelector;
import com.playonlinux.injection.AbstractConfigFile;
import com.playonlinux.injection.Bean;
import com.playonlinux.ui.impl.cli.ControllerCLIImplementation;
import com.playonlinux.ui.impl.javafx.ControllerJavaFXImplementation;
import com.playonlinux.installer.InstallerSourceWebserviceImplementation;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

@SuppressWarnings("unused")
public class PlayOnLinuxConfig extends AbstractConfigFile  {

    private boolean useCliInterface = false;
    private PlayOnLinuxContext playOnLinuxContext = new PlayOnLinuxContext();

    @Bean
    public Controller controller() {
        if(useCliInterface) {
            return new ControllerCLIImplementation();
        } else {
            return new ControllerJavaFXImplementation();
        }
    }

    @Bean
    public InstallerSource installerSource() throws MalformedURLException {
        return new InstallerSourceWebserviceImplementation(new URL(playOnLinuxContext.getProperty("webservice.url")));
    }

    @Bean
    public EventHandler eventHandler() {
            return new EventHandlerPlayOnLinuxImplementation();
    }

    @Bean
    public PlayOnLinuxContext playOnLinuxContext() throws PlayOnLinuxException, IOException {
        return playOnLinuxContext;
    }

    @Bean
    public BackgroundServiceManager playOnLinuxBackgroundServicesManager() {
        return new PlayOnLinuxBackgroundServicesManager();
    }

    @Bean
    public LanguageBundle languageBundle() {
        return LanguageBundleSelector.forLocale(Locale.getDefault());
    }

    @Override
    protected String definePackage() {
        return "com.playonlinux";
    }

    public void setUseCLIInterface(boolean enabled) {
        this.useCliInterface = enabled;
    }
}
