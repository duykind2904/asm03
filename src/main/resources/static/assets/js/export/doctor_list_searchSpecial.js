const app = new Vue({
	el: '#app',
	data: {
		userId: window.userId,
		keySearch: window.keySearchSpecial,

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

				app.totalCount = await countDoctorBySearchSpecial(app.keySearch).done();
				if (app.totalCount > 0) {
					app.listDoctor = await getAllDoctorBySearchSpecial(app.keySearch, app.pageNumber, app.pageSize).done();
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
		
		scheduleDoctor(doctorId) {
			if(app.userId > 0) {
				
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
		}


	},

});

function countDoctorBySearchSpecial(keySearch) {
	return $.ajax({
		url: '/doctor/countDoctorBySearchSpecial',
		data: {
			keySearch: keySearch,
		},
		cache: false,
		method: 'GET',
		type: 'GET'
	});
}

function getAllDoctorBySearchSpecial(keySearch, pageNumber, pageSize) {
	return $.ajax({
		url: '/doctor/getAllDoctorBySearchSpecial',
		data: {
			keySearch: keySearch,
			pageNumber: pageNumber,
			pageSize: pageSize,
		},
		cache: false,
		method: 'GET',
		type: 'GET'
	});
}





