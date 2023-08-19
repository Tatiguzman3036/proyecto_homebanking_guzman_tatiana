const {createApp} = Vue;

const app = createApp({
    data(){
        return{
            client:[],
            account: [],
            loan:[],
            card:[],
            createdAccounts:[],
            id:"",
            accountDTO:{
                accountType: ""
            },
            error1: ""
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
                this.loan= res.data.loans
                this.loan.sort((itemA,itemB) => itemA.id - itemB.id)
                console.log(this.loan);
              
            })
            .catch(error => console.log(error))
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
        loadData1(){
            axios.get("/api/clients/accounts")
            .then(res => {
                this.account = res.data;
                this.account.sort((itemA, itemB)=> itemA.id - itemB.id).filter(account => account.hidden !== true)
                console.log(res);
            })
            .catch(error => console.log(error))
        },
        createAccount() {
            Swal.fire({
              title: 'Select account type',
              input: 'select',
              inputOptions: {
                'SAVINGS': 'SAVINGS',
                'CURRENT': 'CURRENT'
              },
              inputPlaceholder: 'Select account type',
              showCancelButton: true,
              confirmButtonText: 'Create',
              cancelButtonText: 'Cancel',
              icon: 'question'
            }).then((result) => {
              if (result.isConfirmed) {
                const accountType = result.value;
                axios.post(`/api/clients/current/accounts?accountType=${accountType}`)
                  .then(res => {
                    console.log(res);
                    Swal.fire({
                      position: 'center',
                      title: 'Account created successfully!',
                      showConfirmButton: false,
                      timer: 1500
                    })
                    setTimeout(()=>{
                        window.location.href = "accounts.html"
                      },1800)
                  })
                  .catch(err =>  {
                    this.error1 = err.response.data
                Swal.fire({
                    icon:'error',
                    title: `${this.error1}`});
                console.log(err)});
              }
            });
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
        deleteAccount(id) {
          Swal.fire({
              title: 'Are you sure?',
              text: 'Once deleted, you will not be able to recover this account!',
              icon: 'warning',
              showCancelButton: true,
              confirmButtonText: 'Yes, delete it!',
              cancelButtonText: 'Cancel'
          }).then((result) => {
              if (result.isConfirmed) {
                  this.id = id;
                  axios.patch(`/api/accounts/${this.id}/hidden`)
                      .then(response => {
                          console.log(response.data);
                          location.reload();
                          this.account = this.account.filter(account => account.id !== id);
                      })
                      .catch(err =>   {
                        this.error1 = err.response.data
                    Swal.fire({
                        icon:'error',
                        title: `${this.error1}`});
                    console.log(err)});
              }
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

