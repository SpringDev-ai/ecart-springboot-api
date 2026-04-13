import { X, Trash2, CreditCard, ChevronRight, Minus, Plus } from 'lucide-react';
import { useCart } from '../context/CartContext';
import { useAuth } from '../context/AuthContext';
import { placeOrder } from '../services/api';
import './CartDrawer.css';
import toast from 'react-hot-toast';
import { useNavigate } from 'react-router-dom';

const CartDrawer = ({ isOpen, onClose }) => {
  const { cart, removeItem, updateItemQty, clearItems, fetchCart } = useCart();
  const { user } = useAuth();
  const navigate = useNavigate();

  if (!isOpen) return null;

  const handleCheckout = async () => {
    try {
      await placeOrder(user.userId);
      toast.success('Order placed successfully!');
      await fetchCart();
      onClose();
      navigate('/orders');
    } catch (err) {
      const msg = err.response?.data?.message || 'Checkout failed';
      toast.error(msg);
    }
  };

  return (
    <>
      <div className="drawer-overlay" onClick={onClose} />
      <div className={`drawer glass ${isOpen ? 'open' : ''}`}>
        <div className="drawer-header">
          <h2>Your Cart ({cart.totalItems})</h2>
          <button className="close-btn" onClick={onClose}>
            <X size={24} />
          </button>
        </div>

        <div className="drawer-content">
          {!user ? (
            <div className="empty-cart">
              <p>Please login to view your cart</p>
              <button 
                className="btn btn-primary mt-4" 
                onClick={() => { onClose(); navigate('/login'); }}
              >
                Login
              </button>
            </div>
          ) : cart.items && cart.items.length > 0 ? (
            <div className="cart-items">
              {cart.items.map((item) => (
                <div key={item.cartItemId} className="cart-item">
                  <div className="item-info">
                    <h4>{item.productName}</h4>
                    <p className="item-price">₹{item.productPrice.toLocaleString('en-IN')}</p>
                  </div>
                  
                  <div className="item-actions">
                    <div className="qty-controls">
                      <button 
                        onClick={() => updateItemQty(item.cartItemId, item.quantity - 1)}
                        disabled={item.quantity <= 1}
                      >
                        <Minus size={14} />
                      </button>
                      <span>{item.quantity}</span>
                      <button onClick={() => updateItemQty(item.cartItemId, item.quantity + 1)}>
                        <Plus size={14} />
                      </button>
                    </div>
                    
                    <div className="item-subtotal">
                      ₹{item.subtotal.toLocaleString('en-IN')}
                    </div>
                    
                    <button 
                      className="remove-btn"
                      onClick={() => removeItem(item.cartItemId)}
                      title="Remove Item"
                    >
                      <Trash2 size={16} />
                    </button>
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <div className="empty-cart">
              <p>Your cart is empty</p>
              <button className="btn btn-outline mt-4" onClick={onClose}>
                Continue Shopping
              </button>
            </div>
          )}
        </div>

        {user && cart.items && cart.items.length > 0 && (
          <div className="drawer-footer glass">
            <div className="summary-row">
              <span>Total</span>
              <span className="total-price">₹{cart.totalPrice.toLocaleString('en-IN')}</span>
            </div>
            
            <div className="footer-actions">
              <button className="btn btn-danger" onClick={clearItems}>
                Clear
              </button>
              <button className="btn btn-primary checkout-btn" onClick={handleCheckout}>
                Checkout <ChevronRight size={18} />
              </button>
            </div>
          </div>
        )}
      </div>
    </>
  );
};

export default CartDrawer;
