const {createApp} = Vue;

const app = createApp({
    data(){
        return{
            allAccount: [],
            id: "",
            transactions:[],

        }
    },
    created(){
        let parametros = new URLSearchParams (location.search)
        this.id = parametros.get("id")
        axios.get(`http://localhost:8080/api/accounts/${this.id}`)
        .then(res =>{
            this.allAccount = res.data
            console.log(this.allAccount);
            this.transactions = this.allAccount.transaction;
            this.transactions.sort((itemA, itemB) => itemB.id - itemA.id);
            console.log(this.transactions);
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
                paymentAmount(amount, payments){
          let resultado = amount / payments;
          let format = parseFloat(resultado.toFixed(2))
          return format.toLocaleString()
        },
        amount(monto){
            let amount = parseFloat(monto.toFixed(2))
            return amount.toLocaleString()
          },

    }
})
app.mount('#app')