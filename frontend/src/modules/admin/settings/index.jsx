import React, { useState, useEffect } from 'react';
import { restaurantConfigApi, fileApi } from '../../../api';
import styles from './index.module.css';

/**
 * Restaurant Settings Page
 * Allows administrators to update restaurant information, operating hours, and branding.
 */
const RestaurantSettings = () => {
  const [config, setConfig] = useState({
    name: '',
    logo: '',
    address: '',
    phone: '',
    email: '',
    website: '',
    description: '',
    openingTime: '08:00:00',
    closingTime: '22:00:00',
    taxId: '',
    bannerImage: '',
    bannerImage2: '',
    bannerImage3: '',
    bannerImage4: '',
    operatingHours: ''
  });

  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [uploading, setUploading] = useState({ 
    logo: false, 
    banner: false, 
    banner2: false, 
    banner3: false, 
    banner4: false 
  });
  const [message, setMessage] = useState({ type: '', text: '' });

  // Base URL for images (Backend server)
  const API_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';
  const SERVER_URL = import.meta.env.VITE_SERVER_URL || API_URL.split('/api')[0];

  useEffect(() => {
    fetchConfig();
  }, []);

  const fetchConfig = async () => {
    try {
      setLoading(true);
      const response = await restaurantConfigApi.get();
      if (response && response.success && response.data) {
        const data = response.data;
        setConfig(prev => ({
          ...prev,
          ...data,
          openingTime: data.openingTime || '08:00:00',
          closingTime: data.closingTime || '22:00:00',
        }));
      }
    } catch (error) {
      console.error('Error fetching config:', error);
      setMessage({ type: 'error', text: 'Không thể tải cấu hình nhà hàng.' });
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setConfig(prev => ({ ...prev, [name]: value }));
  };

  const handleFileChange = async (e, type) => {
    const file = e.target.files[0];
    if (!file) return;

    // Validate file type
    if (!file.type.startsWith('image/')) {
      setMessage({ type: 'error', text: 'Vui lòng chọn tệp hình ảnh hợp lệ.' });
      return;
    }

    // Validate file size (max 5MB)
    if (file.size > 5 * 1024 * 1024) {
      setMessage({ type: 'error', text: 'Kích thước ảnh không được vượt quá 5MB.' });
      return;
    }

    try {
      setUploading(prev => ({ ...prev, [type]: true }));
      setMessage({ type: '', text: '' });
      
      const response = await fileApi.upload(file);
      if (response && response.success) {
        const imageUrl = response.data; // This will be "/uploads/filename.ext"
        
        const typeMap = {
          logo: 'logo',
          banner: 'bannerImage',
          banner2: 'bannerImage2',
          banner3: 'bannerImage3',
          banner4: 'bannerImage4'
        };

        setConfig(prev => ({ 
          ...prev, 
          [typeMap[type]]: imageUrl 
        }));
      } else {
        setMessage({ type: 'error', text: 'Không thể tải ảnh lên server.' });
      }
    } catch (error) {
      console.error('File upload error:', error);
      setMessage({ type: 'error', text: 'Lỗi khi tải ảnh lên.' });
    } finally {
      setUploading(prev => ({ ...prev, [type]: false }));
    }
  };

  const getFullImageUrl = (path) => {
    if (!path) return '';
    if (path.startsWith('http')) return path;
    return `${SERVER_URL}${path}`;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setSaving(true);
      setMessage({ type: '', text: '' });
      
      const response = await restaurantConfigApi.update(config);
      if (response && response.success) {
        setMessage({ type: 'success', text: 'Cập nhật cấu hình thành công!' });
        window.scrollTo({ top: 0, behavior: 'smooth' });
      } else {
        setMessage({ type: 'error', text: response.message || 'Có lỗi xảy ra khi cập nhật.' });
      }
    } catch (error) {
      console.error('Error updating config:', error);
      setMessage({ type: 'error', text: 'Lỗi kết nối máy chủ.' });
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return <div className={styles.loadingOverlay}>Đang tải cấu hình...</div>;
  }

  const isUploading = Object.values(uploading).some(val => val);

  return (
    <div className={styles.settingsContainer}>
      <div className={styles.header}>
        <h2>Cài đặt nhà hàng</h2>
        <p>Quản lý thông tin hiển thị và cấu hình hệ thống của nhà hàng</p>
      </div>

      {message.text && (
        <div className={`${styles.alert} ${message.type === 'success' ? styles.alertSuccess : styles.alertError}`}>
          {message.text}
        </div>
      )}

      <form onSubmit={handleSubmit}>
        <div className={styles.settingsGrid}>
          {/* General Information */}
          <div className={styles.card}>
            <h3><i className="fas fa-info-circle"></i> Thông tin cơ bản</h3>
            
            <div className={styles.formGroup}>
              <label>Tên nhà hàng</label>
              <input 
                type="text" 
                name="name" 
                value={config.name || ''} 
                onChange={handleChange} 
                required 
                placeholder="Nhập tên nhà hàng"
              />
            </div>

            <div className={styles.formGroup}>
              <label>Địa chỉ</label>
              <textarea 
                name="address" 
                value={config.address || ''} 
                onChange={handleChange} 
                required 
                rows="2"
                placeholder="Số nhà, đường, phường/xã, quận/huyện, tỉnh/thành phố"
              />
            </div>

            <div className={styles.formRow}>
              <div className={styles.formGroup}>
                <label>Số điện thoại (tối thiểu 10 số)</label>
                <input 
                  type="tel" 
                  name="phone" 
                  value={config.phone || ''} 
                  onChange={handleChange} 
                  required 
                  minLength="10"
                  maxLength="15"
                  pattern="[0-9]*"
                  title="Vui lòng nhập số điện thoại hợp lệ (chỉ chứa chữ số, tối thiểu 10 ký tự)"
                  placeholder="VD: 0123456789"
                />
              </div>
            </div>

            <div className={styles.formGroup}>
              <label>Mô tả nhà hàng</label>
              <textarea 
                name="description" 
                value={config.description || ''} 
                onChange={handleChange} 
                rows="4"
                placeholder="Giới thiệu ngắn về nhà hàng của bạn"
              />
            </div>
          </div>

          {/* Operating Hours & Contact */}
          <div className={styles.card}>
            <h3><i className="fas fa-clock"></i> Thời gian & Liên hệ</h3>
            
            <div className={styles.formRow}>
              <div className={styles.formGroup}>
                <label>Giờ mở cửa</label>
                <input 
                  type="time" 
                  name="openingTime" 
                  value={config.openingTime?.substring(0, 5) || '08:00'} 
                  onChange={handleChange} 
                  step="60"
                />
              </div>
              <div className={styles.formGroup}>
                <label>Giờ đóng cửa</label>
                <input 
                  type="time" 
                  name="closingTime" 
                  value={config.closingTime?.substring(0, 5) || '22:00'} 
                  onChange={handleChange} 
                  step="60"
                />
              </div>
            </div>

            <div className={styles.formGroup}>
              <label>Email liên hệ</label>
              <input 
                type="email" 
                name="email" 
                value={config.email || ''} 
                onChange={handleChange} 
                placeholder="example@restaurant.com"
              />
            </div>
          </div>

          {/* Branding & Media */}
          <div className={`${styles.card} ${styles.fullWidth}`}>
            <h3><i className="fas fa-image"></i> Hình ảnh & Thương hiệu</h3>
            
            <div className={styles.formRow}>
              <div className={styles.formGroup}>
                <label>Logo Nhà hàng</label>
                <div className={styles.fileUploadWrapper}>
                  <input 
                    type="file" 
                    id="logo-upload"
                    accept="image/*"
                    onChange={(e) => handleFileChange(e, 'logo')} 
                    className={styles.fileInput}
                  />
                  <label htmlFor="logo-upload" className={styles.fileLabel}>
                    {uploading.logo ? <i className="fas fa-spinner fa-spin"></i> : <i className="fas fa-cloud-upload-alt"></i>}
                    {uploading.logo ? ' Đang tải...' : ' Chọn Logo'}
                  </label>
                </div>
                <div className={styles.imagePreviewContainer}>
                  <label>Xem trước Logo:</label>
                  {config.logo ? (
                    <img src={getFullImageUrl(config.logo)} alt="Logo Preview" className={styles.logoPreview} />
                  ) : (
                    <div className={styles.logoPreview}>Chưa có logo</div>
                  )}
                </div>
              </div>

              <div className={styles.formGroup}>
                <label>Hero Banner (Trang chủ)</label>
                <div className={styles.fileUploadWrapper}>
                  <input 
                    type="file" 
                    id="banner-upload"
                    accept="image/*"
                    onChange={(e) => handleFileChange(e, 'banner')} 
                    className={styles.fileInput}
                  />
                  <label htmlFor="banner-upload" className={styles.fileLabel}>
                    {uploading.banner ? <i className="fas fa-spinner fa-spin"></i> : <i className="fas fa-cloud-upload-alt"></i>}
                    {uploading.banner ? ' Đang tải...' : ' Chọn Banner'}
                  </label>
                </div>
                <div className={styles.imagePreviewContainer}>
                  <label>Xem trước Hero Banner:</label>
                  {config.bannerImage ? (
                    <img src={getFullImageUrl(config.bannerImage)} alt="Banner Preview" className={styles.bannerPreview} />
                  ) : (
                    <div className={styles.bannerPreview}>Chưa có banner</div>
                  )}
                </div>
              </div>
            </div>

            <div className={styles.promotionTitle}>
              <h3><i className="fas fa-images"></i> Slides Quảng cáo (Slider)</h3>
              <p>Các ảnh này sẽ hiển thị trong slider bên dưới Hero Banner</p>
            </div>

            <div className={styles.bannerRow}>
              <div className={styles.formGroup}>
                <label>Slide 1</label>
                <div className={styles.fileUploadWrapper}>
                  <input 
                    type="file" 
                    id="banner2-upload"
                    accept="image/*"
                    onChange={(e) => handleFileChange(e, 'banner2')} 
                    className={styles.fileInput}
                  />
                  <label htmlFor="banner2-upload" className={styles.fileLabel}>
                    {uploading.banner2 ? <i className="fas fa-spinner fa-spin"></i> : <i className="fas fa-cloud-upload-alt"></i>}
                    {uploading.banner2 ? ' Đang tải...' : ' Chọn Slide 1'}
                  </label>
                </div>
                <div className={styles.imagePreviewContainer}>
                  {config.bannerImage2 ? (
                    <img src={getFullImageUrl(config.bannerImage2)} alt="Slide 1 Preview" className={styles.bannerPreviewSmall} />
                  ) : (
                    <div className={styles.bannerPreviewSmall}>Chưa có slide 1</div>
                  )}
                </div>
              </div>

              <div className={styles.formGroup}>
                <label>Slide 2</label>
                <div className={styles.fileUploadWrapper}>
                  <input 
                    type="file" 
                    id="banner3-upload"
                    accept="image/*"
                    onChange={(e) => handleFileChange(e, 'banner3')} 
                    className={styles.fileInput}
                  />
                  <label htmlFor="banner3-upload" className={styles.fileLabel}>
                    {uploading.banner3 ? <i className="fas fa-spinner fa-spin"></i> : <i className="fas fa-cloud-upload-alt"></i>}
                    {uploading.banner3 ? ' Đang tải...' : ' Chọn Slide 2'}
                  </label>
                </div>
                <div className={styles.imagePreviewContainer}>
                  {config.bannerImage3 ? (
                    <img src={getFullImageUrl(config.bannerImage3)} alt="Slide 2 Preview" className={styles.bannerPreviewSmall} />
                  ) : (
                    <div className={styles.bannerPreviewSmall}>Chưa có slide 2</div>
                  )}
                </div>
              </div>

              <div className={styles.formGroup}>
                <label>Slide 3</label>
                <div className={styles.fileUploadWrapper}>
                  <input 
                    type="file" 
                    id="banner4-upload"
                    accept="image/*"
                    onChange={(e) => handleFileChange(e, 'banner4')} 
                    className={styles.fileInput}
                  />
                  <label htmlFor="banner4-upload" className={styles.fileLabel}>
                    {uploading.banner4 ? <i className="fas fa-spinner fa-spin"></i> : <i className="fas fa-cloud-upload-alt"></i>}
                    {uploading.banner4 ? ' Đang tải...' : ' Chọn Slide 3'}
                  </label>
                </div>
                <div className={styles.imagePreviewContainer}>
                  {config.bannerImage4 ? (
                    <img src={getFullImageUrl(config.bannerImage4)} alt="Slide 3 Preview" className={styles.bannerPreviewSmall} />
                  ) : (
                    <div className={styles.bannerPreviewSmall}>Chưa có slide 3</div>
                  )}
                </div>
              </div>
            </div>
          </div>
        </div>

        <div className={styles.actions}>
          <button type="button" onClick={fetchConfig} className={styles.cancelButton}>
            Hủy thay đổi
          </button>
          <button type="submit" className={styles.saveButton} disabled={saving || isUploading}>
            {saving ? <i className="fas fa-spinner fa-spin"></i> : <i className="fas fa-save"></i>}
            {saving ? ' Đang lưu...' : ' Lưu cấu hình'}
          </button>
        </div>
      </form>
    </div>
  );
};

export default RestaurantSettings;
