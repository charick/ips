package TOI.web;

import TOI.model.Product;
import TOI.util.ProductUtils;
import TOI.util.TaobaoUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AddItem2TBServlet extends HttpServlet {
    private static String resultPage = "/result.jsp";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String itemCode = request.getParameter("itemCode");

        // /////////////////////更新product表内一个产品/////////////////////////
        Product p = ProductUtils.grabProductFromIKEA(itemCode);
        ProductUtils.addToSQL(p, 1);
        // ////////////////////从SQL获取一个产品/////////////////////////
        // Product p = ProductUtils.setFromSQL("S69932184");
        // //////////////////////上传至淘宝/////////////////////////
        TaobaoUtils.addTaobaoItem(p);
        TaobaoUtils.uploadExtraPic(p);

        ServletContext context = getServletContext();


        RequestDispatcher dispatcher = context.getRequestDispatcher(resultPage);
        dispatcher.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
