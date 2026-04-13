import axios from 'axios';

// Create base axios instance
const apiClient = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

// User Methods
export const loginUser = async (credentials) => {
  const response = await apiClient.post('/user/userLogin', credentials);
  return response.data;
};

export const registerUser = async (userData) => {
  const response = await apiClient.post('/user/register', userData);
  return response.data;
};

export const getUserById = async (userId) => {
  const response = await apiClient.get(`/user/getUserById?UserId=${userId}`);
  return response.data;
};

// Product Methods
export const getAllProducts = async () => {
  const response = await apiClient.get('/product/getAllProduct');
  return response.data;
};

export const getProductById = async (productId) => {
  const response = await apiClient.get(`/product/getProductById?proId=${productId}`);
  return response.data;
};

// Cart Methods
export const addToCart = async (cartData) => {
  const response = await apiClient.post('/cart/add', cartData);
  return response.data;
};

export const getCart = async (userId) => {
  const response = await apiClient.get(`/cart/${userId}`);
  return response.data;
};

// Expects { cartItemId, quantity }
export const updateCartItem = async (updateData) => {
  const response = await apiClient.put('/cart/update', updateData);
  return response.data;
};

export const removeCartItem = async (cartItemId) => {
  const response = await apiClient.delete(`/cart/remove/${cartItemId}`);
  return response.data;
};

export const clearCart = async (userId) => {
  const response = await apiClient.delete(`/cart/clear/${userId}`);
  return response.data;
};

// Order Methods
export const placeOrder = async (userId) => {
  const response = await apiClient.post(`/order/place/${userId}`);
  return response.data;
};

export const getUserOrders = async (userId) => {
  const response = await apiClient.get(`/order/user/${userId}`);
  return response.data;
};

export const cancelOrder = async (orderId) => {
  const response = await apiClient.put(`/order/cancel/${orderId}`);
  return response.data;
};
