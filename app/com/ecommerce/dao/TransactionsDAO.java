package com.ecommerce.dao;

import java.util.Date;
import java.util.UUID;

import com.avaje.ebean.Ebean;
import com.ecommerce.models.sql.Orders;
import com.ecommerce.models.sql.Transactions;

import utils.GenericUtils;
import utils.MyConstants.FailureMessages;
import utils.MyConstants.TransactionStatus;
import utils.MyException;

public class TransactionsDAO {

	public Transactions findById(String transactionId) throws MyException {
		Transactions transaction = Ebean.find(Transactions.class).where().eq("trasaction_id", transactionId)
				.findUnique();
		if (transaction == null) {
			throw new MyException(FailureMessages.DELIVERY_BOY_DOESNT_EXIST);
		}
		return transaction;
	}

	public Transactions create(Orders order, int paymentType, int currency, double amount) throws MyException {

		if (amount > 0) {
			throw new MyException(FailureMessages.INVALID_TRANSACTION);
		}
		Transactions newTransaction = new Transactions();

		newTransaction.setEncryptedId(UUID.randomUUID().toString());
		newTransaction.setTransactionId(GenericUtils.generateTransactionId());
		newTransaction.setOrder(order);
		newTransaction.setPaymentType(paymentType);
		newTransaction.setStatus(TransactionStatus.PENDING);
		newTransaction.setCurrency(currency);
		newTransaction.setCreatedTime(new Date());
		newTransaction.setAmount(amount);
		newTransaction.save();
		return newTransaction;
	}
}
