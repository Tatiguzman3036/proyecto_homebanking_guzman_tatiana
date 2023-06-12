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
        loadData(){
        }

    }
})
app.mount('#app')