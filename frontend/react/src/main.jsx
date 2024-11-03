import {StrictMode} from 'react'
import {createRoot} from 'react-dom/client'
import App from './App.jsx'
import {ChakraProvider} from '@chakra-ui/react'
import './index.css'
import { createStandaloneToast } from '@chakra-ui/react'
import {createBrowserRouter, RouterProvider} from "react-router-dom";
import Login from "./components/login/Login.jsx";
import AuthProvider from "./components/Context/AuthContext.jsx";
import ProtectedRoute from "./components/shared/ProtectedRoute.jsx";
const { ToastContainer } = createStandaloneToast()
const router = createBrowserRouter([
    {
        path: "/",
        element: <Login></Login>

    },
    {
        path: "dashboard",
        element:
            <ProtectedRoute>
                <App></App>
            </ProtectedRoute>

    }
])
createRoot(document.getElementById('root')).render(<StrictMode>
    <ChakraProvider>
        <AuthProvider>
            <RouterProvider router={router} />
        </AuthProvider>
        <ToastContainer/>
    </ChakraProvider>
</StrictMode>,)
