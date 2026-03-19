import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext';
import { SearchProvider } from './contexts/SearchContext';
import { CategoryProvider } from './contexts/CategoryContext';
import { ProtectedRoute } from './components/ProtectedRoute';
import { AuthPage } from './modules/customer/auth';
import CustomerLayout from './layouts/CustomerLayout';
import CustomerHome from './modules/customer/home';
import ProfilePage from './modules/customer/profile';
import DishDetail from './modules/customer/dish-detail';
import Invoice from './modules/customer/invoice';
import Cart from './modules/customer/cart';
import Orders from './modules/customer/orders';

// Admin imports
import { AdminAuthProvider } from './contexts/admin/AdminAuthContext';
import AdminLogin from './modules/admin/auth/LoginPage';
import Dashboard from './modules/admin/dashboard';
import ProductManagement from './modules/admin/product-management';
import UserManagement from './modules/admin/user-management';
import TableManagement from './modules/admin/table-management';
import OrderManagement from './modules/admin/order-management';
import AdminLayout from './layouts/AdminLayout';
import ProtectedAdminRoute from './components/ProtectedAdminRoute';
import EmployeeLayout from './layouts/EmployeeLayout';
import ProtectedEmployeeRoute from './components/ProtectedEmployeeRoute';
import OrderProcessing from './modules/employee/order-processing';
import TableManagementEmployee from './modules/employee/table-management';
import KitchenView from './modules/employee/kitchen';

function App() {
  return (
    <AdminAuthProvider>
      <AuthProvider>
        <SearchProvider>
          <CategoryProvider>
            <BrowserRouter>
              <Routes>
                {/* ==================== ADMIN ROUTES ==================== */}
                <Route path="/admin/login" element={<AdminLogin />} />
                
                <Route
                  path="/admin/dashboard"
                  element={
                    <ProtectedAdminRoute>
                      <AdminLayout>
                        <Dashboard />
                      </AdminLayout>
                    </ProtectedAdminRoute>
                  }
                />

                <Route
                  path="/admin/products"
                  element={
                    <ProtectedAdminRoute>
                      <AdminLayout>
                        <ProductManagement />
                      </AdminLayout>
                    </ProtectedAdminRoute>
                  }
                />

                <Route
                  path="/admin/users"
                  element={
                    <ProtectedAdminRoute requiredRole="ADMIN">
                      <AdminLayout>
                        <UserManagement />
                      </AdminLayout>
                    </ProtectedAdminRoute>
                  }
                />

                <Route
                  path="/admin/tables"
                  element={
                    <ProtectedAdminRoute>
                      <AdminLayout>
                        <TableManagement />
                      </AdminLayout>
                    </ProtectedAdminRoute>
                  }
                />

                <Route
                  path="/admin/orders"
                  element={
                    <ProtectedAdminRoute>
                      <AdminLayout>
                        <OrderManagement />
                      </AdminLayout>
                    </ProtectedAdminRoute>
                  }
                />

                {/* Redirect /admin to dashboard */}
                <Route path="/admin" element={<Navigate to="/admin/dashboard" replace />} />

                {/* ==================== EMPLOYEE ROUTES ==================== */}
                <Route
                  path="/employee"
                  element={
                    <ProtectedEmployeeRoute>
                      <EmployeeLayout />
                    </ProtectedEmployeeRoute>
                  }
                >
                  {/* Nested routes inside EmployeeLayout */}
                  <Route path="orders" element={<OrderProcessing />} />
                  <Route path="tables" element={<TableManagementEmployee />} />
                  <Route path="kitchen" element={<KitchenView />} />
                  <Route index element={<Navigate to="orders" replace />} />
                </Route>

                {/* ==================== CUSTOMER ROUTES ==================== */}
                {/* Public Routes */}
                <Route path="/auth" element={<AuthPage />} />
                <Route path="/login" element={<Navigate to="/auth" replace />} />
                <Route path="/register" element={<Navigate to="/auth" replace />} />
                
                {/* Customer Routes - All wrapped in CustomerLayout */}
                <Route 
                  path="/customer" 
                  element={
                    <ProtectedRoute>
                      <CustomerLayout />
                    </ProtectedRoute>
                  }
                >
                  {/* Nested routes inside CustomerLayout */}
                  <Route path="home" element={<CustomerHome />} />
                  <Route path="profile" element={<ProfilePage />} />
                  <Route path="orders" element={<Orders />} />
                  <Route path="dish/:dishId" element={<DishDetail />} />
                  <Route path="cart" element={<Cart />} />
                  <Route path="invoices" element={<Invoice />} />
                  <Route path="inbox" element={<div style={{padding: '20px'}}>Inbox Page (Coming Soon)</div>} />
                </Route>
                
                {/* Default Route */}
                <Route path="/" element={<Navigate to="/auth" replace />} />
                
                {/* 404 Route */}
                <Route path="*" element={<Navigate to="/auth" replace />} />
              </Routes>
            </BrowserRouter>
          </CategoryProvider>
        </SearchProvider>
      </AuthProvider>
    </AdminAuthProvider>
  );
}

export default App;
