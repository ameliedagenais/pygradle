/*
 * Copyright 2016 LinkedIn Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.linkedin.gradle.python.util;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of PackageSettings.
 *
 * Returns empty collections mostly.
 * Handles snapshots and project package rebuilds automatically.
 */
public class DefaultPackageSettings implements PackageSettings<PackageInfo> {
    private final String projectName;

    public DefaultPackageSettings(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public Map<String, String> getEnvironment(PackageInfo packageInfo) {
        return Collections.emptyMap();
    }

    @Override
    public List<String> getGlobalOptions(PackageInfo packageInfo) {
        return Collections.emptyList();
    }

    @Override
    public List<String> getInstallOptions(PackageInfo packageInfo) {
        String name = packageInfo.getName();
        String version = packageInfo.getVersion();

        // always reinstall snapshots, but current project is installed editable anyway, no need for other options
        if (!projectName.equals(name)
                && ((version != null && version.contains("-")) || requiresSourceBuild(packageInfo))) {
            return Collections.singletonList("--ignore-installed");
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> getBuildOptions(PackageInfo packageInfo) {
        return Collections.emptyList();
    }

    @Override
    public List<String> getConfigureOptions(PackageInfo packageInfo) {
        return Collections.emptyList();
    }

    @Override
    public List<String> getSupportedLanguageVersions(PackageInfo packageInfo) {
        return Collections.emptyList();
    }

    @Override
    public boolean requiresSourceBuild(PackageInfo packageInfo) {
        String name = packageInfo.getName();
        String version = packageInfo.getVersion();

        // always rebuild the project package itself
        if (projectName.equals(name)) {
            return true;
        }
        // always rebuild snapshots; otherwise no rebuild required, per semver versions with '-' are pre-release
        return (version != null && version.contains("-"));
    }
}
