import React from 'react';
import styles from './PaymentSummary.module.css';

const PaymentSummary = ({ items = [] }) => {

	const subtotal = items.reduce((sum, item) => {
		return sum + (item.totalPrice || item.quantity * item.unitPrice || 0);
	}, 0);

	const total = subtotal;

	const formatCurrency = (amount) => {
		return new Intl.NumberFormat('vi-VN', {
			style: 'currency',
			currency: 'VND'
		}).format(amount);
	};

	return (
		<div className={styles.summary}>
			<h2>Tóm tắt đơn hàng</h2>

			{items.map(item => (
				<div key={item.id} className={styles.item}>
					<span>{item.dish?.name}</span>
					<span>{formatCurrency(item.totalPrice)}</span>
				</div>
			))}

			<div className={styles.total}>
				<span>Tổng cộng:</span>
				<span>{formatCurrency(total)}</span>
			</div>
		</div>
	);
};

export default PaymentSummary;