import {
    Button,
    Drawer,
    DrawerBody,
    DrawerCloseButton,
    DrawerContent, DrawerFooter,
    DrawerHeader,
    DrawerOverlay,
    useDisclosure
} from "@chakra-ui/react";
import UpdateCustomerForm from "./UpdateCustomerForm.jsx";


const AddIcon = ()=> "+"
const closeIcon = ()=> "x"

const UpdateCustomerDrawer = ({fetchCustomers , initialValues , customerId})=>{
    const { isOpen, onOpen, onClose } = useDisclosure()

    return(
        <>
            <Button onClick={onOpen}
                    //colorScheme={"teal"}
                    rounded={'full'}
                    _hover={{
                        transform: 'translateY(-2px)',
                        boxShadow: 'lg'
                    }}
                    _focus={{
                        bg:'grey.500'
                    }}
            >
                Update Customer
            </Button>

            <Drawer size={"xl"} isOpen={isOpen} onClose={onClose}>
                <DrawerOverlay />
                <DrawerContent>
                    <DrawerCloseButton />
                    <DrawerHeader>Create your account</DrawerHeader>

                    <DrawerBody>
                        <UpdateCustomerForm
                            fetchCustomers = {fetchCustomers}
                            initialValues={initialValues}
                            customerId={customerId}
                        ></UpdateCustomerForm>
                    </DrawerBody>

                    <DrawerFooter>
                        <Button leftIcon={closeIcon()} onClick={onClose} colorScheme={"teal"}>
                            Close
                        </Button>
                    </DrawerFooter>
                </DrawerContent>
            </Drawer>
        </>
    )
}

export default UpdateCustomerDrawer;