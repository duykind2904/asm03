
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

		listUserSchedule: [],
	    totalCount: 0,
	    pageTotal: 0,
	    pageNumber: 1,
	    pageSize: 2,
	    activeIndex: -1,
	    descriptionDisease: '',
	},

	mounted() {
		document.onreadystatechange = async () => {
			if (document.readyState == "complete") {
				await getAllSpecialization().done(function(response) {
					app.specializations = response;
				});

				if (app.doctor.id > 0) {
					app.user = app.doctor.user;
					app.clinic = app.doctor.clinic;
					app.selectedSpecialization = app.doctor.specialization.id;
				}
				
				app.totalCount = await countUserSchedule(app.doctor.id).done();
				if(app.totalCount > 0) {
					app.listUserSchedule = await getUserScheduleByDoctorId(app.doctor.id, app.pageNumber, app.pageSize).done();
					this.activeIndex = 1;
					app.pageTotal = Math.floor(app.totalCount / app.pageSize) + (app.totalCount % app.pageSize > 0 ? 1 : 0);
				}
				
			}
		}

	},


	methods: {
	},

});

function getAllSpecialization() {
	return $.ajax({
		url: '/specialization/getAllSpecialization',
		cache: false,
		method: 'GET',
		type: 'GET',
	});
}

function countUserSchedule(doctorId) {
	return $.ajax({
		url: '/schedule/countUserSchedule',
		data: {
			doctorId: doctorId,
		},
		cache: false,
		method: 'GET',
		type: 'GET'
	});
}

function getUserScheduleByDoctorId(doctorId, pageNumber, pageSize) {
	return $.ajax({
		url: '/schedule/getUserScheduleByDoctorId',
		data: {
			doctorId: doctorId,
			pageNumber: pageNumber,
			pageSize: pageSize,
		},
		cache: false,
		method: 'GET',
		type: 'GET',
		success: function(response) {
				var schedules = [];
		for (let schedule of response) {
	        if(!schedule.patient) {
				schedule.patient = {};
			}
	        schedules.push(schedule);
	    }
	    
	    return schedules;
		}
	});
}






