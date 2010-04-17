/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsc.moneta.bean;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;

/**
 *
 * @author sulic
 * Это любая транзакция внутри системы.
 * Пока незадействовано.
 * Будет задействовано, когда пользователи будут друг другу проводить платежи.
 */
@Entity
@SequenceGenerator(
    name="seq_payment_transaction",
    sequenceName="seq_payment_transaction"
)
public class PaymentTransaction implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator="seq_payment_transaction")
    private Long id;

    @Column
    private Float amount;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    @Column(name = "_date")
    private Date date;

    @ManyToOne
    private User fromUser;

    @ManyToOne
    private User toUser;




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    

}
