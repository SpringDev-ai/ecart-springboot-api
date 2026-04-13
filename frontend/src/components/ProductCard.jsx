import { ShoppingBag, Star } from 'lucide-react';
import './ProductCard.css';

const ProductCard = ({ product, onAdd }) => {
  // Use picsum for reliable placeholders
  const fallbackImage = `https://picsum.photos/seed/${product.productId || 'demo'}/400/300`;
  
  return (
    <div className="product-card glass-card">
      <div className="product-image-container">
        <img 
          src={product.imageUrl || fallbackImage} 
          alt={product.name} 
          className="product-image"
          onError={(e) => {
             // Fallback to placeholder if link is broken
             e.target.onerror = null; 
             e.target.src = 'https://picsum.photos/400/300?random=1';
          }}
        />
        {product.stock < 5 && product.stock > 0 && (
           <span className="stock-warning">Only {product.stock} left!</span>
        )}
        {product.stock === 0 && (
           <span className="stock-out">Out of Stock</span>
        )}
      </div>
      
      <div className="product-content">
        <div className="product-tags">
          <span className="badge badge-primary">{product.type}</span>
          <div className="rating">
            <Star size={14} className="star-icon" fill="currentColor" />
            <span>4.5</span>
          </div>
        </div>
        
        <h3 className="product-title">{product.name}</h3>
        <p className="product-desc">{product.description}</p>
        
        <div className="product-footer">
          <div className="product-price">₹{product.price.toLocaleString('en-IN')}</div>
          
          <button 
            className="btn btn-primary add-btn"
            onClick={() => onAdd(product.productId)}
            disabled={product.stock === 0}
          >
            <ShoppingBag size={18} />
          </button>
        </div>
      </div>
    </div>
  );
};

export default ProductCard;
