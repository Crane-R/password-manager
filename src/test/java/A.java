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
            // ���������0���쳣��Ϣ
            System.out.println(1/0);
        }catch (Exception e){
            // ���쳣��Ϣ��ӡ����־�ļ���ȥ
//            String exception = new OutputStreamUtil().util(e);
            // �����־�ļ�
            logger.error("Programer error:"+ e+"\t"+"��"+new SimpleDateFormat("yyyy-MM-dd/HH.mm.ss").format(new Date())+"��");
        }
        logger.debug("start Debug detail......");
    }
}
