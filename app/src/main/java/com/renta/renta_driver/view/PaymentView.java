package com.renta.renta_driver.view;

import com.renta.renta_driver.model.payment.Payment;

public interface PaymentView {

    void onGetPaymentSuccess(Payment payment);
    void onGetPaymentError();
}
