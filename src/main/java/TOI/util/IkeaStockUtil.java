package TOI.util;

import TOI.Constant.Constant;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: W.k
 * Date: 13-5-2
 * Time: 下午10:36
 * To change this template use File | Settings | File Templates.
 */
public class IkeaStockUtil {

    public static String setBufA(String id) {
        String buf = null;
        if(!id.contains("S")){
        try {

            buf = HtmlUtil.getHtmlContent("http://m.ikea.com/cn/zh/store/availability/?storeCode=802&itemType=art&itemNo=" + id + "&change=true");



        } catch (IOException e) {
            e.getMessage();  //To change body of catch statement use File | Settings | File Templates.
            return  null;
        }
        }
        else{
            try {
            buf = HtmlUtil.getHtmlContent("http://m.ikea" +
                    ".com/cn/zh/store/availability/?storeCode=802&itemType=spr&itemNo=" + id.replace("S",""
                    ) + "&change=true");
            } catch (IOException e) {
                e.getMessage();  //To change body of catch statement use File | Settings | File Templates.
                return null;
            }
    }
        return buf;}

    public static String setBufB(String id) {
        String bufB;

        try {
            bufB = HtmlUtil.getHtmlContent("http://www.ikea.com/cn/zh/catalog/packagepopup/" + id);
        } catch (IOException e) {
            bufB=null;
            return bufB;//To change body of catch statement use File | Settings | File Templates.
        }
        return bufB;
    }



    /**
     * 获取IKEA库存信息
     */
    public static String StockInfoCatcher(String id) {
        String buf = setBufA(id);
        if(buf!=null){
        String quantity = new String();
        int status = 0;  //无库存
        String info=null;
        if (buf.contains("有库存")) {
            int beginIx = buf.indexOf("有库存");
            int endIx = buf.indexOf("</b>", beginIx);
            quantity = buf.substring(beginIx + "有库存：".length(), endIx);
            quantity = quantity.replace("<b>", "").replace(" ", "");

            if (buf.contains("联系工作人员")) {
                info="外仓";
                status = 2;  //外仓
            }
            else if(buf.contains("相关区域")){
                info="在家居用品区";
                status = 3;  //在家居用品区
            }
            else if(buf.contains("")) {
                beginIx = buf.indexOf("location-left");
                endIx = buf.indexOf("</div>", beginIx);
                String huojia = buf.substring(beginIx + "location-left".length()+2, endIx);
                beginIx=buf.indexOf("ikea-location-location",buf.indexOf("location-left"));
                endIx = buf.indexOf("</div>", beginIx);
                String weizhi=buf.substring(beginIx + "ikea-location-location".length()+2, endIx);

                info=huojia+"@!@"+weizhi;
                status = 1; //可以发货
            }

        } else if (buf.contains("无库存")) {
            info="无库存";
            quantity = "0";
            status = 0;     //无库存
        } else if (buf.contains("不出售该产品")) {
            info="不出售该产品";
            quantity = "0";
            status = -1;     //不再售
        } else {

            quantity = "0";
        status = -2;     //出错
        }
        return quantity + Constant.split + status+Constant.split + info;
        }
        else
            return "0"+Constant.split + "-2"+Constant.split + "无网页";
    }

    public static String WeightCatcher(String id) {
        double wholeweight = 0;

        String buf2 = setBufB(id);
        if (buf2==null)
            buf2 = setBufB("S"+id);
        List<Double> weight = new ArrayList<Double>();
        String regexStr = "<div class=\"rowContainerPackage\">[\\s\\S]*?<div class=\"clear\"></div>";
        Pattern productCell = Pattern.compile(regexStr);
        Matcher m = productCell.matcher(buf2);
        while (m.find()) {

            String content = m.group();
            int beginIx = content.indexOf("<div class=\"colPack\">");
            int endIx = content.indexOf("</div>", beginIx);
            int count = new Integer(content.substring(beginIx + "<div class=\"colPack\">".length(), endIx).replace("	", "").replace("千克", "").replace("&nbsp;", ""));


            beginIx = content.indexOf("<div class=\"colWeight\">");
            endIx = content.indexOf("</div>", beginIx);
            String tmp=content.substring(beginIx + "<div class=\"colWeight\">".length(),
                    endIx);
            weight.add(count * new Double(tmp.replace("	", "").replace("千克", "").replace("&nbsp;", "0")));


        }
        if (!weight.contains(0)) {
            for (int i = 0; i < weight.size(); i++)
                wholeweight = wholeweight + weight.get(i);
            DecimalFormat df=new DecimalFormat(".##");
            String stringWeight=df.format(wholeweight);
            System.out.println(stringWeight);
            return stringWeight;
        } else return "-1";
    }



    public static double SizeCatcher(String id) {
        String buf2 = setBufB(id);
        double wholesize = 0;
        List<Double> sizer = new ArrayList<Double>();

        String regexStr = "<div class=\"rowContainerPackage\">[\\s\\S]*?<div class=\"clear\"></div>";
        Pattern productCell = Pattern.compile(regexStr);
        Matcher m = productCell.matcher(buf2);
        while (m.find()) {

            String content = m.group();
        int beginIx = content.indexOf("<div class=\"colWidth\">");
        int endIx = content.indexOf("</div>", beginIx);
            int count = new Integer(content.substring(beginIx + "<div class=\"colPack\">".length(), endIx).replace("	", "").replace("千克", "").replace("&nbsp;", ""));
            ArrayList<Double> size = new ArrayList<Double>();
        size.add(new Double(content.substring(beginIx + "<div class=\"colWidth\">".length(), endIx).replace("	", "").replace("厘米", "").replace("&nbsp;", "0")));

        beginIx = content.indexOf("<div class=\"colHeight\">");
        endIx = content.indexOf("</div>", beginIx);
        size.add(new Double(content.substring(beginIx + "<div class=\"colHeight\">".length(), endIx).replace("	", "").replace("厘米", "").replace("&nbsp;", "0")));
        beginIx = content.indexOf("<div class=\"colLength\">");
        endIx = content.indexOf("</div>", beginIx);
        size.add(new Double(content.substring(beginIx + "<div class=\"colLength\">".length(), endIx).replace("	", "").replace("厘米", "").replace("&nbsp;", "0")));
        if (!size.contains((double) 0))
            sizer.add(count * size.get(0) * size.get(1) * size.get(2) / 1000000);
        else
            sizer.add((double) 9999);
    }

        if (!sizer.contains(9999)) {
            for (int i = 0; i < sizer.size(); i++)
                wholesize = wholesize + sizer.get(i);
            return wholesize;
        } else return -1;
}
    public static  void StockInfo2SQL(String id){
        String[] result=IkeaStockUtil.StockInfoCatcher(id).split(Constant.split);
         ItemUtils.updateSingleValue(id,"quantity",result[0]);
         ItemUtils.updateSingleValue(id,"stockType",result[1]);

    }
      public static  void main(String[] args){
          System.out.println(IkeaStockUtil.StockInfoCatcher("70227707"));
      }

}
