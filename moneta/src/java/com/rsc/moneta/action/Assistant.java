/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsc.moneta.action;

import com.rsc.moneta.bean.Market;
import com.opensymphony.xwork2.Action;
import com.rsc.moneta.Config;
import com.rsc.moneta.dao.Dao;
import com.rsc.moneta.bean.PaymentOrder;
import com.rsc.moneta.bean.PaymentOrderStatus;
import com.rsc.moneta.bean.User;
import com.rsc.moneta.dao.MarketDao;
import com.rsc.moneta.dao.PaymentOrderDao;
import com.rsc.moneta.dao.UserDao;
import com.rsc.moneta.util.Utils;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;
import org.apache.struts2.ServletActionContext;

/**
 *
 * @author sulic
 */
public class Assistant extends BaseAction {

    private Long MNT_ID = null;
    private String MNT_TRANSACTION_ID = null;
    private String MNT_CURRENCY_CODE = null;
    private Double MNT_AMOUNT = null;
    private Boolean MNT_TEST_MODE = false;
    private String MNT_DESCRIPTION = null;
    private String MNT_SIGNATURE = null;
    private String MNT_CUSTOM1 = null;
    private String MNT_CUSTOM2 = null;
    private String MNT_CUSTOM3 = null;
    private String MNT_SUCCESS_URL = null;
    private String MNT_FAIL_URL = null;
    private String monetaLocale = null;
    private String paymentSystemUnitId = null;
    private String paymentSystemLimitIds = null;
    private PaymentOrder paymentOrder;
    private Market market;
    private boolean _new;
    private String paymentOrderId;
    private Assistant contact = this;
    private Assistant moneta = this;
    private Assistant paymentSystem = this;
    private String locale;
    private String unitId;
    private String limitIds;
    private String name;
    private String email;
    private String phone;
    private String webmoneyAccount;


    @Override
    public String execute() throws Exception {
        // TODO: Проверить ситуацию когда невозможно создания счетов для пользователя
        // при ситуации когда например последовательность для аккаунт выдает уже суще
        // ствующие значения. При этом пользователь получает доступ к номеру заказа. Почему ???

        if (locale != null){
            try{
                ServletActionContext.getContext().setLocale(new Locale(this.locale));
            }catch(Exception exception){
                Logger.getLogger(Assistant.class.getName()).severe("Cannot set locale = "+ this.locale);
                exception.printStackTrace();
            }
        }

        if (MNT_ID == null) {
            addActionError(getText("MNT_ID_not_defined"));
            return Action.ERROR;
        }
        market = em.find(Market.class, MNT_ID);
        if (market == null) {
            addActionError(getText("market_not_found"));
            return Action.ERROR;
        }

        if (!new MarketDao(em).isMarketHaveAccount(Utils.currencyStringToAccountType(MNT_CURRENCY_CODE), market)) {
            addActionError(getText("market_not_have_account"));
            return Action.ERROR;
        }
        if (MNT_SIGNATURE != null && market.isSignable()) {
            if (checkSignature()) {
                addActionError(getText("invalid_signature"));
                return Action.ERROR;
            }
        }
        if (MNT_TRANSACTION_ID == null) {
            addActionError(getText("MNT_TRANSACTION_ID_not_defined"));
            return Action.ERROR;
        }
        if (MNT_TRANSACTION_ID.length() > 255) {
            addActionError(getText("MNT_TRANSACTION_ID_longer_than_255"));
            return Action.ERROR;
        }
        if (MNT_CURRENCY_CODE == null) {
            addActionError(getText("MNT_CURRENCY_CODE_not_defined"));
            return Action.ERROR;
        }
        if (MNT_AMOUNT == null) {
            addActionError(getText("MNT_AMOUNT_not_defined"));
            return Action.ERROR;
        }

        paymentOrder = new PaymentOrderDao(em).getPaymentOrder(MNT_TRANSACTION_ID, MNT_ID);
        if (paymentOrder != null) {
            if (paymentOrder.getStatus() == PaymentOrderStatus.ORDER_STATUS_ACCEPTED) {
                return Action.SUCCESS;
            } else {
                addActionError(getText("order_exists"));
                return Action.ERROR;
            }
        }

        paymentOrder = new PaymentOrder();
        paymentOrder.setDate(new Date(System.currentTimeMillis()));
        paymentOrder.setAmount(MNT_AMOUNT);
        paymentOrder.setTransactionId(MNT_TRANSACTION_ID);
        paymentOrder.setMarket(market);
        paymentOrder.setCustom1(MNT_CUSTOM1);
        paymentOrder.setCustom2(MNT_CUSTOM2);
        paymentOrder.setCustom3(MNT_CUSTOM3);
        paymentOrder.setDescription(MNT_DESCRIPTION);
        paymentOrder.setFailUrl(MNT_FAIL_URL);
        paymentOrder.setCurrency(Utils.currencyStringToAccountType(MNT_CURRENCY_CODE));
        paymentOrder.setAccount(market.getAccount(paymentOrder.getCurrency()));
        if (MNT_SUCCESS_URL != null) {
            paymentOrder.setSuccessUrl(MNT_SUCCESS_URL);
        } else {
            paymentOrder.setSuccessUrl(market.getSuccessUrl());
        }
        paymentOrder.setPaymentSystemLimitIds(limitIds);
        paymentOrder.setPaymentSystemUnitId(unitId);
        paymentOrder.setTest(MNT_TEST_MODE);
        String result = Action.SUCCESS;
        if (email != null) {
            // mail указан
            UserDao dao = new UserDao(em);
            User u = dao.getUserByEmail(email);
            if (u != null) {
                // пользователь существует
                addActionMessage(getText("you_are_already_registred"));
                _new = false;
            } else {
                // пользователь не существует
                u = dao.createUserAndSendNotify(phone, name, email);
                paymentOrder.setUser(u);
                webmoneyAccount = Config.getWebmoneyAccount(paymentOrder.getCurrency());
                addActionMessage(getText("you_was_success_registred"));
                result = "next";
                _new = true;
            }
        }else{
            //mail не указан
            _new = false;
        }
        if (!new Dao(em).persist(paymentOrder)) {
            addActionError(getText("dbms_save_error"));
            return Action.ERROR;
        }
        if (_new){
            paymentOrderId = String.format("%019d", paymentOrder.getId());
        }
        return result;
    }

    public String getWebmoneyAccount() {
        return webmoneyAccount;
    }

    public void setWebmoneyAccount(String webmoneyAccount) {
        this.webmoneyAccount = webmoneyAccount;
    }

    public Assistant getContact() {
        return contact;
    }

    public void setContact(Assistant contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLimitIds() {
        return limitIds;
    }

    public void setLimitIds(String limitIds) {
        this.limitIds = limitIds;
    }    

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public Assistant getMoneta() {
        return moneta;
    }

    public void setMoneta(Assistant moneta) {
        this.moneta = moneta;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Assistant getPaymentSystem() {
        return paymentSystem;
    }

    public void setPaymentSystem(Assistant paymentSystem) {
        this.paymentSystem = paymentSystem;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public PaymentOrder getPaymentOrder() {
        return paymentOrder;
    }

    public void setPaymentOrder(PaymentOrder paymentOrder) {
        this.paymentOrder = paymentOrder;
    }

    public String getPaymentOrderId() {
        return paymentOrderId;
    }

    public void setPaymentOrderId(String paymentOrderId) {
        this.paymentOrderId = paymentOrderId;
    }

    public boolean isNew() {
        return _new;
    }

    public void setNew(boolean _new) {
        this._new = _new;
    }

    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    public PaymentOrder getPaymentKey() {
        return paymentOrder;
    }

    public void setPaymentKey(PaymentOrder paymentKey) {
        this.paymentOrder = paymentKey;
    }

    public Double getMNT_AMOUNT() {
        return MNT_AMOUNT;
    }

    public void setMNT_AMOUNT(String MNT_AMOUNT) {
        this.MNT_AMOUNT = Double.parseDouble(MNT_AMOUNT);
    }

    public String getMNT_CURRENCY_CODE() {
        return MNT_CURRENCY_CODE;
    }

    public void setMNT_CURRENCY_CODE(String MNT_CURRENCY_CODE) {
        this.MNT_CURRENCY_CODE = MNT_CURRENCY_CODE;
    }

    public String getMNT_CUSTOM1() {
        return MNT_CUSTOM1;
    }

    public void setMNT_CUSTOM1(String MNT_CUSTOM1) {
        this.MNT_CUSTOM1 = MNT_CUSTOM1;
    }

    public String getMNT_CUSTOM2() {
        return MNT_CUSTOM2;
    }

    public void setMNT_CUSTOM2(String MNT_CUSTOM2) {
        this.MNT_CUSTOM2 = MNT_CUSTOM2;
    }

    public String getMNT_CUSTOM3() {
        return MNT_CUSTOM3;
    }

    public void setMNT_CUSTOM3(String MNT_CUSTOM3) {
        this.MNT_CUSTOM3 = MNT_CUSTOM3;
    }

    public String getMNT_DESCRIPTION() {
        return MNT_DESCRIPTION;
    }

    public void setMNT_DESCRIPTION(String MNT_DESCRIPTION) {
        this.MNT_DESCRIPTION = MNT_DESCRIPTION;
    }

    public String getMNT_FAIL_URL() {
        return MNT_FAIL_URL;
    }

    public void setMNT_FAIL_URL(String MNT_FAIL_URL) {
        this.MNT_FAIL_URL = MNT_FAIL_URL;
    }

    public Long getMNT_ID() {
        return MNT_ID;
    }

    public void setMNT_ID(Long MNT_ID) {
        this.MNT_ID = MNT_ID;
    }

    public String getMNT_SIGNATURE() {
        return MNT_SIGNATURE;
    }

    public void setMNT_SIGNATURE(String MNT_SIGNATURE) {
        this.MNT_SIGNATURE = MNT_SIGNATURE;
    }

    public String getMNT_SUCCESS_URL() {
        return MNT_SUCCESS_URL;
    }

    public void setMNT_SUCCESS_URL(String MNT_SUCCESS_URL) {
        this.MNT_SUCCESS_URL = MNT_SUCCESS_URL;
    }

    public Boolean getMNT_TEST_MODE() {
        return MNT_TEST_MODE;
    }

    public void setMNT_TEST_MODE(int MNT_TEST_MODE) {
        this.MNT_TEST_MODE = (MNT_TEST_MODE != 0);
    }

    public String getMNT_TRANSACTION_ID() {
        return MNT_TRANSACTION_ID;
    }

    public void setMNT_TRANSACTION_ID(String MNT_TRANSACTION_ID) {
        this.MNT_TRANSACTION_ID = MNT_TRANSACTION_ID;
    }

    public String getMonetaLocale() {
        return monetaLocale;
    }

    public void setMonetaLocale(String monetaLocale) {
        this.monetaLocale = monetaLocale;
    }

    public String getPaymentSystemLimitIds() {
        return paymentSystemLimitIds;
    }

    public void setPaymentSystemLimitIds(String paymentSystemLimitIds) {
        this.paymentSystemLimitIds = paymentSystemLimitIds;
    }

    public String getPaymentSystemUnitId() {
        return paymentSystemUnitId;
    }

    public void setPaymentSystemUnitId(String paymentSystemUnitId) {
        this.paymentSystemUnitId = paymentSystemUnitId;
    }

    private boolean checkSignature() throws NoSuchAlgorithmException {
        int test = (MNT_TEST_MODE) ? 0 : 1;
        String all = MNT_ID + MNT_TRANSACTION_ID + MNT_AMOUNT + MNT_CURRENCY_CODE + test + market.getPassword();
        return (MNT_SIGNATURE.equalsIgnoreCase(Utils.byteArrayToHexString(Utils.md5(all))));
    }
}


