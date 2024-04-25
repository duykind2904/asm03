const app = new Vue({
  el: '#app',
  data: {
    user: window.user,
    url: window.url,
   
    listUser: [],
    totalCount: 0,
    pageTotal: 0,
    pageNumber: 1,
    pageSize: 2,
    activeIndex: -1,
  },
  
  mounted() {
	  document.onreadystatechange = async () => {
			if (document.readyState == "complete") {
				app.totalCount = await countUsers().done();
				if(app.totalCount > 0) {
					app.listUser = await getAllUser(app.pageNumber, app.pageSize).done();
					this.activeIndex = 1;
					app.pageTotal = Math.floor(app.totalCount / app.pageSize) + (app.totalCount % app.pageSize > 0 ? 1 : 0);
				}
				console.log(app.totalCount % app.pageSize > 0 ? 1 : 0);
				console.log(app.totalCount / app.pageSize);
				console.log(app.pageTotal);
			}
		}
	  
  },

  
  methods: {
	  
	  async getPage(index) {
		  this.activeIndex = index;
		  app.pageNumber = index;
		  app.listUser = await getAllUser(app.pageNumber, app.pageSize).done();
	  },
	  
	  openLockModal(userId) {
		  $('#exampleModalLock-' + userId).modal('show');
	  },
		
	  lock(userId, descriptionLock) {
		  updateLock(userId, false, descriptionLock);
		  
		  app.listUser.forEach((user) => {
			  if (user.id === userId) {
				  user.active = false;
			  }
		  });
	  },
	  
	  unlock(userId) {
		  var descriptionLock = '';
		  updateLock(userId, true, descriptionLock);
		  
		  app.listUser.forEach((user) => {
			  if (user.id === userId) {
				  user.active = true;
			  }
		  });
	  },
	  
	  deleteUser(userId) {
		  deleteUser(userId);
		  app.listUser = app.listUser.filter(user => user.id !== userId);
	  },
	
	
	
  }, 
   
});

function countUsers() {
	return $.ajax({
		url: '/user/countUser',
		cache: false,
		method: 'GET',
		type: 'GET'
	});
}

function getAllUser(pageNumber, pageSize) {
	return $.ajax({
		url: '/user/getAllUser',
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
		url: '/user/updateLock',
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

function deleteUser(userId) {
	return $.ajax({
		url: '/user/deleteUser',
		data: {
			id: userId,
		},
		cache: false,
		method: 'GET',
		type: 'GET'
	});	
}





