const app = new Vue({
  el: '#app',
  data: {
	      
    url: window.url,
    emailLogin: '',
    passwordLogin: '',
    validator: Object.assign({}, null),
    emailForgotPass: '',
    isCode: false,
    
    user: window.user,
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
	  async login() {      
	    var isValid = await this.checkInputLogin(); 
	    if (isValid) {
	        try {
	            const response = await validatorLogin(this.emailLogin, this.passwordLogin);
	            if (response === "LOGIN SUCCESS") {
	                console.log("Đăng nhập thành công.");
					swal("Đăng nhập thành công");
					setTimeout(() => {
						window.location.href = '/asm3/';
					}, 1 * 1000);
	            } else if (response === "USER BLOCKED") {
					swal("Tài khoản của bạn đã bị khóa. Vui lòng liên hệ Admin để được hướng dẫn");
					setTimeout(() => {
						window.location.href = '/asm3/';
					}, 3 * 1000);
				}
	            
	            else {
	                console.log("Đăng nhập thất bại.");
	                app.validator_login = 'Không đúng email hoặc mật khẩu';
	            }
	        } catch (error) {
	            console.error('Lỗi khi gửi yêu cầu đăng nhập:', error);
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
	
	forgotPass() {
		if(app.emailForgotPass.trim()) {
			app.isCode = true;
			sendMail(app.emailForgotPass).done(function(response) {
				console.log(response)
				if(response === 'Email not exist') {
					swal('Email chưa được đăng ký. Bạn vui lòng đăng ký');
					$('#forgotPassword').modal('hide');
				}
			});
		}
	},
	
	
	
	
	async handleregister(user) {
		console.log(user);				
		var isValid = await this.checkInput();		
		if (true) {
			swal("Đăng ký thành công");
			await saveUser(this.user);
			setTimeout(() => {
				window.location.href = '/asm3/user/edit';
			}, 1 * 1000);

		}		
		
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

function validatorLogin(emailLogin, passwordLogin) {
    var loginRequest = {
        email: emailLogin,
        password: passwordLogin
    };
    
    return $.ajax({
        url: '/auth/login',
        data: JSON.stringify(loginRequest),
        contentType: 'application/json',
        method: 'POST',
        type: 'POST',
               
    });
}

function sendMail(email) {
	return $.ajax({
		url: '/auth/sendMailForgotPass.json',
		data: {
			email: email,
		},
		cache: false,
		method: 'GET',
		type: 'GET'
	});	
}

function saveUser(user) {
	return $.ajax({
		url: '/auth/saveUser',
		data: JSON.stringify(user),
		cache: false,
		contentType: 'application/json',
		processData: false,
		method: 'POST',
		type: 'POST'
	});
	
}


function checkEmailRegister(email) {
	return $.ajax({
		url: '/auth/checkEmailRegister',
		data: {
			email: email,
		},
		
		cache: false,
		method: 'GET',
		type: 'GET',
	});	
}





