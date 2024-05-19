const app = new Vue({
	el: '#app',
	data: {
		userId: window.userId,
		listDoctor: [],
		totalCount: 0,
		pageTotal: 0,
		pageNumber: 1,
		pageSize: 2,
		activeIndex: -1,
		
		validator: Object.assign({}, null),
		test: '',
		schedule: Object.assign({}, null),
		
		specialzations: [],
	},

	mounted() {
		document.onreadystatechange = async () => {
			if (document.readyState == "complete") {
				app.specialzations = await getSpecialzationOutStanding().done();
				
				app.listDoctor = await getDoctorOutStanding();
				
			}
		}

	},


	methods: {

		async scheduleDoctor(doctor) {
			app.test = '*';
			/*app.validator.test = '11';
			console.log(app.validator.test);
			Vue.set(app.validator, 'test', '22');
			console.log(app.validator.test);*/
			if(app.userId > 0) {
				var bool = this.validator_schedule();
				
				if(bool) {
					app.schedule.doctorId = doctor.id;
					app.schedule.userId = app.userId;
					let response = await addScheduleUser(app.schedule).done();
					if(response == 'OK') {
						swal("Đặt lịch với bác sĩ " + doctor.user.fullName + " thành công");
						$('#exampleModal-' + doctor.id).modal('hide');
					} else {
						swal("Đặt lịch với bác sĩ " + doctor.user.fullName + " thất bại");
					}
					app.schedule = {};
					app.listDoctor.forEach(function(doc) {
						if(doc.id == doctor.id) {
							doc.isSchedule = true;
						}
				    });
				}
			} else {
				 swal({
				  title: "Bạn chưa đăng nhập",
				  text: "Đến trang đăng nhập?",
				  icon: "warning",
				  buttons: {
					  cancel: "Hủy",
					  confirm: "OK"
				  },
			  }).then((value) => {
				  if (value) {
					  window.location.href = "/asm3/login";
				  }
			  });
			}
		},
		
		validator_schedule: function() {
			let isValid = true;
			if(!app.schedule.date) {
				this.validator.date = 'Ngày không được để trống';
				isValid = false;
			} else {
				this.validator.date = '';
			}
			
			if(!app.schedule.time) {
				this.validator.time = 'Thời gian không được để trống';
				isValid = false;
			} else {
				this.validator.time = '';
			}
			
			if(!app.schedule.description) {
				this.validator.description = 'Mô tả bệnh không được để trống';
				isValid = false;
			} else {
				this.validator.description = '';
			}
			
			return isValid;
		}


	},

});

function getSpecialzationOutStanding() {
	return $.ajax({
		url: '/asm3/schedule/getSpecialzationOutStanding',
		cache: false,
		method: 'GET',
		type: 'GET'
	});
}

async function getDoctorOutStanding() {
    const response = await $.ajax({
        url: '/asm3/schedule/getDoctorOutStanding',
        cache: false,
        method: 'GET',
        type: 'GET',
    });

    if (app.userId > 0) {
        var doctors = [];
        var promises = [];

        for (let doctor of response) {
            promises.push(checkScheduleByUserIdAndDoctorId(app.userId, doctor.id).then((is) => {
                doctor.isSchedule = is;
            }));
            doctors.push(doctor);
        }

        await Promise.all(promises);
        return doctors;
    }

    return response;
}

function checkScheduleByUserIdAndDoctorId(userId, doctorId) {
	return $.ajax({
		url: '/asm3/schedule/checkScheduleByUserIdAndDoctorId',
		data: {
			userId: userId,
			doctorId: doctorId,
		},
		cache: false,
		method: 'GET',
		type: 'GET',
	});
}

function addScheduleUser(schedule) {
	return $.ajax({
		url: '/asm3/schedule/addSchedule',
		data: JSON.stringify(schedule),
		cache: false,
		contentType: 'application/json',
		processData: false,
		method: 'POST',
		type: 'POST'
	});
}





