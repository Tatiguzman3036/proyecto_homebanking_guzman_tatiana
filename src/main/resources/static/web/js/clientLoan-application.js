const {createApp} = Vue;

const app = createApp({
    data(){
        return{
            loanToPay: null,
            account:"",
            loan:[],
            accounts:"",
            error1:""
        }
    },
    created(){
        this.loadData(),
        this.loadData1()
    },
    computed: {
        selectedLoan() {
          return this.loan.find(loan => loan.id === this.loanToPay);
        }
    },
    methods:{
        payToLoan(){
            if(this.loanToPay === null && this.account.length === 0){
                Swal.fire({
                    position: 'center',
                    icon: 'warning',
                    title: "Please enter the data!",
                    showConfirmButton: false,
                    timer:2000
                })
            }else if(this.loanToPay !== null && this.account.length === 0){
                Swal.fire({
                    position: 'center',
                    icon: 'warning',
                    title: "Please enter the accoun to pay!",
                    showConfirmButton: false,
                    timer:2000
                })
            }else if(this.loanToPay === null && this.account.length !== 0){
                Swal.fire({
                    position: 'center',
                    icon: 'warning',
                    title: "Please enter type of loan!",
                    showConfirmButton: false,
                    timer:2000
                })
            }else{
            axios.post(`/api/clientLoan/payments?loanToPay=${this.loanToPay}&account=${this.account}`)
            .then(res => {
                if(res.status == 200){
                    console.log(res);
                    Swal.fire({
                      position: 'center',
                      icon: 'success',
                      title: 'Loan OK!',
                      showConfirmButton: false,
                      timer: 1500
                  })
                    setTimeout(()=>{
                      window.location.href = "accounts.html"
                    },1800)
                  }
            }).catch(err =>  {
                this.error1 = err.response.data
            Swal.fire({
                icon:'error',
                title: `${this.error1}`});
            console.log(err)})
        }
        },
        loadData(){
            axios.get("/api/clients/current")
            .then(res => {
                this.loan= res.data.loans
                this.loan.sort((itemA,itemB) => itemA.id - itemB.id)
                console.log(this.loan);
            })
            .catch(error => console.log(error))
        },
        loadData1(){
            axios.get(`/api/clients/accounts`)
            .then(res => {
                this.accounts = res.data
                this.accounts.sort((itemA,itemB) => itemA.id - itemB.id)
                console.log(this.accounts);
            })
            .catch(error => console.log(error))
        },
        signOut(){
            axios.post('/api/logout')
            .then(response => {
                console.log('signed out!!!')
                window.location.href = "/web/pages/login.html"
            }).catch(error => console.log(error))
        }
    }
})
app.mount('#app')