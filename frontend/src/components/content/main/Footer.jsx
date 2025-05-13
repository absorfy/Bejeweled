import React from 'react';
import styles from './Footer.module.css';

const Footer = () => (
  <footer className={styles.footer}>
    <hr />
    <div className={styles.footerContent}>
      <img src="/images/kpi-logo.png" alt="Logo" className={styles.footerLogo} />
      <span>
        &copy; 2025 Gamestudio was developed by Vladyslav Krykun at{' '}
        <a href="http://kpi.fei.tuke.sk" target="_blank" rel="noopener noreferrer">
          KPI FEI TUKE
        </a>
      </span>
    </div>
  </footer>
);

export default Footer;
