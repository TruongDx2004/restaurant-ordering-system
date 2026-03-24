import axiosInstance from './axiosConfig';
import { RESTAURANT_CONFIG_ENDPOINTS } from '../constants/apiEndpoints';

/**
 * Restaurant Config API Service
 */
export const restaurantConfigApi = {
  /**
   * Lấy cấu hình nhà hàng
   * @returns {Promise} Response với cấu hình
   */
  get: async () => {
    try {
      const response = await axiosInstance.get(RESTAURANT_CONFIG_ENDPOINTS.GET);
      return response;
    } catch (error) {
      throw error;
    }
  },

  /**
   * Cập nhật cấu hình nhà hàng
   * @param {object} configData - Cấu hình mới
   * @returns {Promise} Response với cấu hình đã cập nhật
   */
  update: async (configData) => {
    try {
      const response = await axiosInstance.put(RESTAURANT_CONFIG_ENDPOINTS.UPDATE, configData);
      return response;
    } catch (error) {
      throw error;
    }
  }
};

export default restaurantConfigApi;
