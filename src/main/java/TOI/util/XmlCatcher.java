package TOI.util;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: W.k
 * Date: 13-4-29
 * Time: 上午2:04
 * To change this template use File | Settings | File Templates.
 */
public class XmlCatcher {
    public static String getItem(String buf) {
        String name = null;
        String facts = null;
        String careInst = null;
        String custBenefit = null;
        String designer = null;
        String environment = null;
        String goodToKnow = null;
        String custMaterials = null;
        String measure = null;

        String regexStr = "<item>[\\s\\S]*?</item>";
        Pattern productCell = Pattern.compile(regexStr);
        Matcher m = productCell.matcher(buf);
        while (m.find()) {
            if (!"".equals(m.group())) {
                String content = m.group();
                int beginIx = content.indexOf("<name>");
                int endIx = content.indexOf("</name>", beginIx);
                name = content.substring(beginIx + "<name>".length(), endIx);
                beginIx = content.indexOf("<facts>");
                endIx = content.indexOf("</facts>", beginIx);
                facts = content.substring(beginIx + "<facts>".length(), endIx);
                if (content.contains("<careInst>")) {
                    beginIx = content.indexOf("<careInst>");
                    endIx = content.indexOf("</careInst>", beginIx);
                    StringBuilder careInstB = new StringBuilder();
                    String careInstS = content.substring(beginIx + "<careInst>".length(), endIx);
                    String careInstT = "<ci>[\\s\\S]*?</ci>";
                    Pattern careInstP = Pattern.compile(careInstT);
                    Matcher careInstM = careInstP.matcher(careInstS);
                    while (careInstM.find())
                        careInstB.append(careInstM.group().replace("<h>", "").replace("</h>", ":").replace("<pt>", "").replace("</pt>", ":").replace("<t>", "").replace("</t>", "") + "<br/>");
                    careInst = careInstB.append("!").toString().replace("<br/>!", "").replace("!", "").replace("<ci>", "").replace("</ci>", "");
                }
                if (content.contains("<custBenefit>")) {
                    beginIx = content.indexOf("<custBenefit>");
                    endIx = content.indexOf("</custBenefit>", beginIx);
                    StringBuilder custBenefitB = new StringBuilder();
                    String custBenefitS = content.substring(beginIx + "<custBenefit>".length(), endIx);
                    String custBenefitT = "<t>[\\s\\S]*?</t>";
                    Pattern custBenefitP = Pattern.compile(custBenefitT);
                    Matcher custBenefitM = custBenefitP.matcher(custBenefitS);
                    while (custBenefitM.find())
                        custBenefitB.append(custBenefitM.group() + "<br/>");
                    custBenefit = custBenefitB.append("!").toString().replace("<br/>!", "").replace("!", "").replace("<t>", "").replace("</t>", "");
                }

                if (content.contains("<designer>")) {
                    beginIx = content.indexOf("<designer>");
                    endIx = content.indexOf("</designer>", beginIx);
                    designer = content.substring(beginIx + "<designer>".length(), endIx);
                }
                if (content.contains("<environment>")) {
                    beginIx = content.indexOf("<environment>");
                    endIx = content.indexOf("</environment>", beginIx);
                    StringBuilder environmentB = new StringBuilder();
                    String environmentS = content.substring(beginIx + "<environment>".length(), endIx);
                    String environmentT = "<t>[\\s\\S]*?</t>";
                    Pattern environmentP = Pattern.compile(environmentT);
                    Matcher environmentM = environmentP.matcher(environmentS);
                    while (environmentM.find())
                        environmentB.append(environmentM.group() + "<br/>");
                    environment = environmentB.append("!").toString().replace("<br/>!", "").replace("!", "").replace("<t>", "").replace("</t>", "");
                }

                if (content.contains("<goodToKnow>")) {
                    beginIx = content.indexOf("<goodToKnow>");
                    endIx = content.indexOf("</goodToKnow>", beginIx);
                    StringBuilder goodToKnowB = new StringBuilder();
                    String goodToKnowS = content.substring(beginIx + "<goodToKnow>".length(), endIx);
                    String goodToKnowT = "<t>[\\s\\S]*?</t>";
                    Pattern goodToKnowP = Pattern.compile(goodToKnowT);
                    Matcher goodToKnowM = goodToKnowP.matcher(goodToKnowS);
                    while (goodToKnowM.find())
                        goodToKnowB.append(goodToKnowM.group() + "<br/>");
                    goodToKnow = goodToKnowB.append("!").toString().replace("<br/>!", "").replace("!", "").replace("<t>", "").replace("</t>", "");
                }


                if (content.contains("<custMaterials>")) {
                    beginIx = content.indexOf("<custMaterials>");
                    endIx = content.indexOf("</custMaterials>", beginIx);
                    StringBuilder custMaterialsB = new StringBuilder();
                    String custMaterialsS = content.substring(beginIx + "<custMaterials>".length(), endIx);
                    String custMaterialsT = "<cm>[\\s\\S]*?</cm>";
                    Pattern custMaterialsP = Pattern.compile(custMaterialsT);
                    Matcher custMaterialsM = custMaterialsP.matcher(custMaterialsS);
                    while (custMaterialsM.find())
                        custMaterialsB.append(custMaterialsM.group().replace("<pt>", "").replace("</pt>", "").replace("<t>", "").replace("</t>", "").replace("<m>", "").replace("</m>", "") + "<br/>");
                    custMaterials = custMaterialsB.append("!").toString().replace("<br/>!", "").replace("!", "").replace("<cm>", "").replace("</cm>", "");
                }

                if (content.contains("<measure>")) {
                    beginIx = content.indexOf("<measure>");
                    endIx = content.indexOf("</measure>", beginIx);
                    StringBuilder measureB = new StringBuilder();
                    String measureS = content.substring(beginIx + "<measure>".length(), endIx);
                    String measureT = "<m>[\\s\\S]*?</m>";
                    Pattern measureP = Pattern.compile(measureT);
                    Matcher measureM = measureP.matcher(measureS);
                    while (measureM.find())
                        measureB.append(measureM.group().replace("<d>", "").replace("</d>", ":").replace("<v>", "").replace("</v>", "") + "<br/>");
                    measure = measureB.append("!").toString().replace("<br/>!", "").replace("!", "").replace("<m>", "").replace("</m>", "");
                }


            }
        }
        String result = name + "!!" + facts + "!!" + careInst + "!!" + custBenefit + "!!" + designer + "!!" + environment + "!!" + goodToKnow + "!!" + custMaterials + "!!" + measure;
        return result;
    }

    public static ArrayList<String> getPicUrl(String buf) {
        ArrayList<String> largeA = new ArrayList<String>();

        if (buf.contains("<large>")) {
            int beginIx = buf.indexOf("<large>");
            int endIx = buf.indexOf("</large>", beginIx);
            String largeS = buf.substring(beginIx + "<large>".length(), endIx);
            String largeT = "<image height=\"500\" width=\"500\">[\\s\\S]*?</image>";
            Pattern largeP = Pattern.compile(largeT);
            Matcher largeM = largeP.matcher(largeS);
            while (largeM.find())
                largeA.add(largeM.group().replace("<image height=\"500\" width=\"500\">", "").replace("</image>", ""));
        }
        return largeA;
    }

    public static String getPrice(String buf) {
        String regexStr = "<prices>[\\s\\S]*?</prices>";
        Pattern productCell = Pattern.compile(regexStr);
        Matcher m = productCell.matcher(buf);
        String price = new String();
        while (m.find()) {
            int beginIx = buf.indexOf("<priceNormal unformatted=\"");
            int endIx = buf.indexOf("\"", beginIx + "<priceNormal unformatted=\"".length());
            price = buf.substring(beginIx + "<priceNormal unformatted=\"".length(), endIx);

        }
        return price;
    }


}
