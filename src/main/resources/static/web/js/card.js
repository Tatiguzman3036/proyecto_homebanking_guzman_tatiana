const {createApp} = Vue;

const app = createApp({
    data(){
        return{
            client:[],
            card: [],
            creditCard:[],
            debitCard: [],
        }
    },
    created(){
        this.loadData()
    },
    methods:{
        loadData(){
            axios.get("http://localhost:8080/api/clients/current")
            .then(res => {
                this.client = res.data
                console.log(this.client);
                this.card = this.client.cards
                console.log(this.card);
                this.creditCard = this.card.filter(card => card.type == "CREDIT").sort((itemA, itemB)=> itemA.id - itemB.id)
                console.log(this.creditCard);
                this.debitCard = this.card.filter(card => card.type == "DEBIT").sort((itemA, itemB)=> itemA.id - itemB.id)
                console.log(this.debitCard);
                
            })
            .catch(error => console.log(error))
        },
        colorType(card){
            if (card.color === "GOLD") {
                return 'gold'
            }else if(card.color === "SILVER"){
                return 'silver'
            }else if(card.color === "TITANIUM"){
                return 'titanium'
            }
            
        },
        signOut(){
            axios.post('/api/logout')
            .then(response => {
                console.log('signed out!!!')
                window.location.href = "/web/pages/login.html"
            }).catch(error => console.log(error))
        }
    }
});
app.mount('#app')