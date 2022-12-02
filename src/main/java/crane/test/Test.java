package crane.test;

import crane.model.service.AccountService;
import crane.view.AddFrame;
import crane.view.LookFrame;
import crane.view.MainFrame;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;

/**
 * @author Crane Resigned
 */
public class Test {
    public static void main(String[] args) throws UnsupportedEncodingException {
//        new MainFrame().setVisible(true);

//        new AddFrame().setVisible(true);

//        try {
//            System.out.println(JDBCConnection.getConnection());
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

//        System.out.println(Paths.get("1").toAbsolutePath().toString());
//
        new LookFrame().setVisible(true);
//        System.out.println(AccountService.getRealKey());

//        AccountService.createKey("2002");

//        System.out.println(UUID.randomUUID().toString().length());;

//        System.out.println(AccountService.checkKeyFileIsExist());


//        String a = "abcdefghijkl";
////
//        byte[] encode = Base64.getEncoder().encode(a.concat("*").concat("2002").getBytes());
//////        System.out.println(new String(encode));
////
////
//        String s = AccountService.encodeBase64Salt("123456");
//        System.out.println(s);
////
//        System.out.println( AccountService.decodeBase64Salt(s));;
////        System.out.println(Arrays.toString(decode));
//
//        System.out.println(new String(Base64.getEncoder().encode("456353".getBytes())));

//
//        String a = "你还哦是的的";
//        System.out.println("原密码："+a);
//
//        String s = AccountService.encodeBase64Salt(a);
//        System.out.println("加密后：" + s);
//
//
//        String s1 = AccountService.decodeBase64Salt(s);
//        System.out.println("解密后：" + s1);


    }

}
