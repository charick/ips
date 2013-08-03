package TOI.util;

import com.taobao.api.domain.Trade;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.tools.generic.DateTool;
import org.apache.velocity.tools.generic.MathTool;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: W.k
 * Date: 13-7-24
 * Time: 下午8:31
 * To change this template use File | Settings | File Templates.
 */
public class OrderPrinter {
    public static String getField="pay_time,status,tid,buyer_nick,total_fee,post_fee,receiver_state";
    public static void generateOrderFile(String start, String end){
        String[] statuses={TaobaoUtils.WAIT_SELLER_SEND_GOODS,TaobaoUtils.SELLER_CONSIGNED_PART,
                TaobaoUtils.WAIT_BUYER_CONFIRM_GOODS,TaobaoUtils.TRADE_FINISHED};
        List<Trade> allTrades=new ArrayList<Trade>();
        for(String status:statuses){
            List<Trade> trades= TaobaoUtils.TradeFilter(start, end, status, getField) ;
            if(trades!=null)      allTrades.addAll(trades);
        }

        HashMap<String,OrderItem>  orderMap=new HashMap<String,OrderItem>();
        for(Trade trade:allTrades){
            if(orderMap.containsKey(trade.getBuyerNick())){
                OrderItem order=orderMap.get(trade.getBuyerNick())   ;
                order.setTid(order.getTid()+","+ trade.getTid());
                order.setTotalFee(order.getTotalFee()+trade.getTotalFee());
                order.setPostFee(order.getPostFee()+trade.getPostFee());
                order.setStatus(order.getStatus()+","+ trade.getStatus());
            }   else {
                OrderItem oi=new OrderItem();
                oi.setBuyerNick(trade.getBuyerNick());
                oi.setTotalFee(trade.getTotalFee());
                oi.setTid(trade.getTid()+"");
                oi.setPostFee(trade.getPostFee());
                oi.setStatus(trade.getStatus());
                orderMap.put(trade.getBuyerNick(),oi);
            }
        }

        VelocityContext context = new VelocityContext();
        context.put("allTrades", allTrades);
        context.put("date",new DateTool());
        context.put("math", new MathTool());
        String result = VelocityUtil.filterVM("order.vm", context);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(new File("E:\\发货清单\\交易清单.html"));
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

    static class OrderItem{
       public String payTime;// 成交时间

        String getPayTime() {
            return payTime;
        }

        void setPayTime(String payTime) {
            this.payTime = payTime;
        }

        String getStatus() {
            return status;
        }

        void setStatus(String status) {
            this.status = status;
        }

        String getTid() {
            return tid;
        }

        void setTid(String tid) {
            this.tid = tid;
        }

        String getBuyerNick() {
            return buyerNick;
        }

        void setBuyerNick(String buyerNick) {
            this.buyerNick = buyerNick;
        }

        String getTotalFee() {
            return totalFee;
        }

        void setTotalFee(String totalFee) {
            this.totalFee = totalFee;
        }

        String getPostFee() {
            return postFee;
        }

        void setPostFee(String postFee) {
            this.postFee = postFee;
        }

        String getReceiverState() {
            return receiverState;
        }

        void setReceiverState(String receiverState) {
            this.receiverState = receiverState;
        }

        public String status;//        状态
        public String tid;
        public String buyerNick ; // 买家
        public String totalFee; //商品价格
        public String postFee ;   //    运费
        public String  receiverState;



    }

    public static void main(String[] args)
    {
         OrderPrinter.generateOrderFile("2013-07-26", "2013-07-26");
    }

}
