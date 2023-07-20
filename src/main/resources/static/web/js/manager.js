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
            },
            availablePayments: [3, 6, 9, 12, 18, 24, 36, 48, 60],
            loanDTO:{
                name:"",
                maxAmount: null,
                payments:[],
                percentage: null
            },
            loans:[]
            
        }
        
    },
    created (){
        this.loadData();
        this.loan();
        
    },
    methods: {
        loadData(){
            axios.get("http://localhost:8080/api/clients")
            .then((res) =>{
                this.clients = res.data;
                
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
            axios.post("http://localhost:8080/rest/clients", this.clientData)
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
        },
        signOut(){
            axios.post('/api/logout')
            .then(response => {
                console.log('signed out!!!')
                window.location.href = "/web/pages/login.html"
            }).catch(error => console.log(error))
        },
        createdLoan(){
            let loanDTO =  {
                name: this.loanDTO.name,
                maxAmount: this.loanDTO.maxAmount,
                payments: this.loanDTO.payments,
                percentage: this.loanDTO.percentage,
              }
            axios.post('/api/loans/createdLoans', loanDTO)
            .then(res => {
                console.log(res.data);

            }).catch(err => console.log(err))
        },
        loan(){
              axios.get("/rest/loans")
                .then(res => {
                  console.log(res.data._embedded.loans);
                  this.loans = res.data._embedded.loans
                })
                .catch(error => {
                  this.error1 = error.response.data
              Swal.fire({
                  icon:'error',
                  title: `${this.error1}`});
              console.log(error)});
        }

    }

})
app.mount ('#app')