package com.renta.renta_driver.view;

import com.renta.renta_driver.model.transaction.Transaction;

import java.util.List;

public interface OffersView {

    void onGetAllAvailableTransactionSuccess(List<Transaction> transactionList);
    void onGetAllAvailableTransactionError();
    void onNoTransactionsPending();

    void onUpdateTransactionSuccess();
    void onUpdateTransactionError();
}
