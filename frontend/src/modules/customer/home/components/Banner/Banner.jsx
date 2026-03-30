import React, { useState, useEffect } from 'react';
import { restaurantConfigApi } from '../../../../../api';
import styles from './Banner.module.css';

/**
 * Banner Component
 * Banner với hero image và promotional slider
 */
export const Banner = () => {
  const [currentSlide, setCurrentSlide] = useState(0);
  const [config, setConfig] = useState(null);
  const [loading, setLoading] = useState(true);

  // Base URL for images (Backend server)
  const API_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';
  const SERVER_URL = import.meta.env.VITE_SERVER_URL || API_URL.split('/api')[0];

  const getFullImageUrl = (path) => {
    if (!path) return '/storage/dish/banner.jpg'; // Default fallback
    if (path.startsWith('http')) return path;
    // For images in /uploads/ or /storage/
    return `${SERVER_URL}${path}`;
  };
  
  // Promotional slides - Dynamic from config or fallback to defaults
  const [slides, setSlides] = useState([
    '/storage/dish/ad1.jpg',
    '/storage/dish/ad2.jpg',
    '/storage/dish/ad3.jpg'
  ]);

  useEffect(() => {
    const fetchConfig = async () => {
      try {
        const response = await restaurantConfigApi.get();
        if (response && response.success && response.data) {
          const configData = response.data;
          setConfig(configData);
          
          // Update slides if we have images in config
          const dynamicSlides = [];
          if (configData.bannerImage2) dynamicSlides.push(getFullImageUrl(configData.bannerImage2));
          if (configData.bannerImage3) dynamicSlides.push(getFullImageUrl(configData.bannerImage3));
          if (configData.bannerImage4) dynamicSlides.push(getFullImageUrl(configData.bannerImage4));
          
          // If we found any dynamic banners, use them. Otherwise keep defaults.
          if (dynamicSlides.length > 0) {
            setSlides(dynamicSlides);
          }
        }
      } catch (error) {
        console.error('Error fetching restaurant config:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchConfig();
  }, [SERVER_URL]);

  useEffect(() => {
    if (slides.length <= 1) return;
    
    // Auto slide every 7 seconds for better user experience
    const interval = setInterval(() => {
      setCurrentSlide((prev) => (prev + 1) % slides.length);
    }, 7000);

    return () => clearInterval(interval);
  }, [slides.length]);

  const handleDotClick = (index) => {
    setCurrentSlide(index);
  };

  return (
    <section className={styles.banner}>
      {/* Hero Banner */}
      <div className={styles.heroBanner}>
        <img 
          src={getFullImageUrl(config?.bannerImage)} 
          alt={config?.name || "Restaurant Banner"} 
          className={styles.heroImage}
        />
        <div className={styles.heroOverlay}>
          <div className={styles.heroText}>
            <h2 className={styles.heroTitle}>{config?.name ? "Chào mừng đến với" : "Chào mừng đến với"}</h2>
            <h1 className={styles.heroSubtitle}>{config?.name || "Nhà hàng của chúng tôi!"}</h1>
            <p className={styles.heroDescription}>
              {config?.description || "Thưởng thức những món ăn ngon nhất ngay hôm nay."}
            </p>
          </div>
        </div>
      </div>

      {/* Promotional Slider */}
      <div className={styles.promotionSection}>
        <div className={styles.slider}>
          <div className={styles.slides}>
            {slides.map((slide, index) => (
              <div
                key={index}
                className={`${styles.slide} ${index === currentSlide ? styles.active : ''}`}
              >
                <img 
                  src={slide} 
                  alt={`Promotion ${index + 1}`}
                  className={styles.slideImage}
                />
              </div>
            ))}
          </div>
          
          {/* Navigation Dots */}
          <div className={styles.dots}>
            {slides.map((_, index) => (
              <button
                key={index}
                className={`${styles.dot} ${index === currentSlide ? styles.activeDot : ''}`}
                onClick={() => handleDotClick(index)}
                aria-label={`Go to slide ${index + 1}`}
              />
            ))}
          </div>
        </div>
      </div>
    </section>
  );
};

export default Banner;
