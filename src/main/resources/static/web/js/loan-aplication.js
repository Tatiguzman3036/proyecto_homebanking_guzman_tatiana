const {createApp} = Vue;
const app = createApp ({
    data(){
        return{
            loans: [],
            account:[],
            paymentsOptions:[],
            amountentered: 0,
            loanDTO:{
                idLoanType: null,
                accountDestination:"",
                amount: null,
                payments: "",
            }
            }
    },created(){
        this.typeofloans()
        this.loadData()
    },
    /* computed:{
        totalPayment(){
            return this.amountentered + this.amountentered * 0.2;
        },
        amountWithpayments(){
            return this.totalPayment / this.selectedPaymentsOption;
        }
    }, */
    methods: {
        typeofloans(){
            axios.get("/api/loans")
            .then(res =>{
                this.loans = res.data
                console.log(this.loans);
            }).catch(err => console.log(err))
        },
        loadData(){
            axios.get(`/api/clients/current`)
        .then(res =>{
            this.account = res.data.accounts

            console.log(this.account);
        }).catch(error => console.log(error))
        },
        createdLoans() {
            let loanData = {
              idLoanType: this.loanDTO.idLoanType,
              accountDestination: this.loanDTO.accountDestination,
              amount: this.loanDTO.amount,
              payments: this.loanDTO.payments,
            };
          console.log(loanData);
            axios.post('/api/loans', loanData)
              .then(res => {
                console.log(res);
              })
              .catch(error => {
                console.log(error);
              });
          }
    },
    watch: {
        "loanDTO.idLoanType"(newType){
            const selectedLoan = this.loans.find(loan => loan.id === newType);
            this.paymentsOptions = selectedLoan.payments;
        }
    },
})
app.mount('#app')