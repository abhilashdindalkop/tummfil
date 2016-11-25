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
			throw new MyException(FailureMessages.TRANSACTION_DOESNT_EXIST);
		}
		return transaction;
	}

	public Transactions findByOrderId(Orders order, int transactionStatus) throws MyException {
		Transactions transaction = Ebean.find(Transactions.class).where().eq("order", order)
				.eq("status", transactionStatus).findUnique();
		return transaction;
	}

	public Transactions create(Orders order, int paymentType, int currency) throws MyException {

		if (order.getTotalPrice() <= 0) {
			throw new MyException(FailureMessages.INVALID_TRANSACTION);
		}
		Transactions newTransaction = new Transactions();

		newTransaction.setEncryptedId(UUID.randomUUID().toString());
		newTransaction.setTransactionId(GenericUtils.generateTransactionId());
		newTransaction.setOrder(order);
		newTransaction.setPaymentType(paymentType);
		newTransaction.setStatus(TransactionStatus.SUCCESS);
		newTransaction.setCurrency(currency);
		newTransaction.setCreatedTime(new Date());
		newTransaction.setAmount(order.getTotalPrice());
		newTransaction.save();
		return newTransaction;
	}
}
