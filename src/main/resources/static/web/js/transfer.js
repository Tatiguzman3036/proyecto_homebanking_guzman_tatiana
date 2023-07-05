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
        axios.get(`http://localhost:8080/api/clients/current`)
        .then(res =>{
            this.account = res.data.accounts
            this.account.sort((itemA, itemB)=> itemA.id - itemB.id);
            console.log(this.account);
        }).catch(error => console.log(error))
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
            /* console.log(this.transferDTO.amount, this.transferDTO.accountDestination, this.transferDTO.accountOrigin, this.transferDTO.description); */
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
            axios.post('http://localhost:8080/api/transactions', transferDTO)
            .then(res =>
              console.log(res),
              Swal.fire({
                position: 'center',
                title: 'Transaction OK!',
                showConfirmButton: false,
                timer: 1500
            }),
              setTimeout(()=>{
                window.location.href = "accounts.html"
              },1800) 
              ).catch(error =>{
                this.error1 = error.response.data
                Swal.fire(`${this.error1}`,'error');
                console.log(error)})
                }})
        },
        makeTransfer1() {
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
              icon: 'question'
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
                    }).catch(error =>{
                      this.error1 = error.response.data
                  Swal.fire(`${this.error1}`,'error');
                  console.log(error)});
              }
            });
          }
          
          

    }
})
app.mount('#app')
