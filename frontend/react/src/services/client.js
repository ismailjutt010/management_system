import axios from "axios";

const getAuthConfig = () => ({
    headers: {
        Authorization:`Bearer ${localStorage.getItem("token")}`
    }
})

export const getCustomers = async () => {
    // eslint-disable-next-line no-useless-catch
    try {
        return await axios.get(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customers/getAllCustomers` , getAuthConfig())
    } catch (e) {
        throw e;
    }
}

export const saveCustomer = async (customer) => {
    // eslint-disable-next-line no-useless-catch
    try {
        return await  axios.post(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customers/addCustomer`, customer)
    }catch (e) {
        throw  e;
    }
}


export const deleteCustomer = async (id) => {
    // eslint-disable-next-line no-useless-catch
    try {
        return await  axios.delete(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customers/deleteCustomerById/${id}` , getAuthConfig())
    }catch (e) {
        throw  e;
    }
}

export const updateCustomer = async (id,update) => {
    // eslint-disable-next-line no-useless-catch
    try {
        return await  axios.put(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customers/updateCustomer/${id}`,
            update,
            getAuthConfig()
        )
    }catch (e) {
        throw  e;
    }
}

export const login = async (usernameAndPassword) => {
    // eslint-disable-next-line no-useless-catch
    try {
        return await  axios.post(`${import.meta.env.VITE_API_BASE_URL}/api/v1/auth/login`,usernameAndPassword)
    }catch (e) {
        throw  e;
    }
}