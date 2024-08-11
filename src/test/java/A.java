import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * d
 *
 * @Author Crane Resigned
 * @Date 2024/8/11 15:41:52
 */
public class A {

    private final static Logger logger = LoggerFactory.getLogger(A.class);

    public static void main(String[] args) {
        logger.info("programer processing......");
        try{
            // 会产生除以0的异常信息
            System.out.println(1/0);
        }catch (Exception e){
            // 将异常信息打印到日志文件中去
//            String exception = new OutputStreamUtil().util(e);
            // 输出日志文件
            logger.error("Programer error:"+ e+"\t"+"【"+new SimpleDateFormat("yyyy-MM-dd/HH.mm.ss").format(new Date())+"】");
        }
        logger.debug("start Debug detail......");
    }
}
