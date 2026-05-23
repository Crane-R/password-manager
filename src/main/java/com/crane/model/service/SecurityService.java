package com.crane.model.service;

import cn.hutool.core.codec.Rot;
import cn.hutool.core.lang.generator.SnowflakeGenerator;
import cn.hutool.core.util.StrUtil;
import com.crane.constant.Constant;
import com.crane.model.bean.Account;
import com.crane.model.dao.LightDao;
import com.crane.view.config.Config;
import com.crane.view.tools.CloseTool;
import com.crane.view.tools.ShowMessage;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

/**
 * Security-related helpers.
 */
@Slf4j
public final class SecurityService {

    private static final String KEY_FILE_SEPARATOR = "#PM#";
    private static final int UUID_LENGTH = 19;

    private SecurityService() {
    }

    public static boolean checkKeyAmountIsNotZero() {
        File file = new File(Constant.DIRECTORY_KEYS);
        return Objects.requireNonNull(file.listFiles()).length != 0;
    }

    public static boolean checkKeyFileIsExist(String inputKey) {
        File file = getTargetKeyFile(inputKey);
        boolean exists = file.exists() && Objects.isNull(file.listFiles());
        if (exists) {
            Constant.CURRENT_KEY = inputKey;
            Constant.CURRENT_SCENE_LABEL = getSceneLabel(inputKey);
            LightDao.updatePath();
        }
        return exists;
    }

    public static void createKey(String keyPre) {
        createKey(keyPre, null);
    }

    public static void createKey(String keyPre, String sceneLabel) {
        String checkoutKey = keyPre.replaceAll("\"", "'");
        String finalKey = buildKeyFileContent(checkoutKey, sceneLabel, new SnowflakeGenerator().next().toString());
        writeKeyFile(getTargetKeyFile(keyPre), finalKey);
        Constant.CURRENT_KEY = keyPre;
        Constant.CURRENT_SCENE_LABEL = normalizeSceneLabel(sceneLabel);
        LightDao.updatePath();
    }

    public static void updateSceneLabel(String inputKey, String sceneLabel) {
        String fullKey = getKey(inputKey);
        String realKey = getRealKey(fullKey);
        String uuidKey = getUuidKey(fullKey);
        writeKeyFile(getTargetKeyFile(inputKey), buildKeyFileContent(realKey, sceneLabel, uuidKey));
        if (StrUtil.equals(Constant.CURRENT_KEY, inputKey)) {
            Constant.CURRENT_SCENE_LABEL = normalizeSceneLabel(sceneLabel);
        }
    }

    public static String getKey(String targetKey) {
        if (StrUtil.isBlank(targetKey)) {
            ShowMessage.showErrorMessage("缺失密钥无法解密", "密钥为空");
            return "";
        }
        BufferedReader bufferedReader = null;
        String result = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(getTargetKeyFile(targetKey)));
            result = bufferedReader.readLine();
        } catch (IOException e) {
            log.error("read key file failed", e);
        } finally {
            CloseTool.close(bufferedReader);
        }
        return result == null ? "" : result;
    }

    public static String getKeyByKeyFile(String keyFile) {
        BufferedReader bufferedReader = null;
        String result = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(keyFile));
            result = bufferedReader.readLine();
        } catch (IOException e) {
            ShowMessage.showErrorMessage(e.getStackTrace(), e.getMessage());
        } finally {
            CloseTool.close(bufferedReader);
        }
        return result == null ? "" : result;
    }

    public static String getUuidKey() {
        return getUuidKey(getKey(Constant.CURRENT_KEY));
    }

    public static String getUuidKey(String fullKey) {
        if (StrUtil.isBlank(fullKey)) {
            return "密钥长度不对";
        }
        if (isNewKeyFormat(fullKey)) {
            String[] parts = fullKey.split(KEY_FILE_SEPARATOR, -1);
            return parts.length >= 3 ? parts[2] : "密钥长度不对";
        }
        if (fullKey.length() < Constant.MINIMUM_KEY_LENGTH) {
            return "密钥长度不对";
        }
        return new StringBuilder(new StringBuilder(fullKey).reverse().substring(0, UUID_LENGTH)).reverse().toString();
    }

    public static String getRealKey() {
        return getRealKey(getKey(Constant.CURRENT_KEY));
    }

    public static String getRealKey(String fullKey) {
        if (StrUtil.isBlank(fullKey)) {
            return "密钥长度错误";
        }
        if (isNewKeyFormat(fullKey)) {
            String[] parts = fullKey.split(KEY_FILE_SEPARATOR, -1);
            if (parts.length < 3) {
                return "密钥长度错误";
            }
            return RealKeyEncodeService.realKeyDecode(parts[0]);
        }
        if (fullKey.length() < Constant.MINIMUM_KEY_LENGTH) {
            return "密钥长度错误";
        }
        return RealKeyEncodeService.realKeyDecode(
                new StringBuilder(new StringBuilder(fullKey).reverse().substring(UUID_LENGTH, fullKey.length())).reverse().toString()
        );
    }

    public static String getCurrentSceneLabel() {
        if (StrUtil.isNotBlank(Constant.CURRENT_SCENE_LABEL)) {
            return Constant.CURRENT_SCENE_LABEL;
        }
        if (StrUtil.isBlank(Constant.CURRENT_KEY)) {
            return "";
        }
        String sceneLabel = getSceneLabel(Constant.CURRENT_KEY);
        Constant.CURRENT_SCENE_LABEL = sceneLabel;
        return sceneLabel;
    }

    public static String getSceneLabel(String targetKey) {
        return getSceneLabelByFullKey(getKey(targetKey));
    }

    public static String getSceneLabelByFullKey(String fullKey) {
        if (!isNewKeyFormat(fullKey)) {
            return "";
        }
        String[] parts = fullKey.split(KEY_FILE_SEPARATOR, -1);
        if (parts.length < 3 || StrUtil.isBlank(parts[1])) {
            return "";
        }
        try {
            return new String(Base64.getDecoder().decode(parts[1]), StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            log.warn("invalid scene label encoding");
            return "";
        }
    }

    public static boolean hasSceneLabel(String targetKey) {
        return StrUtil.isNotBlank(getSceneLabel(targetKey));
    }

    private static String buildKeyFileContent(String realKey, String sceneLabel, String uuid) {
        String encodedRealKey = RealKeyEncodeService.realKeyEncode(realKey.replaceAll("\"", "'"));
        String normalizedSceneLabel = normalizeSceneLabel(sceneLabel);
        if (StrUtil.isBlank(normalizedSceneLabel)) {
            return encodedRealKey + uuid;
        }
        String encodedSceneLabel = Base64.getEncoder().encodeToString(normalizedSceneLabel.getBytes(StandardCharsets.UTF_8));
        return encodedRealKey + KEY_FILE_SEPARATOR + encodedSceneLabel + KEY_FILE_SEPARATOR + uuid;
    }

    private static String normalizeSceneLabel(String sceneLabel) {
        if (sceneLabel == null) {
            return null;
        }
        String normalized = sceneLabel.trim().replace("\r", " ").replace("\n", " ");
        return StrUtil.isBlank(normalized) ? null : normalized;
    }

    private static File getTargetKeyFile(String inputKey) {
        return new File(Constant.DIRECTORY_KEYS + new Md5Service().convertMd5(inputKey));
    }

    private static boolean isNewKeyFormat(String fullKey) {
        return StrUtil.isNotBlank(fullKey) && fullKey.contains(KEY_FILE_SEPARATOR);
    }

    private static void writeKeyFile(File targetKeyFile, String content) {
        String clearReadOnlyCommand = "attrib \"" + targetKeyFile.getAbsolutePath() + "\" -R";
        String clearHiddenCommand = "attrib \"" + targetKeyFile.getAbsolutePath() + "\" -H";
        String setReadOnlyCommand = "attrib \"" + targetKeyFile.getAbsolutePath() + "\" +R";
        String setHiddenCommand = "attrib \"" + targetKeyFile.getAbsolutePath() + "\" +H";
        FileOutputStream fileOutputStream = null;
        try {
            if (targetKeyFile.exists()) {
                Runtime.getRuntime().exec(clearReadOnlyCommand);
                Runtime.getRuntime().exec(clearHiddenCommand);
            }
            fileOutputStream = new FileOutputStream(targetKeyFile, false);
            fileOutputStream.write(content.getBytes(StandardCharsets.UTF_8));
            fileOutputStream.flush();
            Runtime.getRuntime().exec(setReadOnlyCommand);
            Runtime.getRuntime().exec(setHiddenCommand);
        } catch (IOException e) {
            log.error("write key file failed", e);
        } finally {
            CloseTool.close(fileOutputStream);
        }
    }

    private static class RealKeyEncodeService {

        private RealKeyEncodeService() {
        }

        public static String realKeyEncode(String originRealKey) {
            StringBuilder result = new StringBuilder();
            int len = originRealKey.length();
            StringBuilder mark = new StringBuilder();
            for (int i = 0; i < len; i++) {
                int c = originRealKey.charAt(i);
                result.append(c);
                int temp = 0;
                while (c != 0) {
                    c /= 10;
                    temp++;
                }
                mark.append(temp);
            }
            return result.append(".").append(mark).toString();
        }

        public static String realKeyDecode(String encodeRealKey) {
            String[] split = encodeRealKey.split("\\.");
            StringBuilder result = new StringBuilder();
            int len = split[1].length();
            int left = 0;
            int right = 0;
            for (int i = 0; i < len; i++) {
                int step = Integer.parseInt(String.valueOf(split[1].charAt(i)));
                right += step;
                result.append((char) Integer.parseInt(split[0].substring(left, right)));
                left = right;
            }
            return result.toString();
        }
    }

    public static String decodeBase64Salt(String password) {
        return decodeBase64Salt(password, getRealKey());
    }

    public static String decodeBase64Salt(String password, String realKey) {
        if (StrUtil.isEmpty(password)) {
            return password;
        }
        String decode;
        try {
            decode = new String(Base64.getDecoder().decode(secondStageDecode(password, realKey)), StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            log.debug("value is not encrypted, return original");
            return password;
        }

        if (!isDecodedWithCurrentKey(decode, realKey)) {
            return "密钥校验失败";
        }
        return decode.substring(0, decode.length() - realKey.length() - 1);
    }

    public static boolean isEncryptedValue(String value) {
        return isEncryptedValue(value, getRealKey());
    }

    public static boolean isEncryptedValue(String value, String realKey) {
        if (StrUtil.isEmpty(value) || StrUtil.isEmpty(realKey)) {
            return false;
        }
        try {
            String decode = new String(Base64.getDecoder().decode(secondStageDecode(value, realKey)), StandardCharsets.UTF_8);
            return isDecodedWithCurrentKey(decode, realKey);
        } catch (Exception e) {
            return false;
        }
    }

    public static String decodeIfEncrypted(String value) {
        return decodeIfEncrypted(value, getRealKey());
    }

    public static String decodeIfEncrypted(String value, String realKey) {
        return isEncryptedValue(value, realKey) ? decodeBase64Salt(value, realKey) : value;
    }

    public static String encodeBase64Salt(String password) {
        if (StrUtil.isBlank(password)) {
            return null;
        }
        return secondStageEncode(Base64.getEncoder().encodeToString(password.concat("1").concat(getRealKey())
                .getBytes(StandardCharsets.UTF_8)));
    }

    private static String secondStageEncode(String firstEncodePassword) {
        String realKey = getRealKey();
        int re = firstEncodePassword.charAt(0) + realKey.charAt(0);
        return Rot.encode13(String.valueOf((char) re).concat(firstEncodePassword.substring(1)));
    }

    private static String secondStageDecode(String secondEncodePassword, String realKey) {
        secondEncodePassword = Rot.decode13(secondEncodePassword);
        return String.valueOf((char) (secondEncodePassword.charAt(0) - realKey.charAt(0)))
                .concat(secondEncodePassword.substring(1));
    }

    private static boolean isDecodedWithCurrentKey(String decode, String realKey) {
        if (StrUtil.isEmpty(decode) || StrUtil.isEmpty(realKey) || decode.length() <= realKey.length()) {
            return false;
        }
        int keyLastIndex = realKey.length() - 1;
        int decodeLastIndex = decode.length() - 1;
        for (int i = 0; i < keyLastIndex + 1; i++) {
            if (decode.charAt(decodeLastIndex--) != realKey.charAt(keyLastIndex--)) {
                return false;
            }
        }
        return true;
    }

    public static String generateRandomStrongPassword() {
        class GeneratePass {
            private final char[] alphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
                    'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
            private final char[] symbols = {'!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '+', '[', ']'};
            private final int[] digits = {1, 2, 3, 4, 5, 6, 7, 8, 9};
            private final int PASS_LENGTH;

            {
                int passLength;
                String length = new Config("config/configurable.properties").get("GENERATE_PASSWORD_LENGTH");
                if (StrUtil.isBlank(length)) {
                    passLength = 12;
                } else {
                    try {
                        passLength = Integer.parseInt(length);
                    } catch (Exception e) {
                        log.error(e.getStackTrace()[0].toString());
                        passLength = 12;
                    }
                }
                PASS_LENGTH = passLength;
            }

            public String generateRandomStrongPassword() {
                StringBuilder finallyPass = new StringBuilder();
                for (int i = 0; i < PASS_LENGTH; i++) {
                    int rand = (int) (Math.random() * 3);
                    switch (rand) {
                        case 0:
                            char currentChar = alphabet[(int) (Math.random() * alphabet.length)];
                            finallyPass.append(Math.round(Math.random()) == 0 ? String.valueOf(currentChar).toUpperCase() : currentChar);
                            break;
                        case 1:
                            finallyPass.append(symbols[(int) (Math.random() * symbols.length)]);
                            break;
                        case 2:
                            finallyPass.append(digits[(int) (Math.random() * digits.length)]);
                            break;
                        default:
                            break;
                    }
                }
                return finallyPass.toString();
            }
        }
        return new GeneratePass().generateRandomStrongPassword();
    }

    public static void encodeAccount(Account account) {
        account.setAccountName(encodeBase64Salt(account.getAccountName()));
        account.setUsername(encodeBase64Salt(account.getUsername()));
        account.setPassword(encodeBase64Salt(account.getPassword()));
        account.setOther(encodeBase64Salt(account.getOther()));
        account.setUserKey(encodeBase64Salt(account.getUserKey()));
    }

    public static void decodeAccount(Account account) {
        account.setAccountName(decodeBase64Salt(account.getAccountName()));
        account.setUsername(decodeBase64Salt(account.getUsername()));
        account.setPassword(decodeBase64Salt(account.getPassword()));
        account.setOther(decodeBase64Salt(account.getOther()));
        account.setUserKey(decodeBase64Salt(account.getUserKey()));
    }

    public static void decodeAccount(Account account, String realKey) {
        account.setAccountName(decodeBase64Salt(account.getAccountName(), realKey));
        account.setUsername(decodeBase64Salt(account.getUsername(), realKey));
        account.setPassword(decodeBase64Salt(account.getPassword(), realKey));
        account.setOther(decodeBase64Salt(account.getOther(), realKey));
        account.setUserKey(decodeBase64Salt(account.getUserKey(), realKey));
    }
}
