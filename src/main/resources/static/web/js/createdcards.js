const {createApp} = Vue;
const app = createApp({
    data(){
        return{
            type:"",
            color:"",
        }
    },
    created(){

    },
    methods:{
        createdCards(){
            if(this.type.length === 0 && this.color.length === 0){
                Swal.fire({
                    position: 'center',
                    icon: 'warning',
                    title: "Please enter the data!",
                    showConfirmButton: false,
                    timer:2000
                })}else if(this.type.length !== 0 && this.color.length === 0){
                    Swal.fire({
                        position: 'center',
                        icon: 'warning',
                        title: "Please enter type of card!",
                        showConfirmButton: false,
                        timer:2000
                })}else if(this.type.length === 0 && this.color.length !== 0){
                    Swal.fire({
                        position: 'center',
                        icon: 'warning',
                        title: "Please enter type of color!",
                        showConfirmButton: false,
                        timer:2000
                    })}else {
            axios.post('/api/clients/current/cards',`type=${this.type}&color=${this.color}`)
            .then(res =>{
                if(res.status == 201){
                    console.log(res);
                    Swal.fire({
                        icon: 'success',
                      position: 'center',
                      title: 'Card Created',
                      showConfirmButton: false,
                      timer: 1500
                  })
                    setTimeout(()=>{
                      window.location.href = "/web/pages/card.html"
                    },1800)
                  }
            }) .catch(error => {
                this.error1 = error.response.data
            Swal.fire({
                icon:'error',
                title: `${this.error1}`});
            console.log(error)});
        }
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