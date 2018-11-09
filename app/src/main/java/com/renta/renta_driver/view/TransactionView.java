package com.renta.renta_driver.view;


import com.renta.renta_driver.model.transaction.Transaction;

import java.util.List;

public interface TransactionView {

    void onGetTransactionViewSuccess(List<Transaction> transactionList);

    void onNoTransaction();

    void onGetTransactionViewError();

    void onTransactionStatusUpdateSuccess();

    void onTransactionStatusUpdateError();

    void onGetTransactions(Transaction transaction);

    void onGetTransaction(Transaction transaction);

    void onGetTransactionError();
}
