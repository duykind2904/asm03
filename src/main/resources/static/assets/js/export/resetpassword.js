const app = new Vue({
  el: '#app',
  data: {    
	id: window.id,
    url: window.url,
    newPassword: '',
    confirmPassword: '',
    validator: Object.assign({}, null),
  },
  
  methods: {
	  async reset() {		
		var bool = this.checkInputLogin();
		if(bool) {
			await updatePassword(app.newPassword).done(function(response) {
				if (response.data === "RESET SUCCESS") {					
					console.log("Reset thành công.");
					swal("Reset Password thành công");
					setTimeout(() => {
						window.location.href = '/asm3/login';
					}, 3 * 1000);
		        } else {		            
		            console.log("Reset thất bại.");
		        }
			});
		}
		
	},
	
	checkInputLogin() {
		let isValid = true;
		if(!app.newPassword || !app.newPassword.trim()) {
			app.validator_newPassword = 'Mật khẩu không được để trống';
			isValid = false;
		}
		if(!this.confirmPassword || !this.confirmPassword.trim()) {
			app.validator_password = 'Xác nhận mật khẩu không được để trống';
			isValid = false;
		}
		return isValid;
	},
  }, 
   
});

function updatePassword(password) {
    var loginRequest = {
        password: password,
    };
    
    return $.ajax({
        url: '/auth/updatePassword.json',
        data: JSON.stringify(loginRequest),
        contentType: 'application/json',
        method: 'POST',
        type: 'POST',
    });
}

