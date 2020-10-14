(function(angular) {
    'use strict';
	app.service('fileNavigator', [
		'$http','$q', 'fileManagerConfig', 'item', '$rootScope' ,function ($http,$q, fileManagerConfig, Item, $rootScope) {

        $http.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
  

        var FileNavigator = function() {
            this.requesting = false;
            this.position = false;
            this.fileList = [];
            this.recycleFileList=[];
            this.modalFileList=[];//用于分享、收藏的文件列表
            this.currentPath = [];
            this.currentModalPath = [];//用于分享、收藏的路径
            this.history = [];
            this.error = '';

            this.currentFileId = '';
            this.currentParentId = '';

            this.currentModalFileId = '';//用于分享、收藏id
            this.currentModalParentId = '';
            
            this.myFolderCurrentPath = ["我的文件"];
            this.myFolderFileList = [];
            this.myFolderHistory = [{name:"我的文件"}];

            this.flowCurrentPath = ["flow/"];
            this.flowFileList = [];
            this.flowHistory = [{name:"共享流程"}];

            this.myFlowCurrentPath = ["myFlow/"];
            this.myFlowFileList = [];
            this.myFlowHistory = [{name:"我的流程"}];

            this.isShowFolder = false;
            this.isShowMyFolder = false;
            this.isShowFlow = false;
            this.isShowMyFlow = false;
            this.isPrivate = false;//是收藏还是共享，默认是共享

        };

        FileNavigator.prototype.deferredHandler = function(data, deferred, defaultMsg) {
            if (!data || typeof data !== 'object') {
                this.error = 'Bridge response error, please check the docs';
            }
            if (!this.error && data.result && data.result.error) {
                this.error = data.result.error;
            }
            if (!this.error && data.error) {
                this.error = data.error.message;
            }
            if (!this.error && defaultMsg) {
                this.error = defaultMsg;
            }
            if (this.error) {
                return deferred.reject(data);
            }
            return deferred.resolve(data);
        };

        FileNavigator.prototype.list = function() {
            var self = this;
            var deferred = $q.defer();
            var path = self.currentPath.join('/');
            var data = {params: {
                fileId: self.currentFileId,
                parentId: self.currentParentId,
                mode: 'list',
                onlyFolders: false,
                path:$rootScope.rootdir + '/' + path
            }};

            self.requesting = true;
            self.fileList = [];
            self.error = '';
            self.showList('showFolder');

            $http.post(fileManagerConfig.listUrl, data).success(function(data) {
                self.deferredHandler(data, deferred);
            }).error(function(data) {
                self.deferredHandler(data, deferred, 'Unknown error listing, check the response');
            })['finally'](function() {
                self.requesting = false;
            });
            return deferred.promise;
        };

        FileNavigator.prototype.myList = function() {
            var self = this;
            var deferred = $q.defer();
            var path = self.currentPath.join('/');
            var data = {params: {
                fileId: self.currentFileId,
                parentId: self.currentParentId,
                mode: 'list',
                onlyFolders: false,
                path:$rootScope.rootdir + '/' + path
            }};

            self.requesting = true;
            self.error = '';
            self.showList('showFolder');

            $http.post(fileManagerConfig.listMyFileUrl, data).success(function(data) {
                self.deferredHandler(data, deferred);
            }).error(function(data) {
                self.deferredHandler(data, deferred, 'Unknown error listing, check the response');
            })['finally'](function() {
                self.requesting = false;
            });
            return deferred.promise;
        };

        //分享模态框的请求
        FileNavigator.prototype.shareModalList = function() {
            var self = this;
            var deferred = $q.defer();
            var data = {params: {
                    fileId: self.currentModalFileId,
                    parentId: self.currentModalParentId,
                    mode: 'list',
                    onlyFolders: false,
                    path: $rootScope.scope.config.fileMemuTitle['allFiles'] + '/' + self.currentModalPath.join('/')
                }};

            self.requesting = true;
            self.error = '';
            self.showList('showFolder');
            $http.post(fileManagerConfig.listUrl, data).success(function(data) {
                self.deferredHandler(data, deferred);
            }).error(function(data) {
                self.deferredHandler(data, deferred, 'Unknown error listing, check the response');
            })['finally'](function() {
                self.requesting = false;
            });
            return deferred.promise;
        };

        //收藏模态框的请求
        FileNavigator.prototype.collectModalList = function() {
                var self = this;
                var deferred = $q.defer();
                var data = {params: {
                        fileId: self.currentModalFileId,
                        parentId: self.currentModalParentId,
                        mode: 'list',
                        onlyFolders: false,
                        path: $rootScope.scope.config.fileMemuTitle['myFiles'] + '/' + self.currentModalPath.join('/')
                    }};

                self.requesting = true;
                self.error = '';
                self.showList('showFolder');
                $http.post(fileManagerConfig.listMyFileUrl, data).success(function(data) {
                    self.deferredHandler(data, deferred);
                }).error(function(data) {
                    self.deferredHandler(data, deferred, 'Unknown error listing, check the response');
                })['finally'](function() {
                    self.requesting = false;
                });
                return deferred.promise;
        };

			
			/********************回收站列表*********************/
        FileNavigator.prototype.listRecycle = function (knowledgeType,keyword) {
				var self = this;
				var deferred = $q.defer();
                var path = self.currentPath.join('/');
				self.requesting = true;
				self.recycleFileList = [];
				self.error = '';
                self.showList('showFolder');
                $http.post(fileManagerConfig.listRecycleFileUrl,{knowledgeType,keyword}).success(function (data) {
                    self.deferredHandler(data, deferred);
                }).error(function (data) {
                    self.deferredHandler(data, deferred, 'Unknown error listing, check the response');
				})['finally'](function () {
					self.requesting = false;
				});
				return deferred.promise;
			};

        FileNavigator.prototype.listMyFolder = function() {
            var self = this;
            var deferred = $q.defer();
            var path = self.myFolderCurrentPath.join('/');
            var data = {params: {
                mode: 'listMyFolder',
                onlyFolders: false,
                path: '/' + path
            }};

            self.requesting = true;
            self.fileList = [];
            self.error = '';
            self.showList('showMyFolder');

            $http.post(fileManagerConfig.listMyFolderUrl, data).success(function(data) {
                self.deferredHandler(data, deferred);
            }).error(function(data) {
                self.deferredHandler(data, deferred, 'Unknown error listing, check the response');
            })['finally'](function() {
                self.requesting = false;
            });
            return deferred.promise;
        };

				// 共享文件搜索
        FileNavigator.prototype.search = function(keyword){
            var self = this;
            var deferred = $q.defer();
            var path = self.currentPath.join('/');
            var data = {
                parentId: self.currentFileId,
                keyword: keyword
            }
            $http.post(fileManagerConfig.searchListFileUrl, data).success(function (data) {
                self.deferredHandler(data, deferred);
            }).error(function (data) {
                self.deferredHandler(data, deferred, 'Unknown error listing, check the response');
            })['finally'](function () {
                // self.position = false;
            });
            return deferred.promise;
        }

				// 我的文件搜索
        FileNavigator.prototype.mySearch = function(keyword){
                var self = this;
                var deferred = $q.defer();
                var path = self.currentPath.join('/');
                var data = {
                    parentId: self.currentFileId,
                    keyword: keyword
                }
                $http.post(fileManagerConfig.searchListMyFileUrl, data).success(function (data) {
                    self.deferredHandler(data, deferred);
                }).error(function (data) {
                    self.deferredHandler(data, deferred, 'Unknown error listing, check the response');
                })['finally'](function () {
                    // self.position = false;
                });
                return deferred.promise;
						}
        FileNavigator.prototype.refresh = function(knowledgeType,keyword) {
            var self = this;
            var path = self.currentPath.join('/');
            self.position = false;
            // 判断不同的index的页面刷新不同的数据
            if ($rootScope.rootdir == $rootScope.scope.config.fileMemuTitle['recycle']){
                if(knowledgeType==undefined){
                    knowledgeType='share_file';
                }
                return self.listRecycle(knowledgeType,keyword).then(function (data) {
                    // self.currentParentId = data.parentId;
                    self.recycleFileList = (data.data || []).map(function (file) {
                        return new Item(file, self.currentPath);
                    });
                    self.buildTree(path);
                });
            } else if(($rootScope.rootdir == $rootScope.scope.config.fileMemuTitle['myFiles'])){
                return self.myList().then(function (data) {
                    self.currentParentId = data.parentId;
                    self.fileList = (data.result || []).map(function (file) {
                        return new Item(file, self.currentPath);
                    });
                    self.buildTree(path);
                });
            }else if(($rootScope.rootdir == $rootScope.scope.config.fileMemuTitle['allFiles'])){
                return self.list().then(function (data) {
                    self.currentParentId = data.parentId;
                    self.fileList = (data.result || []).map(function (file) {
                        return new Item(file, self.currentPath);
                    });
                    self.buildTree(path);
                });
            }
        };

        //刷新分享数据
        FileNavigator.prototype.refreshShareFile = function() {
            var self = this;
            self.position = false;
            return self.shareModalList().then(function (data) {
                self.currentModalParentId = data.parentId;
                self.modalFileList = (data.result || []).map(function (file) {
                    return new Item(file, self.currentModalPath);
                });
            });
        };

        //刷新收藏数据
        FileNavigator.prototype.refreshCollectFile = function() {
            var self = this;
            self.position = false;
            return self.collectModalList().then(function (data) {
                self.currentModalParentId = data.parentId;
                self.modalFileList = (data.result || []).map(function (file) {
                    return new Item(file, self.currentModalPath);
                });
            });
        };
       
		  /********** 更改路径后去掉当前文件夹再渲染*********/
			/* FileNavigator.prototype.selectFolderRefresh = function (item) {
				var self = this;
				var path = self.currentPath.join('/');
				var fileList=[];
				self.position = false;
					return self.list().then(function (data) {
						fileList = self.filterFileListById(data.result,item.model.id);
						self.currentParentId = data.parentId;
						self.fileList = (fileList || []).map(function (file) {
							return new Item(file, self.currentPath);
						});
						self.buildTree(path);
					});
				}; */
			
			/*******************根据id过滤文件列表******/
			/* FileNavigator.prototype.filterFileListById = function (fileList,fileId){
					var fileListTemp = [];
					for (var i = 0; i < fileList.length; i++) {
						if (fileId != fileList[i].id) {
							fileListTemp.push(fileList[i]);
						}
					}
					return fileListTemp;
				} */
				

        FileNavigator.prototype.myFolderRefresh = function() {
            var self = this;
            var path = self.myFolderCurrentPath.join('/');
            return self.listMyFolder().then(function(data) {
                self.myFolderFileList = (data.result || []).map(function(file) {
                    return new Item(file, self.myFolderCurrentPath);
                });
                self.myFolderBuildTree(path);
            });
        };
        
        FileNavigator.prototype.buildTree = function(path) {
            var flatNodes = [], selectedNode = {};
            function recursive(parent, item, path) {
                var absName = path ? (path + '/' + item.model.name) : item.model.name;
                if (parent.name.trim() && path.trim().indexOf(parent.name) !== 0) {
                    parent.nodes = [];
                }
                if (parent.name !== path) {
                    for (var i in parent.nodes) {
                        recursive(parent.nodes[i], item, path);
                    }
                } else {
                    for (var e in parent.nodes) {
                        if (parent.nodes[e].name === absName) {
                            return;
                        }
                    }
                    parent.nodes.push({item: item, name: absName, nodes: []});
                }
                parent.nodes = parent.nodes.sort(function(a, b) {
                    return a.name.toLowerCase() < b.name.toLowerCase() ? -1 : a.name.toLowerCase() === b.name.toLowerCase() ? 0 : 1;
                });
            }

            function flatten(node, array) {
                array.push(node);
                for (var n in node.nodes) {
                    flatten(node.nodes[n], array);
                }
            }

            function findNode(data, path) {
                return data.filter(function (n) {
                    return n.name === path;
                })[0];
            }

            !this.history.length && this.history.push({name: '', nodes: []});
            flatten(this.history[0], flatNodes);
            selectedNode = findNode(flatNodes, path);

            selectedNode.nodes = [];

            for (var o in this.fileList) {
                var item = this.fileList[o];
                item.isFolder() && recursive(this.history[0], item, path);
            }
        };

        FileNavigator.prototype.myFolderBuildTree = function(path) {
            var flatNodes = [], selectedNode = {};

            function recursive(parent, item, path) {
                var absName = path ? (path + '/' + item.model.name) : item.model.name;
                if (parent.name.trim() && path.trim().indexOf(parent.name) !== 0) {
                    parent.nodes = [];
                }
                if (parent.name !== path) {
                    for (var i in parent.nodes) {
                        recursive(parent.nodes[i], item, path);
                    }
                } else {
                    for (var e in parent.nodes) {
                        if (parent.nodes[e].name === absName) {
                            return;
                        }
                    }
                    parent.nodes.push({item: item, name: absName, nodes: []});
                }
                parent.nodes = parent.nodes.sort(function(a, b) {
                    return a.name.toLowerCase() < b.name.toLowerCase() ? -1 : a.name.toLowerCase() === b.name.toLowerCase() ? 0 : 1;
                });
            }

            function flatten(node, array) {
                array.push(node);
                for (var n in node.nodes) {
                    flatten(node.nodes[n], array);
                }
            }

            function findNode(data, path) {
                return data.filter(function (n) {
                    return n.name === path;
                })[0];
            }

            !this.myFolderHistory.length && this.myFolderHistory.push({name: '', nodes: []});
            flatten(this.myFolderHistory[0], flatNodes);
            selectedNode = findNode(flatNodes, path);
            if(!typeof(selectedNode)=="undefined"){
                selectedNode.nodes = [];
            }

            for (var o in this.myFolderFileList) {
                var item = this.myFolderFileList[o];
                item.isFolder() && recursive(this.myFolderHistory[0], item, path);
            }
        };

        /* FileNavigator.prototype.folderClick = function(item) {
            this.currentPath = [];
            this.currentFileId = '';

            if (item && item.isFolder()) {
                this.currentFileId = item.model.id;
                this.currentPath = item.model.fullPath().split('/').splice(1);
            }
            this.refresh();
				}; */
        FileNavigator.prototype.folderClick = function (item) {
            this.currentPath = [];
            this.currentFileId = '';
            this.currentParentId='';

            if (item && item.isFolder()) {
                this.currentFileId = item.model.id;
                this.currentParentId=item.model.parentId;
                this.currentPath = item.model.fullPath().split('/').splice(1);
            }
            this.refresh();
        };

        //收藏模态框文件夹列表点击功能
        FileNavigator.prototype.folderModalClick = function(item){
            this.currentModalFileId = '';
            this.currentModalParentId='';
            this.currentModalPath = [];

            if (item && item.isFolder()) {
                this.currentModalFileId = item.model.id;
                this.currentModalParentId=item.model.parentId;
                this.currentModalPath = item.model.fullPath().split('/').splice(1);
            }
            if(this.isPrivate){
                this.refreshCollectFile();
            }else{
                this.refreshShareFile();
            }
        }

        FileNavigator.prototype.myFolderClick = function(item) {
            //this.myFolderCurrentPath = [];
            if (item && item.isFolder()) {
                this.myFolderCurrentPath = item.model.fullPath().split('/').splice(1);
            }
            this.myFolderRefresh();
        };

        FileNavigator.prototype.upDir = function () {
                if (this.currentPath[0]) {
										this.currentPath = this.currentPath.slice(0, -1);
										//用于返回上一级操作，将当前文件夹的父id赋值给当前id
										this.currentFileId = this.currentParentId;
                    this.refresh();
                }
            };

        //收藏模态框文件夹列表的返回功能
        FileNavigator.prototype.upModalDir = function () {
            if (this.currentModalPath[0]) {
                this.currentModalPath = this.currentModalPath.slice(0, -1);
                //用于返回上一级操作，将当前文件夹的父id赋值给当前id
                this.currentModalFileId = this.currentModalParentId;
                if(this.isPrivate){
                    this.refreshCollectFile();
                }else{
                    this.refreshShareFile();
                }
            }
        };

				/**点击文件夹导航栏实现跳转 */
        FileNavigator.prototype.goTo = function(index) {
            this.currentPath = this.currentPath.slice(0, index + 1);
            this.currentParentId = '';
            var self = this;
            var deferred = $q.defer();
            var path = self.currentPath.join('/');
            var url = "";
            if($rootScope.rootdir == $rootScope.scope.config.fileMemuTitle['allFiles']){
                url = fileManagerConfig.getFileIdByPathUrl;//共享
            }else if($rootScope.rootdir == $rootScope.scope.config.fileMemuTitle['myFiles']){
                url = fileManagerConfig.getMyFileIdByPathUrl;// 我的
            }
            if(url == ""){
                return;
            }
            self.requesting = true;
            $http({
                method: "POST",
                url: url,
                params: {
                    path: ($rootScope.rootdir + '/' + path + '/').replace(/\/\//, '/')
                }
            })
            .success(function (data) {
                if (index == -1) {
                    self.currentFileId = "";
                } else {
                    self.currentFileId = data.data.id;
                }
                self.deferredHandler(data, deferred);
                self.refresh();
            }).error(function (data) {
                self.deferredHandler(data, deferred, 'Unknown error listing, check the response');
            })['finally'](function () {
                self.requesting = false;
            });

				};

        //收藏模态框文件夹的导航栏实现跳转
        FileNavigator.prototype.modalGoTo = function(index) {
            this.currentModalPath = this.currentModalPath.slice(0, index + 1);
            this.currentModalParentId = '';
            var self = this;
            var deferred = $q.defer();
            var path = self.currentModalPath.join('/');
            var url = "";
            var destination = "";
            if(self.isPrivate){//收藏--收藏到我的文件
                url = fileManagerConfig.getMyFileIdByPathUrl;
                destination = $rootScope.scope.config.fileMemuTitle['myFiles'];
            }else{
                url = fileManagerConfig.getFileIdByPathUrl;
                destination = $rootScope.scope.config.fileMemuTitle['allFiles'];
            }
            self.requesting = true;
            $http({
                method: "POST",
                url: url,
                params: {
                    path: (destination + '/' + path + '/').replace(/\/\//, '/')
                }
            })
                .success(function (data) {
                    if (index == -1) {
                        self.currentModalFileId = "";
                    } else {
                        self.currentModalFileId = data.data.id;
                    }
                    self.deferredHandler(data, deferred);
                    if(self.isPrivate){
                        self.refreshCollectFile();
                    }else{
                        self.refreshShareFile();
                    }
                }).error(function (data) {
                self.deferredHandler(data, deferred, 'Unknown error listing, check the response');
            })['finally'](function () {
                self.requesting = false;
            });
        };
	
        FileNavigator.prototype.fileNameExists = function(fileName) {
            for (var item in this.fileList) {
                item = this.fileList[item];
                if (fileName.trim && item.model.name.trim() === fileName.trim()) {
                    return true;
                }
            }
        };

        FileNavigator.prototype.listHasFolders = function() {
            for (var item in this.fileList) {
                if (this.fileList[item].model.type === 'dir') {
                    return true;
                }
            }
        };

        //判断弹出的模态框数据是否有文件夹
        FileNavigator.prototype.listHasModalFolders = function() {
            for (var item in this.modalFileList) {
                if (this.modalFileList[item].model.type === 'dir') {
                    return true;
                }
            }
        };

        FileNavigator.prototype.showList = function(listType) {
            this.isShowFolder = false;
            this.isShowMyFolder = false;
            this.isShowFlow = false;
            this.isShowMyFlow = false;

            if(listType === 'showFolder'){
                 this.isShowFolder = true;
                 return;
            }

            if(listType === 'showMyFolder'){
                 this.isShowMyFolder = true;
                 return;
            }

            if(listType === 'showFlow'){
                 this.showFlow = true;
                 return;
            }

            if(listType === 'showMyFlow'){
                 this.showMyFlow = true;
                 return;
            }
        };

        return FileNavigator;
    }]);
})(angular);