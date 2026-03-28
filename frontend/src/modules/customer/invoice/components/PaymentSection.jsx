import React, { useState } from 'react';
import styles from './PaymentSection.module.css';
import useMomoPayment from '../../payment/hooks/useMomoPayment';

/**
 * PaymentSection Component
 * Hiển thị thông tin thanh toán và nút thanh toán
 */
export const PaymentSection = ({
	invoice,
	items,
	onPayment,
	isProcessing
}) => {
	const [paymentMethod, setPaymentMethod] = useState('CASH');

	// 👉 Hook MoMo
	const { createMomoPayment, loading: momoLoading } = useMomoPayment();

	/**
	 * Format currency
	 */
	const formatCurrency = (amount) => {
		return new Intl.NumberFormat('vi-VN', {
			style: 'currency',
			currency: 'VND'
		}).format(amount);
	};

	/**
	 * Calculate totals
	 */
	const calculateTotals = () => {
		const subtotal = items.reduce((sum, item) => sum + (item.totalPrice || 0), 0);
		const serviceFee = subtotal * 0.1;
		const total = invoice?.totalAmount || (subtotal + serviceFee);

		return { subtotal, serviceFee, total };
	};

	const { subtotal, serviceFee, total } = calculateTotals();

	/**
	 * Check if all items are ready
	 */
	const hasPendingItems = items && items.some(item =>
		item.status === 'WAITING' || item.status === 'PREPARING'
	);

	const canPay = items && items.length > 0 && !isProcessing && !hasPendingItems;

	/**
	 * Handle payment
	 */
	const handlePayment = async () => {
		if (!canPay) return;

		// 👉 Nếu chọn MoMo → gọi API
		if (paymentMethod === 'MOMO') {
			await createMomoPayment(invoice);
			return;
		}

		// 👉 Thanh toán thường (CASH)
		if (onPayment) {
			onPayment(paymentMethod, total);
		}
	};

	return (
		<div className={styles.paymentSection}>
			{/* Payment Summary */}
			<div className={styles.summary}>
				<h3>Tổng kết</h3>

				<div className={styles.summaryRow}>
					<span>Tạm tính:</span>
					<span>{formatCurrency(subtotal)}</span>
				</div>

				<div className={`${styles.summaryRow} ${styles.total}`}>
					<span>Tổng cộng:</span>
					<span>{formatCurrency(total)}</span>
				</div>
			</div>

			{/* Payment Method */}
			<div className={styles.paymentMethod}>
				<h3>Phương thức thanh toán</h3>

				<div className={styles.methodOptions}>
					{/* CASH */}
					<label className={styles.methodOption}>
						<input
							type="radio"
							name="paymentMethod"
							value="CASH"
							checked={paymentMethod === 'CASH'}
							onChange={(e) => setPaymentMethod(e.target.value)}
						/>
						<div className={styles.methodInfo}>
							<i className="fas fa-money-bill-wave"></i>
							<span>Tiền mặt</span>
						</div>
					</label>

					{/* MOMO */}
					<label className={styles.methodOption}>
						<input
							type="radio"
							name="paymentMethod"
							value="MOMO"
							checked={paymentMethod === 'MOMO'}
							onChange={(e) => setPaymentMethod(e.target.value)}
						/>
						<div className={styles.methodInfo}>
							<i className="fas fa-wallet"></i>
							<span>MoMo</span>
						</div>
					</label>
				</div>
			</div>

			{/* Payment Button */}
			<button
				className={styles.paymentButton}
				onClick={handlePayment}
				disabled={!canPay || momoLoading}
			>
				{isProcessing || momoLoading ? (
					<>
						<i className="fas fa-spinner fa-spin"></i>
						<span>Đang xử lý...</span>
					</>
				) : (
					<>
						<i className="fas fa-check-circle"></i>
						<span>Thanh toán {formatCurrency(total)}</span>
					</>
				)}
			</button>

			{/* Warning */}
			{!canPay && !isProcessing && (
				<p className={styles.warning}>
					<i className="fas fa-info-circle"></i>
					{items.length === 0
						? 'Chưa có món ăn nào để thanh toán'
						: 'Vui lòng đợi tất cả món đã giao'}
				</p>
			)}
		</div>
	);
};

export default PaymentSection;