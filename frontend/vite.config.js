import {defineConfig, loadEnv} from 'vite';
import react from '@vitejs/plugin-react';
import {qrcode} from "vite-plugin-qrcode";

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd())

  return {
    plugins: [react(), qrcode()],
    server: {
      host: true,
      port: 5173,
      allowedHosts: ['.ngrok-free.app'],
      proxy: {
        '/api': {
          target: `${env.VITE_BASE_URL}`,
          changeOrigin: true,
          secure: false,
        },
        '/ws': {
          target: `${env.VITE_BASE_URL}`,
          ws: true,
          changeOrigin: true,
          secure: false,
        },
      },
    },
    define: {
      global: 'window',
    }
  }
});
