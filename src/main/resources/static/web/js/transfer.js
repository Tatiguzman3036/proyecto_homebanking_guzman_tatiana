const {createApp} = Vue;

const app = createApp({
    data(){
        return{
            account: [],
            transferDTO: {
            amount: null,
            accountOrigin: "",
            accountDestination:"",
            description:"",
            },
            error1:""
        }
    },
    created(){
        axios.get(`/api/clients/accounts`)
        .then(res =>{
            this.account = res.data
            this.account.sort((itemA, itemB)=> itemA.id - itemB.id);
            console.log(this.account);
        }).catch(error => console.log(error))
    },
    computed:{
      accountsExceptSelected(){
        return this.account.filter(account => account.number !== this.transferDTO.accountOrigin)
      }
    },
    methods:{
        signOut(){
            axios.post('/api/logout')
            .then(response => {
                console.log('signed out!!!')
                window.location.href = "/web/pages/login.html"
            }).catch(error => console.log(error))
        },
        makeTransfer(){
          if (this.transferDTO.amount === null && this.transferDTO.accountOrigin.length === 0 && this.transferDTO.accountDestination.length === 0 && this.transferDTO.description.length === 0) {
            Swal.fire({
              icon:'error',
              title: 'The data entered is incorrect, please re-enter it!'});
          }else if (this.transferDTO.amount === null && this.transferDTO.accountOrigin.length !== 0 && this.transferDTO.accountDestination.length !== 0 && this.transferDTO.description.length !== 0) {
            Swal.fire({
              icon:'error',
              title: 'The amount is missing!'});
          }else if (this.transferDTO.amount !== null && this.transferDTO.accountOrigin.length === 0 && this.transferDTO.accountDestination.length !== 0 && this.transferDTO.description.length !== 0) {
            Swal.fire({
              icon:'error',
              title: 'The account origin is missing!'});
          }else if (this.transferDTO.amount !== null && this.transferDTO.accountOrigin.length !== 0 && this.transferDTO.accountDestination.length === 0 && this.transferDTO.description.length !== 0) {
            Swal.fire({
              icon:'error',
              title: 'The account destination is missing!'});
          }else if (this.transferDTO.amount !== null && this.transferDTO.accountOrigin.length !== 0 && this.transferDTO.accountDestination.length !== 0 && this.transferDTO.description.length === 0) {
            Swal.fire({
              icon:'error',
              title: 'The description is missing!'});
          }else{
           Swal.fire({
            title: 'Are you sure to send this transaction?',
            showCancelButton: true,
            confirmButtonText: 'Yes',
            confirmButtonColor: '#3085d6',
            cancelButtonText: 'No',
            cancelButtonColor: '#d33',
            icon: 'question'
          }).then((result) => {
            if (result.isConfirmed) {
            const transferDTO = {
                amount: this.transferDTO.amount,
                accountOrigin: this.transferDTO.accountOrigin,
                accountDestination: this.transferDTO.accountDestination,
                description: this.transferDTO.description
            };
            axios.post('/api/transactions', transferDTO)
            .then(res =>
              console.log(res),
              Swal.fire({
                position: 'center',
                title: 'Transaction OK!',
                showConfirmButton: false,
                timer: 1500,
              }),
              setTimeout(()=>{
                window.location.href = "accounts.html"
              },1800) 
              ).catch(error =>{
                this.error1 = error.response.data
                Swal.fire(`${this.error1}`);
                console.log(error)})
                }}).catch(err => console.log(err));}
        },
        makeTransfer1() {
          if (this.transferDTO.amount === null && this.transferDTO.accountOrigin.length === 0 && this.transferDTO.accountDestination.length === 0 && this.transferDTO.description.length === 0) {
            Swal.fire({
              icon:'error',
              title: 'The data entered is incorrect, please re-enter it!'});
          }else if (this.transferDTO.amount === null && this.transferDTO.accountOrigin.length !== 0 && this.transferDTO.accountDestination.length !== 0 && this.transferDTO.description.length !== 0) {
            Swal.fire({
              icon:'error',
              title: 'The amount is missing!'});
          }else if (this.transferDTO.amount !== null && this.transferDTO.accountOrigin.length === 0 && this.transferDTO.accountDestination.length !== 0 && this.transferDTO.description.length !== 0) {
            Swal.fire({
              icon:'error',
              title: 'The account origin is missing!'});
          }else if (this.transferDTO.amount !== null && this.transferDTO.accountOrigin.length !== 0 && this.transferDTO.accountDestination.length === 0 && this.transferDTO.description.length !== 0) {
            Swal.fire({
              icon:'error',
              title: 'The account destination is missing!'});
          }else if (this.transferDTO.amount !== null && this.transferDTO.accountOrigin.length !== 0 && this.transferDTO.accountDestination.length !== 0 && this.transferDTO.description.length === 0) {
            Swal.fire({
              icon:'error',
              title: 'The description is missing!'});
          }else{
            const currentClientAccounts = this.account.map(acc => acc.number);
            if (currentClientAccounts.includes(this.transferDTO.accountDestination)) {
              Swal.fire('Invalid destination account', '', 'error');
              return;
            }
            Swal.fire({
              title: 'Are you sure to send this transaction?',
              showCancelButton: true,
              confirmButtonText: 'Yes',
              cancelButtonText: 'No',
              icon: 'warning',
            }).then((result) => {
              if (result.isConfirmed) {
                const transferDTO = {
                  amount: this.transferDTO.amount,
                  accountOrigin: this.transferDTO.accountOrigin,
                  accountDestination: this.transferDTO.accountDestination,
                  description: this.transferDTO.description
                };
                axios.post('http://localhost:8080/api/transactions', transferDTO)
                  .then(res => {
                    console.log(res);
                    if(res.status == 201){
                      console.log(res);
                      Swal.fire({
                        position: 'center',
                        title: 'Transaction OK!',
                        showConfirmButton: false,
                        timer: 1500
                    })
                      setTimeout(()=>{
                        window.location.href = "accounts.html"
                      },1800)
                    }
                    }).catch(error => {
                      this.error1 = err.response.data
                  Swal.fire({
                      icon:'error',
                      title: `${this.error1}`});
                  console.log(err)})
                    
                  }})
                  .catch(err => console.log(err));
                }
          }
          
          

    }
})
app.mount('#app')
