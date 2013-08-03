package TOI.util;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: W.k
 * Date: 13-4-30
 * Time: 上午1:00
 * To change this template use File | Settings | File Templates.
 */
public class IkeaUtils {
    /**
     * 设置MOBILE_BUF
     */
    public static String setBuf(String pid) {
        String bufS = new String();
        try {
            if (pid.contains("(?i)S"))
                bufS = HtmlUtil.getHtmlContent("http://m.ikea.com/cn/zh/catalog/products/spr/" + pid.replace("(?i)S", "") + "/");
            else
                bufS = HtmlUtil.getHtmlContent("http://m.ikea.com/cn/zh/catalog/products/art/" + pid + "/");

        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.err.println("设置ITEM失败");
//            e.printStackTrace();
        }
        return bufS;
    }

    /**
     * 设置分类(mobile)
     */
    public static List<String> setCategory(String bufS) {
        if(bufS.contains("breadcrumbs")){
        int beginIx = bufS.indexOf("<div class=\"ikea-breadcrumbs\"");
        int endIx = bufS.indexOf("</div>", beginIx);
        String result = bufS.substring(beginIx, endIx);
        String regexStr = "<a href=[\\s\\S]*?</a>";
        Pattern productCell = Pattern.compile(regexStr);
        Matcher m = productCell.matcher(result);
        List<String> cates = new ArrayList<String>();
        while (m.find()) {
            String date = m.group();
            int beginIx1 = date.indexOf(">");
            int endIx1 = date.indexOf("</a>", beginIx1);
            String cate = date.substring(beginIx1 + ">".length(), endIx1);
            cates.add(cate);
        }

        return cates;}
        else
            return  null;
    }

    /**
     * 设置ITEMTYPES
     */
    public static Map<String, String> setItemTypes(String bufS) {
        Map<String, String> items = new HashMap<String, String>();
        if(bufS.contains("itemLookup")){
        int beginIx = bufS.indexOf("var itemLookup");
        int endIx = bufS.indexOf("var firstAttrLookup", beginIx);
        String result = bufS.substring(beginIx + "var itemLookup".length(), endIx);
        String regexStr = "A1:[\\s\\S]*?}";
        Pattern productCell = Pattern.compile(regexStr);
        Matcher m = productCell.matcher(result);
        while (m.find()) {
            String date = m.group();
            int beginIx1 = date.indexOf("A1:");
            int endIx1 = date.indexOf("|", beginIx1);
            String type = date.substring(beginIx1 + "A1:".length(), endIx1);
            beginIx1 = date.indexOf("'t':'");
            endIx1 = date.indexOf("',", beginIx1);
            String as = date.substring(beginIx1 + "'t':'".length(), endIx1);
            beginIx1 = date.indexOf("i':'");
            endIx1 = date.indexOf("'}", beginIx1);
            String id = date.substring(beginIx1 + "i':'".length(), endIx1);
            if (as.equals("spr"))
                id = "S" + id;
            items.put(id, type);
        }

        return items;  }
        else
           return items;
    }


}
