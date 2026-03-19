import React, { useState, useEffect } from 'react';
import { dishApi, categoryApi } from '../../../../api';
import { DishModal } from './DishModal';
import styles from './DishManagement.module.css';

/**
 * Dish Management Component
 * CRUD operations for dishes
 */
export const DishManagement = () => {
  const [dishes, setDishes] = useState([]);
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [filterCategory, setFilterCategory] = useState('all');
  const [filterStatus, setFilterStatus] = useState('all');
  const [showModal, setShowModal] = useState(false);
  const [editingDish, setEditingDish] = useState(null);

  // Load dishes and categories
  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      setLoading(true);
      setError(null);
      const [dishesRes, categoriesRes] = await Promise.all([
        dishApi.getAll(),
        categoryApi.getAll()
      ]);
      
      if (dishesRes.success) {
        setDishes(dishesRes.data);
      }
      if (categoriesRes.success) {
        setCategories(categoriesRes.data);
      }
    } catch (err) {
      setError(err.message || 'Không thể tải dữ liệu');
    } finally {
      setLoading(false);
    }
  };

  // Filter dishes
  const filteredDishes = dishes.filter(dish => {
    const matchesSearch = dish.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         dish.description?.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesCategory = filterCategory === 'all' || dish.category?.id === parseInt(filterCategory);
    const matchesStatus = filterStatus === 'all' || dish.status === filterStatus;
    return matchesSearch && matchesCategory && matchesStatus;
  });

  // Handle create
  const handleCreate = () => {
    setEditingDish(null);
    setShowModal(true);
  };

  // Handle edit
  const handleEdit = (dish) => {
    setEditingDish(dish);
    setShowModal(true);
  };

  // Handle delete
  const handleDelete = async (id) => {
    if (!window.confirm('Bạn có chắc muốn xóa món ăn này?')) return;

    try {
      const response = await dishApi.deleteDish(id);
      if (response.success) {
        await loadData();
        alert('Xóa món ăn thành công!');
      }
    } catch (err) {
      alert(err.response?.data?.message || 'Không thể xóa món ăn');
    }
  };

  // Handle save
  const handleSave = async (dishData) => {
    try {
      if (editingDish) {
        const response = await dishApi.updateDish(editingDish.id, dishData);
        if (response.success) {
          await loadData();
          setShowModal(false);
          alert('Cập nhật món ăn thành công!');
        }
      } else {
        const response = await dishApi.createDish(dishData);
        if (response.success) {
          await loadData();
          setShowModal(false);
          alert('Tạo món ăn thành công!');
        }
      }
    } catch (err) {
      throw err;
    }
  };

  // Handle status change
  const handleStatusChange = async (id, newStatus) => {
    try {
      const response = await dishApi.updateDishStatus(id, newStatus);
      if (response.success) {
        await loadData();
      }
    } catch (err) {
      alert(err.response?.data?.message || 'Không thể cập nhật trạng thái');
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
        <button onClick={loadData}>Thử lại</button>
      </div>
    );
  }

  return (
    <div className={styles.dishManagement}>
      {/* Toolbar */}
      <div className={styles.toolbar}>
        <div className={styles.searchBox}>
          <i className="fas fa-search"></i>
          <input
            type="text"
            placeholder="Tìm kiếm món ăn..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>

        <div className={styles.filters}>
          <select value={filterCategory} onChange={(e) => setFilterCategory(e.target.value)}>
            <option value="all">Tất cả danh mục</option>
            {categories.map(cat => (
              <option key={cat.id} value={cat.id}>{cat.name}</option>
            ))}
          </select>

          <select value={filterStatus} onChange={(e) => setFilterStatus(e.target.value)}>
            <option value="all">Tất cả trạng thái</option>
            <option value="AVAILABLE">Có sẵn</option>
            <option value="OUT_OF_STOCK">Hết hàng</option>
            <option value="DISCONTINUED">Ngừng bán</option>
          </select>
        </div>

        <button className={styles.createBtn} onClick={handleCreate}>
          <i className="fas fa-plus"></i>
          Thêm món ăn
        </button>
      </div>

      {/* Stats */}
      <div className={styles.stats}>
        <div className={styles.stat}>
          <span className={styles.statLabel}>Tổng món ăn:</span>
          <span className={styles.statValue}>{dishes.length}</span>
        </div>
        <div className={styles.stat}>
          <span className={styles.statLabel}>Có sẵn:</span>
          <span className={styles.statValue}>
            {dishes.filter(d => d.status === 'AVAILABLE').length}
          </span>
        </div>
        <div className={styles.stat}>
          <span className={styles.statLabel}>Hết hàng:</span>
          <span className={styles.statValue}>
            {dishes.filter(d => d.status === 'OUT_OF_STOCK').length}
          </span>
        </div>
      </div>

      {/* Table */}
      <div className={styles.tableWrapper}>
        <table className={styles.table}>
          <thead>
            <tr>
              <th>Hình ảnh</th>
              <th>Tên món</th>
              <th>Danh mục</th>
              <th>Giá</th>
              <th>Trạng thái</th>
              <th>Thao tác</th>
            </tr>
          </thead>
          <tbody>
            {filteredDishes.length === 0 ? (
              <tr>
                <td colSpan="6" className={styles.emptyState}>
                  <i className="fas fa-inbox"></i>
                  <p>Không tìm thấy món ăn</p>
                </td>
              </tr>
            ) : (
              filteredDishes.map(dish => (
                <tr key={dish.id}>
                  <td>
                    <img
                      src={dish.imageUrl || '/placeholder-dish.jpg'}
                      alt={dish.name}
                      className={styles.dishImage}
                    />
                  </td>
                  <td>
                    <div className={styles.dishName}>{dish.name}</div>
                    <div className={styles.dishDesc}>{dish.description}</div>
                  </td>
                  <td>{dish.category?.name || 'N/A'}</td>
                  <td className={styles.price}>
                    {new Intl.NumberFormat('vi-VN', {
                      style: 'currency',
                      currency: 'VND'
                    }).format(dish.price)}
                  </td>
                  <td>
                    <select
                      className={`${styles.statusSelect} ${styles[dish.status?.toLowerCase()]}`}
                      value={dish.status}
                      onChange={(e) => handleStatusChange(dish.id, e.target.value)}
                    >
                      <option value="AVAILABLE">Có sẵn</option>
                      <option value="OUT_OF_STOCK">Hết hàng</option>
                      <option value="DISCONTINUED">Ngừng bán</option>
                    </select>
                  </td>
                  <td>
                    <div className={styles.actions}>
                      <button
                        className={styles.editBtn}
                        onClick={() => handleEdit(dish)}
                        title="Sửa"
                      >
                        <i className="fas fa-edit"></i>
                      </button>
                      <button
                        className={styles.deleteBtn}
                        onClick={() => handleDelete(dish.id)}
                        title="Xóa"
                      >
                        <i className="fas fa-trash"></i>
                      </button>
                    </div>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      {/* Modal */}
      {showModal && (
        <DishModal
          dish={editingDish}
          categories={categories}
          onSave={handleSave}
          onClose={() => setShowModal(false)}
        />
      )}
    </div>
  );
};
