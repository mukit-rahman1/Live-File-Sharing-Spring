import { StrictMode } from 'react';
import { createRoot } from 'react-dom/client';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import './index.css';
import App from './App.tsx';
import LoginPage from './pages/loginpage.tsx';
import RegisterPage from './pages/registerpage.tsx';
import HomePage from './pages/homepage.tsx';
import UploadPage from './pages/upload.tsx';
import ViewImages from './pages/view.tsx';
import ConnectionPage from './pages/connect.tsx';
import Landing from './pages/Landing';

const router = createBrowserRouter([
  {
    path: "/",
    element: <Landing />
  },
  {
    path: "/app",
    element: <App />
  },
  {
    path: "/register",
    element: <RegisterPage />
  },
  {
    path: "/login",
    element: <LoginPage />
  },
  {
    path: "/homepage",
    element: (
        <HomePage />
    )
  },
  {
    path: "/imagetransfer", 
    element: (
        <ConnectionPage />
    )
  },
  {
    path:'/uploadimage',
    element: (
        <UploadPage />
    )
  },
  {
    path:'/viewstoredimages',
    element: (
        <ViewImages />
    )
  }
])

createRoot(document.getElementById('root')!).render(
  <StrictMode>
      <RouterProvider router={router} />
  </StrictMode>,
)
