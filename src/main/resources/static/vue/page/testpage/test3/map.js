var Test3MapApiPage;
Test3MapApiPage = Vue.component("test-map-api-page", async function(resolve) {
    resolve({
        "template": (await axios.get("/vue/page/testpage/test3/map.html")).data,
        "data": function() {
            return {
                "map": null,
                "ps": null,
                "bounds": null,
                "marker": null,
                "markers": [],
                "infowindow": null,
                "infowindows": [],
                "inputAddr": '',
                "outputAddr": [],
                "keyword": null,
                "sort": "A",
                "places": [],
                "pagination": null,
                "crntLat": null,
                "crntLng": null,
                "curruntLocation": null,
                "distance": "-",
                "linePath": [],
                "polyline": null,
                "dataTable": {
                    "headers": [
                        {"text": "idx", "sortable": false, "value": "idx"},
                        {"text": "장소", "sortable": false, "value": "place_name"},
                        {"text": "주소", "sortable": false, "value": "road_address_name"},
                        {"text": "거리", "sortable": false, "value": "distance"},
                    ],
                },
                "flag": {
                    "removeable": true,
                },
                "rvmap": null,
                "roadview": null,
                "roadviewClient": null,
                "rvMarker": null,
            }
        },
        "computed": {
        },
        "watch": {
            "pagination.current": function(val) {
                this.pagination.gotoPage(val)  
            },
        },
        "methods": {
            // 카카오맵API 키 등록
            "addScript": function() {
                const script = document.createElement('script');
                /* global kakao */
                script.onload = () => kakao.maps.load(this.initSet);
                script.src = 'http://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=7c6172991f6f9765bf3e7492e8fbce9e&libraries=services';
                document.head.appendChild(script);
            },
            // 초기 설정 (지도 위치 및 마커 표시)
            "initSet": function() {
                var self = this;

                var mapContainer = document.getElementById('map');      // 지도를 표시할 div
                var mapOptions = {
                    center: new kakao.maps.LatLng(37.48709487635324, 126.89366618576527),   // 지도의 중심좌표
                    level: 6
                };
                self.map = new kakao.maps.Map(mapContainer, mapOptions);    // 지도를 생성합니다
                
                self.setCurrentLocation();
                
                self.setRvmap();
                self.setRoadview();
            },
            // 지도 사이즈 변경 (지도 숨기기-펼치기)
            "changeSize": function(size) {
                const container = document.getElementById("map");
                container.style.width = `${size}px`; // = (size +'px')
                container.style.height = `${size}px`;
                this.map.relayout();
            },
            // 마커 등록
            "setMarkers": function(data, idx) {
                var self = this;
                // marker 생성
                self.marker = new kakao.maps.Marker({
                    map: self.map,
                    position: new kakao.maps.LatLng(data.y, data.x)
                });
                // 마커를 배열에 추가
                this.$set(self.marker, 'id', idx);
                self.markers.push(self.marker);
            },
            // 마커를 지도에 표시
            "displayMarkers": function() {
                var self = this;
                // markers 배열을 지도에 표시합니다
                for (var i = 0; i < self.markers.length; i++) {
                    self.markers[i].setMap(self.map);
                }
            },
            // 마커에서 클릭 이벤트 (마커 줌인)
            "clickMarkers": function(data){
                var self = this;
                
                self.markers.forEach(e => {
                    data.forEach(el => {
                        if (e.id == el.id) {
                            // 경도위도값 잡아주기
                            var bounds = new kakao.maps.LatLngBounds();
                            bounds.extend(new kakao.maps.LatLng(el.y, el.x));
                            // 검색된 장소 위치를 기준으로 지도 범위를 재설정
                            kakao.maps.event.addListener(e, 'click', function() {
                                self.map.setBounds(bounds);
                            });
                        }
                    })
                })
            },
            // 인포윈도우 이벤트 생성,  마커에서 마우스오버 이벤트 (인포윈도우)
            "displayInfowindow": function(data) {
                var self = this;
                var infowindows = self.infowindows;

                self.markers.forEach(e => {
                    data.forEach(el => {
                        if (e.id == el.id) {
                            // 장소명이 인포윈도우에 표출됩니다
                            if (el.place_name == null || el.place_name == undefined) {
                                var text = '<div style="padding:5px;font-size:12px;">' + el.road_address_name + '</div>';
                            } else {
                                var text = '<div style="padding:5px;font-size:12px;">' + el.place_name + '</div>';
                            }
                            // 인포윈도우로 장소에 대한 설명을 표시합니다
                            infowindow = new kakao.maps.InfoWindow({
                                content: text, // 인포윈도우에 표시할 내용
                            });
                            // 인포윈도우를 배열에 추가
                            self.$set(infowindow, "id", e.id);
                            infowindows.push(infowindow);

                            // 마커에 이벤트를 등록합니다
                            kakao.maps.event.addListener(e, 'mouseover', function() {
                                infowindows[e.id].open(self.map, e);
                            });
                            kakao.maps.event.addListener(e, 'mouseout', function() {
                                infowindows[e.id].close();
                            });
                        }
                    })
                })
            },
            // 검색 결과 리스트에서 클릭 이벤트 (마커 줌인 + 인포윈도우)
            "clickList": function(place) {
                var self = this;
                var infowindows = self.infowindows,
                    i;

                for(i = 0; i < infowindows.length; i++){
                    infowindows[i].close();
                }
                infowindows[place.id].open(self.map, self.markers[place.id]);

                // 경도위도값 잡아주기
                var bounds = new kakao.maps.LatLngBounds();
                bounds.extend(new kakao.maps.LatLng(place.y, place.x));
                // 검색된 장소 위치를 기준으로 지도 범위를 재설정
                self.map.setBounds(bounds);
            },
            // 마커 및 인포윈도우 지우기 (초기화)
            "resetData": function() {
                var self = this,
                    i;
                self.bounds = null;
                for (i = 0; i < self.markers.length; i++) {
                    self.markers[i].setMap(null);
                }
                self.marker = null;
                self.markers = [];
                self.places = [];
                self.pagination = null;
                self.infowindows = [];
                for(i = 0; i < infowindows.length; i++){
                    infowindows[i].close();
                }
                self.polyline = null;
            },
            // 주소 검색
            "searchAddress": function() {
                var self = this;

                // 주소 검색을 요청하는 함수입니다
                if (self.inputAddr == null || !self.inputAddr.replace(/^\s+|\s+$/g, '')) {
                    alert('주소를 입력해주세요!');
                    return false;
                }

                // 주소-좌표 변환 객체
                var geocoder = new kakao.maps.services.Geocoder();

                self.outputAddr = function(result, status) {
                    // 정상적으로 검색이 완료됐으면 
                    if (status === kakao.maps.services.Status.OK) {

                        let data = [],
                            temp = {};
                        self.$set(temp, "y", result[0].y);
                        self.$set(temp, "x", result[0].x);
                        self.$set(temp, "road_address_name", result[0].road_address.address_name);
                        self.$set(temp, "id", 0);
                        data.push(temp);
                        console.log(data);

                        self.resetData();

                        // 결과값으로 받은 위치를 마커로 표시합니다
                        // 마커를 생성합니다
                        self.setMarkers(data[0], 0);
                        self.displayMarkers();
                        self.clickMarkers(data);
                        self.displayInfowindow(data);

                        // 경도위도값 잡아주기
                        var bounds = new kakao.maps.LatLngBounds();
                        bounds.extend(new kakao.maps.LatLng(data[0].y, data[0].x));
                        // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
                        self.map.setBounds(bounds);
                    } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
                        alert('검색 결과가 존재하지 않습니다.');
                    } else if (status === kakao.maps.services.Status.ERROR) {
                        alert('검색 결과 중 오류가 발생했습니다.');
                    }
                }
                // 주소로 좌표를 검색
                geocoder.addressSearch(self.inputAddr, self.outputAddr)
            },
            // 키워드 검색
            "searchKeyword": async function() {
                var self = this;

                // 장소 검색 객체를 생성합니다
                self.ps = new kakao.maps.services.Places();

                // 키워드 검색을 요청하는 함수입니다
                if (self.keyword == null || !self.keyword.replace(/^\s+|\s+$/g, '')) {
                    alert('키워드를 입력해주세요!');
                    return false;
                }

                // 검색 옵션 설정 (데이터 ~45개까지 검색가능)
                if(self.sort == "D"){
                    if(self.crntLat !== null && self.crntLng !== null){
                        var option = {
                            size: 9,    // 리스트 한 페이지에 정렬되는 데이터 갯수
                            page: 1,
                            sort: kakao.maps.services.SortBy.DISTANCE,    // 정렬 옵션 > DISTANCE - 지정한 좌표값에 기반하여 동작함, ACCURACY - 정확도 순(기본값)
                            x: this.crntLng,
                            y: this.crntLat,
                        };
                    } else {
                        alert("현위치를 탐색 할 수 없습니다.")
                        return false;
                    }
                } else if(self.sort == "A"){
                    var option = {
                        size: 9
                    };
                }

                // 키워드로 장소를 검색합니다
                await self.ps.keywordSearch(self.keyword, self.placesSearchCB, option);
            },
            // 키워드 검색 완료 시 호출되는 콜백함수 입니다
            "placesSearchCB": function(data, status, pagination) {
                var self = this,
                    i = 0;

                if (status === kakao.maps.services.Status.OK) {

                    self.resetData();

                    // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
                    // LatLngBounds 객체에 좌표를 추가합니다
                    self.bounds = new kakao.maps.LatLngBounds();
                    
                    // 마커를 표출합니다
                    paging = pagination.current - 1;
                    for (i = 0; i < data.length; i++) {
                        self.setMarkers(data[i], i);
                        this.$set(data[i], "id", i);
                        this.$set(data[i], "idx", i+(9*paging)+1);
                        self.bounds.extend(new kakao.maps.LatLng(data[i].y, data[i].x));
                    }
                    self.displayMarkers();
                    self.clickMarkers(data);
                    self.displayInfowindow(data);

                    // 검색 목록을 표출합니다
                    data.forEach(e => {
                        self.forDistance(e);
                        self.$set(e, "distance", self.distance);
                    })
                    self.places = data;
                    self.pagination = pagination;

                    // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
                    self.map.setBounds(self.bounds);
                    
                    console.log(self.places);
                    console.log(status);
                    console.log(pagination);
                } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
                    alert('검색 결과가 존재하지 않습니다.');
                } else if (status === kakao.maps.services.Status.ERROR) {
                    alert('검색 결과 중 오류가 발생했습니다.');
                }
            },
            // HTML5 GeoLocation을 이용해 접속위치를 표시합니다. (현위치 찾기)
            // Chrome 브라우저는 https 환경에서만 geolocation을 지원합니다. 
            "setCurrentLocation": async function() {
                var self = this;
                
                // HTML5의 geolocation으로 사용할 수 있는지 확인합니다 
                if (navigator.geolocation) {
                    // GeoLocation을 이용해서 접속 위치를 얻어옵니다
                    navigator.geolocation.getCurrentPosition(function(position) {
                        if (self.curruntLocation !== null) {
                            self.curruntLocation.setMap(null);
                            self.crntLat = null;
                            self.crntLng = null;
                        }

                        self.crntLat = position.coords.latitude,
                        self.crntLng = position.coords.longitude;

                        // 마커 이미지의 이미지 주소입니다
                        var imageSrc = "/resources/img/marker_red.png";
                        // 마커 이미지의 이미지 크기 입니다
                        var imageSize = new kakao.maps.Size(24, 40);
                        // 마커 이미지를 생성합니다    
                        var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);

                        // 결과값으로 받은 위치를 마커로 표시합니다
                        // 마커를 생성합니다
                        self.curruntLocation = new kakao.maps.Marker({
                            map: self.map,
                            position: new kakao.maps.LatLng(self.crntLat, self.crntLng),
                            image: markerImage,
                        });
                        self.curruntLocation.setMap(self.map);

                        if (self.markers.length == "0") {    // 키워드 검색 결과가 없을 시 현위치로 지도 범위 설정
                            // 경도위도값 잡아주기
                            var bounds = new kakao.maps.LatLngBounds();
                            bounds.extend(new kakao.maps.LatLng(self.crntLat, self.crntLng));
                            // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
                            self.map.setBounds(bounds);
                        } else {    // 키워드 검색 결과가 있을 때는 검색 데이터 + 현위치 포함 지도 범위 설정
                            self.bounds.extend(new kakao.maps.LatLng(self.crntLat, self.crntLng));
                            self.map.setBounds(self.bounds);
                        }
                    });
                } else { // HTML5의 GeoLocation을 사용할 수 없을때 마커 표시 위치와 인포윈도우 내용을 설정합니다
                    alert('geolocation을 사용할수 없어요..');
                }
            },
            // 직선거리 측정
            "forDistance": function(data) {
                var self = this;

                if (self.crntLat !== null && self.crntLng !== null) {
                    self.distance = "-";
                    if (self.polyline !== null) {
                        self.polyline.setMap(null);
                    }

                    // 선을 구성하는 좌표 배열입니다. 이 좌표들을 이어서 선을 표시합니다
                    self.linePath = [
                        new kakao.maps.LatLng(data.y, data.x),
                        new kakao.maps.LatLng(self.crntLat, self.crntLng),
                    ];

                    // 지도에 표시할 선을 생성합니다
                    self.polyline = new kakao.maps.Polyline({
                        path: self.linePath, // 선을 구성하는 좌표배열 입니다
//                        strokeWeight: 5, // 선의 두께 입니다
//                        strokeColor: '#FFAE00', // 선의 색깔입니다
//                        strokeOpacity: 0.7, // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
//                        strokeStyle: 'solid' // 선의 스타일입니다
                    });

//                    self.polyline.setMap(self.map);

                    self.distance = Math.round(self.polyline.getLength());    // getLength()
                    self.distance = self.distance.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',') + 'm';
                }
            },
            // 로드뷰 지도
            "setRvmap": function(){
                var self = this;
                
                var rvmapContainer = document.getElementById('rvmap');
                var rvmapOptions = {
                    center: new kakao.maps.LatLng(37.48709487635324, 126.89366618576527),
                    level: 3
                };
                self.rvmap = new kakao.maps.Map(rvmapContainer, rvmapOptions);
                
                self.rvmap.addOverlayMapTypeId(kakao.maps.MapTypeId.ROADVIEW);
                
                self.displayRoadViewMarker();
            },
            // 로드뷰
            "setRoadview": function(){
                var self = this;
                
                var roadviewContainer = document.getElementById('roadview'); //로드뷰를 표시할 div
                self.roadview = new kakao.maps.Roadview(roadviewContainer); //로드뷰 객체
                self.roadviewClient = new kakao.maps.RoadviewClient(); //좌표로부터 로드뷰 파노ID를 가져올 로드뷰 helper객체

                var position = new kakao.maps.LatLng(37.48709487635324, 126.89366618576527);

                // 특정 위치의 좌표와 가까운 로드뷰의 panoId를 추출하여 로드뷰를 띄운다.
                self.roadviewClient.getNearestPanoId(position, 50,function(panoId){
                    console.log(panoId);
                    self.roadview.setPanoId(panoId, position); //panoId와 중심좌표를 통해 로드뷰 실행
                    self.roadview.relayout();
                });
            },
            // 로드뷰 마커
            "displayRoadViewMarker": function(){
                var self = this;
                
                var markImage = new kakao.maps.MarkerImage(
                    'https://t1.daumcdn.net/localimg/localimages/07/2018/pc/roadview_minimap_wk_2018.png',
                    new kakao.maps.Size(26, 46),
                    {
                        // 스프라이트 이미지를 사용합니다.
                        // 스프라이트 이미지 전체의 크기를 지정하고
                        spriteSize: new kakao.maps.Size(1666, 168),
                        // 사용하고 싶은 영역의 좌상단 좌표를 입력합니다.
                        // background-position으로 지정하는 값이며 부호는 반대입니다.
                        spriteOrigin: new kakao.maps.Point(705, 114),
                        offset: new kakao.maps.Point(13, 46)
                    }
                );
                // 드래그가 가능한 마커를 생성합니다.
                self.rvMarker = new kakao.maps.Marker({
                    image: markImage,
                    position: new kakao.maps.LatLng(37.48709487635324, 126.89366618576527),
                    draggable: true,
                    map: self.rvmap,
                });
                //마커에 dragend 이벤트를 할당합니다
                kakao.maps.event.addListener(self.rvMarker, 'dragend', function(mouseEvent) {
                    console.log("??ㅇㅅㅇ??");
                    var position = self.rvMarker.getPosition(); //현재 마커가 놓인 자리의 좌표
//                    toggleRoadview(position); //로드뷰를 토글합니다
                });
                //지도에 클릭 이벤트를 할당합니다
                kakao.maps.event.addListener(self.rvmap, 'click', function(mouseEvent) {

                    // 현재 클릭한 부분의 좌표를 리턴 
                    var position = mouseEvent.latLng;

                    self.rvMarker.setPosition(position);
                    toggleRoadview(position); //로드뷰를 토글합니다
                });
            },
            //로드뷰 toggle함수
            "toggleRoadview": function(position) {
                //전달받은 좌표(position)에 가까운 로드뷰의 panoId를 추출하여 로드뷰를 띄웁니다
                self.roadviewClient.getNearestPanoId(position, 50, function(panoId) {
                    if (panoId !== null) {
                        self.roadview.setPanoId(panoId, position); //panoId를 통한 로드뷰 실행
                        self.roadview.relayout(); //로드뷰를 감싸고 있는 영역이 변경됨에 따라, 로드뷰를 재배열합니다
                    }
                });
            },

        },
        "mounted": function() {
            window.kakao && window.kakao.maps ? this.initSet() : this.addScript();
        },
        "created": async function() {
        },
    });
});