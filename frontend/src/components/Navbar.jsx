import { Link, useNavigate } from 'react-router-dom';
import { ShoppingCart, User as UserIcon, LogOut, Package } from 'lucide-react';
import { useAuth } from '../context/AuthContext';
import { useCart } from '../context/CartContext';
import './Navbar.css'; // We'll add some specific nav CSS if needed

const Navbar = ({ onCartClick }) => {
  const { user, logout } = useAuth();
  const { cart } = useCart();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  return (
    <nav className="navbar glass">
      <div className="container navbar-container">
        <Link to="/" className="navbar-logo">
           <span className="text-gradient" style={{fontSize: '1.5rem', fontWeight: 700}}>E-Cart</span>
        </Link>

        <div className="navbar-actions">
          {user ? (
            <>
              <Link to="/orders" className="nav-btn" title="My Orders">
                <Package size={20} />
                <span className="nav-text">Orders</span>
              </Link>
              
              <button className="nav-btn cart-btn" onClick={onCartClick}>
                <ShoppingCart size={20} />
                {cart.totalItems > 0 && <span className="cart-badge">{cart.totalItems}</span>}
              </button>
              
              <div className="user-profile">
                <UserIcon size={18} />
                <span className="user-name">{user.name}</span>
              </div>
              
              <button className="nav-btn text-danger" onClick={handleLogout} title="Logout">
                <LogOut size={20} />
              </button>
            </>
          ) : (
            <>
              <Link to="/login" className="btn btn-outline">Login</Link>
              <Link to="/register" className="btn btn-primary">Sign Up</Link>
            </>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
