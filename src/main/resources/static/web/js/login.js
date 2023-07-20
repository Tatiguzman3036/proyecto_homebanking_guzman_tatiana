
const {createApp} = Vue;

const app = createApp({
    data(){
        return{
        signInActive: false,
        signUpActive: true,
        previousState: false,
        showSecondForm: false,
        email: "",
        password: "",
        firstName:"",
        lastName:"",
        emailRegister: "",
        passwordRegister: "",
        error1: ""
        }
    },
    created(){
        
    },
    methods:{
        toggleForm() {
            this.previousState = this.signInActive;
            this.signInActive = !this.signInActive;
            this.signUpActive = !this.signUpActive;
            this.showSecondForm = true;
          },
          toggleBack() {
            this.signInActive = this.previousState;
            this.signUpActive = !this.previousState;
            this.showSecondForm = false
          },
          login() {
            if (this.email.length === 0  && this.password.length  !== 0 ) {
                Swal.fire({
                    position: 'center',
                    icon: 'warning',
                    title: "Please enter an email!",
                    showConfirmButton: false,
                    timer:2000
                })
            }else if(this.email.length !== 0  && this.password.length  === 0 ){
                Swal.fire({
                    position: 'center',
                    icon: 'warning',
                    title: "Please enter a password!",
                    showConfirmButton: false,
                    timer:2000
                })
            }else if (this.email.length === 0  && this.password.length  === 0 ) {
                Swal.fire({
                    position: 'center',
                    icon: 'warning',
                    title: "Please enter the data!",
                    showConfirmButton: false,
                    timer:2000
                })
            }else if (this.email.length  !== 0  && this.password.length  !== 0) {
                if (this.email.includes("admin")) {
                    axios.post("/api/login", `email=${this.email}&password=${this.password}`,
                        { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                        .then(res => {
                            if (res.status == 200) {
                                Swal.fire({
                                    position: 'center',
                                    icon: 'success',
                                    title: 'Welcome!',
                                    showConfirmButton: false,
                                    timer: 1500
                                })
                                setTimeout(() => {
                                    window.location.href = "/web/manager.html";
                                }, 1800)
                            }
                        }).catch(err => {
                            Swal.fire({
                                position: 'center',
                                icon: 'error',
                                title: 'Incorrect, try again!!',
                                showConfirmButton: false,
                                timer: 1500
                            })
                        })
                } else {
                    axios.post('/api/login', `email=${this.email}&password=${this.password}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' }})
                        .then( response => {{
                                Swal.fire({
                                    position: 'center',
                                    icon: 'success',
                                    title: 'Welcome!',
                                    showConfirmButton: false,
                                    timer: 1000
                                })
                                setTimeout(() => {
                                    window.location.href = "/web/pages/accounts.html";
                                }, 1800)
                            }
                        }).catch(err => {
                            Swal.fire({
                                position: 'center',
                                icon: 'error',
                                title: 'Incorrect, try again!!',
                                showConfirmButton: false,
                                timer: 1500
                            })
                        })
                }
            }
        },
          register(){
            if (this.firstName.length === 0 && this.lastName.length !== 0 && this.emailRegister.length !== 0 && this.passwordRegister.length !== 0) {
                Swal.fire({
                    position: 'center',
                    icon: 'warning',
                    title: "Please enter your FirstName!",
                    showConfirmButton: false,
                    timer:2000
                })
            }
            else if (this.firstName.length !== 0 && this.lastName.length === 0 && this.emailRegister.length !== 0 && this.passwordRegister.length !== 0) {
                Swal.fire({
                    position: 'center',
                    icon: 'warning',
                    title: "Please enter your LastName!",
                    showConfirmButton: false,
                    timer:2000
                })
            }
            else if (this.firstName.length !== 0 && this.lastName.length !== 0 && this.emailRegister.length === 0 && this.passwordRegister.length !== 0) {
                Swal.fire({
                    position: 'center',
                    icon: 'warning',
                    title: "Please enter an email!",
                    showConfirmButton: false,
                    timer:2000
                })
            }
            else if (this.firstName.length !== 0 && this.lastName.length !== 0 && this.emailRegister.length !== 0 && this.passwordRegister.length === 0) {
                Swal.fire({
                    position: 'center',
                    icon: 'warning',
                    title: "Please enter a password!",
                    showConfirmButton: false,
                    timer:2000
                })
            }
            else if (this.firstName.length === 0 && this.lastName.length === 0 && this.emailRegister.length === 0 && this.passwordRegister.length === 0) {
                Swal.fire({
                    icon:'error',
                    title: 'The data entered is incorrect, please re-enter it!'})
            }
            else {
                axios.post('/api/clients',`firstName=${this.firstName}&lastName=${this.lastName}&email=${this.emailRegister}&password=${this.passwordRegister}`)
                .then(res =>{
                    if (this.emailRegister && this.passwordRegister) {
                        if (this.emailRegister.includes("admin")) {

                            axios.post('/api/login',`email=${this.emailRegister}&password=${this.passwordRegister}`)

                                .then(res => {
                                    if (res.status == 200) {
                                        Swal.fire({
                                            position: 'center',
                                            icon: 'success',
                                            title: 'Welcome!',
                                            showConfirmButton: false,
                                            timer: 1500
                                        })
                                        setTimeout(() => {
                                            window.location.href = "/web/manager.html";
                                        }, 1800)
                                    }
                                }).catch(err => {
                                    Swal.fire({
                                        position: 'center',
                                        icon: 'error',
                                        title: 'Incorrect, try again!!',
                                        showConfirmButton: false,
                                        timer: 1500
                                    })
                                })
                        } else {
                            axios.post('/api/login',`email=${this.emailRegister}&password=${this.passwordRegister}`)
                                .then( response => {{
                                        Swal.fire({
                                            position: 'center',
                                            icon: 'success',
                                            title: 'Welcome!',
                                            showConfirmButton: false,
                                            timer: 1000
                                        })
                                        setTimeout(() => {
                                            window.location.href = "/web/pages/accounts.html";
                                        }, 1800)
                                    }
                                }).catch(err => {
                                    Swal.fire({
                                        position: 'center',
                                        icon: 'error',
                                        title: 'Incorrect, try again!!',
                                        showConfirmButton: false,
                                        timer: 1500
                                    })
                                })
                        }
                    }   /* console.log(error) */});
                }
            
          }
        },
} )
app.mount('#app')
/* axios.post('/api/login',"email=melba@mindhub.com&password=melba",{headers:{'content-type':'application/x-www-form-urlencoded'}}).then(response => console.log('signed in!!!')) */
