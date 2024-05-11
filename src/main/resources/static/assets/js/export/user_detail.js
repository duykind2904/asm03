const app = new Vue({
	el: '#app',
	data: {
		user: window.user,
		url: window.url,

		listScheduleByUser: [],
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
				app.totalCount = await countScheduleByUser(app.user.id).done();
				if (app.totalCount > 0) {
					app.listScheduleByUser = await getAllScheduleByUser(app.user.id, app.pageNumber, app.pageSize).done();
					this.activeIndex = 1;
					app.pageTotal = Math.floor(app.totalCount / app.pageSize) + (app.totalCount % app.pageSize > 0 ? 1 : 0);
				}
			}
		}

	},


	methods: {

	},

});

function countScheduleByUser(userId) {
	return $.ajax({
		url: '/schedule/countScheduleByUser',
		data: {
			userId: userId,
		},
		cache: false,
		method: 'GET',
		type: 'GET'
	});
}

function getAllScheduleByUser(userId, pageNumber, pageSize) {
	return $.ajax({
		url: '/schedule/getAllScheduleByUser',
		data: {
			userId: userId,
			pageNumber: pageNumber,
			pageSize: pageSize,
		},
		cache: false,
		method: 'GET',
		type: 'GET'
	});	
}





