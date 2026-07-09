package com.craisinlord.sizeablefoliage;

import com.craisinlord.sizeablefoliage.platform.PlatformHelper;

public final class SizeableFoliage {

    public static void init() {
        Constants.LOGGER.info("{} {} initializing on {}", Constants.MOD_NAME,
            PlatformHelper.getInstance().getModVersion(),
            PlatformHelper.getInstance().getPlatformName());
    }

    private SizeableFoliage() {
    }
}
