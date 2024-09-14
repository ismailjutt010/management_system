import {
    Button,
    Drawer,
    DrawerBody,
    DrawerCloseButton,
    DrawerContent, DrawerFooter,
    DrawerHeader,
    DrawerOverlay, Input, useDisclosure
} from "@chakra-ui/react";
import CreateCustomerForm from "./CreateCustomerForm.jsx";


const AddIcon = ()=> "+"
const closeIcon = ()=> "x"

const CreateCustomerDrawer = ({fetchCustomers})=>{
    const { isOpen, onOpen, onClose } = useDisclosure()

    return(
        <>
            <Button leftIcon={AddIcon()} onClick={onOpen} colorScheme={"teal"}>
                Create Customer
            </Button>

            <Drawer size={"xl"} isOpen={isOpen} onClose={onClose}>
                <DrawerOverlay />
                <DrawerContent>
                    <DrawerCloseButton />
                    <DrawerHeader>Create your account</DrawerHeader>

                    <DrawerBody>
                        <CreateCustomerForm
                            fetchCustomers = {fetchCustomers}
                        ></CreateCustomerForm>
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

export default CreateCustomerDrawer;