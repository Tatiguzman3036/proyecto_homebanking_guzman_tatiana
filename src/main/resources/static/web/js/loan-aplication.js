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
            },
            error1:""
            }
    },created(){
        this.typeofloans()
        this.loadData()
    },
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
            Swal.fire({
              title: 'Are you sure you want to proceed with the loan?',
              icon: 'question',
              showCancelButton: true,
              confirmButtonText: 'Confirm',
              cancelButtonText: 'Cancel',
              showCheckbox: true,
              input: 'checkbox',
              inputValue: 1,
              inputPlaceholder: 'I agree to the terms and conditions',
              inputValidator: (result) => {
                return !result && 'You need to agree to the terms and conditions'
              }
            }).then((result) => {
              if (result.isConfirmed) {
                let loanData = {
                  idLoanType: this.loanDTO.idLoanType,
                  accountDestination: this.loanDTO.accountDestination,
                  amount: this.loanDTO.amount,
                  payments: this.loanDTO.payments,
                };
                console.log(loanData);
                axios.post('/api/loans', loanData)
                  .then(res => {
                    if(res.status == 201){
                        console.log(res);
                        Swal.fire({
                          position: 'center',
                          title: 'Loan OK!',
                          showConfirmButton: false,
                          timer: 1500
                      })
                        setTimeout(()=>{
                          window.location.href = "accounts.html"
                        },1800)
                      }
                  })
                  .catch(error => {
                    this.error1 = error.response.data
                Swal.fire({
                    icon:'error',
                    title: `${this.error1}`});
                console.log(error)});
              }
            }).catch(err => console.log(err))
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