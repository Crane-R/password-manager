package crane.model.service;

import cn.hutool.core.util.StrUtil;
import crane.constant.Constant;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;

/**
 * Description: 有关加密的类
 * Author: ZhouXingxue
 * Date: 2023/1/7 22:32
 *
 * @author Crane Resigned
 */
@Slf4j
public final class SecurityService {

    private SecurityService() {
    }

    /**
     * 检测key路径是否含有密钥
     * Author: Crane Resigned
     * Date: 2023-01-07 23:04:49
     */
    public static boolean checkKeyAmountIsNotZero() {
        File file = new File(Paths.get(Constant.DIRECTORY_KEYS).toAbsolutePath().toString());
        return Objects.requireNonNull(file.listFiles()).length != 0;
    }

    /**
     * 检测密钥文件是否存在的方法
     * Author: Crane Resigned
     * Date: 2022-11-27 11:54:47
     */
    public static boolean checkKeyFileIsExist(String inputKey) {
        File file = new File(Paths.get(Constant.DIRECTORY_KEYS + "/" + inputKey).toAbsolutePath().toString());
        boolean b = file.exists() && Objects.isNull(file.listFiles());
        if (b) {
            Constant.CURRENT_KEY = inputKey;
        }
        return b;
    }

    /**
     * 创建密匙的方法
     * Author: Crane Resigned
     * Date: 2022-11-27 12:07:28
     */
    public static void createKey(String keyPre) {
        String checkoutKey = keyPre.replaceAll("\"", "'");
        String finalKey = checkoutKey.concat(UUID.randomUUID().toString());

        //以密钥作为文件名创建密匙
        File targetKeyFile = new File(Paths.get(Constant.DIRECTORY_KEYS + "/" + keyPre).toAbsolutePath().toString());

        //设为只读命令
        String command1 = "attrib \"" + targetKeyFile.getAbsolutePath() + "\" +R";
        //隐藏命令
        String command2 = "attrib \"" + targetKeyFile.getAbsolutePath() + "\" +H";

        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(targetKeyFile);
            fileOutputStream.write(finalKey.getBytes());

            Runtime.getRuntime().exec(command1);
            Runtime.getRuntime().exec(command2);

            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Constant.CURRENT_KEY = keyPre;
    }

    /**
     * 获取密匙
     * Author: Crane Resigned
     * Date: 2022-11-27 13:14:59
     */
    public static String getKey(String targetKey) {
        BufferedReader bufferedReader;
        String result = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(Paths.get(Constant.DIRECTORY_KEYS + "/" + targetKey).toAbsolutePath().toString()));
            result = bufferedReader.readLine();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取密钥的uuid
     * Author: Crane Resigned
     * Date: 2022-11-27 17:01:41
     */
    public static String getUuidKey() {
        String uuidKey = getKey(Constant.CURRENT_KEY);
        if (uuidKey.length() < Constant.MINIMUM_KEY_LENGTH) {
            return "密匙长度不对";
        }
        return new StringBuilder(new StringBuilder(uuidKey).reverse().substring(0, 36)).reverse().toString();
    }

    /**
     * 获取真实密钥
     * Author: Crane Resigned
     * Date: 2022-11-27 13:52:51
     */
    public static String getRealKey() {
        String uuidKey = getKey(Constant.CURRENT_KEY);
        if (uuidKey.length() < Constant.MINIMUM_KEY_LENGTH) {
            return "密匙长度不对";
        }
        return new StringBuilder(new StringBuilder(uuidKey).reverse().substring(36, uuidKey.length())).reverse().toString();
    }

    /**
     * 解密算法
     * Author: Crane Resigned
     * Date: 2022-11-27 13:43:29
     */
    public static String decodeBase64Salt(String password) {
        if (StrUtil.isEmpty(password)) {
            return password;
        }
        String decode;
        try {
            decode = new String(Base64.getDecoder().decode(secondStageDecode(password)), StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            log.error("该信息未加密，返回原值" + password);
            return password;
        }
        String key = getRealKey();

        int keyLastIndex = key.length() - 1;
        int decodeLastIndex = decode.length() - 1;
        for (int i = 0; i < keyLastIndex + 1; i++) {
            if (decode.charAt(decodeLastIndex--) != key.charAt(keyLastIndex--)) {
                return "假密匙";
            }
        }
        return new StringBuilder(decode).substring(0, decodeLastIndex - keyLastIndex - 1);
    }

    /**
     * 加密算法
     * Author: Crane Resigned
     * Date: 2022-11-27 14:13:12
     */
    public static String encodeBase64Salt(String password) {
        if (StrUtil.isBlank(password)) {
            return null;
        }
        return secondStageEncode(Base64.getEncoder().encodeToString(password.concat("1").concat(getRealKey()).getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * 二阶加密：掐头加密算法
     * Author: Crane Resigned
     * Date: 2022-12-01 19:01:18
     */
    private static String secondStageEncode(String firstEncodePassword) {
        String realKey = getRealKey();
        int re = firstEncodePassword.charAt(0) + realKey.charAt(0);
        return String.valueOf((char) re).concat(firstEncodePassword.substring(1));
    }

    /**
     * 二阶解密
     * Author: Crane Resigned
     * Date: 2022-12-01 19:44:23
     */
    private static String secondStageDecode(String secondEncodePassword) {
        return String.valueOf((char) ((int) secondEncodePassword.charAt(0) - (int) getRealKey().charAt(0))).concat(secondEncodePassword.substring(1));
    }

}
