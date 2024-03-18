new Vue({
	el: '#app',
	data: {
		user: window.user,
		url: window.url,
		image_user_file: null,
		pdfFile: null,
		pdfFileName: '',
		pdfUrl: '',
		company: window.company,
		
	},

	created() {
		console.log(this.user);
	},


	mounted() {
		document.onreadystatechange = async () => {
			if (document.readyState == "complete") {
				if (this.user) {
					if (this.user.image) {
						this.image_user_file = await getFile(this.user.image).done();
						this.user.image_user_url = URL.createObjectURL(this.image_user_file);
					}
					if(this.user.cvName) {
						this.pdfFile = await getFile(this.user.cvName).done();
						this.pdfFileName = this.user.cvName;
					}
					if(this.company.logo) {
						this.company.logo_file = await getFile(this.company.logo).done();
						this.company.logoUrl = URL.createObjectURL(this.company.logo_file);
					}
				}
			}
		}
	},

	methods: {
		checkImage(event) {
			const file = event.target.files[0];
			if (file.type.startsWith('image/')) {
				this.image_user_file = file;
				this.user.image = file.name.trim();
				this.user.image_user_url = URL.createObjectURL(file);
			} else {
				swal("Vui lòng chọn một tập tin hình ảnh.");
				event.target.value = '';
				this.image_profile = null;
			}
		},
		
		checklogo(event) {
			const file = event.target.files[0];
			if (file.type.startsWith('image/')) {
				company.logo_file = file;
				company.logo = file.name.trim();
				company.logoUrl = URL.createObjectURL(file);
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
		
		async saveUser() {
			
			swal("Cập nhật thông tin cá nhân thành công");
			if(this.image_user_file) {
				this.user.image = await saveFile(this.image_user_file).done();
			}
			if(this.pdfFile) {
				this.user.cvName = await saveFile(this.pdfFile).then();
			}
			saveUser(this.user);
			setTimeout(function() {
		  	  swal.close();
			}, 2000);
		},
		
		async saveCompany() {
			swal("Cập nhật thông tin công ty thành công");
			
			if(this.company.logo_file) {
				this.company.logo = await saveFile(this.company.logo_file).done();
			}
			this.company.user = this.user;
			saveCompany(this.company);
			setTimeout(function() {
		  	  swal.close();
			}, 2000);
		}

	},

});

function saveUser(user) {
    return $.ajax({
        url: `${window.url}/profile/saveUser.json`, 
        data: JSON.stringify(user),
		cache: false,
		contentType: 'application/json',
		processData: false,
		method: 'POST',
		type: 'POST'
    });
}

function saveFile(file) {
    var formData = new FormData(); 
    formData.append('file', file); 

    return $.ajax({
        url: `${window.url}/profile/saveFile.json`, 
        type: 'POST',
        data: formData, 
        processData: false, 
        contentType: false, 
    });
}

function saveCompany(company) {
	return $.ajax({
		url: `${window.url}/profile/saveCompany.json`, 
		data: JSON.stringify(company),
		cache: false,
		contentType: 'application/json',
		processData: false,
		method: 'POST',
		type: 'POST'
	});	
}

function getFile(fileName) {
	return $.ajax({
		url: `${window.url}/profile/getFile.json`,
		cache: false,
		data: {
			fileName: fileName,
		},
		method: 'GET',
		type: 'GET'
	});
}




