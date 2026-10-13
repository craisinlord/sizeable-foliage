package com.craisinlord.sizeablefoliage;

import com.craisinlord.sizeablefoliage.platform.PlatformHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class SizeableFoliageConfig {
    public static volatile boolean torchflowerGlow = true;

    private SizeableFoliageConfig() {
    }

    public static void load() {
        Path file = configFile();
        Properties properties = new Properties();
        if (Files.exists(file)) {
            try (InputStream in = Files.newInputStream(file)) {
                properties.load(in);
            } catch (IOException e) {
                Constants.LOGGER.warn("Failed to read {}, using defaults", file, e);
            }
        }
        torchflowerGlow = Boolean.parseBoolean(properties.getProperty("torchflowerGlow", "true"));
        save();
    }

    public static void save() {
        Path file = configFile();
        Properties properties = new Properties();
        properties.setProperty("torchflowerGlow", Boolean.toString(torchflowerGlow));
        try {
            Files.createDirectories(file.getParent());
            try (OutputStream out = Files.newOutputStream(file)) {
                properties.store(out, "Sizeable Foliage configuration");
            }
        } catch (IOException e) {
            Constants.LOGGER.warn("Failed to write {}", file, e);
        }
    }

    private static Path configFile() {
        return PlatformHelper.getInstance().getConfigDir().resolve(Constants.MOD_ID + ".properties");
    }
}
