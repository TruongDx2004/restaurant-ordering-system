package restaurant.project.order_table.service;

import java.math.BigDecimal;
import java.util.List;

import restaurant.project.order_table.entity.PaymentEntity;
import restaurant.project.order_table.entity.enums.PaymentMethod;
import restaurant.project.order_table.entity.enums.PaymentStatus;

public interface PaymentService {

    /**
     * Create a new payment
     *
     * @param payment payment data
     * @return created payment
     */
    PaymentEntity createPayment(PaymentEntity payment);

    /**
     * Get payment by ID
     *
     * @param id payment ID
     * @return payment entity
     */
    PaymentEntity getPaymentById(Long id);

    /**
     * Get all payments
     *
     * @return list of all payments
     */
    List<PaymentEntity> getAllPayments();

    /**
     * Update payment
     *
     * @param id payment ID
     * @param payment updated payment data
     * @return updated payment
     */
    PaymentEntity updatePayment(Long id, PaymentEntity payment);

    /**
     * Delete payment
     *
     * @param id payment ID
     */
    void deletePayment(Long id);

    /**
     * Get payment by invoice
     *
     * @param invoiceId invoice ID
     * @return payment entity
     */
    PaymentEntity getPaymentByInvoice(Long invoiceId);

    /**
     * Get payment by transaction code
     *
     * @param transactionCode transaction code
     * @return payment entity
     */
    PaymentEntity getPaymentByTransactionCode(String transactionCode);

    /**
     * Get payments by status
     *
     * @param status payment status
     * @return list of payments
     */
    List<PaymentEntity> getPaymentsByStatus(PaymentStatus status);

    /**
     * Get payments by method
     *
     * @param method payment method
     * @return list of payments
     */
    List<PaymentEntity> getPaymentsByMethod(PaymentMethod method);

    /**
     * Update payment status
     *
     * @param id payment ID
     * @param status new status
     * @return updated payment
     */
    PaymentEntity updatePaymentStatus(Long id, PaymentStatus status);

    /**
     * Process payment for invoice
     *
     * @param invoiceId invoice ID
     * @param method payment method
     * @param amount payment amount
     * @return created payment
     */
    PaymentEntity processPayment(Long invoiceId, PaymentMethod method, BigDecimal amount);

    /**
     * Confirm payment
     *
     * @param id payment ID
     * @param transactionCode transaction code
     * @return updated payment
     */
    PaymentEntity confirmPayment(Long id, String transactionCode);

    /**
     * Cancel payment
     *
     * @param id payment ID
     * @return updated payment
     */
    PaymentEntity cancelPayment(Long id);
}
