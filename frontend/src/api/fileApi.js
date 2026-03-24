import axiosInstance from './axiosConfig';

/**
 * File API Service
 */
export const fileApi = {
  /**
   * Upload một file lên server
   * @param {File} file - Đối tượng file từ input
   * @returns {Promise} Response với URL của file sau khi upload
   */
  upload: async (file) => {
    try {
      const formData = new FormData();
      formData.append('file', file);
      
      const response = await axiosInstance.post('/files/upload', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });
      return response;
    } catch (error) {
      throw error;
    }
  }
};

export default fileApi;
