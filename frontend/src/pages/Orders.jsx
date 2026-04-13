import { useEffect, useState } from 'react';
import { getUserOrders, cancelOrder } from '../services/api';
import { useAuth } from '../context/AuthContext';
import { Package, XCircle, CheckCircle, Clock } from 'lucide-react';
import toast from 'react-hot-toast';

const Orders = () => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const { user } = useAuth();

  const fetchOrders = async () => {
    try {
      setLoading(true);
      const res = await getUserOrders(user.userId);
      if (res.data) {
        // Sort by newest first
        setOrders(res.data.sort((a,b) => new Date(b.orderDate) - new Date(a.orderDate)));
      }
    } catch (err) {
       console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (user) {
      fetchOrders();
    }
  }, [user]);

  const handleCancel = async (orderId) => {
    if(!window.confirm('Are you sure you want to cancel this order?')) return;
    try {
      await cancelOrder(orderId);
      toast.success('Order cancelled. Stock restored and amount refunded.');
      fetchOrders();
    } catch (err) {
      toast.error('Failed to cancel order.');
    }
  };

  const formatDate = (dateString) => {
    const options = { year: 'numeric', month: 'short', day: 'numeric', hour: '2-digit', minute: '2-digit' };
    return new Date(dateString).toLocaleDateString('en-IN', options);
  };

  if (loading) {
    return <div className="loader-container"><div className="spinner"></div></div>;
  }

  return (
    <div className="container">
      <div className="mt-8 mb-8">
        <h2 className="text-gradient" style={{ fontSize: '2rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
          <Package /> My Orders
        </h2>
        <p style={{ color: 'var(--text-secondary)' }}>View and manage your purchase history</p>
      </div>

      {orders.length === 0 ? (
        <div className="glass-card" style={{ padding: '4rem 2rem', textAlign: 'center' }}>
          <Package size={48} style={{ color: 'var(--text-secondary)', margin: '0 auto 1rem', opacity: 0.5 }} />
          <h3>No orders found</h3>
          <p style={{ color: 'var(--text-secondary)', marginTop: '0.5rem' }}>Looks like you haven't placed any orders yet.</p>
        </div>
      ) : (
        <div style={{ display: 'flex', flexDirection: 'column', gap: '1.5rem' }}>
          {orders.map(order => (
            <div key={order.orderId} className="glass-card" style={{ padding: '1.5rem' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', flexWrap: 'wrap', gap: '1rem', borderBottom: '1px solid var(--border-color)', paddingBottom: '1rem', marginBottom: '1rem' }}>
                <div>
                  <h3 style={{ fontSize: '1.1rem', marginBottom: '0.25rem' }}>Order #{order.orderId}</h3>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', color: 'var(--text-secondary)', fontSize: '0.85rem' }}>
                    <Clock size={14} /> {formatDate(order.orderDate)}
                  </div>
                </div>
                
                <div style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
                  <div style={{ textAlign: 'right' }}>
                    <div style={{ fontSize: '0.85rem', color: 'var(--text-secondary)' }}>Total Amount</div>
                    <div style={{ fontSize: '1.25rem', fontWeight: 'bold', color: 'var(--text-primary)' }}>
                      ₹{order.totalAmount.toLocaleString('en-IN')}
                    </div>
                  </div>
                  
                  {order.orderStatus === 'PLACED' && (
                    <button onClick={() => handleCancel(order.orderId)} className="btn btn-danger" style={{ padding: '0.5rem 1rem' }}>
                      Cancel Order
                    </button>
                  )}
                </div>
              </div>

              <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '1.5rem', padding: '0.75rem', background: 'rgba(0,0,0,0.2)', borderRadius: '8px' }}>
                <div>
                  <span style={{ fontSize: '0.85rem', color: 'var(--text-secondary)' }}>Status: </span>
                  <span style={{ fontWeight: 'bold', color: order.orderStatus === 'CANCELLED' ? 'var(--danger)' : 'var(--success)' }}>
                    {order.orderStatus === 'CANCELLED' ? <XCircle size={14} style={{display:'inline', verticalAlign:'middle'}}/> : <CheckCircle size={14} style={{display:'inline', verticalAlign:'middle'}}/>} {order.orderStatus}
                  </span>
                </div>
                <div>
                  <span style={{ fontSize: '0.85rem', color: 'var(--text-secondary)' }}>Payment: </span>
                  <span style={{ fontWeight: 'bold' }}>{order.paymentStatus}</span>
                </div>
              </div>

              <div>
                <h4 style={{ fontSize: '0.9rem', color: 'var(--text-secondary)', marginBottom: '0.75rem', textTransform: 'uppercase', letterSpacing: '0.05em' }}>Items Purchased</h4>
                <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(250px, 1fr))', gap: '1rem' }}>
                  {order.items.map(item => (
                    <div key={item.orderItemId} style={{ display: 'flex', alignItems: 'center', gap: '1rem', background: 'rgba(255,255,255,0.03)', padding: '0.75rem', borderRadius: '8px', border: '1px solid rgba(255,255,255,0.05)' }}>
                      <div style={{ width: '40px', height: '40px', background: 'rgba(59,130,246,0.1)', borderRadius: '6px', display: 'flex', alignItems: 'center', justifyContent: 'center', color: 'var(--accent-primary)' }}>
                        <Package size={20} />
                      </div>
                      <div style={{ flex: 1 }}>
                        <div style={{ fontWeight: '600', fontSize: '0.95rem' }}>{item.productName}</div>
                        <div style={{ color: 'var(--text-secondary)', fontSize: '0.85rem' }}>Qty: {item.quantity} × ₹{item.price.toLocaleString('en-IN')}</div>
                      </div>
                      <div style={{ fontWeight: 'bold' }}>
                        ₹{item.subtotal.toLocaleString('en-IN')}
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default Orders;
