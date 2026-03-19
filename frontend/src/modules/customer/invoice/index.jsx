import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useOrders } from '../orders/hooks';
import { useInvoicePayment } from './hooks';
import { InvoiceTable, PaymentSection } from './components';
import { DesktopWarning } from '../../../components/shared';
import storage from '../../../utils/storage';
import styles from './index.module.css';

/**
 * Invoice Component
 * Trang hóa đơn - hiển thị chi tiết và thanh toán
 */
const Invoice = () => {
  const navigate = useNavigate();
  const [tableNumber, setTableNumber] = useState(null);
  const [toast, setToast] = useState(null);

  // Get table number from storage
  useEffect(() => {
    const storedTableNumber = storage.getTableNumber();
    if (storedTableNumber) {
      setTableNumber(storedTableNumber);
    } else {
      setTableNumber('1');
      storage.setTableNumber('1');
    }
  }, []);

  // Fetch orders data (reuse hook from orders page)
  const { invoice, items, loading, error, refetch } = useOrders(tableNumber, 0); // No auto-refresh

  // Payment processing
  const { processPayment, isProcessing, error: paymentError } = useInvoicePayment();

  /**
   * Show toast notification
   */
  const showToast = (message, type = 'info') => {
    setToast({ message, type });
    setTimeout(() => setToast(null), 3000);
  };

  /**
   * Handle payment
   */
  const handlePayment = async (paymentMethod, amount) => {
    if (!invoice || !invoice.id) {
      showToast('Không tìm thấy hóa đơn', 'error');
      return;
    }

    const result = await processPayment(invoice.id, paymentMethod, amount);

    if (result.success) {
      showToast('Thanh toán thành công!', 'success');
      
      // Redirect to success page after 2 seconds
      setTimeout(() => {
        navigate('/customer/payment-success', {
          state: {
            payment: result.payment,
            invoice: invoice,
            items: items
          }
        });
      }, 2000);
    } else {
      showToast(result.error || 'Thanh toán thất bại!', 'error');
    }
  };

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
   * Format date
   */
  const formatDate = (dateString) => {
    if (!dateString) return '';
    const date = new Date(dateString);
    return date.toLocaleString('vi-VN');
  };

  // Loading state
  if (loading && !invoice) {
    return (
      <div className={styles.invoiceContainer}>
        <div className={styles.loading}>
          <i className="fas fa-spinner fa-spin"></i>
          <p>Đang tải hóa đơn...</p>
        </div>
      </div>
    );
  }

  // Error state
  if (error && !invoice) {
    return (
      <div className={styles.invoiceContainer}>
        <div className={styles.error}>
          <i className="fas fa-exclamation-triangle"></i>
          <h3>Có lỗi xảy ra</h3>
          <p>{error}</p>
          <button onClick={() => refetch()} className={styles.retryBtn}>
            <i className="fas fa-redo"></i> Thử lại
          </button>
        </div>
      </div>
    );
  }

  // Empty state - no invoice
  if (!invoice) {
    return (
      <div className={styles.invoiceContainer}>
        <div className={styles.emptyState}>
          <div className={styles.emptyIcon}>
            <i className="fas fa-receipt"></i>
          </div>
          <h3>Chưa có hóa đơn nào</h3>
          <p>Hãy thêm món ăn từ trang menu</p>
          <a href="/customer/home" className={styles.browseMenuBtn}>
            Xem Menu
          </a>
        </div>
      </div>
    );
  }

  return (
    <>
      <DesktopWarning />
      <div className={styles.invoiceContainer}>
      {/* Invoice Header */}
      <div className={styles.invoiceHeader}>
        <div className={styles.headerTop}>
          <button 
            onClick={() => navigate(-1)} 
            className={styles.backBtn}
          >
            <i className="fas fa-arrow-left"></i>
          </button>
          <h1>Hóa Đơn</h1>
          <button 
            onClick={() => window.print()} 
            className={styles.printBtn}
          >
            <i className="fas fa-print"></i>
          </button>
        </div>

        <div className={styles.invoiceInfo}>
          <div className={styles.infoRow}>
            <span className={styles.label}>Mã hóa đơn:</span>
            <span className={styles.value}>#{invoice.id}</span>
          </div>
          <div className={styles.infoRow}>
            <span className={styles.label}>Bàn:</span>
            <span className={styles.value}>{invoice.table?.tableNumber || tableNumber}</span>
          </div>
          <div className={styles.infoRow}>
            <span className={styles.label}>Ngày giờ:</span>
            <span className={styles.value}>{formatDate(invoice.createdAt)}</span>
          </div>
          <div className={styles.infoRow}>
            <span className={styles.label}>Trạng thái:</span>
            <span className={`${styles.status} ${styles[invoice.status?.toLowerCase()]}`}>
              {invoice.status === 'OPEN' ? 'Đang phục vụ' : 
               invoice.status === 'PAID' ? 'Đã thanh toán' : 
               invoice.status}
            </span>
          </div>
        </div>
      </div>

      {/* Invoice Table */}
      <InvoiceTable items={items} />

      {/* Payment Section */}
      {invoice.status === 'OPEN' && (
        <PaymentSection
          invoice={invoice}
          items={items}
          onPayment={handlePayment}
          isProcessing={isProcessing}
        />
      )}

      {/* Already Paid Message */}
      {invoice.status === 'PAID' && (
        <div className={styles.paidMessage}>
          <i className="fas fa-check-circle"></i>
          <h3>Hóa đơn đã được thanh toán</h3>
          <p>Cảm ơn bạn đã sử dụng dịch vụ!</p>
          {invoice.paidAt && (
            <p className={styles.paidTime}>Thanh toán lúc: {formatDate(invoice.paidAt)}</p>
          )}
        </div>
      )}

      {/* Toast Notification */}
      {toast && (
        <div className={`${styles.toast} ${styles[`toast${toast.type.charAt(0).toUpperCase() + toast.type.slice(1)}`]}`}>
          {toast.message}
        </div>
      )}
    </div>
    </>
  );
};

export default Invoice;
