const app = new Vue({
  el: '#app',
  data: {
	user: window.user,
	doctor: window.doctor,
    url: window.url,
    mode: window.mode,
    
    specializations: [],
    selectedSpecialization: '',
    clinic: Object.assign({}, null),
    validator: Object.assign({}, null),
    
    emailLogin: '',
    passwordLogin: '',
    
    emailForgotPass: '',
    isCode: false,
    
    
    validator_login: null,
  },
  
  mounted() {
	  document.onreadystatechange = async () => {
			if (document.readyState == "complete") {
				await getAllSpecialization().done(function(response) {
					app.specializations = response;
				});
				
				if(app.doctor.id > 0) {
					app.user = app.doctor.user;
					app.clinic = app.doctor.clinic;
					app.selectedSpecialization = app.doctor.specialization.id;
				}
				console.log(app.specializations);
			}
		}
	  
  },

  
  methods: {
	  async create() {      
	    var boolUser = await this.checkInputUser(); 
	    var boolDoctor = await this.checkInputDoctor();
	    if (boolUser && boolDoctor) {
			app.doctor.user = app.user;
			app.doctor.specialization = app.specializations.find(spe => spe.id = app.selectedSpecialization);
			app.doctor.clinic = app.clinic;
	        const response = await saveDoctor(app.doctor);
      	  	if (response === "SAVE DOCTOR SUCCESS") {
				if(mode == 'EDIT') {
					console.log("Cập nhật bác sĩ thành công thành công.");
				swal("Cập nhật bác sĩ thành công");
				} else {
					console.log("thêm bác sĩ thành công thành công.");
					swal("Thêm bác sĩ thành công");
				}
                
				
				setTimeout(() => {
					window.location.href = '/asm3/doctor/list';
				}, 1 * 1000);				
            } else {
                console.log("Thất bại");
                swal("Thất bại")
            }
	    }
	},
	
	checkInputUser: async function() {
		var isValid = true;
		
		const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
		if(!this.user.email) {
			app.validator.email = "(Email không được để trống)";
			isValid = false;
		} else if (!emailRegex.test(this.user.email)){
			this.validator.email = "(Email không đúng định dạng)";
			isValid = false;
		} else {
			this.validator.email = '';
		}
		
		if(!this.user.fullName) {
			this.validator.fullName = "(Tên không được để trống)";
			isValid = false;
		} else {
			this.validator.fullName = '';
		}
		
		var checkpass = true;
		if(mode == 'EDIT') {
			if(!this.user.password && !this.user.rePassword) {
				checkpass = false;
			}
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
		
		if(mode !== 'EDIT' && this.user.email) {
			const emailValid = await checkEmailRegister(this.user.email).done();
			if (emailValid.data) {
				this.validator.email = "(Email đã tồn tại)";
				isValid = false;
			} 
		}
		
		this.validator = {...this.validator};	
		return isValid;			
	}, 
	
	checkInputDoctor: async function() {
		var isValid = true;
		
		if(!this.doctor.introduction) {
			this.validator.doctorIntroduction = "(Giới thiệu không được để trống)";
			isValid = false;
		}  else {
			this.validator.doctorIntroduction = '';
		}
		
		if(!this.doctor.trainingProcess) {
			this.validator.doctorTrainingProcess = "(Quá trình đào tạo không được để trống)";
			isValid = false;
		} else {
			this.validator.doctorTrainingProcess = '';
		}
		
		if(!this.doctor.achievements) {
			this.validator.doctorAchievements = "(Thành tựu không được để trống)";
			isValid = false;
		} else {
			this.validator.doctorAchievements = '';
		}
		
		if(!this.selectedSpecialization) {
			this.validator.doctorSelectedSpecialization = "(Chuyên khoa không được để trống)";
			isValid = false;
		} else {
			this.validator.doctorSelectedSpecialization = '';
		}
		
		if(!this.clinic.name) {
			this.validator.clinicName = "(Tên cơ sở y tế không được để trống)";
			isValid = false;
		} else {
			this.validator.clinicName = '';
		}
		
		if(!this.clinic.description) {
			this.validator.clinicDescription = "(Thông tin mô tả không được để trống)";
			isValid = false;
		} else {
			this.validator.clinicDescription = '';
		}
		
		if(!this.clinic.address) {
			this.validator.clinicAddress = "(Địa chỉ không được để trống)";
			isValid = false;
		} else {
			this.validator.clinicAddress = '';
		}
		
		if(!this.clinic.phone) {
			this.validator.clinicPhone = "(Số điện thoại không được để trống)";
			isValid = false;
		} else {
			this.validator.clinicPhone = '';
		}
		
				
		return isValid;			
	}, 
	
	
	
  }, 
   
});

function checkEmailRegister(email) {
	return $.ajax({
		url: '/user/checkEmailRegister',
		data: {
			email: email,
		},
		cache: false,
		method: 'GET',
		type: 'GET',
	});
}

function getAllSpecialization() {
	return $.ajax({
		url: '/specialization/getAllSpecialization',
		cache: false,
		method: 'GET',
		type: 'GET',
	});
}

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





