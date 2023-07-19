
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
              axios.post('/api/login', `email=${this.email}&password=${this.password}`, {
                headers: { 'content-type': 'application/x-www-form-urlencoded' }
              })
              .then((res) => {
                Swal.fire({
                  position: 'center',
                  icon: 'success',
                  title: 'Welcome!',
                  showConfirmButton: false,
                  timer: 2000
                });
                setTimeout(()=>{
                    window.location.href = "/web/pages/accounts.html"
                  },2500)
              })
              .catch(error => {
            Swal.fire({
                icon:'error',
                title: 'The data entered is incorrect, please re-enter it!'});
            console.log(error)});
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
                    axios.post('/api/login',`email=${this.emailRegister}&password=${this.passwordRegister}`)
                    .then(res=>{
                        window.location.href = "/web/pages/accounts.html"
                        console.log('Inicio de sesión exitoso:', res.data);
                    }).catch(error =>console.log(error))
                }).catch(error => {
                    this.error1 = error.response.data
                Swal.fire({
                    icon:'error',
                    title: `${this.error1}`});
                /* console.log(error) */});
                }
            
          }
        },
} )
app.mount('#app')
/* axios.post('/api/login',"email=melba@mindhub.com&password=melba",{headers:{'content-type':'application/x-www-form-urlencoded'}}).then(response => console.log('signed in!!!')) */
