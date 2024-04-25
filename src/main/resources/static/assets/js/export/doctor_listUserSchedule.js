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
	  
	  Examination(patientId, event) {
		  const schedule = app.listUserSchedule.find(s => s.patient && s.patient.id === patientId);
		  schedule.patient.status = true;
		  var status = true;
		  updateConfirm(patientId, status, schedule.patient.descriptionDisease);
		  event.preventDefault();
	  },
	  
	  cancelExamination(patientId) {
		  const schedule = app.listUserSchedule.find(s => s.patient && s.patient.id === patientId);
		  schedule.patient.status = false;
		  var status = false;
		  updateConfirm(patientId, status, schedule.patient.descriptionDisease);
	  },
	  
	  handelDesExam(patientId) {
		  const schedule = app.listUserSchedule.find(s => s.patient && s.patient.id === patientId);
		  schedule.patient.examStatus = true;
		  var examStatus = true;
		  updateExamination(patientId, examStatus, schedule.patient.decExam);
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
		type: 'GET'
	});	
}

function updateConfirm(patientId, status, descriptionDisease) {
	return $.ajax({
		url: '/schedule/updateConfirm',
		data: {
			patientId: patientId,
			status: status,
			descriptionDisease: descriptionDisease,
		},
		cache: false,
		method: 'GET',
		type: 'GET'
	});
}

function updateExamination(patientId, examStatus, decExam) {
	return $.ajax({
		url: '/schedule/updateExamination',
		data: {
			patientId: patientId,
			examStatus: examStatus,
			decExam: decExam,
		},
		cache: false,
		method: 'GET',
		type: 'GET'
	});
}




