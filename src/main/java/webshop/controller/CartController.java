package webshop.controller;

import webshop.dao.CartDao;
import webshop.dao.DataManager;
import webshop.config.TemplateEngineUtil;
import webshop.dao.LineItemDao;
import webshop.model.Cart;
import webshop.model.LineItem;
import webshop.model.User;
import webshop.util.Error;
import webshop.util.Util;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/cart"})
public class CartController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        CartDao cartDataStore = DataManager.getCartDao();

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        User user = (User) req.getSession().getAttribute("user");
        if (user != null) {
            Cart cart = cartDataStore.find(user.getCartId());
            if (cart != null) {
                context.setVariable("cart", cart);
                engine.process("product/cart.html", context, resp.getWriter());
                return;
            }
        }

        resp.sendRedirect("/");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        LineItemDao lineItemDao = DataManager.getLineItemDao();
        int lineItemId = Integer.parseInt(req.getParameter("line-item-id"));
        LineItem lineItem = lineItemDao.find(lineItemId);

        switch (req.getParameter("edit")) {
            case "increase":
                lineItemDao.update(lineItem, lineItem.getQuantity() + 1);
                break;
            case "decrease":
                if (lineItem.getQuantity() > 1) {
                    lineItemDao.update(lineItem, lineItem.getQuantity() - 1);
                } else {
                    lineItemDao.remove(lineItem);
                }
                break;
            case "clear":
                lineItemDao.remove(lineItem);
                break;
            default:
                Util.handleError(req, resp, HttpServletResponse.SC_BAD_REQUEST, Error.INVALID_CART_OPERATION);
                return;
        }

        resp.sendRedirect("/cart");
    }
}