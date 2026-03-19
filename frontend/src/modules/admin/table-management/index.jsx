import React, { useState, useEffect } from 'react';
import { tableApi } from '../../../api';
import { TableModal } from './components/TableModal';
import styles from './index.module.css';

/**
 * Table Management Page
 * Manages restaurant tables
 */
const TableManagement = () => {
  const [tables, setTables] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState('');
  const [filterStatus, setFilterStatus] = useState('all');
  const [filterArea, setFilterArea] = useState('all');
  const [showModal, setShowModal] = useState(false);
  const [editingTable, setEditingTable] = useState(null);

  // Load tables
  useEffect(() => {
    loadTables();
  }, []);

  const loadTables = async () => {
    try {
      setLoading(true);
      setError(null);
      const response = await tableApi.getAllTables();
      
      if (response.success) {
        setTables(response.data);
      }
    } catch (err) {
      setError(err.message || 'Không thể tải dữ liệu');
    } finally {
      setLoading(false);
    }
  };

  // Get unique areas
  const areas = [...new Set(tables.map(t => t.area).filter(Boolean))];

  // Filter tables
  const filteredTables = tables.filter(table => {
    const matchesSearch = table.tableNumber?.toString().includes(searchTerm) ||
                         table.area?.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesStatus = filterStatus === 'all' || table.status === filterStatus;
    const matchesArea = filterArea === 'all' || table.area === filterArea;
    return matchesSearch && matchesStatus && matchesArea;
  });

  // Handle create
  const handleCreate = () => {
    setEditingTable(null);
    setShowModal(true);
  };

  // Handle edit
  const handleEdit = (table) => {
    setEditingTable(table);
    setShowModal(true);
  };

  // Handle delete
  const handleDelete = async (id) => {
    if (!window.confirm('Bạn có chắc muốn xóa bàn này?')) return;

    try {
      const response = await tableApi.deleteTable(id);
      if (response.success) {
        await loadTables();
        alert('Xóa bàn thành công!');
      }
    } catch (err) {
      alert(err.response?.data?.message || 'Không thể xóa bàn');
    }
  };

  // Handle save
  const handleSave = async (tableData) => {
    try {
      if (editingTable) {
        const response = await tableApi.updateTable(editingTable.id, tableData);
        if (response.success) {
          await loadTables();
          setShowModal(false);
          alert('Cập nhật bàn thành công!');
        }
      } else {
        const response = await tableApi.createTable(tableData);
        if (response.success) {
          await loadTables();
          setShowModal(false);
          alert('Tạo bàn thành công!');
        }
      }
    } catch (err) {
      throw err;
    }
  };

  // Handle status change
  const handleStatusChange = async (id, newStatus) => {
    try {
      const response = await tableApi.updateTableStatus(id, newStatus);
      if (response.success) {
        await loadTables();
      }
    } catch (err) {
      alert(err.response?.data?.message || 'Không thể cập nhật trạng thái');
    }
  };

  // Get status color
  const getStatusColor = (status) => {
    switch (status) {
      case 'AVAILABLE': return 'green';
      case 'OCCUPIED': return 'red';
      case 'RESERVED': return 'orange';
      case 'MAINTENANCE': return 'gray';
      default: return 'gray';
    }
  };

  // Get status text
  const getStatusText = (status) => {
    switch (status) {
      case 'AVAILABLE': return 'Trống';
      case 'OCCUPIED': return 'Đang dùng';
      case 'RESERVED': return 'Đã đặt';
      case 'MAINTENANCE': return 'Bảo trì';
      default: return status;
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
        <button onClick={loadTables}>Thử lại</button>
      </div>
    );
  }

  return (
    <div className={styles.tableManagement}>
      {/* Header */}
      <div className={styles.header}>
        <div className={styles.headerLeft}>
          <h1>Quản lý bàn ăn</h1>
          <p>Quản lý bàn và khu vực nhà hàng</p>
        </div>
      </div>

      {/* Toolbar */}
      <div className={styles.toolbar}>
        <div className={styles.searchBox}>
          <i className="fas fa-search"></i>
          <input
            type="text"
            placeholder="Tìm kiếm bàn..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
        </div>

        <div className={styles.filters}>
          <select value={filterArea} onChange={(e) => setFilterArea(e.target.value)}>
            <option value="all">Tất cả khu vực</option>
            {areas.map(area => (
              <option key={area} value={area}>{area}</option>
            ))}
          </select>

          <select value={filterStatus} onChange={(e) => setFilterStatus(e.target.value)}>
            <option value="all">Tất cả trạng thái</option>
            <option value="AVAILABLE">Trống</option>
            <option value="OCCUPIED">Đang dùng</option>
            <option value="RESERVED">Đã đặt</option>
            <option value="MAINTENANCE">Bảo trì</option>
          </select>
        </div>

        <button className={styles.createBtn} onClick={handleCreate}>
          <i className="fas fa-plus"></i>
          Thêm bàn
        </button>
      </div>

      {/* Stats */}
      <div className={styles.stats}>
        <div className={styles.stat}>
          <span className={styles.statLabel}>Tổng bàn:</span>
          <span className={styles.statValue}>{tables.length}</span>
        </div>
        <div className={styles.stat}>
          <span className={styles.statLabel}>Trống:</span>
          <span className={styles.statValue}>
            {tables.filter(t => t.status === 'AVAILABLE').length}
          </span>
        </div>
        <div className={styles.stat}>
          <span className={styles.statLabel}>Đang dùng:</span>
          <span className={styles.statValue}>
            {tables.filter(t => t.status === 'OCCUPIED').length}
          </span>
        </div>
        <div className={styles.stat}>
          <span className={styles.statLabel}>Đã đặt:</span>
          <span className={styles.statValue}>
            {tables.filter(t => t.status === 'RESERVED').length}
          </span>
        </div>
      </div>

      {/* Table Grid */}
      <div className={styles.tableGrid}>
        {filteredTables.length === 0 ? (
          <div className={styles.emptyState}>
            <i className="fas fa-inbox"></i>
            <p>Không tìm thấy bàn</p>
          </div>
        ) : (
          filteredTables.map(table => (
            <div 
              key={table.id} 
              className={`${styles.tableCard} ${styles[getStatusColor(table.status)]}`}
            >
              <div className={styles.cardHeader}>
                <div className={styles.tableNumber}>
                  <i className="fas fa-table"></i>
                  Bàn {table.tableNumber}
                </div>
                <div className={styles.cardActions}>
                  <button
                    className={styles.editBtn}
                    onClick={() => handleEdit(table)}
                    title="Sửa"
                  >
                    <i className="fas fa-edit"></i>
                  </button>
                  <button
                    className={styles.deleteBtn}
                    onClick={() => handleDelete(table.id)}
                    title="Xóa"
                  >
                    <i className="fas fa-trash"></i>
                  </button>
                </div>
              </div>

              <div className={styles.cardBody}>
                <div className={styles.info}>
                  <i className="fas fa-users"></i>
                  <span>{table.capacity} người</span>
                </div>
                <div className={styles.info}>
                  <i className="fas fa-map-marker-alt"></i>
                  <span>{table.area || 'N/A'}</span>
                </div>
              </div>

              <div className={styles.cardFooter}>
                <select
                  className={`${styles.statusSelect} ${styles[table.status?.toLowerCase()]}`}
                  value={table.status}
                  onChange={(e) => handleStatusChange(table.id, e.target.value)}
                >
                  <option value="AVAILABLE">Trống</option>
                  <option value="OCCUPIED">Đang dùng</option>
                  <option value="RESERVED">Đã đặt</option>
                  <option value="MAINTENANCE">Bảo trì</option>
                </select>
              </div>
            </div>
          ))
        )}
      </div>

      {/* Modal */}
      {showModal && (
        <TableModal
          table={editingTable}
          onSave={handleSave}
          onClose={() => setShowModal(false)}
        />
      )}
    </div>
  );
};

export default TableManagement;
