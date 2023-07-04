const {createApp} = Vue;

const app = createApp({
    data(){
        return{
            account: [],
            transferDTO: {
            amount: 0.0,
            accountOrigin: "",
            accountDestination:"",
            description:"",
            }
            

        }
    },
    created(){
        axios.get(`http://localhost:8080/api/clients/current`)
        .then(res =>{
            this.account = res.data.accounts
            this.account.sort((itemA, itemB)=> itemA.id - itemB.id);
            console.log(this.account);
        }).catch(error => console.log(error))
    },
    methods:{
        signOut(){
            axios.post('/api/logout')
            .then(response => {
                console.log('signed out!!!')
                window.location.href = "/web/pages/login.html"
            }).catch(error => console.log(error))
        },
        makeTransfer(){
            console.log(this.transferDTO.amount, this.transferDTO.accountDestination, this.transferDTO.accountOrigin, this.transferDTO.description);
           // Crea un objeto JSON para enviar en la solicitud POST
            const transferData = {
                amount: this.transferDTO.amount,
                accountOrigin: this.transferDTO.accountOrigin,
                accountDestination: this.transferDTO.accountDestination,
                description: this.transferDTO.description
            };
            axios.post('http://localhost:8080/api/transactions', transferData)
            .then(res =>
                console.log(res),
                /* window.location.href = "accounts.html" */
                ).catch(error => console.log(error))
        },
        makeTransfer1() {
            console.log(this.transferDTO.amount, this.transferDTO.accountDestination, this.transferDTO.accountOrigin, this.transferDTO.description);
            
            // Filtrar las cuentas disponibles para la cuenta de destino
            const availableAccounts = this.account.filter(acc => !currentClientAccounts.includes(acc.number));
            
            // Validar que la cuenta de destino seleccionada esté en las cuentas disponibles
            if (!availableAccounts.find(acc => acc.number === this.transferDTO.accountDestination)) {
              console.log('La cuenta de destino seleccionada no es válida.');
              return;
            }
            
            // Crear el objeto JSON para enviar en la solicitud POST
            const transferData = {
              amount: this.transferDTO.amount,
              accountOrigin: this.transferDTO.accountOrigin,
              accountDestination: this.transferDTO.accountDestination,
              description: this.transferDTO.description
            };
            
            axios.post('http://localhost:8080/api/transactions', transferData)
              .then(res => {
                console.log(res);
                /* window.location.href = "accounts.html" */
              })
              .catch(error => console.log(error));
          }
          
          

    }
})
app.mount('#app')
/* makeTransfer() {
  console.log(this.tranferDTO.amount, this.tranferDTO.accountDestiny, this.tranferDTO.accountOrigin, this.tranferDTO.description);
  
  
  
  axios.post('http://localhost:8080/api/transactions', transferData)
    .then(res => {
      console.log(res);
      // Resto del código después de una respuesta exitosa
    })
    .catch(error => {
      console.log(error);
      // Resto del código para manejar el error
    });
}
 */