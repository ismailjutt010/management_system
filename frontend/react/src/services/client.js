import axios from "axios";

export const getCustomers = async () => {
    try {
        return await axios.get(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customers/getAllCustomers`)
    } catch (e) {
        throw e;
    }
}

export const saveCustomer = async (customer) => {
    try {
        return await  axios.post(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customers/addCustomer`, customer)
    }catch (e) {
        throw  e;
    }
}


export const deleteCustomer = async (id) => {
    try {
        return await  axios.delete(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customers/deleteCustomerById/${id}`)
    }catch (e) {
        throw  e;
    }
}

export const updateCustomer = async (id,update) => {
    try {
        return await  axios.put(`${import.meta.env.VITE_API_BASE_URL}/api/v1/customers/updateCustomer/${id}`, update)
    }catch (e) {
        throw  e;
    }
}