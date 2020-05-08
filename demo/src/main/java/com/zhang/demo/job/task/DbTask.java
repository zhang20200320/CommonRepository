package com.zhang.demo.job.task;

import com.zhang.demo.common.CommonResult;
import com.zhang.demo.job.entity.JdbcBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

/**
 * @author zhang
 * @date 2020-05-08 15:56:00
 */
@RestController
public class DbTask {

    public static final String WINDOWS_UPLOAD_PATH = "D:/data/peng/dbfiles/";

    public static final String LINUX_UPLOAD_PATH = "/data/peng/dbfiles/";

    public static final String SQL_BACKUP_PREFIX_FORMAT = "yyyyMMddHHmmss";

    private Logger logger = LoggerFactory.getLogger(getClass());

    static String filePath;

    /**
     * 数据库备份 导出.sql文件到本地
     * @return
     */
    @Scheduled(cron="59 59 23 * * ? ")//表示每天晚上23点59分59秒执行一次
    public CommonResult backup(){
        try {
            String fileName = new SimpleDateFormat(SQL_BACKUP_PREFIX_FORMAT).format(new Date())+"_backup";
            filePath = getFilePath(fileName+".sql");
            /*
             * 动态获取数据库配置
             */
            JdbcBean j = new JdbcBean();
            Properties prop = new Properties();//读取properties 文件所用的类  
                try {
                    InputStream in = DbTask.class.getResourceAsStream("/application.properties");
//                    InputStream in = DbTask.class.getResourceAsStream("/application.yml");
                    prop.load(in); // /加载属性列表  
                    String [] a = prop.getProperty("spring.datasource.druid.first.url").split("/");
                    j.setIp(a[2].split(":")[0]);
                    j.setDb(a[3].trim().split("[?]")[0]);
                    j.setPort(Integer.parseInt(a[2].split(":")[1]));
                    j.setUsername(prop.getProperty("spring.datasource.druid.first.username"));
                    j.setPassword(prop.getProperty("spring.datasource.druid.first.password"));
                    in.close();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            boolean exportFlag = executeExportCommond(j,filePath);
            if(exportFlag){
                logger.info("数据库数据备份本地成功！");
                return CommonResult.success("数据库数据备份本地成功！");
            }else{
                logger.info("数据库数据备份失败");
                return CommonResult.failed("数据库数据备份失败");
            }
        } catch (Exception e) {
            logger.info("运行异常，数据库数据备份失败！");
            e.printStackTrace();
            return CommonResult.failed("备份失败");
        }
    }

    /**
     * 导入数据
     * @param jdbcBean
     * @param filePath
     * @return
     * @throws Exception
     */
    //执行导入命令
    public static boolean executeImportCommond(JdbcBean jdbcBean,String filePath) throws Exception {
        //动态获取本地MySQL数据库安装目录bin下的目录
        String C=getMysqlPath();

        //静态获取本地MySQL数据库安装目录下bin的目录
//		String C="C:\\Program Files\\MySQL\\MySQL Server 5.6\\bin\\";

        String sql_1 ="mysql -P "+jdbcBean.getPort()+" -h "+jdbcBean.getIp()+
                " -u "+jdbcBean.getUsername()+" -p"+jdbcBean.getPassword()+
                " --default-character-set=utf8";
        String sql_2 = "use "+jdbcBean.getDb();
        String sql_3 = "source "+filePath;
        Process process = Runtime.getRuntime().exec(sql_1);
        OutputStream os = process.getOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(os);
        writer.write(sql_2 + "\r\n" + sql_3);
        writer.flush();
        writer.close();
        os.close();
        if(process.waitFor()==0){
            return true;
        }
        return false;
    }

    /**
        * 导出数据(方法一)
        * dos命令执行备份 mysqldump -P port -h ip -u username -ppassWord projectName > d:\db.sql
        * C:\Program Files\MySQL\MySQL Server 5.6\bin\mysqldump -P 3306 -h 127.0.0.1 -u root -proot demo --default-character-set=utf8 --lock-tables=false --result-file=D:/data/design_center/dbfiles/20180817185421_backup.sql
        * @param jdbcBean
        * @param filePath
        * @return
        * @throws IOException
        * @throws InterruptedException
        */
        //执行导出命令
    public static boolean executeExportCommond(JdbcBean jdbcBean,String filePath) throws IOException, InterruptedException{

        //动态获取本地MySQL数据库安装目录bin下的目录
        String C=getMysqlPath();

        //静态获取本地MySQL数据库安装目录下bin的目录
//        String C="C:\\Program Files\\MySQL\\MySQL Server 5.6\\bin\\";

        String sql ="mysqldump -P "+jdbcBean.getPort()+" -h "+jdbcBean.getIp()+
                 " -u "+jdbcBean.getUsername()+" -p"+jdbcBean.getPassword()+
                 " "+jdbcBean.getDb()+" --default-character-set=utf8 "+
                 "--lock-tables=false --result-file="+filePath;
        System.out.println(sql);
        Process process = Runtime.getRuntime().exec(sql);
        if(process.waitFor()==0){//0 表示线程正常终止。
            return true;
        }
        return false;//异常终止
    }

    /**
     * 动态获取mysql安装路径(bin下的路径)
     *【注】:本地MySQL数据库配置环境变量(新建MYSQL_HOME变量，值为“MySQL安装路径下【bin的上一层】”，
     *                                   在Path变量中配置MYSQL_HOME变量，值为【%MYSQL_HOME%bin】)。
     * @return
     */
    public static String getMysqlPath(){
        @SuppressWarnings("rawtypes")
        Map m=System.getenv();
        String s2=(String) m.get("MYSQL_HOME");//获取本计算机环境变量中PATH的内容
        String mySqlPath=s2+"\\bin\\";
        System.out.println("MySQL本地安装路径："+mySqlPath);
        return mySqlPath;
    }


    /**
     * 获得文件路径
     * @param fileName
     * @return
     */
    public static String getFilePath(String fileName){
        String os = System.getProperty("os.name"); //获取操作系统的名称 
        String rootPath;
        String filPath;
        if(os.toLowerCase().startsWith("win") || os.toLowerCase().startsWith("Win")){
            rootPath = WINDOWS_UPLOAD_PATH;
        }else{
            rootPath = LINUX_UPLOAD_PATH;
        }
        if(!new File(rootPath).exists()){//判断文件是否存在
            new File(rootPath).mkdirs();//可以在不存在的目录中创建文件夹。诸如：a\\b,既可以创建多级目录。
        }
        if(fileName==null){
            filPath = rootPath+new SimpleDateFormat(SQL_BACKUP_PREFIX_FORMAT).format(new Date())+"_backup.sql";
        }else{
            filPath = rootPath+fileName;
        }
        return filPath;
    }


}
