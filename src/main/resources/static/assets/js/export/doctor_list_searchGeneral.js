const app = new Vue({
	el: '#app',
	data: {
		userId: window.userId,
		keySearch: window.keySearchGeneral,

		listDoctor: [],
		totalCount: 0,
		pageTotal: 0,
		pageNumber: 1,
		pageSize: 2,
		activeIndex: -1,
		
		validator: Object.assign({}, null),
		test: '',
		schedule: Object.assign({}, null),
	},

	mounted() {
		document.onreadystatechange = async () => {
			if (document.readyState == "complete") {
				app.totalCount = await countDoctorBySearchGeneral(app.keySearch).done();
				if (app.totalCount > 0) {
					app.listDoctor = await getAllDoctorBySearchGeneral(app.keySearch, app.pageNumber, app.pageSize);
					this.activeIndex = 1;
					app.pageTotal = Math.floor(app.totalCount / app.pageSize) + (app.totalCount % app.pageSize > 0 ? 1 : 0);
				}
			}
		}

	},


	methods: {

		async getPage(index) {
			this.activeIndex = index;
			app.pageNumber = index;
			app.listDoctor = await getAllDoctorBySearchGeneral(app.keySearch, app.pageNumber, app.pageSize);
		},
		
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

function countDoctorBySearchGeneral(keySearch) {
	return $.ajax({
		url: '/asm3/doctor/countDoctorBySearchGeneral',
		data: {
			keySearch: keySearch,
		},
		cache: false,
		method: 'GET',
		type: 'GET'
	});
}

async function getAllDoctorBySearchGeneral(keySearch, pageNumber, pageSize) {
    const response = await $.ajax({
        url: '/asm3/doctor/getAllDoctorBySearchGeneral',
        data: {
            keySearch: keySearch,
            pageNumber: pageNumber,
            pageSize: pageSize,
        },
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





