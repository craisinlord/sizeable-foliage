package com.craisinlord.sizeablefoliage.platform;

import java.nio.file.Path;

public interface PlatformHelper {

    static PlatformHelper getInstance() {
        return PlatformHelperImpl.INSTANCE;
    }

    String getPlatformName();

    boolean isModLoaded(String modId);

    boolean isDevelopmentEnvironment();

    boolean isClient();

    boolean isServer();

    String getModVersion();

    Path getConfigDir();
}
