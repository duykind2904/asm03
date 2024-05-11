const app = new Vue({
  el: '#app',
  data: {
    doctorId: window.doctorId,
   
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
				app.totalCount = await countUserSchedule(app.doctorId).done();
				if(app.totalCount > 0) {
					app.listUserSchedule = await getUserScheduleByDoctorId(app.doctorId, app.pageNumber, app.pageSize).done();
					this.activeIndex = 1;
					app.pageTotal = Math.floor(app.totalCount / app.pageSize) + (app.totalCount % app.pageSize > 0 ? 1 : 0);
				}
			}
		}
	  
  },

  
  methods: {
	  openModal(id) {
	    $('#exampleModalExam-' + id).modal('show');
	  },
	  
	  async getPage(index) {
		  this.activeIndex = index;
		  app.pageNumber = index;
		  app.listUserSchedule = await getUserScheduleByDoctorId(app.doctorId, app.pageNumber, app.pageSize).done();
	  },
	  
	  async comfirmExam(scheduleIndex, event) {
		  var patient = {
			  scheduleId: scheduleIndex.id,
			  status: true,
		  }
		  await createPatient(patient).done(function(data) {
			  patient = data;
			  app.listUserSchedule.forEach(schedule => {
				 if(schedule.id == scheduleIndex.id) schedule.patient = patient;
			  });
		  });
		  
		  event.preventDefault();
	  },
	  
	  async cancelExam(scheduleIndex) {
		  var patient = {
			  scheduleId: scheduleIndex.id,
			  status: false,
			  descriptionDisease: scheduleIndex.cancelExamDes,
		  }
		  
		  await createPatient(patient).done(function(data) {
			  patient = data;
			  app.listUserSchedule.forEach(schedule => {
				 if(schedule.id == scheduleIndex.id) schedule.patient = patient;
			  });
		  });
	  },
	  
	  handelExamed(patientId) {
		  const schedule = app.listUserSchedule.find(s => s.patient && s.patient.id === patientId);
		  schedule.patient.examStatus = true;
		  var examStatus = true;
		  updateExamination(schedule.id, patientId, examStatus, schedule.patient.decExam);
	  },
	
  }, 
   
});

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

function updateExamination(scheduleId, patientId, examStatus, decExam) {
	return $.ajax({
		url: '/patient/updateExamination',
		data: {
			scheduleId: scheduleId,
			patientId: patientId,
			examStatus: examStatus,
			decExam: decExam,
		},
		cache: false,
		method: 'GET',
		type: 'GET'
	});
}


function createPatient(patient) {
	return $.ajax({
		url: '/patient/create',
		data: JSON.stringify(patient),
		cache: false,
		contentType: 'application/json',
		processData: false,
		method: 'POST',
		type: 'POST'
	});	
}



