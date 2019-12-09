package com.zss.myspringboot.client;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlUtil {
    WebClient webClient;
    static  Connection conn;
    static String stockUrlExg="http://stockpage.10jqka.com.cn";
    static String regexChn="[\u4e00-\u9fa5]+";
    /*"http://(.*?)/(.*?)/"*/;
    static String urlRgex = "http://(.*?)/(.*?)\\d{4,}";
    static String codeRgex =  "\\d{4,}";
    static  String url;
    static  String name;
    static  String code;




    //初始化
    public HtmlUtil() throws ClassNotFoundException {

        webClient = new WebClient(BrowserVersion.CHROME);//新建一个模拟谷歌Chrome浏览器的浏览器客户端对象
        webClient.getOptions().setThrowExceptionOnScriptError(false);//当JS执行出错的时候是否抛出异常, 这里选择不需要
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);//当HTTP的状态非200时是否抛出异常, 这里选择不需要
        webClient.getOptions().setActiveXNative(false);
        webClient.getOptions().setCssEnabled(false);//是否启用CSS, 因为不需要展现页面, 所以不需要启用
        webClient.getOptions().setJavaScriptEnabled(true); //很重要，启用JS。有些网站要开启！
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());//很重要，设置支持AJAX
        webClient.getOptions().setTimeout(30000);
        conn=connectInit();
    }
    //获取某个url的web客户端
    public String htmlUnitUrl(String url, WebClient webClient) {
        try {
            WebRequest request = new WebRequest(new URL(url), HttpMethod.GET);
            Map<String, String> additionalHeaders = new HashMap<String, String>();
            additionalHeaders
                    .put("User-Agent",
                            "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.104 Safari/537.36");
            additionalHeaders.put("Accept-Language", "zh-CN,zh;q=0.8");
            additionalHeaders.put("Accept", "*/*");
            request.setAdditionalHeaders(additionalHeaders);
            // 获取某网站页面
            Page page = webClient.getPage(request);
            return page.getWebResponse().getContentAsString();
        } catch (Exception e) {

        }
        return null;
    }

    public static void close(Connection conn) {
        if(conn != null) {
            try {
                conn.close();  //关闭数据库链接
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static Connection connectInit()  throws ClassNotFoundException {
        /* 连接数据库 */

        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://127.0.0.1:3306/asharequerysystem";
        String user = "root";
        String password = "123456";

        //建立数据库连接
        Connection con=null;
        String sql;
        Statement stmt;
        try{
             con= DriverManager.getConnection(url,user,password);

        }catch (Exception e){
            System.out.println(e.toString());
        }
        return con;


    }
    /*
        提取包含url 代码  和名称的 td
     */
    private static void getBatchStackCodeAndNameAndUrl(String fileUrl) throws IOException {
        BufferedReader br = null;

        try {
            //得到输入流
            br = new BufferedReader(new FileReader(fileUrl));
            String contentLine ;

            while ((contentLine = br.readLine()) != null) {
                 //读取每一行，并输出
                //System.out.println(contentLine);
                if (StringUtils.contains(contentLine,stockUrlExg)&&isContainChinese(contentLine)){
                   //System.out.println(contentLine);
                    //提取url ，code ，name

                   /* System.out.println(stringRgex(contentLine,regexChn));
                        System.out.println(stringRgex(contentLine,urlRgex));
                    System.out.println(stringRgex(contentLine,codeRgex));*/
                   insertCode(stringRgex(contentLine,codeRgex),stringRgex(contentLine,regexChn),stringRgex(contentLine,urlRgex),conn);

                }

                }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }
    //爬取某网页
    public void work(String url) {
        ExecutorService pool = Executors.newCachedThreadPool();
        try {
            HtmlPage page = webClient.getPage(url);//打开网页
            int pageCount = 2;

            for(int i=1;i<=pageCount;i++) {
                //当访问速度过快时，后台浏览器会禁止，在这里可加入适当延迟的代码
                /**
                 *延迟执行的代码
                 */
                Thread.currentThread().sleep(10000);

                String content = htmlUnitUrl("http://q.10jqka.com.cn/index/index/board/all/field/zdf/order/desc/page/"+i+"/ajax/1/",webClient);
                if(content.contains("Nginx forbidden.")){
                    i--;
                }

                else {
                    System.out.println(content);

                    writeFile("D://测试//"+i+".html",content);
                }



            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存抓取的html到本地
     * @param path
     * @param content
     */
    public static boolean writeFile(String path,String content) {

        File file = new File(path);
        boolean isSuccess = true;
        System.out.println(path);
        // if file doesnt exists, then create it
        if (!file.exists()) {
            try {
                isSuccess = file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                isSuccess = false;
            }
        }else {
            file.delete();
        }
        FileWriter fw;
        try {
            fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
            System.out.println("写入成功.");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("写入失败.");
            isSuccess = false;
        }
        return isSuccess;
    }
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }
    public static String filterChinese(String str) {
        // 用于返回结果
        String result = str;
        boolean flag = isContainChinese(str);
        if (flag) {// 包含中文
            // 用于拼接过滤中文后的字符
            StringBuffer sb = new StringBuffer();
            // 用于校验是否为中文
            boolean flag2 = false;
            // 用于临时存储单字符
            char chinese = 0;
            // 5.去除掉文件名中的中文
            // 将字符串转换成char[]
            char[] charArray = str.toCharArray();
            // 过滤到中文及中文字符
            for (int i = 0; i < charArray.length; i++) {
                chinese = charArray[i];
                flag2 = isChinese(chinese);
                if (!flag2) {// 不是中日韩文字及标点符号
                    sb.append(chinese);
                }
            }
            result = sb.toString();
        }
        return result;
    }
    /**
     * 判定输入的是否是汉字
     *
     * @param c
     *  被校验的字符
     * @return true代表是汉字
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }
    /**
     * 正则表达式匹配两
     * @param line
     * @return
     */
    private static String stringRgex(String line, String rgex) {

        Pattern pattern = Pattern.compile(rgex);// 匹配的模式
        Matcher m = pattern.matcher(line);
        while (m.find()) {
            return m.group();

        }

        return "";
    }
    private static void insertCode(String code,String name,String url,Connection conn) {
        PreparedStatement ps=null;
        //先判断这个代码是否存在
        String selectSql="SELECT * FROM CODE WHERE code=?";
        String sql = "INSERT INTO code (code,name,url,active) VALUES(?,?,?,1)";        //插入sql语句

       /* boolean flag=true;
        while (!flag)break;*/


        try {
            // System.out.println("开始检查");
            ps = conn.prepareStatement(selectSql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,code);
            if(ps.executeQuery().next())  {
                // System.out.println("code存在");
                return;
            }
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1,code);
            ps.setString(2,name);
            ps.setString(3,url);
            ps.executeUpdate();            //执行sql语句
            System.out.println("insert success");
            System.out.println(code+name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException, ClassNotFoundException {
       //抓取数据
       HtmlUtil demo=new HtmlUtil();
        //String url = "http://q.10jqka.com.cn/";
         //demo.work(url);
       for(int i=1;i<=185;i++){
           getBatchStackCodeAndNameAndUrl("D://测试//"+i+".html");

       }


    }
}
