const {createApp} = Vue;

const app = createApp({
    data(){
        return{
            client:[],
            account: [],
            loan:[],
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
            })
            .catch(error => console.log(error))
        },
    }
});
app.mount('#app')