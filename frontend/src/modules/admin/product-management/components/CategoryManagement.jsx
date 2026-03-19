import React, { useState, useEffect } from 'react';
import { categoryApi } from '../../../../api';
import { CategoryModal } from './CategoryModal';
import styles from './CategoryManagement.module.css';

/**
 * Category Management Component
 * CRUD operations for categories
 */
export const CategoryManagement = () => {
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [editingCategory, setEditingCategory] = useState(null);

  // Load categories
  useEffect(() => {
    loadCategories();
  }, []);

  const loadCategories = async () => {
    try {
      setLoading(true);
      setError(null);
      const response = await categoryApi.getAll();
      
      if (response.success) {
        setCategories(response.data);
      }
    } catch (err) {
      setError(err.message || 'Không thể tải danh mục');
    } finally {
      setLoading(false);
    }
  };

  // Filter categories
  const filteredCategories = categories.filter(cat =>
    cat.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
    cat.description?.toLowerCase().includes(searchTerm.toLowerCase())
  );

  // Handle create
  const handleCreate = () => {
    setEditingCategory(null);
    setShowModal(true);
  };

  // Handle edit
  const handleEdit = (category) => {
    setEditingCategory(category);
    setShowModal(true);
  };

  // Handle delete
  const handleDelete = async (id) => {
    if (!window.confirm('Bạn có chắc muốn xóa danh mục này? Tất cả món ăn trong danh mục sẽ bị ảnh hưởng.')) {
      return;
    }

    try {
      const response = await categoryApi.deleteCategory(id);
      if (response.success) {
        await loadCategories();
        alert('Xóa danh mục thành công!');
      }
    } catch (err) {
      alert(err.response?.data?.message || 'Không thể xóa danh mục');
    }
  };

  // Handle save
  const handleSave = async (categoryData) => {
    try {
      if (editingCategory) {
        const response = await categoryApi.updateCategory(editingCategory.id, categoryData);
        if (response.success) {
          await loadCategories();
          setShowModal(false);
          alert('Cập nhật danh mục thành công!');
        }
      } else {
        const response = await categoryApi.createCategory(categoryData);
        if (response.success) {
          await loadCategories();
          setShowModal(false);
          alert('Tạo danh mục thành công!');
        }
      }
    } catch (err) {
      throw err;
    }
  };

  if (loading) {
    return (
      <div className={styles.loading}>
        <i className="fas fa-spinner fa-spin"></i>
        <p>Đang tải...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className={styles.error}>
        <i className="fas fa-exclamation-triangle"></i>
        <p>{error}</p>
        <button onClick={loadCategories}>Thử lại</button>
      </div>
    );
  }

  return (
    <div className={styles.categoryManagement}>
      {/* Toolbar */}
      <div className={styles.toolbar}>
        <div className={styles.searchBox}>
          <i className="fas fa-search"></i>
          <input
            type="text"
            placeholder="Tìm kiếm danh mục..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>

        <button className={styles.createBtn} onClick={handleCreate}>
          <i className="fas fa-plus"></i>
          Thêm danh mục
        </button>
      </div>

      {/* Stats */}
      <div className={styles.stats}>
        <div className={styles.stat}>
          <span className={styles.statLabel}>Tổng danh mục:</span>
          <span className={styles.statValue}>{categories.length}</span>
        </div>
      </div>

      {/* Grid */}
      <div className={styles.categoryGrid}>
        {filteredCategories.length === 0 ? (
          <div className={styles.emptyState}>
            <i className="fas fa-inbox"></i>
            <p>Không tìm thấy danh mục</p>
          </div>
        ) : (
          filteredCategories.map(category => (
            <div key={category.id} className={styles.categoryCard}>
              <div className={styles.cardHeader}>
                <h3>{category.name}</h3>
                <div className={styles.cardActions}>
                  <button
                    className={styles.editBtn}
                    onClick={() => handleEdit(category)}
                    title="Sửa"
                  >
                    <i className="fas fa-edit"></i>
                  </button>
                  <button
                    className={styles.deleteBtn}
                    onClick={() => handleDelete(category.id)}
                    title="Xóa"
                  >
                    <i className="fas fa-trash"></i>
                  </button>
                </div>
              </div>
              <p className={styles.cardDescription}>
                {category.description || 'Không có mô tả'}
              </p>
              <div className={styles.cardFooter}>
                <span className={styles.dishCount}>
                  <i className="fas fa-utensils"></i>
                  {category.dishCount || 0} món ăn
                </span>
              </div>
            </div>
          ))
        )}
      </div>

      {/* Modal */}
      {showModal && (
        <CategoryModal
          category={editingCategory}
          onSave={handleSave}
          onClose={() => setShowModal(false)}
        />
      )}
    </div>
  );
};
