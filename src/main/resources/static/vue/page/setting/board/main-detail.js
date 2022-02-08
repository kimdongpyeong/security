

var SettingBoardDetailPage;
SettingBoardDetailPage = Vue.component("setting-board-detail-page", async function (resolve) {
    resolve({
	    "template": (await axios.get("/vue/page/setting/board/main-detail.html")).data,
	    "data": function () {
	        return {
	        	"data":{
	        		"user":{}
	        	},
	        	"editor":{},
	        	"post":{
	        		"importantYn":'N',
	        		"noticeYn":'Y',
	        		"deleteYn":'N',
		        	"fileList":[],
		        	"removeFileList":[]
	        	},
	        	"select":{
	        		"codeList":[]
	        	},
	        };
	    },
	    "watch": {
	    	"post.deleteYn": function(e){
	    		if(e === "Y"){
	    			this.post.importantYn = "N";
	    			this.post.noticeYn = "N";
	    		}
	    	}
	    },
	    "methods": {
	    	//공지사항 목록으로 이동
	    	"goList": function(){
	    		this.$router.replace("/settings/board");
	    	},
	    	//분류코드 가져오기
	    	"loadCodeList": async function(){
	    		let codeList = (await meta.api.common.code.getCodeList({
                    "page": 1,
                    "rowSize": 100000000,
                    "parentId": 44
	    		})).data.items;
	    		
	    		codeList.forEach(e => {
	    			this.select.codeList.push({"text": e.name, "value": e.value});
	    		});
	    	},
	    	//id가 존재할 경우 해당 공지사항 불러오기
	    	"loadNotice": async function(id){
	    		let notice = (await meta.api.common.notice.getNotice({
	    			"id": id
	    		})).data;

	    		this.post = notice;
	    		if(!this.post.fileList){
	    			this.post.fileList = [];
	    		}
	    		this.post.removeFileList = [];
	    	},
	    	//저장 및 수정
	    	"save": async function(){
	    		let i, self = this;
	    		
	    		self.post.createdBy = self.data.user.id;
	    		console.log(self.post.fileList);
	    		console.log(self.post.removeFileList);
	    		//필수항목 입력되어야 저장 및 수정가능
	    		if(self.post.kindsCd && self.post.title){
	    			//게시글의 id가 존재할 경우 수정
	    			if(self.post.id){
	    				if(await meta.confirm("수정하시겠습니까?")){
	    					self.editor.opts.saveMethod = 'PUT';
	    					self.editor.opts.saveParam = 'content';
	    					self.editor.opts.saveURL = '/api/common/boardTest/modify';
	    					self.editor.opts.saveParams = self.post;
	    					//저장
	    					self.editor.save.save();
	    					meta.alert("수정되었습니다.");
	    					this.$router.replace("/settings/board");
	    				}
	    			} else{
	    				if(await meta.confirm("저장하시겠습니까?")){
	    					self.editor.opts.saveParam = 'desc';
	    					self.editor.opts.saveURL = '/api/common/boardTest/save';
	    					self.editor.opts.saveParams = self.post;
	    					//저장
	    					self.editor.save.save();
	    					meta.alert("저장되었습니다.");
	    					this.$router.replace("/settings/board");
	    				}
	    			}
	    		} else{
	    			meta.alert("필수 항목을 입력해주세요.");
	    		}
	    	}
	    },
	    "mounted": async function () {
	    	let self = this, link, fileNm,
    			boardId = this.$route.query.id;
	    	
	    	if(boardId){
	    		await this.loadNotice(boardId);
	    	}
	    	
	    	//froala_editor 생성 및 옵션 설정
	    	var editor = new FroalaEditor('#froala',{
	    		// 필요없는 기능 제외 툴바옵션설정
	    		toolbarButtons:{
					'moreText': {
					  'buttons': ['bold', 'italic', 'underline', 'strikeThrough', 'subscript', 'superscript', 'fontFamily', 'fontSize', 'textColor', 'backgroundColor', 'inlineStyle', 'clearFormatting']
					},
					'moreParagraph': {
					  'buttons': ['alignLeft', 'alignCenter', 'formatOLSimple', 'alignRight', 'alignJustify', 'formatOL', 'formatUL', 'paragraphFormat', 'paragraphStyle', 'lineHeight', 'outdent', 'indent', 'quote']
					},
					'moreRich': {
					  'buttons': ['insertImage', 'insertVideo', 'insertFile', 'insertLink', 'insertTable', 'emoticons', 'fontAwesome', 'specialCharacters', 'embedly', 'insertHR']
					},
					'moreMisc': {
					  'buttons': ['spellChecker', 'selectAll', 'html', 'help']
					}
	    		},
	    		// 이미지/비디오/파일 업로드 param 및 경로설정
	    		imageUploadURL: '/api/common/boardTest/file',
	    		videoUploadURL: '/api/common/boardTest/file',
	    		fileUploadURL: '/api/common/boardTest/file',
	    		filesManagerUploadParam: 'file',
	    		filesManagerUploadURL: '/api/common/boardTest/file',
	    		saveInterval: 0,
	    		events: {
					'image.inserted' : function($img){
						link = $img[0].src;
						fileNm = link.substring(link.lastIndexOf("/") + 1);
						
						self.post.fileList.push(fileNm);
					},
					'image.replaced' : function($img){
						link = $img[0].src;
						fileNm = link.substring(link.lastIndexOf("/") + 1);
						
						self.post.fileList.push(fileNm);
					},
					'image.removed' : function ( $img ) {
						link = $img[0].src;
						fileNm = link.substring(link.lastIndexOf("/") + 1);

						self.post.removeFileList.push(fileNm);
					},
					'video.inserted' : function ($video) {
						link = $video[0].src;
						fileNm = link.substring(link.lastIndexOf("/") + 1);
						
						self.post.fileList.push(fileNm);
					},
					'video.removed': function($video){
						link = $video[0].src;
						fileNm = link.substring(link.lastIndexOf("/") + 1);

						self.post.removeFileList.push(fileNm);
					},
					'file.inserted' : function ($file, response) {
						link = $file[0].href;
						fileNm = link.substring(link.lastIndexOf("/") + 1);
						
						self.post.fileList.push(fileNm);
					},
					'file.unlink': function (file) { 
						link = file.href;
						fileNm = link.substring(link.lastIndexOf("/") + 1);

						self.post.removeFileList.push(fileNm);
					}
	    		}
	    	}, function(){
	    		if(self.post.desc){
	    			editor.html.set(self.post.desc);
	    		}
	    	});
	    	this.editor = editor;

	    	this.loadCodeList();
	    },
	    "created": function(){
            let user = _.cloneDeep(store.state.app.user);
            this.$set(this.data.user, "id", user.id);
	    }
    }); 
});