import React from 'react';
import NavbarPpal from '../components/NavbarPpal';
import VulcanoMain from '../components/VulcanoMain';
import VulcanoCarousel from '../components/VulcanoCarousel';
import VulcanoFooter from '../components/VulcanoFooter';
import VulcanoLogin from '../components/VulcanoLogin';
import '../styles/index.css';


const VulcanoHome = () => {
  return (
    <div className="vh-container">
      <NavbarPpal />
      <VulcanoMain />
      <VulcanoCarousel />    
      <VulcanoFooter />
    </div>
  );
};

export default VulcanoHome;