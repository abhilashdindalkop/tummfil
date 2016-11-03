package services;

import javax.inject.Inject;

import com.ecommerce.dao.TransactionsDAO;
import com.ecommerce.dto.request.CreateTransactionRequestDTO;
import com.ecommerce.models.sql.Orders;
import com.ecommerce.models.sql.Transactions;
import com.ecommerce.models.sql.UserSession;

import utils.MyConstants.CurrencyType;
import utils.MyConstants.FailureMessages;
import utils.MyConstants.PaymentStatus;
import utils.MyConstants.PaymentType;
import utils.MyConstants.TransactionStatus;
import utils.MyException;

public class PaymentService {

	TransactionsDAO transactionsDAO;

	@Inject
	public PaymentService(TransactionsDAO transactionsDAO) {
		this.transactionsDAO = transactionsDAO;
	}

	public Transactions createTransaction(CreateTransactionRequestDTO requestDTO) throws MyException {

		String userId = UserSession.getUserEncryptedIdByContext();

		Orders order = Orders.findById(requestDTO.orderId);

		if (!(order.getPaymentStatus() == PaymentStatus.PENDING || order.getPaymentStatus() == PaymentStatus.FAILURE)) {
			throw new MyException(FailureMessages.TRANSACTION_ALREADY_DONE);
		}

		if (!userId.equals(order.getUser().getEncryptedUserId())) {
			throw new MyException(FailureMessages.ORDER_DOESNT_BELONG_TO_USER);
		}

		Transactions newTransaction = null;
		switch (requestDTO.paymentType) {

		case PaymentType.COD:
			newTransaction = transactionsDAO.create(order, PaymentType.COD, CurrencyType.INR, order.getTotalPrice());
			if (newTransaction.getStatus() == TransactionStatus.PENDING) {
				order.updatePaymentTypeAndStatus(PaymentType.COD, PaymentStatus.PENDING);
			}
			break;
		case PaymentType.CARD:
			throw new MyException(FailureMessages.YET_TO_IMPLEMENT);
		case PaymentType.INTERNET_BANKING:
			throw new MyException(FailureMessages.YET_TO_IMPLEMENT);
		default:
			throw new MyException(FailureMessages.INVALID_PAYMENT_TYPE);
		}

		if (newTransaction == null) {
			throw new MyException(FailureMessages.TRANSACTION_FAILED);
		}
		return newTransaction;
	}
}
