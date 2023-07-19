const {createApp} = Vue;

const app = createApp({
    data(){
        return{
            client:[],
            card: [],
            creditCard:[],
            debitCard: [],
            id: null,
            number: "",
        }
    },
    created(){
        this.loadData()
    },
    methods:{
        loadData(){
            axios.get("/api/clients/current/cards/active")
            .then(res => {
                /* this.client = res.data
                console.log(this.client); */
                this.card = res.data
                console.log(this.card);
                this.creditCard = this.card.filter(card => card.type == "CREDIT").sort((itemA, itemB)=> itemA.id - itemB.id)
                console.log(this.creditCard);
                this.debitCard = this.card.filter(card => card.type == "DEBIT").sort((itemA, itemB)=> itemA.id - itemB.id)
                console.log(this.debitCard);
                
            })
            .catch(error => console.log(error))
        },
        colorType(card){
            let fechaActual = new Date()
            if (card.color === "GOLD") {
                if (fechaActual < new Date(card.thruDate)) {
                    return 'gold'
                }else return 'expired-card'
            }else if(card.color === "SILVER"){
                if (fechaActual < new Date(card.thruDate)) {
                    return 'silver'
                }else return 'expired-card'
            }else if(card.color === "TITANIUM"){
                if (fechaActual < new Date(card.thruDate)) {
                    return 'titanium'
                }else return 'expired-card'
            }
            
        },
        signOut(){
            axios.post('/api/logout')
            .then(response => {
                console.log('signed out!!!')
                window.location.href = "/web/pages/login.html"
            }).catch(error => console.log(error))
        },
        renewTarget(thruDate){
          /* console.log(thruDate < (new Date().toLocaleDateString().split('/').reverse().join('-')));
            return thruDate < (new Date().toLocaleDateString().split('/').reverse().join('-')) */
            const currenDate = new Date();
            const formatThruDate = new Date(thruDate);
            return formatThruDate < currenDate
        },
        renewCard(numero){
          console.log(this.number);
          this.number = numero
          axios.post(`/api/clients/current/cards/renew?number=${this.number}`)
          .then(res =>{
            location.reload()
          }).catch(err => console.log(err))
        },
        deleteCard(id) {
            Swal.fire({
              title: 'Are you sure?',
              text: 'Do you want to delete this card?',
              icon: 'warning',
              showCancelButton: true,
              confirmButtonText: 'Yes, delete it!',
              cancelButtonText: 'Cancel',
              reverseButtons: true,
            }).then((result) => {
              if (result.isConfirmed) {
                console.log(this.id);
                this.id = id
                axios.patch(`/api/clients/current/cards/${this.id}/desactivate`)
                  .then((response) => {
                    Swal.fire({
                      title: 'Card Deleted!',
                      text: 'The card has been successfully deleted.',
                      icon: 'success',
                    }).then(() => {
                      location.reload();
                    });
                  })
                  .catch((error) => {
                    Swal.fire({
                      title: 'Error!',
                      text: 'An error occurred while deleting the card.',
                      icon: 'error',
                    });
                    console.error('Error al eliminar la tarjeta:', error.message);
                  });
              }
            });
          },
    }
})
app.mount('#app')