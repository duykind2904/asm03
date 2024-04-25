const app = new Vue({
  el: '#app',
  data: {
    keySearchGeneral: window.keySearchGeneral,
    keySearchSpecial: window.keySearchSpecial,
    keySearch: null,
   
    listDoctor: [],
    totalCount: 0,
    pageTotal: 0,
    pageNumber: 1,
    pageSize: 2,
    activeIndex: -1,
  },
  
  mounted() {
	  document.onreadystatechange = async () => {
			if (document.readyState == "complete") {
				app.keySearchGeneral ? app.keySearch = app.keySearchGeneral : app.keySearch = app.keySearchSpecial;
				
				app.totalCount = await countDoctorsBySearch(app.keySearch).done();
				if(app.totalCount > 0) {
					app.listDoctor = await getAllDoctor(app.pageNumber, app.pageSize).done();
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
		  app.listDoctor = await getAllDoctor(app.pageNumber, app.pageSize).done();
	  },
		
	  openLockModal(userId) {
		  $('#exampleModalLock-' + userId).modal('show');
	  },
    
	  lock(userId, descriptionLock) {
		  updateLock(userId, false, descriptionLock);
		  
		  app.listDoctor.forEach((doctor) => {
			  if (doctor.user.id === userId) {
				  doctor.user.active = false;
			  }
		  });
	  },
	  
	  unlock(userId) {
		  var descriptionLock = '';
		  updateLock(userId, true, descriptionLock);
		  
		  app.listDoctor.forEach((doctor) => {
			  if (doctor.user.id === userId) {
				  doctor.user.active = true;
			  }
		  });
		  $('#exampleModalLock-' + userId).modal('hide');
	  },
	  
	  deleteDoctor(doctorId) {
		  deleteDoctor(doctorId);
		  app.listDoctor = app.listDoctor.filter(doctor => doctor.id !== doctorId);
	  },
	
	
	
  }, 
   
});

function countDoctors() {
	return $.ajax({
		url: '/doctor/countDoctor',
		cache: false,
		method: 'GET',
		type: 'GET'
	});
}

function getAllDoctor(pageNumber, pageSize) {
	return $.ajax({
		url: '/doctor/getAllDoctor',
		data: {
			pageNumber: pageNumber,
			pageSize: pageSize,
		},
		cache: false,
		method: 'GET',
		type: 'GET'
	});	
}

function updateLock(userId, isLock, descriptionLock) {
	return $.ajax({
		url: '/doctor/updateLock',
		data: {
			userId: userId,
			isLock: isLock,
			descriptionLock: descriptionLock,
		},
		cache: false,
		method: 'GET',
		type: 'GET'
	});	
}

function deleteDoctor(doctorId) {
	return $.ajax({
		url: '/doctor/deleteDoctor',
		data: {
			doctorId: doctorId,
		},
		cache: false,
		method: 'GET',
		type: 'GET'
	});	
}





