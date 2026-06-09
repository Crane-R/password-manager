package com.crane.view.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.crane.model.service.ExcelService;
import com.crane.view.config.Config;
import com.crane.view.config.Language;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Date;

/**
 * 定时导出明文数据服务。
 */
@Slf4j
public final class TimedExportService {

    private static final String FEATURE_SWITCH_PATH = "config/configurable.properties";
    private static final String RUNTIME_PATH = "config/timed_export.properties";
    private static final String FEATURE_KEY = "TIMED_EXPORT_ENABLED";
    private static final String EXPORT_PATH_KEY = "timedExportPath";
    private static final String INTERVAL_HOURS_KEY = "timedExportIntervalHours";
    private static final String LAST_TIME_KEY = "timedExportLastTime";
    private static final String LAST_TIME_TEXT_KEY = "timedExportLastTimeText";
    private static final long DEFAULT_INTERVAL_HOURS = 12L;

    private TimedExportService() {
    }

    public static boolean isFeatureEnabled() {
        return Boolean.parseBoolean(new Config(FEATURE_SWITCH_PATH).get(FEATURE_KEY));
    }

    public static String getExportPath() {
        String path = new Config(RUNTIME_PATH).get(EXPORT_PATH_KEY);
        return path == null ? "" : path.trim();
    }

    public static long getIntervalHours() {
        String value = new Config(RUNTIME_PATH).get(INTERVAL_HOURS_KEY);
        if (StrUtil.isBlank(value)) {
            return DEFAULT_INTERVAL_HOURS;
        }
        String normalized = value.trim().toLowerCase();
        if (normalized.endsWith("h")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        try {
            long hours = Long.parseLong(normalized);
            return hours <= 0 ? DEFAULT_INTERVAL_HOURS : hours;
        } catch (NumberFormatException e) {
            return DEFAULT_INTERVAL_HOURS;
        }
    }

    public static String getLastExportTimeText() {
        String value = new Config(RUNTIME_PATH).get(LAST_TIME_TEXT_KEY);
        return value == null ? "" : value.trim();
    }

    public static String getNextExportTimeText() {
        long lastTime = getLastExportTime();
        if (lastTime <= 0L) {
            return Language.get("timedExportNextTimePending");
        }
        long nextTime = lastTime + getIntervalHours() * 60L * 60L * 1000L;
        return DateUtil.formatDateTime(new Date(nextTime));
    }

    public static String getExecutionRuleText() {
        return Language.get("timedExportExecutionRule");
    }

    public static void saveConfig(String exportPath, String intervalValue) {
        String normalizedPath = normalizePath(exportPath);
        ensureExportDirectory(normalizedPath);
        Config runtimeConfig = new Config(RUNTIME_PATH);
        runtimeConfig.set(EXPORT_PATH_KEY, normalizedPath);
        runtimeConfig.set(INTERVAL_HOURS_KEY, String.valueOf(parseIntervalHours(intervalValue)));
    }

    public static boolean tryExportIfDue() {
        if (!isFeatureEnabled()) {
            return false;
        }
        String exportPath = getExportPath();
        if (StrUtil.isBlank(exportPath)) {
            log.info("定时导出已启用，但未配置导出路径");
            MessageService.outputMessage(Language.get("timedExportLogSkipped") + Language.get("timedExportSkipReasonNoPath"));
            return false;
        }
        long intervalMillis = getIntervalHours() * 60L * 60L * 1000L;
        long lastTime = getLastExportTime();
        long now = System.currentTimeMillis();
        if (lastTime > 0 && now - lastTime < intervalMillis) {
            return false;
        }
        ensureExportDirectory(exportPath);
        MessageService.outputMessage(Language.get("timedExportLogTriggered") + exportPath);
        return exportToPath(exportPath, now, true);
    }

    public static boolean testExportNow(String exportPath) {
        String normalizedPath = normalizePath(exportPath);
        if (StrUtil.isBlank(normalizedPath)) {
            return false;
        }
        ensureExportDirectory(normalizedPath);
        return exportToPath(normalizedPath, System.currentTimeMillis(), false);
    }

    private static long getLastExportTime() {
        String value = new Config(RUNTIME_PATH).get(LAST_TIME_KEY);
        if (StrUtil.isBlank(value)) {
            return 0L;
        }
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    private static void recordExportTime(long currentTimeMillis) {
        Config runtimeConfig = new Config(RUNTIME_PATH);
        runtimeConfig.set(LAST_TIME_KEY, String.valueOf(currentTimeMillis));
        runtimeConfig.set(LAST_TIME_TEXT_KEY, DateUtil.formatDateTime(new Date(currentTimeMillis)));
    }

    private static boolean exportToPath(String exportPath, long currentTimeMillis, boolean recordTime) {
        boolean success = ExportService.exportCurrentScenePlaintext(exportPath);
        if (success) {
            if (recordTime) {
                recordExportTime(currentTimeMillis);
            }
            log.info("定时明文导出成功，路径：{}", exportPath);
            MessageService.outputMessage(Language.get("timedExportLogSuccess") + exportPath);
        } else {
            log.warn("定时明文导出失败，路径：{}", exportPath);
            MessageService.outputMessage(Language.get("timedExportLogFail") + exportPath);
        }
        return success;
    }

    private static long parseIntervalHours(String intervalValue) {
        if (StrUtil.isBlank(intervalValue)) {
            return DEFAULT_INTERVAL_HOURS;
        }
        String normalized = intervalValue.trim().toLowerCase();
        if (normalized.endsWith("h")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        try {
            long hours = Long.parseLong(normalized);
            return hours <= 0 ? DEFAULT_INTERVAL_HOURS : hours;
        } catch (NumberFormatException e) {
            return DEFAULT_INTERVAL_HOURS;
        }
    }

    private static void ensureExportDirectory(String exportPath) {
        File file = new File(exportPath);
        File targetDir = ExcelService.checkPathIsContainXlsx(exportPath) ? file.getParentFile() : file;
        if (targetDir != null && !targetDir.exists() && !targetDir.mkdirs()) {
            log.warn("创建定时导出目录失败：{}", targetDir.getAbsolutePath());
        }
    }

    private static String normalizePath(String exportPath) {
        return exportPath == null ? "" : exportPath.trim();
    }
}
