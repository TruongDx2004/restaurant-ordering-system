import React, { useState, useEffect } from 'react';
import { userApi } from '../../../api';
import { UserModal } from './components/UserModal';
import styles from './index.module.css';

/**
 * User Management Page
 * Manages employees and admin users
 */
const UserManagement = () => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [filterRole, setFilterRole] = useState('all');
  const [showModal, setShowModal] = useState(false);
  const [editingUser, setEditingUser] = useState(null);

  // Load users
  useEffect(() => {
    loadUsers();
  }, []);

  const loadUsers = async () => {
    try {
      setLoading(true);
      setError(null);
      const response = await userApi.getAllUsers();
      
      if (response.success) {
        setUsers(response.data);
      }
    } catch (err) {
      setError(err.message || 'Không thể tải dữ liệu');
    } finally {
      setLoading(false);
    }
  };

  // Filter users
  const filteredUsers = users.filter(user => {
    const matchesSearch = user.fullName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         user.email?.toLowerCase().includes(searchTerm.toLowerCase()) ||
                         user.phone?.includes(searchTerm);
    const matchesRole = filterRole === 'all' || user.role === filterRole;
    return matchesSearch && matchesRole;
  });

  // Handle create
  const handleCreate = () => {
    setEditingUser(null);
    setShowModal(true);
  };

  // Handle edit
  const handleEdit = (user) => {
    setEditingUser(user);
    setShowModal(true);
  };

  // Handle delete
  const handleDelete = async (id) => {
    if (!window.confirm('Bạn có chắc muốn xóa người dùng này?')) return;

    try {
      const response = await userApi.deleteUser(id);
      if (response.success) {
        await loadUsers();
        alert('Xóa người dùng thành công!');
      }
    } catch (err) {
      alert(err.response?.data?.message || 'Không thể xóa người dùng');
    }
  };

  // Handle save
  const handleSave = async (userData) => {
    try {
      if (editingUser) {
        const response = await userApi.updateUser(editingUser.id, userData);
        if (response.success) {
          await loadUsers();
          setShowModal(false);
          alert('Cập nhật người dùng thành công!');
        }
      } else {
        const response = await userApi.createUser(userData);
        if (response.success) {
          await loadUsers();
          setShowModal(false);
          alert('Tạo người dùng thành công!');
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
        <button onClick={loadUsers}>Thử lại</button>
      </div>
    );
  }

  return (
    <div className={styles.userManagement}>
      {/* Header */}
      <div className={styles.header}>
        <div className={styles.headerLeft}>
          <h1>Quản lý người dùng</h1>
          <p>Quản lý nhân viên và quản trị viên</p>
        </div>
      </div>

      {/* Toolbar */}
      <div className={styles.toolbar}>
        <div className={styles.searchBox}>
          <i className="fas fa-search"></i>
          <input
            type="text"
            placeholder="Tìm kiếm người dùng..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>

        <div className={styles.filters}>
          <select value={filterRole} onChange={(e) => setFilterRole(e.target.value)}>
            <option value="all">Tất cả vai trò</option>
            <option value="ADMIN">Quản trị viên</option>
            <option value="EMPLOYEE">Nhân viên</option>
          </select>
        </div>

        <button className={styles.createBtn} onClick={handleCreate}>
          <i className="fas fa-plus"></i>
          Thêm người dùng
        </button>
      </div>

      {/* Stats */}
      <div className={styles.stats}>
        <div className={styles.stat}>
          <span className={styles.statLabel}>Tổng người dùng:</span>
          <span className={styles.statValue}>{users.length}</span>
        </div>
        <div className={styles.stat}>
          <span className={styles.statLabel}>Quản trị viên:</span>
          <span className={styles.statValue}>
            {users.filter(u => u.role === 'ADMIN').length}
          </span>
        </div>
        <div className={styles.stat}>
          <span className={styles.statLabel}>Nhân viên:</span>
          <span className={styles.statValue}>
            {users.filter(u => u.role === 'EMPLOYEE').length}
          </span>
        </div>
      </div>

      {/* Table */}
      <div className={styles.tableWrapper}>
        <table className={styles.table}>
          <thead>
            <tr>
              <th>Họ tên</th>
              <th>Email</th>
              <th>Số điện thoại</th>
              <th>Vai trò</th>
              <th>Ngày tạo</th>
              <th>Thao tác</th>
            </tr>
          </thead>
          <tbody>
            {filteredUsers.length === 0 ? (
              <tr>
                <td colSpan="6" className={styles.emptyState}>
                  <i className="fas fa-inbox"></i>
                  <p>Không tìm thấy người dùng</p>
                </td>
              </tr>
            ) : (
              filteredUsers.map(user => (
                <tr key={user.id}>
                  <td>
                    <div className={styles.userName}>{user.name}</div>
                  </td>
                  <td>{user.email}</td>
                  <td>{user.phone}</td>
                  <td>
                    <span className={`${styles.roleBadge} ${styles[user.role?.toLowerCase()]}`}>
                      {user.role === 'ADMIN' ? 'Quản lý' : 'Nhân viên'}
                    </span>
                  </td>
                  <td>
                    {user.createdAt ? new Date(user.createdAt).toLocaleDateString('vi-VN') : 'N/A'}
                  </td>
                  <td>
                    <div className={styles.actions}>
                      <button
                        className={styles.editBtn}
                        onClick={() => handleEdit(user)}
                        title="Sửa"
                      >
                        <i className="fas fa-edit"></i>
                      </button>
                      <button
                        className={styles.deleteBtn}
                        onClick={() => handleDelete(user.id)}
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
        <UserModal
          user={editingUser}
          onSave={handleSave}
          onClose={() => setShowModal(false)}
        />
      )}
    </div>
  );
};

export default UserManagement;
