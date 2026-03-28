import axiosInstance from './axiosConfig';

const momoApi = {
  createPayment: async (data) => {
    return await axiosInstance.post('/paymentsonline/momo/create', data);
  }
};

export default momoApi;