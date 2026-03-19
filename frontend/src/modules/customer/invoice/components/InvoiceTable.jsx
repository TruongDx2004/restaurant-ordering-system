import React from 'react';
import styles from './InvoiceTable.module.css';

/**
 * InvoiceTable Component
 * Hiển thị danh sách món ăn trong hóa đơn dạng bảng
 */
export const InvoiceTable = ({ items }) => {
  /**
   * Format currency
   */
  const formatCurrency = (amount) => {
    return new Intl.NumberFormat('vi-VN', {
      style: 'currency',
      currency: 'VND'
    }).format(amount);
  };

  if (!items || items.length === 0) {
    return (
      <div className={styles.emptyTable}>
        <p>Chưa có món ăn nào trong hóa đơn</p>
      </div>
    );
  }

  return (
    <div className={styles.tableContainer}>
      <table className={styles.invoiceTable}>
        <thead>
          <tr>
            <th>STT</th>
            <th>Tên món</th>
            <th>Đơn giá</th>
            <th>SL</th>
            <th>Thành tiền</th>
          </tr>
        </thead>
        <tbody>
          {items.map((item, index) => (
            <tr key={item.id}>
              <td className={styles.stt}>{index + 1}</td>
              <td className={styles.dishName}>
                <div className={styles.dishInfo}>
                  {item.dish?.image && (
                    <img 
                      src={item.dish.image} 
                      alt={item.dish.name}
                      className={styles.dishImage}
                      onError={(e) => {
                        e.target.style.display = 'none';
                      }}
                    />
                  )}
                  <span>{item.dish?.name || 'Món ăn'}</span>
                </div>
              </td>
              <td className={styles.price}>
                {formatCurrency(item.unitPrice || 0)}
              </td>
              <td className={styles.quantity}>
                {item.quantity}
              </td>
              <td className={styles.totalPrice}>
                {formatCurrency(item.totalPrice || 0)}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default InvoiceTable;
