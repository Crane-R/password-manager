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

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

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
        File file = new File(Constant.DIRECTORY_KEYS);
        //tip:发生在v23.1014，使用innosetup打包的软件，安装时创建桌面快捷方式的时候获取地址不同，故报错空指针
        return Objects.requireNonNull(file.listFiles()).length != 0;
    }

    /**
     * 检测密钥文件是否存在的方法
     * Author: Crane Resigned
     * Date: 2022-11-27 11:54:47
     */
    public static boolean checkKeyFileIsExist(String inputKey) {
        File file = new File(Constant.DIRECTORY_KEYS + new Md5Service().convertMd5(inputKey));
        boolean b = file.exists() && Objects.isNull(file.listFiles());
        if (b) {
            Constant.CURRENT_KEY = inputKey;
            LightDao.updatePath();
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
        String finalKey = RealKeyEncodeService.realKeyEncode(checkoutKey).concat(new SnowflakeGenerator().next().toString());
        //以密钥作为文件名创建密匙
        File targetKeyFile = new File(Constant.DIRECTORY_KEYS + new Md5Service().convertMd5(keyPre));
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
        LightDao.updatePath();
    }

    /**
     * 获取密匙
     * Author: Crane Resigned
     * Date: 2022-11-27 13:14:59
     */
    public static String getKey(String targetKey) {
        if (StrUtil.isBlank(targetKey)) {
            ShowMessage.showErrorMessage("缺失密钥无法解密", "密钥为空");
            return "";
        }
        BufferedReader bufferedReader;
        String result = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(
                    Constant.DIRECTORY_KEYS + new Md5Service().convertMd5(targetKey)));
            result = bufferedReader.readLine();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 去加密导入，给一个方法可以直接输入密钥文件获取密钥
     *
     * @Author CraneResigned
     * @Date 2024/8/21 18:56:36
     */
    public static String getKeyByKeyFile(String keyFile) {
        BufferedReader bufferedReader = null;
        String result = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(keyFile));
            result = bufferedReader.readLine();
            bufferedReader.close();
        } catch (IOException e) {
            ShowMessage.showErrorMessage(e.getStackTrace(), e.getMessage());
        } finally {
            CloseTool.close(bufferedReader);
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
        return new StringBuilder(new StringBuilder(uuidKey).reverse().substring(0, 19)).reverse().toString();
    }

    /**
     * 获取真实密钥
     * Author: Crane Resigned
     * Date: 2022-11-27 13:52:51
     */
    public static String getRealKey() {
        String uuidKey = getKey(Constant.CURRENT_KEY);
        return getRealKey(uuidKey);
    }

    public static String getRealKey(String fullKey) {
        if (fullKey.length() < Constant.MINIMUM_KEY_LENGTH) {
            return "密匙长度错误";
        }
        return RealKeyEncodeService.realKeyDecode(
                new StringBuilder(new StringBuilder(fullKey).reverse().substring(19, fullKey.length())).reverse().toString()
        );
    }

    /**
     * 真实密钥加密内部服务
     *
     * @Author AXing
     * @Date 2023/12/22 22:02:26
     */
    private static class RealKeyEncodeService {

        private RealKeyEncodeService() {
        }

        /**
         * 加密真实的密钥
         * 非真实样例：1111 -->
         * 每个字符转为int加一个.然后追加每个字符/10后不为0的次数
         *
         * @Author CraneResigned
         * @Date 2024/8/21 17:38:54
         */
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
            //标识当前检测索引的左右指针
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

    /**
     * 解密算法
     * Author: Crane Resigned
     * Date: 2022-11-27 13:43:29
     */
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
            log.error("该信息未加密，返回原值{}", password);
            return password;
        }

        int keyLastIndex = realKey.length() - 1;
        int decodeLastIndex = decode.length() - 1;
        for (int i = 0; i < keyLastIndex + 1; i++) {
            if (decode.charAt(decodeLastIndex--) != realKey.charAt(keyLastIndex--)) {
                return "密钥校验失败";
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
        return secondStageEncode(Base64.getEncoder().encodeToString(password.concat("1").concat(getRealKey())
                .getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * 二阶加密：掐头加密算法
     * Author: Crane Resigned
     * Date: 2022-12-01 19:01:18
     */
    private static String secondStageEncode(String firstEncodePassword) {
        String realKey = getRealKey();
        int re = firstEncodePassword.charAt(0) + realKey.charAt(0);
        return Rot.encode13(String.valueOf((char) re).concat(firstEncodePassword.substring(1)));
    }

    /**
     * 二阶解密
     * Author: Crane Resigned
     * Date: 2022-12-01 19:44:23
     */
    private static String secondStageDecode(String secondEncodePassword, String realKey) {
        secondEncodePassword = Rot.decode13(secondEncodePassword);
        return String.valueOf((char) ((int) secondEncodePassword.charAt(0) - (int) realKey.charAt(0)))
                .concat(secondEncodePassword.substring(1));
    }

    /**
     * 生成强密码
     * 不使用static是因为这个方法在程序中不是必须的
     *
     * @Author Crane Resigned
     * @Date 2023-05-24 16:52:33
     */
    public static String generateRandomStrongPassword() {
//        return new SnowflakeGenerator().next().toString();
        /**
         * 安全服务内部类
         * 生成强密码所需内容，后续可改进
         *
         * @Author Crane Resigned
         * @Date 2023-09-01 13:08:14
         */
        class GeneratePass {
            private final char[] alphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
                    'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
            private final char[] symbols = {'!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '+', '[', ']'};

            private final int[] digits = {1, 2, 3, 4, 5, 6, 7, 8, 9};

            /**
             * 密码长度
             *
             * @Author Crane Resigned
             * @Date 2023-09-01 13:14:03
             */
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
                    //随机因子0和1,0取字母1取符号
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
                    }
                }

                return finallyPass.toString();
            }
        }
        return new GeneratePass().generateRandomStrongPassword();
    }


    /**
     * 全局加密Account类
     *
     * @Author Crane Resigned
     * @Date 2023-06-12 19:21:15
     */
    public static void encodeAccount(Account account) {
        account.setAccountName(encodeBase64Salt(account.getAccountName()));
        account.setUsername(encodeBase64Salt(account.getUsername()));
        account.setPassword(encodeBase64Salt(account.getPassword()));
        account.setOther(encodeBase64Salt(account.getOther()));
        account.setUserKey(encodeBase64Salt(account.getUserKey()));
    }

    /**
     * 全局解密
     *
     * @Author Crane Resigned
     * @Date 2023-06-12 19:24:53
     */
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
