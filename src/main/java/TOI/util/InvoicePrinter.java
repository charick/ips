package TOI.util;

import TOI.Constant.Constant;
import TOI.model.TradeItem;
import com.taobao.api.domain.Trade;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.tools.generic.MathTool;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: W.k
 * Date: 13-7-25
 * Time: 下午10:18
 * To change this template use File | Settings | File Templates.
 */
public class InvoicePrinter {
    public static void printInvoice(List<String> InvoiceList) {
        VelocityContext context = new VelocityContext();
        context.put("InvoiceList", InvoiceList);
        context.put("math", new MathTool());
        String result = VelocityUtil.filterVM("invoices.vm", context);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(new File("E:\\发货清单\\00清单.html"));
            out.write(result.getBytes("UTF-8"));
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private static List<String> generateInvoiceList(List<Trade> tradeList){
        Set<String> buyerSet = new HashSet<String>();
        for (Trade trade : tradeList) {
            buyerSet.add(trade.getBuyerNick());
        }
        List<String> invoiceList=new ArrayList<String>();
        for (String buyer : buyerSet) {
            List<Trade> trade1buyer = new ArrayList<Trade>();
            for (Trade trade : tradeList) {
                if (trade.getBuyerNick().equals(buyer))
                    trade1buyer.add(trade);
            }
            invoiceList.add(generateSingleInvoice(trade1buyer));

        }
       return  invoiceList;
    }

public static String generateSingleInvoice(List<Trade> trade1buyer){
    String buyer=trade1buyer.get(0).getBuyerNick();
    String tel= trade1buyer.get(0).getReceiverMobile();
    String receiver= trade1buyer.get(0).getReceiverName();
    List<TradeItem> tradeItems=Fahuo.generateTradeItems(trade1buyer);
    List<TradeItem> tradeItemList=enrichTradeItem(tradeItems);

    VelocityContext context = new VelocityContext();
    context.put("buyer", buyer);
    context.put("tradeItemList", tradeItemList);
    context.put("tel", tel);
    context.put("receiver", receiver);
    context.put("math", new MathTool());
    String result = VelocityUtil.filterVM("singleInvoice.vm", context);
    return result;
}
//        Map<String,Trade> buyerTradeMap=new HashMap<String, Trade>();
//        for (Trade trade : tradeList) {
//                if (buyerTradeMap.containsKey(trade.getBuyerNick())) {
//                    String memoOld=buyerTradeMap.get(trade.getBuyerNick()).getSellerMemo();
//                    String memoNew=trade.getSellerMemo();
//                    buyerTradeMap.put(trade.getBuyerNick(), trade);
//                } else {
//                    buyerTradeMap.put(trade.getBuyerNick(), trade);
////                }
//                memoList.add(trade.getSellerMemo());
//        }
//
//    }
public static List<TradeItem> enrichTradeItem(List<TradeItem> tradeItems)
{
    List tradeItemList = new ArrayList();
    for (TradeItem tradeItem : tradeItems) {
        String sql = "select * from  " + Constant.item_sql + " where pid='" + tradeItem.id + "'";
        try {
            Statement stmt = SQLUtils.getConnection().createStatement();
            ResultSet rs = null;
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                tradeItem.name = rs.getString("name");
                tradeItem.facts=rs.getString("facts");
                tradeItem.price=rs.getString("price");
                tradeItem.type = rs.getString("type");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tradeItemList.add(tradeItem);
    }
    return tradeItemList;
}
    public static void main(String[] args){
        ArrayList<Trade> tradeList = Fahuo.TradeFilter("2013-07-25  00:00:00", "2013-08-02  23:59:59");
        List<String> InvoiceList= generateInvoiceList(tradeList);
        printInvoice(InvoiceList);
        System.out.println();

    }
}
