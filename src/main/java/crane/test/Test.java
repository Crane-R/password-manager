package crane.test;

import crane.view.ExportImportDataFrame;
import crane.view.MainFrame;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * @author Crane Resigned
 */
public class Test {
    public static void main(String[] args) throws IOException {
        new MainFrame().setVisible(true);

//
//        String recentlyPath = ExportImportDataFrame.getRecentlyPath();
//        System.out.println(recentlyPath);
//        
//        ExportImportDataFrame.setRecentlyPath("你倒是覅多少积分");


//        new AddFrame().setVisible(true);

//        try {
//            System.out.println(JDBCConnection.getConnection());
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

//        System.out.println(Paths.get("1").toAbsolutePath().toString());
//
//     new MainFrame().setVisible(true);
//        String a = "config/recently_path.properties";
//
//        URL systemResource = ClassLoader.getSystemResource(a);
//
//        InputStream systemResourceAsStream = ClassLoader.getSystemResourceAsStream("config/recently_path.properties");
//        OutputStream writer = null;
//        try {
//            writer = Files.newOutputStream(new File(systemResource.toURI()).toPath());
//        } catch (URISyntaxException e) {
//            throw new RuntimeException(e);
//        }

//        Properties props = new Properties();
//        try {
//            props.load(ClassLoader.getSystemResourceAsStream("config/recently_path.properties"));
//            String b = props.getProperty("url");
//            System.out.println(b);
//            
//            props.setProperty("abcdef","1213212121");
//            
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
////        ClassLoader.getSystemResource()
//        
//        OutputStream writer = null;
//        try {
//            writer = new BufferedOutputStream(new FileOutputStream("src/main/resources/config/recently_path.properties"));
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//        props.store(writer, "a");
//

//        try {
//            new File("E:\\Record\\a.xlsx").createNewFile();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }


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
