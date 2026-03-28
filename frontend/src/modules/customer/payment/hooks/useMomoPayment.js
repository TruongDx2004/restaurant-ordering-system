import { useState } from 'react';
import momoApi from '../../../../api/momoApi';

const useMomoPayment = () => {
  const [loading, setLoading] = useState(false);

  const createMomoPayment = async (invoice) => {
    try {
      setLoading(true);

      const payload = {
        invoiceId: invoice.id,
        amount: invoice.totalAmount,
        orderInfo: `Thanh toán hóa đơn #${invoice.id}`
      };

      const res = await momoApi.createPayment(payload);

      // 🔍 DEBUG — xem cấu trúc response thực tế
      console.log('[MoMo] Full response:', JSON.stringify(res?.data, null, 2));

      // Thử tất cả các path có thể
      const paymentUrl =
        res?.data?.data?.paymentUrl ||   // ApiResponse wrapper lồng
        res?.data?.paymentUrl    ||       // ApiResponse wrapper phẳng
        res?.paymentUrl;                  // Không có wrapper

      console.log('[MoMo] paymentUrl:', paymentUrl);

      if (paymentUrl) {
        window.location.href = paymentUrl;
      } else {
        console.error('[MoMo] Không tìm thấy paymentUrl trong response:', res?.data);
        alert('Không lấy được link thanh toán MoMo!');
      }

    } catch (err) {
      console.error('[MoMo] Lỗi:', err);
      alert('Thanh toán MoMo thất bại!');
    } finally {
      setLoading(false);
    }
  };

  return { createMomoPayment, loading };
};

export default useMomoPayment;