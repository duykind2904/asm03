const app = new Vue({
  el: '#app',
  data: {
	user: window.user,
    url: window.url,
 
    validator: Object.assign({}, null),
    test: '',
  },

  
  methods: {
	  async create() {      
	    var bool = await this.checkInputUser();
	    if (bool) {
	        const response = await saveUser(app.user);
      	  	if (response === "OK") {
				console.log("Cập nhật thành công.");
				swal("Cập nhật thành công");
				setTimeout(() => { swal.close(); }, 3 * 1000);	
						
            } else {
                console.log("Thất bại");
                swal("Thất bại")
            }
	    }
	},
	
	checkInputUser: async function() {
		app.test = '*';
		var isValid = true;
		
		if(!this.user.fullName) {
			this.validator.fullName = "(Tên không được để trống)";
			isValid = false;
		} else {
			this.validator.fullName = '';
		}
		
		var checkpass = true;
		if(!this.user.password && !this.user.rePassword) {
			checkpass = false;
		}
		
		if(checkpass) {
			const passwordPattern = /^(?=.*[A-Z])(?=.*[0-9])(?=.*[a-z])(?=.*[^a-zA-Z0-9]).{8,}$/;
			if(!this.user.password) {
				this.validator.password = "(Password không được để trống)";
				isValid = false;
			} else if (!passwordPattern.test(this.user.password)) {
				this.validator.password = "(Password không đúng định dạng. Ít nhất 8 ký tự, ít nhất 1 ký tự viết hoa, 1 ký tự số, 1 ký tự đặc biệt)";
				isValid = false;
			} else if(this.user.rePassword && this.user.password !== this.user.rePassword ) {				 
				this.validator.Password = "(Password không giống nhau)";
				isValid = false;
			} else {
				this.validator.Password = '';
			}
		}
		
		return isValid;			
	}, 
	
  }, 
   
});

function saveUser(user) {
	return $.ajax({
		url: '/user/saveUser',
		data: JSON.stringify(user),
		cache: false,
		contentType: 'application/json',
		processData: false,
		method: 'POST',
		type: 'POST'
	});
	
}





