package services;

import javax.inject.Inject;

import com.ecommerce.dao.TransactionsDAO;
import com.ecommerce.dto.request.CreateTransactionRequestDTO;
import com.ecommerce.models.sql.Orders;
import com.ecommerce.models.sql.Transactions;
import com.ecommerce.models.sql.UserSession;

import integrations.fcm.TummfilNotificationMessages;
import utils.MyConstants.CurrencyType;
import utils.MyConstants.FailureMessages;
import utils.MyConstants.OrderStatus;
import utils.MyConstants.PaymentStatus;
import utils.MyConstants.PaymentType;
import utils.MyConstants.TransactionStatus;
import utils.MyException;

public class PaymentService {

	TransactionsDAO transactionsDAO;
	TummfilNotificationMessages tummfilNotificationMessages;

	@Inject
	public PaymentService(TransactionsDAO transactionsDAO, TummfilNotificationMessages townNotificationMessages) {
		this.transactionsDAO = transactionsDAO;
		this.tummfilNotificationMessages = townNotificationMessages;
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
			/* Exception case for COD - because Payment Status : PENDING */
			if (transactionsDAO.findByOrderId(order, TransactionStatus.SUCCESS) != null) {
				throw new MyException(FailureMessages.TRANSACTION_ALREADY_DONE);
			}
			newTransaction = transactionsDAO.create(order, PaymentType.COD, CurrencyType.INR);
			if (newTransaction.getStatus() == TransactionStatus.SUCCESS) {
				order.setStatus(OrderStatus.PENDING);
				order.updatePaymentTypeAndStatus(PaymentType.COD, PaymentStatus.PENDING);
				tummfilNotificationMessages.placeOrderMessage(order);
			}
			break;
		case PaymentType.CARD:
			throw new MyException(FailureMessages.YET_TO_IMPLEMENT);
		case PaymentType.INTERNET_BANKING:
			throw new MyException(FailureMessages.YET_TO_IMPLEMENT);
		default:
			throw new MyException(FailureMessages.INVALID_PAYMENT_TYPE);
		}

		return newTransaction;
	}
}
