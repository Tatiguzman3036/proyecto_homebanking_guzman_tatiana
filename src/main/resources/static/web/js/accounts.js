const {createApp} = Vue;

const app = createApp({
    data(){
        return{
            client:[],
            account: [],
            loan:[],
            card:[],
        }
    },
    created(){
        this.loadData()
    },
    methods:{
        loadData(){
            axios.get("http://localhost:8080/api/clients/1")
            .then(res => {
                this.client = res.data
                console.log(this.client);
                this.account = res.data.accounts;
                this.account.sort((itemA, itemB)=> itemA.id - itemB.id)
                console.log(this.account);
                this.loan= res.data.loans
                this.loan.sort((itemA,itemB) => itemA.id - itemB.id)
                console.log(this.loan);
                this.card = this.client.cards;
                this.card((itemA,itemB) => itemA.id - itemB.id)
                console.log(this.card);

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
            
        }
    }
});
app.mount('#app')