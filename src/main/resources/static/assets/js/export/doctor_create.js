const app = new Vue({
  el: '#app',
  data: {
    /*user: window.user,
    url: window.url,
    password1: '',
    passwordVisible1: true,
    password2: '',
    passwordVisible2: true,
    emailLogin: null,
    passwordLogin: null,
    errorLogin: true,
    emailLoginMsg: "",
    passwordLoginMsg: "",
    emailMsg: "",
    fullNameMsg: "",
    passwordMsg: "",
    rePasswordMsg: "",
    roleMsg: "",
    roleId: 0,*/
    
    url: window.url,
    emailLogin: '',
    passwordLogin: '',
    validator: Object.assign({}, null),
    emailForgotPass: '',
    isCode: false,
    
    user: window.user,
    doctor: window.doctor,
    validator_login: null,
  },
  
  mounted() {
	  document.onreadystatechange = async () => {
			if (document.readyState == "complete") {
				console.log(app.user);
			}
		}
	  
  },

  
  methods: {
	  async create() {      
	    //var isValid = await this.checkInput(); 
	    if (true) {
			app.doctor.user = app.user;
	        const response = await saveDoctor(app.doctor);
      	  	if (response === "SAVE DOCTOR SUCCESS") {
                console.log("thêm bác sĩ thành công thành công.");
				swal("Thêm bác sĩ thành công");
				setTimeout(() => {
					window.location.href = '/asm3/doctor/list';
				}, 1 * 1000);				
            } else {
                console.log("thêm bác sĩ thất bại");
            }
	    }
	},
	
	checkInputLogin() {
		let isValid = true;
		if(!app.emailLogin || !app.emailLogin.trim()) {
			app.validator_emailLogin = 'Email không được để trống';
			isValid = false;
		}
		if(!this.passwordLogin || !this.passwordLogin.trim()) {
			app.validator_password = 'Password không được để trống';
			isValid = false;
		}
		return isValid;
	},
	
	checkInput: async function() {
		var isValid = true;
		this.emailLoginMsg = "";
		this.passwordLoginMsg = "";
		
		const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
		if(!this.user.email) {
			this.emailMsg = "(Email không được để trống)";
			isValid = false;
		} else if (!emailRegex.test(this.user.email)){
			this.emailMsg = "(Email không đúng định dạng)";
			isValid = false;
		}
		
		if(!this.user.fullName) {
			this.fullNameMsg = "(Tên không được để trống)";
			isValid = false;
		}
		
		const passwordPattern = /^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z])(?=.*[^a-zA-Z0-9]).{8,}$/;
		if(!this.user.password) {
			this.passwordMsg = "(Password không được để trống)";
			isValid = false;
		} else if (!passwordPattern.test(this.user.password)) {
			this.passwordMsg = "(Password không đúng định dạng. Ít nhất 8 ký tự, ít nhất 1 ký tự viết hoa, 1 ký tự số, 1 ký tự đặc biệt)";
			isValid = false;
		} else if(this.user.rePassword && this.user.password !== this.user.rePassword ) {				 
			this.rePasswordMsg = "(Password không giống nhau)";
			isValid = false;
		}	
		
		if(!this.user.rePassword) {
			this.rePasswordMsg = "(RePassword không được để trống)";
			isValid = false;
		} 
		
		if(this.user.email) {
			const emailValid = await  checkEmailRegister(this.user.email).done();
			if (emailValid) {
				this.emailMsg = "(Email đã tồn tại)";
				isValid = false;
			}
		}
				
		return isValid;			
	}, 
	
	
	
  }, 
   
});

function saveDoctor(doctor) {
	return $.ajax({
		url: '/doctor/saveDoctor',
		data: JSON.stringify(doctor),
		cache: false,
		contentType: 'application/json',
		processData: false,
		method: 'POST',
		type: 'POST'
	});
	
}





