import {Alert, AlertIcon, Wrap, WrapItem, Spinner} from '@chakra-ui/react'
import SidebarWithHeader from "./components/shared/SideBar.jsx";
import {useEffect , useState} from "react";
import {getCustomers} from "./services/client.js";
import CardWithImage from "./components/Card.jsx";
import CreateCustomerDrawer from "./components/CreateCustomerDrawer.jsx";
import {errorNotification} from "./services/notification.js";

const App= ()=>{

    const [customers , setCustomers] = useState([]);
    const [loading , setLoading] = useState(false);
    const [err , setError] = useState("");

    const fetchCustomers = () => {
        setLoading(true);
        getCustomers().then(
            res=>{
                setCustomers(res.data);
            }).catch(err=>{
                setError(err.response.data.message)
                errorNotification(
                    err.code,
                    err.response.data.message
                )
            }).finally(()=>{
                setLoading(false);
            })
    }


    useEffect(() => {
        fetchCustomers();
    }, []);


    if(err!==""){
        return (
            <SidebarWithHeader>
                <CreateCustomerDrawer
                    fetchCustomers = {fetchCustomers}
                ></CreateCustomerDrawer>
                <Alert mt={5} status='error'>
                    <AlertIcon />
                    Oooops there was an error
                </Alert>
            </SidebarWithHeader>
        )
    }



    if(loading){
        return (
            <SidebarWithHeader>
                <Spinner
                    thickness='4px'
                    speed='0.65s'
                    emptyColor='gray.200'
                    color='blue.500'
                    size='xl'
                />
            </SidebarWithHeader>
        )
    }

    if(customers.length <= 0){
        return (
            <SidebarWithHeader>
                <CreateCustomerDrawer
                    fetchCustomers = {fetchCustomers}
                ></CreateCustomerDrawer>
                <Alert mt={5} status='warning'>
                    <AlertIcon />
                    Customers list is empty
                </Alert>
            </SidebarWithHeader>
        )
    }

    return (
        <SidebarWithHeader>
            <CreateCustomerDrawer
                fetchCustomers = {fetchCustomers}
            ></CreateCustomerDrawer>
            <Wrap justify={"center"} spacing={"30px"}>
                {customers.map((customer , index) => (
                    <WrapItem key={index}>
                        <CardWithImage
                            {...customer}
                            number={index}
                            fetchCustomers = {fetchCustomers}
                        >
                        </CardWithImage>
                    </WrapItem>
                ))}
            </Wrap>

        </SidebarWithHeader>
    )
}

export default App;