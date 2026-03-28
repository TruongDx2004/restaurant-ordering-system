import React from 'react';
import { useLocation } from 'react-router-dom';
import styles from './PaymentSuccess.module.css';

const PaymentSuccess = () => {
	const location = useLocation();

	const params = new URLSearchParams(location.search);
	const orderId = params.get('orderId');
	const resultCode = params.get('resultCode');

	const isSuccess = resultCode === '0';

	return (
		<div className={styles.container}>
			{isSuccess ? (
				<>
					<h1>🎉 Thanh toán thành công</h1>
					<p>Mã đơn: {orderId}</p>
				</>
			) : (
				<>
					<h1>❌ Thanh toán thất bại</h1>
					<p>Vui lòng thử lại</p>
				</>
			)}
		</div>
	);
};

export default PaymentSuccess;