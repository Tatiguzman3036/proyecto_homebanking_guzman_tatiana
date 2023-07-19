const {createApp} = Vue;

const app = createApp({
    data(){
        return{
            client:[],
            account: [],
            loan:[],
            card:[],
            createdAccounts:[],
            id:""
        }
    },
    created(){
        this.loadData();
        this.loadData1()
    },
    methods:{
        loadData(){
            axios.get("/api/clients/current")
            .then(res => {
                this.client = res.data
                console.log(this.client);
                /* this.account = res.data.accounts;
                this.account.sort((itemA, itemB)=> itemA.id - itemB.id).filter(account => account.hidden !== true)
                console.log(this.account); */
                this.loan= res.data.loans
                this.loan.sort((itemA,itemB) => itemA.id - itemB.id)
                console.log(this.loan);
            })
            .catch(error => console.log(error))
        },
        loadData1(){
            axios.get("/api/clients/accounts")
            .then(res => {
                /* this.client = res.data
                console.log(this.client); */
                this.account = res.data;
                this.account.sort((itemA, itemB)=> itemA.id - itemB.id).filter(account => account.hidden !== true)
                console.log(res);
            })
            .catch(error => console.log(error))
        },
        created(){
            axios.post("/api/clients/current/accounts")
            .then(res => {
                    this.loadData()
                    if(res.status == 201){
                        console.log(res);
                        Swal.fire({
                          position: 'center',
                          title: 'Account Created',
                          showConfirmButton: false,
                          timer: 1500
                      })
                        setTimeout(()=>{
                          window.location.href = "accounts.html"
                        },1800)
                      }
            }).catch(error =>
                console.log(error))
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
        deleteAccount(id){
            this.id = id
            axios.patch(`/api/accounts/${this.id}/hidden`)
              .then(response => {
                console.log(response.data);
                location.reload()
                this.account.filter(account => account.id !== id);
              })
              .catch(error => {
                console.error(error.response.data)
            });
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