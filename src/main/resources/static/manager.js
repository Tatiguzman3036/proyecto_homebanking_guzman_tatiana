const {createApp} = Vue;

const app = createApp ({
    data(){
        return{
            clients: [],
            restResponse: [],
            clientData: {
                firstName: "",
                lastName:"",
                email:"" 
            }
        }
        
    },
    created (){
        this.loadData()
    },
    methods: {
        loadData(){
            axios.get("http://localhost:8080/clients")
            .then((res) =>{
                this.clients = res.data._embedded.clients;
                this.restResponse = res.data;
                console.log(this.clients);

            })
            .catch(error => console.log(error))
        },
        addClient (){
            if(this.clientData.firstName != "" && this.clientData.lastName != "" && this.clientData.email != ""){
                this.postClient()
            }else {
                alert("It is necessary to complete all the fields...")
            }
            
        },
        
        postClient(){
            axios.post("http://localhost:8080/clients", this.clientData)
            .then(response => {
                this.loadData()
            })
            .catch(error => console.log(error))
        },
        deleteClient(id){
            axios.delete(id)
            .then (response => {
                this.loadData()
            })
            .catch(error => console.log(error))
        }

    }

})
app.mount ('#app')