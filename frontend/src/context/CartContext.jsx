import { createContext, useState, useContext, useEffect, useCallback } from 'react';
import { getCart, addToCart, removeCartItem, updateCartItem, clearCart } from '../services/api';
import { useAuth } from './AuthContext';
import toast from 'react-hot-toast';

const CartContext = createContext();

export const useCart = () => useContext(CartContext);

export const CartProvider = ({ children }) => {
  const { user } = useAuth();
  const [cart, setCart] = useState({ items: [], totalPrice: 0, totalItems: 0 });
  const [loading, setLoading] = useState(false);

  const fetchCart = useCallback(async () => {
    if (!user) {
      setCart({ items: [], totalPrice: 0, totalItems: 0 });
      return;
    }
    
    try {
      setLoading(true);
      const res = await getCart(user.userId);
      if (res.data) {
        setCart(res.data);
      }
    } catch (err) {
      console.error('Failed to fetch cart', err);
      // Cart might be empty or 404, which is fine
      if(err.response?.status === 404) {
         setCart({ items: [], totalPrice: 0, totalItems: 0 });
      }
    } finally {
      setLoading(false);
    }
  }, [user]);

  useEffect(() => {
    fetchCart();
  }, [fetchCart]);

  const addProductToCart = async (productId, quantity = 1) => {
    if (!user) {
      toast.error('Please login to add items to cart');
      return;
    }
    try {
      await addToCart({ userId: user.userId, productId, quantity });
      toast.success('Added to cart!');
      fetchCart();
    } catch (err) {
      const msg = err.response?.data?.message || err.response?.data || 'Failed to add item';
      toast.error(typeof msg === 'string' ? msg : 'Failed to add item');
    }
  };

  const removeItem = async (cartItemId) => {
    try {
      await removeCartItem(cartItemId);
      toast.success('Item removed');
      fetchCart();
    } catch (err) {
      toast.error('Failed to remove item');
    }
  };

  const updateItemQty = async (cartItemId, quantity) => {
    if(quantity < 1) return;
    try {
      await updateCartItem({ cartItemId, quantity });
      fetchCart();
    } catch (err) {
       toast.error('Failed to update quantity');
    }
  };
  
  const clearItems = async () => {
    if (!user) return;
    try {
      await clearCart(user.userId);
      fetchCart();
    } catch (err) {
       console.error("Failed to clear", err);
    }
  }

  return (
    <CartContext.Provider value={{ 
      cart, 
      loading, 
      fetchCart, 
      addProductToCart, 
      removeItem, 
      updateItemQty,
      clearItems
    }}>
      {children}
    </CartContext.Provider>
  );
};
