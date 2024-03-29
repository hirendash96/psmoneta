/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.test.module;

import org.junit.Test;
import javax.persistence.EntityManager;
import java.util.Map;
import java.util.Properties;
import junit.framework.Assert;
import java.net.MalformedURLException;
import java.io.IOException;
import java.util.List;

import com.rsc.moneta.bean.Market;
import com.rsc.moneta.module.inputhandler.EPortInputHandler;
import com.rsc.moneta.bean.PaymentOrder;
import com.rsc.moneta.bean.PaymentOrderStatus;
import com.rsc.moneta.module.inputhandler.Const;
import com.rsc.moneta.dao.EMF;
import com.rsc.moneta.dao.Dao;
import com.rsc.moneta.dao.MarketDao;
import com.rsc.moneta.dao.PaymentOrderDao;
import com.rsc.moneta.dao.UserDao;
import com.rsc.moneta.test.TestConf;


/**
 *
 * @author Солодовников Д.А.
 */
public class TestEPortHandler {

    @Test
    // public void test0() throws MalformedURLException, IOException, ParserConfigurationException, SAXException {
    public void test0() throws MalformedURLException, IOException {
        TestConf.initConfig();
        new GenerateOrder().generateOrder();
        new AddOrderToTlsm().testAddOrderToTlsm("soloddemiurg@mail.ru");
        EntityManager em = EMF.getEntityManager();
        Market market = new MarketDao(em).getMarketByName("test");
        Assert.assertNotNull(market);
        List<PaymentOrder> orderList = new PaymentOrderDao(em).getAllPaymentOrderFilterByStatus(PaymentOrderStatus.ORDER_STATUS_ACCEPTED);
        for (PaymentOrder order : orderList) {
            System.out.println(order.getAccountId());
//            double val = order.getAccount().getBalance();
//            Assert.assertNull(order.getUser());
//            System.out.println(val);
//            String query = "http://localhost:8084/moneta/Platika.html?command=check&txn_id=" + getTxnId()
//                    + "&account=" + String.format("%019d", order.getId());
//            Document doc = execute(query);
//            Response response = parseResponse(doc);
//            Assert.assertNotNull(response.result);
//            Assert.assertEquals(OSMPInputHandler.OSMP_RETURN_CODE_OK, (int) response.result);
//            Assert.assertTrue(response.sum > 0);
//            query = "http://localhost:8084/moneta/Platika.html?command=check&txn_id=" + getTxnId()
//                    + "&account=" + String.format("%019d", order.getId()) + "&sum=" + response.sum;
//            doc = execute(query);
//            response = parseResponse(doc);
//            Assert.assertNotNull(response.result);
//            Assert.assertEquals(OSMPInputHandler.OSMP_RETURN_CODE_OK, (int) response.result);
//            Assert.assertTrue(response.sum > 0);
//            query = "http://localhost:8084/moneta/Platika.html?command=pay&txn_id=" + getTxnId()
//                    + "&account=" + String.format("%019d", order.getId()) + "&sum=" + response.sum
//                    + "&txn_date=" + df.format(new Date());
//            doc = execute(query);
//            response = parseResponse(doc);
//            Assert.assertNotNull(response);
//            Assert.assertNotNull(response.result);
//            Assert.assertEquals((int) response.result, OSMPInputHandler.OSMP_RETURN_CODE_OK);
//            em.refresh(order.getAccount());
//            Assert.assertEquals(val + order.getAmount(), order.getAccount().getBalance(), 0);
        }
    }
    
    /*
     * Тест-метод запроса "check" - платеж уже завершён
     */
//    @Test
//    public void testEPortCheckForPaidAndCompletedOrder() {
//        // 1. Создать тестовый ИМ-н, записать его в БД
//        EntityManager em = EMF.getEntityManager();
//        Market market = new MarketDao(em).getMarketByName("testMarket2");
//        if (market == null) {
//            market = new Market();
//            market.setName("testMarket2");
//            Assert.assertNotNull(new UserDao(em).getUserByPhone("test"));
//            market.setUser(new UserDao(em).getUserByPhone("test"));
//            market.setCheckUrl("http://localhost:8084/testIM/Check");
//            market.setFailUrl("http://localhost:8084/testIM/fail.jsp");
//            market.setPayUrl("http://localhost:8084/testIM/Pay");
//            market.setSignable(true);
//            market.setSuccessUrl("http://localhost:8084/testIM/success.jsp");
//            market.setPassword("12345");
//            market.setOutputHandlerType(0);
////            new Dao(em).persist(market);
////            Vector vec = new Vector();
////            vec.addAll(market.getUser().getAccounts());
////            market.setAccounts(vec);
//            new Dao(em).persist(market);
//        }
//
//        // 2. Создать запись о заказе в т-це PaymentOrder, выставить заказу
//        // статус "ORDER_STATUS_PAID_AND_COMPLETED"
//        PaymentOrder paymentOrder = new PaymentOrder();
//        paymentOrder.setStatus(PaymentOrderStatus.ORDER_STATUS_PAID_AND_COMPLETED);
//        paymentOrder.setTest(Boolean.TRUE);
//        paymentOrder.setMarket(market);
//        new Dao(em).persist(paymentOrder);
//        em.close();
//
//        // 3. Сэмулировать тестовый запрос "check" к EPort-хендлеру по созданному
//        // в п.1 номеру заказа
//        long orderId = paymentOrder.getId();
//        String account = String.format("%019d", orderId);
//        String timestamp = "2010-05-04T16:15:45";
//        EPortInputHandler handler = new EPortInputHandler();
//        Map map = new Properties();
//
//        /**
//         *Поля запроса:
//         *account - Идентификатор абонента
//         *sum - Сумма платежа
//         *timestamp - Дата/время формирования запроса
//         *Поля ответа:
//         *result - результат (допустимы E*, S1, F1, F2, F3, F4)
//         *reason - уточняющий комментарий отказа для СП (не обязателен)
//         *account - копия из запроса
//         *sum - копия из запроса
//         *timestamp - копия из запроса
//         * @param inputData
//         * @return
//         */
//
//        map.put("type", "check");
//        map.put("account", account);
//        map.put("timestamp", timestamp);
//        String response = handler.check(map);
//
//        // 4. Удалить созданную в п.1 запись о заказе
//        // TODO: Денис - что-то не даёт нормально удалять
//        em = EMF.getEntityManager();
//        paymentOrder = new PaymentOrderDao(em).getPaymentOrderById(orderId);
//        new Dao(em).remove(paymentOrder);
//        em.close();
//
//
////    case ResultCode.ORDER_ALREADY_PAID:
////                                                    result = EPORT_RETURN_CODE_F4;
////                                                    reason = Const.STRING_ORDER_PAID_AND_COMPLETED;
//
//        // 5. Сравнить полученный response с планируемым
//        Assert.assertEquals("account=" + account + "&"
//                + "reason=" + Const.STRING_ORDER_PAID_AND_COMPLETED + "&"
//                + "result=" + EPortInputHandler.EPORT_RETURN_CODE_F4 + "&"
//                + "timestamp=" + timestamp,
//                response);
//
//    }
}
