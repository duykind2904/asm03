new Vue({
	el: '#app',
	data: {
		user: window.user,
		url: window.url,
		image_profile: null,
		image_profile_url: '',
		pdfFile: null,
		pdfFileName: '',
		pdfUrl: '',
		//company: Object.assign({}, null),
		
	},

	created() {
		console.log(this.user);
	},


	computed: {
		
				
	},

	methods: {
		checkImage(event) {
			const file = event.target.files[0];
			if (file.type.startsWith('image/')) {
				this.image_profile = file;
				this.image_profile_url = URL.createObjectURL(file);
			} else {
				swal("Vui lòng chọn một tập tin hình ảnh.");
				event.target.value = '';
				this.image_profile = null;
			}
		},

		checkPdf(event) {
			const file = event.target.files[0];

			if (file.type === 'application/pdf') {
				this.pdfFile = file;
				this.pdfFileName = file.name.trim();

			} else {
				swal("Vui lòng chọn một tập tin PDF.");		
			}
			event.target.value = '';
		},

		viewCV() {
			var file = this.pdfFile;
			if (file.type === 'application/pdf') {
				var reader = new FileReader();

				reader.onload = () => {
					var pdfUrl = URL.createObjectURL(file);
					window.open(pdfUrl, '_blank');
				};

				reader.readAsDataURL(file);
			}
		},

		deleteCV() {
			swal({
				title: "Bạn có chắc chắn muốn xóa?",
				icon: "warning",
				buttons: ["Hủy", "Xác nhận"],
			})
				.then((confirm) => {
					if (confirm) {
						this.pdfFile = null;
						this.pdfFileName = '';
						//swal("Đã xóa!", "Tệp tin đã được xóa.", "success");
					} else {
						//swal("Hủy", "Hành động đã bị hủy bỏ.", "error");
					}
				});
		},
		
		saveUser() {
			
			swal("Cập nhật thông tin thành công");
			setTimeout(function() {
		  	  swal.close();
			}, 2000);
		}

	},

});




